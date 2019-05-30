package com.jtoru.project2.Fragments


import android.app.AlertDialog
import android.app.Dialog
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
import android.support.v4.app.DialogFragment
import android.widget.LinearLayout
import com.firebase.ui.auth.AuthUI
import com.jtoru.project2.Actitivies.HomeActivity
import com.jtoru.project2.Actitivies.FriendsActivity
import com.jtoru.project2.Actitivies.ProfileActivity





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

        goToFriends.setOnClickListener {
            val i = Intent(activity!!, FriendsActivity::class.java)
            startActivity(i)
        }

        signOut.setOnClickListener {
            class MyDialogFragment : DialogFragment() {
                override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                    return android.support.v7.app.AlertDialog.Builder(activity!!)
                        .setTitle("Exit")
                        .setMessage("Do you want to close session?")
                        .setPositiveButton("Yes") { dialog, which ->
                            dialog.dismiss()
                            AuthUI.getInstance().signOut(activity!!)
                            activity?.finishAffinity()
                            val i = Intent(getActivity(), HomeActivity::class.java)
                            startActivity(i)

                        }
                        .setNegativeButton("No",null)
                        .create()
                }
            }
            MyDialogFragment().show(fragmentManager!!,"HALP")
        }



        // Inflate the layout for this fragment
        return view
    }


}
