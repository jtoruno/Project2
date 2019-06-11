package com.jtoru.project2.Actitivies

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jtoru.project2.Model.ImageUploadInfo
import com.jtoru.project2.Model.Post
import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var mStorageRef: StorageReference
    private var fileUri: Uri? = null
    private var downloadUri: String? = ""
    private var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        supportActionBar?.title = "Add Post"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        imageView4.visibility = View.GONE
        id = this.intent.getStringExtra("id")
        database = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference
        button.setOnClickListener {
            uploadPhoto()
            imageView4.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.add_action)
        {
            if(add_post_txt.text.isNotEmpty()){
                adddPost()
            }
            else{
                Toast.makeText(this,"Please!, Add a description.",Toast.LENGTH_SHORT).show()
            }
            return true
        }
        else
        {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun adddPost(){
        if(fileUri != null)
        {
            savePhoto()
        }
        else
        {
            var idPost = UUID.randomUUID().toString()
            var userId = id
            var publish = Date().time
            var desc = add_post_txt.text.toString()
            var type = "TEXT"
            var content = ""
            val post = Post(idPost,userId,publish,desc,type,content)
            database.child("posts").child(idPost).setValue(post)
                .addOnSuccessListener {
                    Toast.makeText(this@AddPostActivity, "Post saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@AddPostActivity, "Post not saved!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, TAKE_PICTURE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data?.data
                //val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, fileUri)
                Picasso.get().load(fileUri).error(R.drawable.download).into(imageView4)
            }

        }
    }


    private fun savePhoto(){
        val ref = mStorageRef.child(UUID.randomUUID().toString())
        var uploadTask = ref.putFile(fileUri!!)

        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri = task.result.toString()
                var idPost = UUID.randomUUID().toString()
                var userId = id
                var publish = Date().time
                var desc = add_post_txt.text.toString()
                var type = "IMAGE"
                var content = downloadUri
                val post = Post(idPost,userId,publish,desc,type,content)
                database.child("posts").child(idPost).setValue(post)
                    .addOnSuccessListener {
                        Toast.makeText(this@AddPostActivity, "Post saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@AddPostActivity, "Post not saved!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Handle failures
                // ...
            }
        }
    }
    companion object {
        private const val TAKE_PICTURE = 101
    }
}
