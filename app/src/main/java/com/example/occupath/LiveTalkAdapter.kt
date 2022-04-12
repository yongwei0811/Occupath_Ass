package com.example.occupath

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.occupath.databinding.LiveTalkCardsBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL
import android.widget.Toast




class LiveTalkAdapter(val liveTalkList : ArrayList<LiveTalk>) : RecyclerView.Adapter<LiveTalkAdapter.LiveTalkViewHolder>() {

    class LiveTalkViewHolder(private val binding: LiveTalkCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val button = binding.btnJoin
        val talkTitle = binding.textTalkTitle.text
        val hostUsername = binding.textHostUsername.text

        fun bindItem(liveTalk: LiveTalk) {
            binding.textTalkTitle.text = liveTalk.talkTitle
            binding.textTalkDesc.text = liveTalk.talkDesc
            binding.textHostUsername.text = liveTalk.username
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

        var talkTitle = holder.talkTitle
        var username = holder.hostUsername

        holder.button.setOnClickListener(){
            val intent = Intent(it.context,LiveTalkRoomActivity::class.java)
            intent.putExtra("talkTitle", talkTitle)
            intent.putExtra("username", username)
            it.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return liveTalkList.size
    }

}