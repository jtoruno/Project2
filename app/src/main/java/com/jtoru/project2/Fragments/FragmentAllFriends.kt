package com.jtoru.project2.Fragments

import android.content.Intent
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
import com.jtoru.project2.Actitivies.FriendsActivity
import com.jtoru.project2.Actitivies.ProfileActivity
import com.jtoru.project2.Model.Friendship
import com.jtoru.project2.R
import com.jtoru.project2.Utils.FriendViewHolder






class FragmentAllFriends : Fragment() {
    private lateinit var myActivity: FriendsActivity
    private var id: String = ""
    lateinit var recyclerView : RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    private var adapter : FirebaseRecyclerAdapter<Friendship, FriendViewHolder>? = null
    private lateinit var manager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_fragment_all_friends, container, false)

        myActivity = activity!! as FriendsActivity

        recyclerView = view.findViewById(R.id.recycler_allFriends )
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
        id = myActivity.getId()
        var me = FirebaseAuth.getInstance().currentUser?.uid
        val query = mRef.child("friendship").orderByChild("status").equalTo(true)
        val options = FirebaseRecyclerOptions.Builder<Friendship>()
            .setQuery(query, Friendship::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Friendship, FriendViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FriendViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return FriendViewHolder(inflater.inflate(R.layout.friends_row,p0,false))
            }

            override fun onBindViewHolder(holder: FriendViewHolder, position: Int, model: Friendship) {
                if(model.sender == id || model.receiver == id) {
                    holder.bindToItem(model, id)
                    if(model.sender != id)
                    {
                        holder.itemView.setOnClickListener {
                            val intent = Intent(activity!!, ProfileActivity::class.java)
                            if(id == me)
                            {
                                intent.putExtra("owner", true)
                            }
                            intent.putExtra("id", model.sender)
                            startActivity(intent)
                        }
                    }
                    else
                    {
                        val intent = Intent(activity!!, ProfileActivity::class.java)
                        if(id == me)
                        {
                            intent.putExtra("owner", true)
                        }
                        intent.putExtra("id", model.receiver)
                        startActivity(intent)
                    }
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
