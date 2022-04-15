package com.example.occupath

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.occupath.databinding.FragmentLiveTalkBinding

class LiveTalkFragment : Fragment() {

    private lateinit var binding: FragmentLiveTalkBinding
    private lateinit var topicAdapter : TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        topicAdapter = TopicAdapter(TopicList.topicList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveTalkBinding.inflate(layoutInflater,container,false)

        // topic recyclerview
        binding.recViewDiscoverTopic.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recViewDiscoverTopic.adapter = topicAdapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.live_talk_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_createLiveTalk -> { // create live talk
                findNavController().navigate(LiveTalkFragmentDirections.actionLiveTalkFragmentToCreateLiveTalkFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}