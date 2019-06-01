package com.jtoru.project2.Fragments


import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.firebase.database.DatabaseReference
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.jtoru.project2.Utils.ItemViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.jtoru.project2.Actitivies.ProfileActivity

class FragmentPeopleSearch : Fragment() {
    lateinit var recyclerView : RecyclerView
    lateinit var database: DatabaseReference
    private lateinit var manager: LinearLayoutManager
    private var adapter : FirebaseRecyclerAdapter<User, ItemViewHolder>? = null
    private lateinit var btnSearch:ImageButton
    private lateinit var filterText:TextInputEditText
    private var textQuery = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_fragment_people_search, container, false)

        recyclerView = view.findViewById(R.id.recycler_peopleSearch)
        manager = LinearLayoutManager(activity!!)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        database = FirebaseDatabase.getInstance().reference
        filterText = activity!!.findViewById(R.id.input_search)
        filterText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textQuery = p0.toString()
            }

        })
        btnSearch = activity!!.findViewById(R.id.btn_search)
        btnSearch.setOnClickListener {
        }
        queryInfo()

        return view
    }

    fun queryInfo(){
        val itemQuery = getQuery(database)
        val options =  FirebaseRecyclerOptions.Builder<User>()
            .setQuery(itemQuery, User::class.java)
            .build()
        adapter = object : FirebaseRecyclerAdapter<User, ItemViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
                val inflater = LayoutInflater.from(activity!!)
                return ItemViewHolder(inflater.inflate(R.layout.friends_row, p0,false))

            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: User) {
                holder.bindToItem(model)
            }
        }
        recyclerView.adapter = adapter
    }

    fun getQuery(databaseReference: DatabaseReference): Query {
        return databaseReference.child("users")
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
