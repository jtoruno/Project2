package com.jtoru.project2.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jtoru.project2.Actitivies.AddPostActivity
import com.jtoru.project2.Model.User

import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_fragment_timeline.view.*


class FragmentTimeline : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var profilePic: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fragment_timeline, container, false)
        profilePic = view.findViewById(R.id.time_line_pic)
        database = FirebaseDatabase.getInstance().reference
        view.time_line_txt.setOnClickListener {
            val intent = Intent(activity!!,AddPostActivity::class.java)
            startActivity(intent)
        }
        getUser()

        return view
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


}
