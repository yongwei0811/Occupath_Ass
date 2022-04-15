package com.example.occupath.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.occupath.ChatActivity
import com.example.occupath.R
import com.example.occupath.User
import com.example.occupath.databinding.ItemProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class UserAdapter(var context:Context,var userList:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding: ItemProfileBinding = ItemProfileBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var v = LayoutInflater.from(context).inflate(
            R.layout.item_profile,
            parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position:Int) {
        val user = userList[position]
        holder.binding.username.text = user.name
//        holder.binding.
//        val database = FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/")
//        var senderRoom = FirebaseAuth.getInstance().uid + user.uid
//        val randomKey = database!!.reference.push().key
//        val fromId = FirebaseAuth.getInstance().uid ?: return
//        val ref = FirebaseDatabase.getInstance().getReference("/latMsg/$fromId")
//        holder.binding.lastMessage.text =  ref.toString()
//        database!!.reference.child("chats")
//            .child(senderRoom)
////            .get()
//            .child("latMsg")
////            . {
////                holder.binding.lastMessage.text =  it.getValue(User::class.java).toString()
////            }
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                        val user:User? = snapshot.getValue()
//                        if(!user!!.uid.equals(FirebaseAuth.getInstance().uid))
//                            users!!.add(user)
//
//                    }
//                    usersAdapter!!.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })




        if (!user.profileImage!!.isEmpty()) {
            val requestOptions = RequestOptions().placeholder(R.drawable.user_icon)

            Glide.with(context)
                .load(user.profileImage)
                .apply(requestOptions)
                .into(holder.binding!!.profile)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("image", user.profileImage)
            intent.putExtra("uid", user.uid)
            context.startActivity(intent)
        }



    }  override fun getItemCount(): Int = userList.size
}