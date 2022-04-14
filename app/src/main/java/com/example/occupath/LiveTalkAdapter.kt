package com.example.occupath

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.example.occupath.databinding.LiveTalkCardsBinding
import androidx.navigation.findNavController


class LiveTalkAdapter(val liveTalkList : ArrayList<LiveTalk>) : RecyclerView.Adapter<LiveTalkAdapter.LiveTalkViewHolder>() {

    class LiveTalkViewHolder(private val binding: LiveTalkCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            // recyclerview item is clicked
            binding.root.setOnClickListener{
                val name = binding.textHostName.text.toString()
                val email = binding.textHostEmail.text.toString()
                val talkTitle = binding.textTalkTitle.text.toString()
                val talkDesc = binding.textTalkDesc.text.toString()
                val topic = binding.textTopicName.text.toString()

                val liveTalk = LiveTalk(email,name,talkTitle,talkDesc,topic)
                it.findNavController().navigate(LiveTalkTopicFragmentDirections.actionLiveTalkTopicFragmentToLiveTalkRoomFragment(liveTalk))
            }
        }

        fun bindItem(liveTalk: LiveTalk) {
            binding.textTalkTitle.text = liveTalk.talkTitle
            binding.textTalkDesc.text = liveTalk.talkDesc
            binding.textHostEmail.text = liveTalk.email
            binding.textHostName.text = liveTalk.name
            binding.textTopicName.text = liveTalk.topic
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveTalkViewHolder {
        return LiveTalkAdapter.LiveTalkViewHolder(
            LiveTalkCardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LiveTalkViewHolder, position: Int) {
        val liveTalk = liveTalkList[position]
        holder.bindItem(liveTalk)
    }

    override fun getItemCount(): Int {
        return liveTalkList.size
    }

}