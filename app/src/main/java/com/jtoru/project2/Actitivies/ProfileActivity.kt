package com.jtoru.project2.Actitivies

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.database.GenericTypeIndicator



class ProfileActivity : AppCompatActivity() {

    private enum class FriendshipState {
        ADD, WAITING, PENDING, ACCEPTED
    }

    private lateinit var database: DatabaseReference
    private var owner = false
    private var id : String = ""
    private var state = FriendshipState.ADD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        owner = this.intent.getBooleanExtra("owner", false)
        id = this.intent.getStringExtra("id")
        database = FirebaseDatabase.getInstance().reference


        btn_editInfoProfile.setOnClickListener {
            val i = Intent(this,ProfileDetailsActivity::class.java)
            i.putExtra("id",id)
            i.putExtra("owner",owner)
            startActivity(i)
        }
        btn_viewFriendsProfile.setOnClickListener {
            val i = Intent(this,FriendsActivity::class.java)
            startActivity(i)
        }

        btn_addFriendProfile.setOnClickListener {
            Log.e("ONCLICK",state.toString())
            when (state)
            {
                FriendshipState.ADD -> {
                    friendRequest()
                }
                FriendshipState.WAITING ->
                {

                }
                FriendshipState.PENDING ->
                {
                    acceptFriend()
                }
                FriendshipState.ACCEPTED ->
                {
                    deleteFriend()
                }
            }
            Log.e("ONCLICK",state.toString())
        }

        if(!owner)
        {
            btn_addFriendProfile.visibility = View.VISIBLE
        }

        getUser()
        //setFriendshipState()
        //Log.e("ONCREATE",state.toString())
        test()

    }

    private fun getUser(){
        val query = database.child("users").child(id)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity",p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user!= null){
                    txt_nameProfile.text = user.name
                }
            }
        }
        query.addValueEventListener(listener)
    }

    private fun test()
    {
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        var userKey1 = user1 + "&" + user2
        var userKey2 = user2 + "&" + user1
        val query1 = database.child("friendship")
        val listener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity",p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var check = false
                for (postSnapshot in p0.children) {
                    if(postSnapshot.key == userKey1 || postSnapshot.key == userKey2){
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
                                btn_addFriendProfile.isEnabled = false
                            } else {
                                state = FriendshipState.PENDING
                                btn_addFriendProfile.setImageResource(R.drawable.ic_transfer_within_a_station_black_24dp)
                                btn_addFriendProfile.isEnabled = true
                            }

                        }
                        Log.e("After",state.toString())
                    }

                }
                if(!check)
                {
                    state = FriendshipState.ADD
                    btn_addFriendProfile.setImageResource(R.drawable.ic_person_add_black_24dp)
                    btn_addFriendProfile.clearColorFilter()
                    btn_addFriendProfile.isEnabled = true
                    Log.e("AfterElse",state.toString())
                }
            }

        }
        query1.addValueEventListener(listener)
    }

    private fun setFriendshipState()
    {
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        val query1 = database.child("friendship").child("$user1&$user2")
        val query2 = database.child("friendship").child("$user2&$user1")
        Log.e("QUERY1",query1.toString())
        Log.e("QUERY2",query2.toString())
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity",p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val friendship = p0.getValue(Friendship::class.java)
                if (friendship != null) {
                    if (friendship.status == true) {
                        state = FriendshipState.ACCEPTED
                        btn_addFriendProfile.setImageResource(R.drawable.ic_check_box_black_24dp)
                        btn_addFriendProfile.setColorFilter(R.color.colorPrimary)
                        btn_addFriendProfile.isEnabled = true
                        btn_addFriendProfile.visibility = View.VISIBLE
                    } else {
                        if (friendship.sender == user1) {
                            state = FriendshipState.WAITING
                            btn_addFriendProfile.setImageResource(R.drawable.ic_transfer_within_a_station_black_24dp)
                            btn_addFriendProfile.clearColorFilter()
                            btn_addFriendProfile.isEnabled = false
                        } else {
                            state = FriendshipState.PENDING
                            btn_addFriendProfile.setImageResource(R.drawable.ic_transfer_within_a_station_black_24dp)
                            btn_addFriendProfile.clearColorFilter()
                            btn_addFriendProfile.isEnabled = true
                        }

                    }
                    Log.e("After",state.toString())
                }
                else
                {
                    state = FriendshipState.ADD
                    btn_addFriendProfile.setImageResource(R.drawable.ic_person_add_black_24dp)
                    btn_addFriendProfile.clearColorFilter()
                    btn_addFriendProfile.isEnabled = true
                    Log.e("AfterElse",state.toString())
                }

            }
        }

        query1.addValueEventListener(listener)
        query2.addValueEventListener(listener)
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
                btn_addFriendProfile.isEnabled = false
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Friend request not send!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteFriend(){
        var user1 = FirebaseAuth.getInstance().currentUser?.uid
        var user2 = id
        database.child("friendship").child("$user2&$user1").removeValue()
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Friend deleted :(", Toast.LENGTH_SHORT).show()
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
}
