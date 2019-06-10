package com.jtoru.project2.Utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jtoru.project2.Model.EducationUploadInfo
import com.jtoru.project2.R

class EducationAdapter(id:String,owner:Boolean, context: Context) : RecyclerView.Adapter<EducationAdapter.EducationVH>(){
    private var mDataset: MutableList<EducationUploadInfo> = mutableListOf()
    private lateinit var database: DatabaseReference
    var userId = id
    var state = owner
    var thisContext = context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EducationAdapter.EducationVH {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.education_row, p0, false)
        database = FirebaseDatabase.getInstance().reference
        return EducationAdapter.EducationVH(v)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(p0: EducationVH, p1: Int) {
        val item = mDataset[p1]
        p0.grade.text = item.educationGrade
        p0.detail.text = item.educationInfo
        if(state)
        {
            p0.btnDelete.visibility = View.VISIBLE
        }
        p0.btnDelete.setOnClickListener {
            database.child("education").child(userId).child(item.educationGrade+"&"+item.educationInfo).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(thisContext, "Education detail removed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(thisContext, "Education not removed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    class EducationVH(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var grade: TextView
        var detail: TextView
        var btnDelete: ImageView

        init {
            grade = v.findViewById(R.id.txt_educationGrade)
            detail = v.findViewById(R.id.txt_educationInfo)
            btnDelete = v.findViewById(R.id.img_educationDelete)
        }
    }

    fun setEducation(education:MutableList<EducationUploadInfo>){
        mDataset = education
        notifyDataSetChanged()
    }
}