package com.jtoru.project2.Utils

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jtoru.project2.Actitivies.ProfileActivity
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friends_row.view.*

class MutualFriendsAdapter(val context: Context): RecyclerView.Adapter<MutualFriendsAdapter.MutualFriendsVH>(){
    private var mDataset: MutableList<String> = mutableListOf()
    private val database = FirebaseDatabase.getInstance().reference

    class MutualFriendsVH(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var name: TextView
        var image: ImageView?= null

        init {
            name = v.findViewById(R.id.txt_userFriends)
            image = v.findViewById(R.id.img_picFriends)
        }
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MutualFriendsVH {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.friends_row, p0, false)
        return MutualFriendsVH(v)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(p0: MutualFriendsVH, p1: Int) {
        val id = mDataset[p1]
        getUser(p0,id)
        p0.itemView.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    private fun getUser(view: MutualFriendsVH,id:String) {
        val query = database.child("users").child(id)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user != null) {
                    view.name.text = user?.name
                    var pp = user.profilePic
                    if(pp != null && pp.isNotEmpty()) {
                        Picasso.get().load(pp).error(R.drawable.download).into(view.image)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }

    fun setMutualFriends(mutualFriends:MutableList<String>){
        mDataset = mutualFriends
        notifyDataSetChanged()
    }
}