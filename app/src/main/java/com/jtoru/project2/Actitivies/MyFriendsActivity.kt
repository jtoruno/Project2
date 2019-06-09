package com.jtoru.project2.Actitivies

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
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
import com.jtoru.project2.Utils.FriendViewHolder

class MyFriendsActivity : AppCompatActivity() {
    private var id: String = ""
    private var idFriend: String = ""
    lateinit var recyclerView : RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    private var adapter : FirebaseRecyclerAdapter<Friendship, FriendViewHolder>? = null
    private lateinit var manager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_friends)
        supportActionBar?.title = "Friends"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        id = this.intent.getStringExtra("id")

        recyclerView = findViewById(R.id.recycler_myFriends)
        manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        database = FirebaseDatabase.getInstance()
        //mRef = database.getReference("users")
        mRef = database.reference
        query()
    }

    private fun query(){
        var user = FirebaseAuth.getInstance().currentUser?.uid
        val query = mRef.child("friendship")
        val options = FirebaseRecyclerOptions.Builder<Friendship>()
            .setQuery(query, Friendship::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Friendship,FriendViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FriendViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return FriendViewHolder(inflater.inflate(R.layout.friends_row,p0,false))
            }

            override fun onBindViewHolder(holder: FriendViewHolder, position: Int, model: Friendship) {
                if((model.sender == id || model.receiver == id) && model.status == true) {
                    holder.bindToItem(model)
                    if(model.sender != user)
                    {
                        holder.itemView.setOnClickListener {
                            val intent = Intent(this@MyFriendsActivity, ProfileActivity::class.java)
                            intent.putExtra("id", model.sender)
                            startActivity(intent)
                        }
                    }
                    else
                    {
                        holder.itemView.setOnClickListener {
                            val intent = Intent(this@MyFriendsActivity, ProfileActivity::class.java)
                            intent.putExtra("id", model.receiver)
                            startActivity(intent)
                        }
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



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }
}
