package com.jtoru.project2.Utils

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.Model.User
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    private val database = FirebaseDatabase.getInstance().reference
    fun bindToItem(user: Friendship){
        itemView.user_not_txt_row.text = user.receiver
        itemView.btn_acceptNotification.setOnClickListener {
            acceptFriend(user.sender?:"",user.receiver?:"")
        }
        itemView.btn_declineNotification.setOnClickListener {
            deleteFriend(user.sender?:"",user.receiver?:"")
        }
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
        database.child("friendship").child("$user2&$user1").removeValue()
            .addOnSuccessListener {
                Log.e("DeleteFriend", "Deleted")
            }
            .addOnFailureListener {
                Log.e("DeleteFriend", "Error",it)
            }
    }
}