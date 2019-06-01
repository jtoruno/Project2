package com.jtoru.project2.Utils

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jtoru.project2.Model.User
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.friends_row.view.*

class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun bindToItem(user : User){
        itemView.txt_userFriends.text = user.name
    }
}