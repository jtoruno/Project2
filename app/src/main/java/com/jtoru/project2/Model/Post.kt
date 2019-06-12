package com.jtoru.project2.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    var id:String? = "",
    var idUser:String? = "",
    var publish: Long? = 0,
    var description: String? = "",
    var type:String? = "TEXT",
    var content:String? = ""
    )


