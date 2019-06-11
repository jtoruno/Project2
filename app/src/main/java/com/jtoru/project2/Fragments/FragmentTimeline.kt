package com.jtoru.project2.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jtoru.project2.Actitivies.AddPostActivity
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.Model.Post
import com.jtoru.project2.Model.User

import com.jtoru.project2.R
import com.jtoru.project2.Utils.NotificationViewHolder
import com.jtoru.project2.Utils.PhotoAlbumAdapter
import com.jtoru.project2.Utils.PostVH
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_fragment_timeline.view.*


class FragmentTimeline : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var profilePic: ImageView
    private var adapter : FirebaseRecyclerAdapter<Post,PostVH> ? = null
    private lateinit var manager: LinearLayoutManager
    lateinit var recycler : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fragment_timeline, container, false)
        profilePic = view.findViewById(R.id.time_line_pic)
        database = FirebaseDatabase.getInstance().reference

        recycler = view.findViewById(R.id.recycler_timeline)
        manager = LinearLayoutManager(activity!!)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recycler.layoutManager = manager

        view.time_line_txt.setOnClickListener {
            val intent = Intent(activity!!,AddPostActivity::class.java)
            intent.putExtra("id", FirebaseAuth.getInstance().currentUser?.uid)
            startActivity(intent)
        }
        getUser()
        query()
        return view
    }

    private fun query(){
        var user = FirebaseAuth.getInstance().currentUser?.uid
        val query = database.child("posts")
        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Post, PostVH>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostVH {
                val inflater = LayoutInflater.from(p0.context)
                return PostVH(inflater.inflate(R.layout.post_row,p0,false))
            }

            override fun onBindViewHolder(holder: PostVH, position: Int, model: Post) {
                holder.bindToItem(model)
            }

        }
        recycler.adapter = adapter
        adapter?.startListening()
    }

    private fun getUser(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = database.child("users").child(currentUser?.uid!!)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity",p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user!= null){
                    var pp = user.profilePic
                    if(pp != null && pp.isNotEmpty()) {
                        Picasso.get().load(pp).error(R.drawable.download).into(profilePic)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

}
