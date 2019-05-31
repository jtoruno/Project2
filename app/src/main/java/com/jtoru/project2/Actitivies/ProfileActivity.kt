package com.jtoru.project2.Actitivies

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var owner = false
    private var id : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        owner = this.intent.getBooleanExtra("owner", false)
        id = this.intent.getStringExtra("id")
        database = FirebaseDatabase.getInstance().reference

        btn_editInfoProfile.setOnClickListener {
            val i = Intent(this,ProfileDetailsActivity::class.java)
            i.putExtra("id",id)
            i.putExtra("owner",owner)
            startActivity(i)
        }
        getUser()
    }

    private fun getUser(){
        val query = database.child("users").child(id)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity",p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user!= null){
                    txt_nameProfile.text = user.name
                }
            }
        }
        query.addValueEventListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }
}
