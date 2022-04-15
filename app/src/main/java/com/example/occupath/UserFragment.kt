package com.example.occupath

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.occupath.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //view binding
    private lateinit var binding: FragmentUserBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //firebase current user
    private lateinit var firebaseUser: FirebaseUser

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater,container,false)


        firebaseAuth = FirebaseAuth.getInstance()


        firebaseUser = firebaseAuth.currentUser!!

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadUserInfo()

        //handle click, open edit profile
        binding.profileEditBtn.setOnClickListener {
            val intent = Intent(this@UserFragment.requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this@UserFragment.requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        //handle click, verify user
        binding.accountStatusRTv.setOnClickListener {
            if(firebaseUser.isEmailVerified) {
                val text = "Already verified!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            }
            else {
                emailVerificationDialog()
            }
        }

        //handle click, verify user
        binding.accountStatusGTv.setOnClickListener {
            if(firebaseUser.isEmailVerified) {
                val text = "Already verified!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            }
            else {
                emailVerificationDialog()
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun emailVerificationDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Verify Email")
            .setMessage("Are you sure you want to send email verification instructions to your email \n${firebaseUser.email}")
            .setPositiveButton("SEND") { d,e->
                sendEmailVerification()
            }
            .setNegativeButton("CANCEL") { d,e->
                d.dismiss()
            }
            .show()
    }

    private fun sendEmailVerification() {
        progressDialog.setMessage("Sending email verification instructions to email ${firebaseUser.email}")
        progressDialog.show()

        firebaseUser.sendEmailVerification()
            .addOnSuccessListener {
                progressDialog.dismiss()
                val text = "Instructions sent! Check your email \n${firebaseUser.email}"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                val text = "Failed to send due to ${e.message}"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            }
    }

    private fun loadUserInfo() {
        //check if user is verified
        if(firebaseUser.isEmailVerified) {
            //binding.accountStatusTv.text = "Verified"
            binding.accountStatusGTv.visibility = View.VISIBLE
            binding.accountStatusRTv.visibility = View.GONE
        }
        else {
            //binding.accountStatusTv.text = "Not Verified"
            binding.accountStatusGTv.visibility = View.GONE
            binding.accountStatusRTv.visibility = View.VISIBLE
        }

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val uid = "${snapshot.child("uid").value}"

                    //set data
                    binding.nameTv.text = name
                    binding.emailTv.text = email

                    //set image
                    try {
                        Glide.with(this@UserFragment).load(profileImage).placeholder(R.drawable.ic_person_gray).into(binding.profileIv)
                    }
                    catch (e: Exception) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}