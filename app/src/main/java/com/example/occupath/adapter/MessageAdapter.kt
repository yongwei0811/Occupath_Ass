package com.example.occupath.adapter

import android.app.AlertDialog
import android.content.Context
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.bumptech.glide.request.RequestOptions
import com.example.occupath.R
import com.example.occupath.databinding.DeleteLayoutBinding
import com.example.occupath.databinding.ReceiveMsgBinding
import com.example.occupath.databinding.SendMsgBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.jvm.javaClass as javaClass
import com.example.occupath.Message
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList


class MessageAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom:String,
    receiverRoom:String
):RecyclerView.Adapter<RecyclerView.ViewHolder?>(){

    lateinit var  messages:ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    val senderRoom :String
    var receiverRoom:String

    override fun getItemCount(): Int = messages.size

    inner class SentMsgHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var binding:SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var binding:SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    init {
        if(messages != null) {
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ITEM_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg, parent, false)
              SentMsgHolder(view)
        }else{
            val view =  LayoutInflater.from(context).inflate(R.layout.receive_msg, parent, false)
              ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if(FirebaseAuth.getInstance().uid == messages.senderId){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]
        if(holder.javaClass == SentMsgHolder::class.java){
            val viewHolder = holder as SentMsgHolder
            if(message.message.equals("photo")){
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                if (!message.imageUrl!!.isEmpty()) {
                    val requestOptions = RequestOptions().placeholder(R.drawable.img_placeholder)

                    Glide.with(context)
                        .load(message.imageUrl)
                        .apply(requestOptions)
                        .into(viewHolder.binding.image)
                }
            }
            viewHolder.binding.message.text = message.message
            viewHolder.binding.TxtTime.text = getShortDate(message.timeStamp)
            viewHolder.itemView.setOnLongClickListener{
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding:DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener{
                    message.message = "This message was removed"

                    message.messageId?.let { it1->
                        FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/")
                            .reference.child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(it1).setValue(message)

                    }

                    message.messageId.let { it1->
                        FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/")
                            .reference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)

                    }
                    dialog.dismiss()

                }

                binding.delete.setOnClickListener{
                    message.messageId?.let{ it1->
                    FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/")
                        .reference.child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancel.setOnClickListener{
                    dialog.dismiss()
                }
                dialog.show()
                false

        }
        }
        else{
            val viewHolder = holder as ReceiveMsgHolder
            if(message.message.equals("photo")){
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE

                if (!message.imageUrl!!.isEmpty()) {
                    val requestOptions = RequestOptions().placeholder(R.drawable.img_placeholder)

                    Glide.with(context)
                        .load(message.imageUrl)
                        .apply(requestOptions)
                        .into(viewHolder.binding.image)
                }
            }
            viewHolder.binding.message.text = message.message
            viewHolder.binding.TxtTime.text = getShortDate(message.timeStamp)
            viewHolder.itemView.setOnLongClickListener{
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding:DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener{
                    message.message = "This message was removed"

                    message.messageId?.let { it1->
                        FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/").reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)

                    }

                    message.messageId.let { it1->
                        FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/").reference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)

                    }
                    dialog.dismiss()

                }

                binding.delete.setOnClickListener{
                    message.messageId?.let{ it1->
                        FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/").reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancel.setOnClickListener{
                    dialog.dismiss()
                }
                dialog.show()
            false
            }}
        }

    fun getShortDate(ts:Long?):String{
        if(ts == null) return ""
        //Get instance of calendar
        val calendar = Calendar.getInstance(Locale.getDefault())
        //get current date from ts
        calendar.timeInMillis = ts
        //return formatted date
        return android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", calendar).toString()
    }
}