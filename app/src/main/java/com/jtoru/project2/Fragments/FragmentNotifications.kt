package com.jtoru.project2.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jtoru.project2.Model.Friendship

import com.jtoru.project2.R
import com.jtoru.project2.Utils.NotificationViewHolder


class FragmentNotifications : Fragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    private var adapter : FirebaseRecyclerAdapter<Friendship,NotificationViewHolder> ? = null
    private lateinit var manager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fragment_notifications, container, false)

        recyclerView = view.findViewById(R.id.fragment_notifications_recycler)
        manager = LinearLayoutManager(activity!!)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        database = FirebaseDatabase.getInstance()
        //mRef = database.getReference("users")
        mRef = database.reference
        query()

        return view
    }

    private fun query(){
        var user = FirebaseAuth.getInstance().currentUser?.uid
        val query = mRef.child("friendship").orderByChild("receiver").equalTo(user)
        val options = FirebaseRecyclerOptions.Builder<Friendship>()
            .setQuery(query, Friendship::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Friendship,NotificationViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NotificationViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return NotificationViewHolder(inflater.inflate(R.layout.notification_row,p0,false))
            }

            override fun onBindViewHolder(holder: NotificationViewHolder, position: Int, model: Friendship) {
                if(model.status == false) {
                    holder.bindToItem(model)
                }
                else
                {
                    holder.itemView.visibility = View.GONE
                    holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }

        }
        recyclerView.adapter = adapter
        adapter?.startListening()
    }


}
