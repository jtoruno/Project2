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
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.gms.common.util.Strings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jtoru.project2.Actitivies.FriendsActivity
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.R
import com.jtoru.project2.Utils.FriendViewHolder
import com.jtoru.project2.Utils.MutualFriendsAdapter
import com.squareup.picasso.Picasso


class FragmentMutualFriends : Fragment() {
    private lateinit var myActivity: FriendsActivity
    private var id: String = ""
    lateinit var recyclerView : RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    lateinit var adapter : MutualFriendsAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var friendsMe: MutableSet<String>
    private lateinit  var friendsId: MutableSet<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_fragment_mutual_friends, container, false)

        myActivity = activity!! as FriendsActivity
        friendsMe = mutableSetOf()
        friendsId = mutableSetOf()
        recyclerView = view.findViewById(R.id.recycler_mutualFriends )
        manager = LinearLayoutManager(activity!!)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        adapter = MutualFriendsAdapter(activity!!)
        recyclerView.adapter = adapter
        database = FirebaseDatabase.getInstance()
        //mRef = database.getReference("users")
        mRef = database.reference
        query()

        return view
    }

    private fun query(){
        id = myActivity.getId()
        var me = FirebaseAuth.getInstance().currentUser?.uid
        //Log.e("id",id)
        //Log.e("me",me)
        val query = mRef.child("friendship").orderByChild("status").equalTo(true)
        val listener = object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children )
                {
                    val friendshit = postSnapshot.getValue(Friendship::class.java)
                    if (friendshit?.sender == me)
                    {
                        friendsMe.add(friendshit?.receiver?:"")
                    }
                    if(friendshit?.receiver == me)
                    {
                        friendsMe.add(friendshit?.sender?:"")
                    }
                    if (friendshit?.sender == id)
                    {
                        friendsId.add(friendshit?.receiver?:"")
                    }
                    if(friendshit?.receiver == id)
                    {
                        friendsId.add(friendshit?.sender?:"")
                    }
                }
                var result = friendsMe.intersect(friendsId)
                adapter.setMutualFriends(result.toMutableList())
            }

        }
        query.addValueEventListener(listener)
    }
}
