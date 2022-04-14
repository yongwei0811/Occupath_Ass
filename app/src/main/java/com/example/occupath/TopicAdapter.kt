package com.example.occupath

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.occupath.databinding.TopicCardsBinding

class TopicAdapter(val topicList : List<Topic>) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    class TopicViewHolder(private val binding: TopicCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(topic: Topic) {
            binding.imageTopic.setImageResource(topic.image)
            binding.textTopicName.text = topic.topicName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            TopicCardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topicList[position]
        holder.bindItem(topic)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

}