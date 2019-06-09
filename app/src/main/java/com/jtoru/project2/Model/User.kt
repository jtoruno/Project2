package com.jtoru.project2.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

@IgnoreExtraProperties
data class User(
    var id:String? = "",
    var name:String? = "",
    var profilePic:String? = "",
    var pictures:HashMap<String,String>? = hashMapOf(),
    var education:List<String>? = mutableListOf(),
    var city:String ? = "",
    var birth: String? = "",
    var cellphone:String? = "",
    var email:String? = "",
    var gender:String? = ""
    ){
    @Exclude
    fun convertDate():String{
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(this.birth)
    }
}
