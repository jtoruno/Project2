package com.jtoru.project2.Actitivies

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jtoru.project2.Model.EducationUploadInfo
import com.jtoru.project2.Model.ImageUploadInfo
import com.jtoru.project2.Model.User
import com.jtoru.project2.R
import com.jtoru.project2.Utils.DatePickerFragment
import com.jtoru.project2.Utils.EducationAdapter
import kotlinx.android.synthetic.main.activity_profile_details.*
import kotlinx.android.synthetic.main.activity_profile_details.view.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class ProfileDetailsActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var owner = false
    private var id : String = ""
    private lateinit var btnEducation: TextView
    lateinit var adapterEducation : EducationAdapter
    private lateinit var managerEducation: LinearLayoutManager
    lateinit var education : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        database = FirebaseDatabase.getInstance().reference
        owner = this.intent.getBooleanExtra("owner", false)
        id = this.intent.getStringExtra("id")
        btnEducation = findViewById(R.id.txt_addEducation)

        education = findViewById(R.id.recycler_educationDetails)
        managerEducation = LinearLayoutManager(this)
        managerEducation.reverseLayout = true
        managerEducation.stackFromEnd = true
        education.layoutManager = managerEducation
        adapterEducation = EducationAdapter(id,owner,this)
        education.adapter = adapterEducation

        spin_gender_profile_details.setEnabled(false)
        spin_gender_profile_details.setClickable(false)
        img_calendar_details.setEnabled(false)
        img_calendar_details.setClickable(false);
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this, R.array.spin_Gender,
                R.layout.spinner_item
            )

        // Specify the layout to use when the list of choices appears
        staticAdapter
            .setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        spin_gender_profile_details.adapter = staticAdapter
        getUser()
        getEducation()
        if (owner == true)
        {
            btnEducation.visibility = View.VISIBLE
            btn_edit_profile_details.visibility = View.VISIBLE
            btn_edit_profile_details.isEnabled = true
        }
        img_calendar_details.setOnClickListener {
            showDatePickerDialog()
        }
        btn_edit_profile_details.setOnClickListener {
            editUser()
        }
        btn_cancel_profile_details.setOnClickListener {
            cancelEdit()
        }
        btn_save_profile_details.setOnClickListener {
            saveUser()
        }
        btnEducation.setOnClickListener {
            addEducation()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    private fun addEducation(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add education")
        builder.setMessage("Add education level and info")
        val view = LayoutInflater.from(this).inflate(R.layout.input_education,null)
        val inputGrade : EditText = view.findViewById(R.id.input_educationGrade)
        val inputInfo : EditText = view.findViewById(R.id.input_educationInfo)
        builder.setView(view)
        builder.setPositiveButton("Add"){
                dialog, which ->
            if(inputGrade.text.toString().isNotEmpty() && inputInfo.text.toString().isNotEmpty()){
                val edu = EducationUploadInfo(inputGrade.text.toString(),inputInfo.text.toString())
                database.child("education").child(id).child(inputGrade.text.toString()+"&"+inputInfo.text.toString()).setValue(edu)
            }
            else{
                Toast.makeText(this, "Data incorrect", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel"){
                dialog, which ->
            dialog.cancel()
        }
        builder.show()
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
                    input_name_details.setText(user.name)
                    input_city_details.setText(user.city)
                    input_phone_details.setText(user.cellphone)
                    datePicker_details.setText(user.birth)
                    if(user.gender == "Male")
                    {
                        spin_gender_profile_details.setSelection(0)
                    }
                    if(user.gender == "Female")
                    {
                        spin_gender_profile_details.setSelection(1)
                    }
                    if(user.gender == "Other")
                    {
                        spin_gender_profile_details.setSelection(2)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }

    private fun getEducation(){
        database.child("education").child(id)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var educations: MutableList<EducationUploadInfo> = mutableListOf()
                    for (postSnapshot in p0.children ) {
                        val education = postSnapshot.getValue(EducationUploadInfo::class.java)
                        var temp = EducationUploadInfo(education?.educationGrade, education?.educationInfo)
                        educations.add(temp)
                    }
                    adapterEducation.setEducation(educations)
                }
            })
    }
    fun showDatePickerDialog(){
        val listener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth ->
            val selectedDate = twoDigits(dayOfMonth) + "/" + (twoDigits(month + 1)) + "/" + twoDigits(year)
            datePicker_details.text = selectedDate

        }
        val newFragment: DatePickerFragment = DatePickerFragment.newInstance(listener)
        newFragment.show(supportFragmentManager,"datePicker")
    }
    fun twoDigits(n: Int): String {
        return if (n <= 9) "0$n" else n.toString()
    }

    private fun editUser(){
        input_name_details.isEnabled = true
        input_city_details.isEnabled = true
        input_phone_details.isEnabled = true
        img_calendar_details.isEnabled = true
        spin_gender_profile_details.isEnabled = true
        spin_gender_profile_details.isClickable = true
        btn_cancel_profile_details.visibility = View.VISIBLE
        btn_save_profile_details.visibility = View.VISIBLE
        btn_save_profile_details.isEnabled = true
        btn_cancel_profile_details.isEnabled = true
    }
    private fun cancelEdit(){
        input_name_details.isEnabled = false
        input_city_details.isEnabled = false
        input_phone_details.isEnabled = false
        img_calendar_details.isEnabled = false
        spin_gender_profile_details.isEnabled = false
        spin_gender_profile_details.isClickable = false
        btn_cancel_profile_details.visibility = View.GONE
        btn_save_profile_details.visibility = View.GONE
        btn_save_profile_details.isEnabled = false
        btn_cancel_profile_details.isEnabled = false
        btn_save_profile_details.isClickable = false
        btn_cancel_profile_details.isClickable = false
        getUser()
    }

    fun saveUser(){
        val userHash:HashMap<String,Any> = hashMapOf()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = input_name_details.text.toString()
        val city = input_city_details.text.toString()
        val birth = datePicker_details.text.toString()
        val number = input_phone_details.text.toString()
        val gender = spin_gender_profile_details.selectedItem.toString()
        if (TextUtils.isEmpty(city)) {
            input_cityRegister.error = "Required"
            return
        }
        if (TextUtils.isEmpty(number)) {
            input_phoneRegister.error = "Required"
            return
        }
        userHash.set("birth",birth)
        userHash.set("cellphone",number)
        userHash.set("city",city)
        userHash.set("gender",gender)
        userHash.set("name",name)
        database.child("users").child(currentUser?.uid?:"Error").updateChildren(userHash)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileDetailsActivity, "Edited Correctly", Toast.LENGTH_SHORT).show()
                cancelEdit()
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileDetailsActivity, "Error with data", Toast.LENGTH_SHORT).show()
            }
    }
}
