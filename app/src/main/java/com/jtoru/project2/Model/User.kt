package com.jtoru.project2.Model

import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class User(
    var id:String,
    var name:String,
    var lastname:String,
    var profilePic:String?,
    var pictures:List<String>,
    var education:List<String>,
    var city:String,
    var birth: Date,
    var cellphone:String,
    var email:String,
    var gender:Boolean
    ){

    fun convertDate():String{
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(this.birth)
    }
}
