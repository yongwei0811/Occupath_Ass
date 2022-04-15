package com.example.occupath

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.occupath.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class chatHist : AppCompatActivity() {

   // var binding: ActivityChatHistBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat_hist)
//        binding = ActivityChatHistBinding.inflate(layoutInflater)
//        setContentView(binding!!.root)
      //   setSupportActionBar(binding!!.toolbar)
        val database =
            FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/")
        val storage =
            FirebaseStorage.getInstance()//https://console.firebase.google.com/project/occupath-57628/storage/occupath-57628.appspot.com/files")
        var users: ArrayList<User>? = null
        var usersAdapter: UserAdapter? = null
        var dialog: ProgressDialog? = null
        var user: User? = null
        val layoutManager =  LinearLayoutManager(this@chatHist)
        users = ArrayList<User>()
        usersAdapter = UserAdapter(this@chatHist, users!!)
//        binding!!.mRec.layoutManager = layoutManager
        var userid = FirebaseAuth.getInstance().uid.toString()
        database!!.reference.child("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot1 in snapshot.children) {
                        val chatRoom = snapshot1.getValue().toString()

                        //binding!!.tetsing.text = chatRoom
                        if (chatRoom!!.contains(userid)) {
                            var dropN = userid!!.length
                            var receiveUserID = chatRoom.drop(dropN)
                            database!!.reference.child("Users")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        users!!.clear()
                                        for (snapshot1 in snapshot.children) {
                                            val user: User? = snapshot1.getValue(User::class.java)
                                            if (user!!.uid.equals(receiveUserID))
                                                users!!.add(user)
//
//                                            binding!!.tetsing.text = user.name
                                        }
                                        usersAdapter!!.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        }
                    }

                     usersAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
//        binding!!.mRec.adapter = usersAdapter
    //    binding!!.tetsing.text = "jdhsjhskdskj"


    }}