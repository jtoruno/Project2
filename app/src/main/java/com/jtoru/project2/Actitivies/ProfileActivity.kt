package com.jtoru.project2.Actitivies

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.jtoru.project2.Utils.MyFriendsViewHolder
import com.jtoru.project2.Utils.PhotoAlbumViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.collections.HashMap


class ProfileActivity : AppCompatActivity() {

    private enum class FriendshipState {
        ADD, WAITING, PENDING, ACCEPTED
    }

    private lateinit var database: DatabaseReference
    private lateinit var mStorageRef: StorageReference
    private var owner = false
    private var id: String = ""
    private var state = FriendshipState.ADD
    private var fileUri: Uri? = null
    private var adapterAlbum : FirebaseRecyclerAdapter<HashMap<String,String>, PhotoAlbumViewHolder>? = null
    private lateinit var managerPhotos: LinearLayoutManager
    lateinit var photoAlbum : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        owner = this.intent.getBooleanExtra("owner", false)
        id = this.intent.getStringExtra("id")
        database = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference

        photoAlbum = findViewById(R.id.recycler_photoProfile)
        managerPhotos = LinearLayoutManager(this)
        managerPhotos.orientation = LinearLayoutManager.HORIZONTAL
        managerPhotos.reverseLayout = true
        managerPhotos.stackFromEnd = true
        photoAlbum.layoutManager = managerPhotos

        btn_editInfoProfile.setOnClickListener {
            val i = Intent(this, ProfileDetailsActivity::class.java)
            i.putExtra("id", id)
            i.putExtra("owner", owner)
            startActivity(i)
        }


        btn_addFriendProfile.setOnClickListener {
            Log.e("ONCLICK", state.toString())
            when (state) {
                FriendshipState.ADD -> {
                    friendRequest()
                }
                FriendshipState.WAITING -> {
                    deleteFriend("Friend request canceled")
                }
                FriendshipState.PENDING -> {
                    acceptFriend()
                }
                FriendshipState.ACCEPTED -> {
                    deleteFriend("Friend deleted :(")
                }
            }
            Log.e("ONCLICK", state.toString())
        }

        txt_addPhotoProfile.setOnClickListener {
            addPhoto()
        }

        if (!owner) {
            setFriendshipState()
            btn_addFriendProfile.visibility = View.VISIBLE
            btn_viewFriendsProfile.setOnClickListener {
                val i = Intent(this, FriendsActivity::class.java)
                i.putExtra("id", id)
                startActivity(i)
            }
        } else {
            img_profilePic.setOnClickListener {
                changeProfilePic()
            }
            btn_viewFriendsProfile.setOnClickListener {
                val i = Intent(this, MyFriendsActivity::class.java)
                i.putExtra("id", id)
                startActivity(i)
            }
        }

