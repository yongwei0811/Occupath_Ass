package com.example.occupath

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.occupath.databinding.RecycleRowBinding
import com.squareup.picasso.Picasso

class new_adapter(val post_list : ArrayList<Post>) : RecyclerView.Adapter<new_adapter.PostHolder>() {
    class PostHolder(private val binding: RecycleRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(post: Post){
            binding.recComment.text = post.comment
            binding.recName.text = post.name
            Picasso.get().load(post.image).into(binding.recImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return new_adapter.PostHolder(RecycleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = post_list[position]
        holder.bindItem(post)
    }

    override fun getItemCount(): Int {
        return post_list.size
    }
}