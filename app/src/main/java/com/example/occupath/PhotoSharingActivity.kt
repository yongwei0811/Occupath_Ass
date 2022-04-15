package com.example.occupath

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_photo_sharing.*
import java.util.*

class PhotoSharingActivity : AppCompatActivity() {
    //variable for selected image from the gallery. Transfer the Uri file that is not in the project to the project
    var picked_image : Uri? = null
    //use bitmap method in photo transfer in sharing process
    var picked_bitmap : Bitmap? = null
    //variable to use the firebase store feature
    private lateinit var storage : FirebaseStorage
    //variable to use the firebase database feature
    private lateinit var database : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_sharing)
        //sync the storage variable with firebase
        storage = FirebaseStorage.getInstance()
        //sync the database variable with firebase
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    //to test share button
    //when saving the post to firebase, have to give the post a name but it is unreasonable to use different variable each time to share
    //therefore, generate a random number and use the random number when saving post to firebase
    fun share_button_onclick (view: View){
        //variable to create the random number inside the share button function
        val uuid = UUID.randomUUID()
        //save the photo in jpg format
        val name_image = "${uuid}.jpg"
        //specify the storage reference
        val reference = storage.reference
        //create an image reference variable for storage
        val imageReference = reference.child("image").child(name_image)

        var name  = ""
        var email = ""

        auth = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    name = "${snapshot.child("name").value}"
                    email = "${snapshot.child("email").value}"
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        //if the selected image is not null, create the function to put it in a file using the image reference
        if (picked_image != null) {
            //taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            imageReference.putFile(picked_image!!).addOnSuccessListener { taskSnaphot ->
                //variable to save the uploaded image to firebase
                val uploadImageReference = FirebaseStorage.getInstance().reference.child("image").child(name_image)
                //id the uri of the uploaded image is successful, sync the image uri with the variable
                uploadImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val dowloadUrl = uri.toString()
                    //variable to sync using the id of the comment_text
                    val user_comment = comment_text.text.toString()
                    //variable to sync it with the current date to specify the date of the share image and comment
                    val date = com.google.firebase.Timestamp.now()
                    val name = name
                    val email = email
                    //post hash to save all the data
                    val postHashMap = hashMapOf<String,Any>()
                    //submit the address of the image in post hash
                    postHashMap.put("imageUrl", dowloadUrl)
                    //post the post comment in post hash
                    postHashMap.put("comment",user_comment)
                    //submit the current photo and comment post state in post hash
                    postHashMap.put("date",date)
                    postHashMap.put("name", name)
                    postHashMap.put("email", email)
                    //if all is successful, create the post collection
                    database.collection("Post").add(postHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            finish()
                            Toast.makeText(applicationContext,"Successful Post!", Toast.LENGTH_LONG).show()
                        }
                    //function to show the user if an error occur for any reason
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext,"Didn't Get To Post!", Toast.LENGTH_LONG).show()
                    }
                }
            //function to show an error toast message to the user if an error occur before the download start
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,"Didn't Get To Post!", Toast.LENGTH_LONG).show()
            }
        }
    }

    //function to assign a test when the user click on the blank image to add image from the gallery and use the name onclick on the function
    fun imageview_onclick (view: View){
        //obtain the Manifest.permission to access the user gallery
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //used to request code as one since the gallery is not yet granted access
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //variable to access the gallery directly if the user has permission
            val photo_file_intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //initialize the variable created for permission to access the gallery, we used to requestCode at 2 as the gallery was granted access
            startActivityForResult(photo_file_intent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //if the user has not yet been granted access to the gallery create request question to access the gallery
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //variable to access the gallery if the gallery if the gallery access request question is allow by the user
                val photo_file_intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photo_file_intent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //to show the image after the user choose the image from the gallery
    //create another result function to specify this
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //check the user permission again so that there are no error, if allow start the data transfer process
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            picked_image = data.data
            if (picked_image != null) {
                //since the build file are not in our project, imported into the project, create the resource variable of the android phone version is greater than 28
                if (Build.VERSION.SDK_INT > 27) {
                    val source = ImageDecoder.createSource(this.contentResolver,picked_image!!)
                    //use the source variable with the image decoder function using the bitmap method
                    picked_bitmap = ImageDecoder.decodeBitmap(source)
                    //transfer the selected image to the blank image we created in the design section
                    imageView.setImageBitmap(picked_bitmap)
                } else {
                    //if the android phone version used is less than 28, we access the gallery directly
                    picked_bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,picked_image)
                    imageView.setImageBitmap(picked_bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}