        getUser()
        //getPhotos()

    }

    private fun getUser() {
        val query = database.child("users").child(id)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user != null) {
                    txt_nameProfile.text = user.name
                    var pp = user.profilePic
                    if(pp != null && pp.isNotEmpty()) {
                        Picasso.get().load(pp).error(R.drawable.download).into(img_profilePic)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }

    /*private fun getPhotos()
    {
        var user = FirebaseAuth.getInstance().currentUser?.uid
        val query = database.child("users").child(id).child("pictures")
        val options = FirebaseRecyclerOptions.Builder<HashMap<String,String>>()
            .setQuery(query,HashMap::class.java)
            .build()

        adapterAlbum = object : FirebaseRecyclerAdapter<HashMap<String,String>,PhotoAlbumViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoAlbumViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return PhotoAlbumViewHolder(inflater.inflate(R.layout.photos_row,p0,false))
            }

            override fun onBindViewHolder(holder: PhotoAlbumViewHolder, position: Int, model: HashMap<String,String>) {

            }

        }
        photoAlbum.adapter = adapterAlbum
        adapterAlbum?.startListening()
    }*/


    private fun setFriendshipState() {
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        var userKey1 = user1 + "&" + user2
        var userKey2 = user2 + "&" + user1
        val query1 = database.child("friendship")
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var check = false
                for (postSnapshot in p0.children) {
                    if (postSnapshot.key == userKey1 || postSnapshot.key == userKey2) {
                        check = true
                        val friendship = postSnapshot.getValue(Friendship::class.java)
                        if (friendship?.status == true) {
                            state = FriendshipState.ACCEPTED
                            btn_addFriendProfile.setImageResource(R.drawable.ic_check_box_black_24dp)
                            btn_addFriendProfile.isEnabled = true
                            btn_addFriendProfile.visibility = View.VISIBLE
                        } else {
                            if (friendship?.sender == user1) {
                                state = FriendshipState.WAITING
                                btn_addFriendProfile.setImageResource(R.drawable.ic_transfer_within_a_station_black_24dp)
                                btn_addFriendProfile.isEnabled = true
                            } else {
                                state = FriendshipState.PENDING
                                btn_addFriendProfile.setImageResource(R.drawable.ic_transfer_within_a_station_black_24dp)
                                btn_addFriendProfile.isEnabled = true
                            }

                        }
                        Log.e("After", state.toString())
                    }

                }
                if (!check) {
                    state = FriendshipState.ADD
                    btn_addFriendProfile.setImageResource(R.drawable.ic_person_add_black_24dp)
                    btn_addFriendProfile.clearColorFilter()
                    btn_addFriendProfile.isEnabled = true
                    Log.e("AfterElse", state.toString())
                }
            }

        }
        query1.addValueEventListener(listener)
    }


    private fun changeProfilePic() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, TAKE_PICTURE)
    }

    private fun addPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, ADD_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data?.data
                //val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, fileUri)
                Picasso.get().load(fileUri).error(R.drawable.download).into(img_profilePic)
                val ref = mStorageRef.child(id)
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
                        val downloadUri = task.result
                        loadProfilePic(downloadUri.toString())
                    } else {
                        // Handle failures
                        // ...
                    }
                }

            }

        }
        if(requestCode == ADD_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data?.data
                //val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, fileUri)
                //Picasso.get().load(fileUri).error(R.drawable.download).into(img_profilePic)
                val ref = mStorageRef.child(id).child(UUID.randomUUID().toString())
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
                        val downloadUri = task.result
                        savePhoto(downloadUri.toString())
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun loadProfilePic(downloadUri: String) {
        database.child("users").child(id).child("profilePic").setValue(downloadUri)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Profile pictured saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Profile picture not saved", Toast.LENGTH_SHORT).show()
            }
    }

    private fun savePhoto(downloadUri: String)
    {
        database.child("users").child(id).child("pictures").push().setValue(downloadUri)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Pictured saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Picture not saved", Toast.LENGTH_SHORT).show()
            }
    }






    private fun friendRequest(){
        //La llave de friendship es la suma del id del usuario que envia & el usuario que recibe
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        var friendship = Friendship(user1,user2)
        database.child("friendship").child("$user1&$user2").setValue(friendship)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Friend request send!", Toast.LENGTH_SHORT).show()
                state = FriendshipState.WAITING
                Log.e("ONREQUEST",state.toString())
                btn_addFriendProfile.setImageResource(R.drawable.ic_transfer_within_a_station_black_24dp)
                btn_addFriendProfile.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Friend request not send!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteFriend(toast:String){
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        database.child("friendship").child("$user2&$user1").removeValue()
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, toast, Toast.LENGTH_SHORT).show()
                state = FriendshipState.ADD
                btn_addFriendProfile.setImageResource(R.drawable.ic_person_add_black_24dp)
                btn_addFriendProfile.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Friend not deleted :)", Toast.LENGTH_SHORT).show()
            }
        database.child("friendship").child("$user1&$user2").removeValue()
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Friend deleted :(", Toast.LENGTH_SHORT).show()
                state = FriendshipState.ADD
                btn_addFriendProfile.setImageResource(R.drawable.ic_person_add_black_24dp)
                btn_addFriendProfile.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Friend not deleted :)", Toast.LENGTH_SHORT).show()
            }
    }

    private fun acceptFriend(){
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        database.child("friendship").child("$user2&$user1").child("status").setValue(true)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Friend accepted!", Toast.LENGTH_SHORT).show()
                state = FriendshipState.ACCEPTED
                btn_addFriendProfile.setImageResource(R.drawable.ic_check_box_black_24dp)
                btn_addFriendProfile.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Friend not added", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        private const val TAKE_PICTURE = 101
        private const val ADD_PICTURE = 102
    }
}
