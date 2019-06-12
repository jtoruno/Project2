package com.jtoru.project2.Model

data class Friendship (
    var sender:String? = "",
    var receiver:String? = "",
    var startTime: Long? = 0,
    var status:Boolean? = false
)