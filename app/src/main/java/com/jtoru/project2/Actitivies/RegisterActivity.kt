package com.jtoru.project2.Actitivies


import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.jtoru.project2.Utils.DatePickerFragment
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var spinnerGender: Spinner
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "Register your account!"

        database = FirebaseDatabase.getInstance().reference

        spinnerGender = findViewById(R.id.spin_genderRegister)
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this, R.array.spin_Gender,
                R.layout.spinner_item
            )

        // Specify the layout to use when the list of choices appears
        staticAdapter
            .setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerGender.adapter = staticAdapter

        img_calendarRegister.setOnClickListener {
            showDatePickerDialog()
        }

        btn_cancelRegister.setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        }
        btn_acceptRegister.setOnClickListener {
            createUser()
        }



    }

    fun createUser(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val city = input_cityRegister.text.toString()
        val birth = datePicker.text.toString()
        val number = input_phoneRegister.text.toString()
        val gender = spinnerGender.selectedItem.toString()
        Log.e("ID",currentUser?.uid)
        Log.e("NAME",currentUser?.displayName)
        Log.e("CITY",city)
        Log.e("BIRTH",birth)
        Log.e("NUMBER",number)
        Log.e("EMAIL",currentUser?.email)
        Log.e("GENDER",gender)
        if (TextUtils.isEmpty(city)) {
            input_cityRegister.error = "Required"
            return
        }
        if (TextUtils.isEmpty(number)) {
            input_phoneRegister.error = "Required"
            return
        }
        btn_acceptRegister.isEnabled = false
        val user = User(currentUser?.uid?:"",currentUser?.displayName?:"","",
            mutableListOf(), mutableListOf(),city,birth,number,currentUser?.email?:"",gender)
        database.child("users").child(currentUser?.uid?:"Error").setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this@RegisterActivity, "Added Correctly", Toast.LENGTH_SHORT).show()
                btn_acceptRegister.isEnabled = true
                val i = Intent(this,MainActivity::class.java)
                startActivity(i)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@RegisterActivity, "Error with user", Toast.LENGTH_SHORT).show()
                Log.e("ID",currentUser?.uid)
                Log.e("NAME",currentUser?.displayName)
                Log.e("CITY",city)
                Log.e("BIRTH",birth)
                Log.e("NUMBER",number)
                Log.e("EMAIL",currentUser?.email)
                Log.e("GENDER",gender)
                btn_acceptRegister.isEnabled = true

            }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun showDatePickerDialog(){
        val listener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth ->
            val selectedDate = twoDigits(dayOfMonth) + "/" + (twoDigits(month + 1)) + "/" + twoDigits(year)
            datePicker.text = selectedDate

        }
        val newFragment: DatePickerFragment = DatePickerFragment.newInstance(listener)
        newFragment.show(supportFragmentManager,"datePicker")
    }
    fun twoDigits(n: Int): String {
        return if (n <= 9) "0$n" else n.toString()
    }
}

