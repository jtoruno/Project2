package com.jtoru.project2.Fragments


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.jtoru.project2.R
import kotlinx.android.synthetic.*
import com.jtoru.project2.Actitivies.MainActivity
import android.content.Intent
import android.widget.LinearLayout
import com.jtoru.project2.Actitivies.ProfileActivity
import com.jtoru.project2.Utils.MyDialogFragment


class FragmentProfile : Fragment() {
    private lateinit var profilePic:ImageView
    private lateinit var profileName: TextView
    private lateinit var goToProfile:TextView
    private lateinit var goToFriends:LinearLayout
    private lateinit var goToSettings:LinearLayout
    private lateinit var signOut:LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fragment_profile, container, false)
        profilePic = view.findViewById(R.id.img_profileFragment)
        profileName = view.findViewById(R.id.txt_nameProfileFragment)
        goToProfile = view.findViewById(R.id.txt_goProfileFragment)
        goToFriends = view.findViewById(R.id.lay_goToFriendsProfile)
        goToSettings = view.findViewById(R.id.lay_goToSettingsProfile)
        signOut = view.findViewById(R.id.lay_signOutProfile)

        goToProfile.setOnClickListener {
            val i = Intent(activity, ProfileActivity::class.java)
            startActivity(i)
        }

        signOut.setOnClickListener {
            MyDialogFragment().show(fragmentManager!!,"HALP")
        }



        // Inflate the layout for this fragment
        return view
    }


}
