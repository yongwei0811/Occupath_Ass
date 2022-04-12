package com.example.occupath

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.occupath.databinding.ActivityCreateLiveTalkBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class CreateLiveTalkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCreateLiveTalkBinding
    private val topics = arrayOf("Computer Science", "Finance", "Hospitality", "Engineering")
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLiveTalkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topicSpinner = binding.spinnerTopic
        topicSpinner.adapter = ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item, topics)

        db = FirebaseFirestore.getInstance()

        binding.buttonStartNow.setOnClickListener{
            val username = "denisek"
            val talkTitle = binding.editLiveTalkTitle.text.toString()
            val talkDesc = binding.editLiveTalkDesc.text.toString()
            val topic = binding.spinnerTopic.selectedItem.toString()

            addToFirestore(username, talkTitle, talkDesc, topic)
            joinLiveTalk(talkTitle, username)

        }
    }

    fun addToFirestore(username:String,talkTitle:String,talkDesc:String,topic:String){
        db = FirebaseFirestore.getInstance()

        val liveTalk : MutableMap<String, Any> = HashMap()
        liveTalk["username"] = username
        liveTalk["talkTitle"] = talkTitle
        liveTalk["talkDesc"] = talkDesc
        liveTalk["topic"] = topic

        db.collection("LiveTalk").add(liveTalk)
    }

    fun joinLiveTalk(talkTitle: String, username : String) {

        var roomName = talkTitle + " By " + username

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

        JitsiMeetActivity.launch(this, options)

    }
}