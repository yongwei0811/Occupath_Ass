package com.example.occupath

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import android.R
import android.app.ProgressDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.occupath.adapter.UserAdapter
import com.example.occupath.databinding.*
//import com.example.occupath.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions



class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    var database: FirebaseDatabase? = null
    var users: ArrayList<User>? = null
    var usersAdapter: UserAdapter? = null
    var dialog: ProgressDialog? = null
    var user:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater,container,false)
        dialog = ProgressDialog(activity)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        database = FirebaseDatabase.getInstance("https://occupath-57628-default-rtdb.firebaseio.com/")
        users = ArrayList<User>()
        usersAdapter = UserAdapter(activity!!, users!!)
        val layoutManager =  LinearLayoutManager(activity)
        binding!!.mRec.layoutManager =layoutManager
        database!!.reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)

                }

                override fun onCancelled(error: DatabaseError) {}

            })

        binding!!.mRec.adapter = usersAdapter

        database!!.reference.child("Users")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    users!!.clear()
                    for(snapshot1 in snapshot.children){
                        val user:User? = snapshot1.getValue(User::class.java)
                        if(!user!!.uid.equals(FirebaseAuth.getInstance().uid))
                            users!!.add(user)

                    }
                    usersAdapter!!.notifyDataSetChanged()
                }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!).setValue("Online")

    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!).setValue("Offline")
    }

}