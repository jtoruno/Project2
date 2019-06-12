package com.jtoru.project2.Fragments

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jtoru.project2.Actitivies.ProfileActivity
import com.jtoru.project2.Model.Post
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.jtoru.project2.Utils.ItemViewHolder
import com.jtoru.project2.Utils.PostVH

class FragmentPostsSearch : Fragment() {
    lateinit var searchView : SearchView
    lateinit var recyclerView : RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    private lateinit var manager: LinearLayoutManager
    private var adapter : FirebaseRecyclerAdapter<Post, PostVH>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_fragment_posts_search, container, false)
        recyclerView = view.findViewById(R.id.recycler_postSearch)
        manager = LinearLayoutManager(activity!!)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        database = FirebaseDatabase.getInstance()
        mRef = database.reference
        searchView = activity!!.findViewById(R.id.searchView)
        /*
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                queryInfo(p0?:"")
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })*/
        return view
    }

    fun queryInfo(value : String){
        //val itemQuery: Query = mRef//.orderByChild("name")//.startAt(text).endAt(text + "\uf8ff")
        val mquery = FirebaseDatabase.getInstance()
            .reference
            .child("posts")
            .orderByChild("description")
            .startAt(value)
            .endAt(value + "\uf8ff")
        val options =  FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(mquery, Post::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<Post, PostVH>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostVH {
                val inflater = LayoutInflater.from(p0.context)
                return PostVH(inflater.inflate(R.layout.education_row, p0,false))
            }

            override fun onBindViewHolder(holder: PostVH, position: Int, model: Post) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(activity!!, ProfileActivity::class.java)
                    intent.putExtra("id",model.idUser)
                    startActivity(intent)
                }
                holder.bindToItem(model)

            }
        }
        adapter?.notifyDataSetChanged()
        recyclerView.adapter = adapter
        adapter?.startListening()
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
