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
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photos_row.view.*

class PhotoAlbumViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    private val database = FirebaseDatabase.getInstance().reference
    fun bindToItem(url: String){
        if(!url.isNullOrEmpty()) {
            Picasso.get().load(url).error(R.drawable.download).into(itemView.img_profileRow)
        }
    }

}