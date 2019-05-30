package com.jtoru.project2.Actitivies

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.jtoru.project2.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun onBackPressed() {
            val dialogBack = AlertDialog.Builder(this)
            dialogBack.setTitle("Exit")
            dialogBack.setMessage("Do you want to close session?")
            dialogBack.setPositiveButton("Yes"){
                    dialog,_ ->
                dialog.dismiss()
                finish()
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
            }
            dialogBack.setNegativeButton("No"){
                    dialog,_ ->
                dialog.dismiss()
            }
            val d = dialogBack.create()
            d.show()
        }
    }

