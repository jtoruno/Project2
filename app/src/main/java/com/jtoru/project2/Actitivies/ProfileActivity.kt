package com.jtoru.project2.Actitivies

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jtoru.project2.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile"

        btn_editInfoProfile.setOnClickListener {
            val i = Intent(this,ProfileDetailsActivity::class.java)
            startActivity(i)
        }

    }
}
