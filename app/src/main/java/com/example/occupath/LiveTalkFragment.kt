package com.example.occupath

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.occupath.databinding.FragmentLiveTalkBinding
import com.google.firebase.firestore.*


class LiveTalkFragment : Fragment() {

    private lateinit var binding: FragmentLiveTalkBinding
    private lateinit var topicAdapter : TopicAdapter
    private lateinit var liveTalkArrayList : ArrayList<LiveTalk>
    private lateinit var liveTalkAdapter: LiveTalkAdapter

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        topicAdapter = TopicAdapter(TopicList.topicList)

        liveTalkArrayList = arrayListOf()
        liveTalkAdapter = LiveTalkAdapter(liveTalkArrayList)

        updateLiveTalk()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveTalkBinding.inflate(layoutInflater,container,false)

        binding.recViewDiscoverTopic.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recViewDiscoverTopic.adapter = topicAdapter

//        val myLinearLayoutManager = object : LinearLayoutManager(activity) {
//            override fun canScrollVertically(): Boolean {
//                return false
//            }
//        }

        binding.recViewLiveTalk.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        binding.recViewLiveTalk.adapter = liveTalkAdapter
//        binding.recViewLiveTalk.setHasFixedSize(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.live_talk_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_createLiveTalk -> {
                createLiveTalk()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createLiveTalk(){
        val intent = Intent(activity,CreateLiveTalkActivity::class.java)
        startActivity(intent)
    }

    private fun updateLiveTalk(){
        db = FirebaseFirestore.getInstance()

        db.collection("LiveTalk").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        liveTalkArrayList.add(dc.document.toObject(LiveTalk::class.java))
                    }
                }

                liveTalkAdapter.notifyDataSetChanged()
            }

        })

    }

}