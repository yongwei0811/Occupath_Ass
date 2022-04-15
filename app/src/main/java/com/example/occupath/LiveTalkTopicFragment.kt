package com.example.occupath

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.occupath.databinding.FragmentLiveTalkBinding
import com.example.occupath.databinding.FragmentLiveTalkTopicBinding
import com.google.firebase.firestore.*


class LiveTalkTopicFragment : Fragment() {
    private lateinit var binding : FragmentLiveTalkTopicBinding
    private lateinit var liveTalkArrayList : ArrayList<LiveTalk>
    private lateinit var liveTalkAdapter: LiveTalkAdapter

    private lateinit var db : FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveTalkTopicBinding.inflate(layoutInflater,container,false)

        liveTalkArrayList = arrayListOf()
        liveTalkAdapter = LiveTalkAdapter(liveTalkArrayList)

        binding.recViewLiveTalk.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recViewLiveTalk.adapter = liveTalkAdapter
        binding.recViewLiveTalk.setHasFixedSize(true)

        // get selected topic from previous fragment
   //     val args = LiveTalkTopicFragmentArgs.fromBundle(requireArguments())
     //   val topic = args.topic

        // update the live talks related to topic in recyclerview
     //   updateLiveTalk(topic)

    //    binding.textTopicName.text = topic

        return binding.root
    }

    private fun updateLiveTalk(topic : String){
        db = FirebaseFirestore.getInstance()

        db.collection("LiveTalk")
            .whereEqualTo("topic",topic)
            .orderBy("createdTime",Query.Direction.DESCENDING)  // latest created live talk is shown on top
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    liveTalkArrayList.add(document.toObject(LiveTalk::class.java))
                }

                liveTalkAdapter.notifyDataSetChanged()
            }
    }
}