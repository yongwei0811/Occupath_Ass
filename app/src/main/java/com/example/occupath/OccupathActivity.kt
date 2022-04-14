package com.example.occupath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OccupathActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occupath)

        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500) // 1.5 secs
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser

        if(firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        else {
            val ref = FirebaseDatabase.getInstance().getReference("Users")

            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        startActivity(Intent(this@OccupathActivity, FeedFragment::class.java))
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}