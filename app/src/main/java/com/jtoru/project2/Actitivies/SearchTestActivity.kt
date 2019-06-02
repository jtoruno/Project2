package com.jtoru.project2.Actitivies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.jtoru.project2.Utils.ItemViewHolder

class SearchTestActivity : AppCompatActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var database: FirebaseDatabase
    lateinit var mRef: DatabaseReference
    private lateinit var manager: LinearLayoutManager
    private var adapter : FirebaseRecyclerAdapter<User, ItemViewHolder>? = null
    private lateinit var filterText: TextInputEditText
    private var textQuery = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_test)


        recyclerView = findViewById(R.id.recycler_test)
        manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        recyclerView.layoutManager = manager
        database = FirebaseDatabase.getInstance()
        mRef = database.getReference("users").ref
        filterText = findViewById(R.id.input_test)
        filterText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                queryInfo(p0.toString())
            }

        })
    }

    fun queryInfo(text:String){
        Log.e("Error","FAGIT")
        val itemQuery: Query = mRef//.orderByChild("name")//.startAt(text).endAt(text + "\uf8ff")
        val options =  FirebaseRecyclerOptions.Builder<User>()
            .setQuery(itemQuery, User::class.java)
            .build()
        var adapter = object : FirebaseRecyclerAdapter<User, ItemViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return ItemViewHolder(inflater.inflate(R.layout.friends_row, p0,false))
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: User) {
                holder.bindToItem(model)
            }
        }
        recyclerView.adapter = adapter
    }



    override fun onStart() {
        super.onStart()
        val options =  FirebaseRecyclerOptions.Builder<User>()
            .setQuery(mRef, User::class.java)
            .build()
        var adapter = object : FirebaseRecyclerAdapter<User, ItemViewHolder>(options){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return ItemViewHolder(inflater.inflate(R.layout.friends_row, p0,false))
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: User) {
                holder.bindToItem(model)
            }
        }
        recyclerView.adapter = adapter

    }

    override fun onStop() {
        super.onStop()

    }
}
