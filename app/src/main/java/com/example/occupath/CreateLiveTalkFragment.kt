package com.example.occupath

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.occupath.databinding.FragmentCreateLiveTalkBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateLiveTalkFragment : Fragment() {

    private lateinit var binding : FragmentCreateLiveTalkBinding
    private val topics = arrayOf("Programming and Tech", "Finance", "Engineering", "Design", "Soft Skills")
    private lateinit var db : FirebaseFirestore

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateLiveTalkBinding.inflate(layoutInflater,container,false)

        // populate spinner with talk topics
        val topicSpinner = binding.spinnerTopic
        topicSpinner.adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, topics)

        var name  = ""
        var email  = ""
        var profileImage = ""

        firebaseAuth = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    name = "${snapshot.child("name").value}"
                    email = "${snapshot.child("email").value}"
                    profileImage = "${snapshot.child("profileImage").value}"
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        // start button
        binding.buttonStartNow.setOnClickListener{
            val talkTitle = binding.editLiveTalkTitle.text.toString()
            val talkDesc = binding.editLiveTalkDesc.text.toString()
            val topic = binding.spinnerTopic.selectedItem.toString()
            val createdTime = Timestamp.now()

            val liveTalk = LiveTalk(email, name, talkTitle, talkDesc, topic, profileImage)

            when {
                talkTitle.isEmpty() -> {  // check if talk title is empty
                    Toast.makeText(activity,"Enter talk title",Toast.LENGTH_SHORT).show()
                }
                talkDesc.isEmpty() -> { // check if talk description is empty
                    Toast.makeText(activity,"Enter talk description",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    addToFirestore(liveTalk, createdTime) // add live talk to firestore
                    joinLiveTalk(talkTitle, name) // start new live talk room
                }
            }
        }

        binding.buttonClose.setOnClickListener{

            if(binding.editLiveTalkTitle.text.isNotEmpty()){
                val talkTitle = binding.editLiveTalkTitle.text.toString()
                deleteLiveTalk(email,talkTitle) // end live talk
            }

            // go back to live talk fragment
        //    it.findNavController().navigate(CreateLiveTalkFragmentDirections.actionCreateLiveTalkFragmentToLiveTalkFragment())
        }

        return binding.root
    }

    fun addToFirestore(liveTalkDetails: LiveTalk, createdTime : Timestamp){
        db = FirebaseFirestore.getInstance()

        val liveTalk : MutableMap<String, Any> = HashMap()
        liveTalk["email"] = liveTalkDetails.email.toString()
        liveTalk["name"] = liveTalkDetails.name.toString()
        liveTalk["talkTitle"] = liveTalkDetails.talkTitle.toString()
        liveTalk["talkDesc"] = liveTalkDetails.talkDesc.toString()
        liveTalk["topic"] = liveTalkDetails.topic.toString()
        liveTalk["userImage"] = liveTalkDetails.userImage.toString()
        liveTalk["createdTime"] = createdTime

        db.collection("LiveTalk").add(liveTalk) // add new document in firestore
    }

    fun joinLiveTalk(talkTitle: String, name : String) {

        val roomName = "$talkTitle by $name"

        val url = URL("https://meet.jit.si")

        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(url)
            .setRoom(roomName)
            .setAudioMuted(true)
            .setVideoMuted(true)
            .setAudioOnly(false)
            .setWelcomePageEnabled(false)
            .setConfigOverride("requireDisplayName", false)
            .setFeatureFlag("tile-view.enabled",false)
            .build()

        JitsiMeetActivity.launch(activity, options) // launch jitsi meeting room
    }

    fun deleteLiveTalk(email: String,talkTitle: String){
        val liveTalkRef = db.collection("LiveTalk")

        // delete live talk associated with user's email
        val deleteQuery = liveTalkRef
            .whereEqualTo("email",email)
            .whereEqualTo("talkTitle",talkTitle)
            .get()

        deleteQuery.addOnSuccessListener {
            for(document in it){
                liveTalkRef.document(document.id).delete()
            }
        }

    }

}