package com.jtoru.project2.Utils

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.Model.Post
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friends_row.view.*
import kotlinx.android.synthetic.main.post_row.view.*

class PostVH(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private val database = FirebaseDatabase.getInstance().reference
    fun bindToItem(post: Post){
        itemView.txt_datePost.text = post.publish.toString()
        itemView.txt_descPost.text = post.description
        var pp = post.content
        if(pp != null && pp.isNotEmpty()) {
            itemView.imageView5.visibility = View.VISIBLE
            Picasso.get().load(pp).error(R.drawable.download).into(itemView.imageView5)
        }
        else
        {
            itemView.imageView5.visibility = View.GONE
        }
        getUser(post.idUser?:"")
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
                    itemView.txt_userPost.text = user.name
                    var pp = user.profilePic
                    if(pp != null && pp.isNotEmpty()) {
                        Picasso.get().load(pp).error(R.drawable.download).into(itemView.img_picPost)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }
}