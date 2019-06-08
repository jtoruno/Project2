package com.jtoru.project2.Utils

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    private val database = FirebaseDatabase.getInstance().reference
    fun bindToItem(user: Friendship){
        itemView.btn_acceptNotification.setOnClickListener {
            acceptFriend(user.sender?:"",user.receiver?:"")
        }
        itemView.btn_declineNotification.setOnClickListener {
            deleteFriend(user.sender?:"",user.receiver?:"")
        }
        getUser(user.receiver?:"")
    }

    private fun getUser(id:String) {
        val query = database.child("users").child(id)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user != null) {
                    itemView.user_not_txt_row.text = user?.name
                    var pp = user.profilePic
                    if(pp != null && pp.isNotEmpty()) {
                        Picasso.get().load(pp).error(R.drawable.download).into(itemView.img_picNotification)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }

    private fun acceptFriend(user1 : String, user2: String){
        database.child("friendship").child("$user1&$user2").child("status").setValue(true)
            .addOnSuccessListener {
                Log.e("AcceptFriend", "Accepted")
            }
            .addOnFailureListener {
                Log.e("AcceptFriend", "Error",it)
            }
    }

    private fun deleteFriend(user1 : String, user2: String){
        database.child("friendship").child("$user1&$user2").removeValue()
            .addOnSuccessListener {
                Log.e("DeleteFriend", "Deleted")
            }
            .addOnFailureListener {
                Log.e("DeleteFriend", "Error",it)
            }
    }
}