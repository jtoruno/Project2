package com.jtoru.project2.Utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jtoru.project2.Model.EducationUploadInfo
import com.jtoru.project2.R

class EducationAdapter : RecyclerView.Adapter<EducationAdapter.EducationVH>(){
    private var mDataset: MutableList<EducationUploadInfo> = mutableListOf()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EducationAdapter.EducationVH {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.education_row, p0, false)
        return EducationAdapter.EducationVH(v)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(p0: EducationVH, p1: Int) {
        val item = mDataset[p1]
        p0.grade.text = item.educationGrade
        p0.detail.text = item.educationInfo
    }


    class EducationVH(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var grade: TextView
        var detail: TextView

        init {
            grade = v.findViewById(R.id.txt_educationGrade)
            detail = v.findViewById(R.id.txt_educationInfo)
        }
    }

    fun setEducation(education:MutableList<EducationUploadInfo>){
        mDataset = education
        notifyDataSetChanged()
    }
}