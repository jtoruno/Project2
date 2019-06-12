package com.jtoru.project2.Fragments



import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.jtoru.project2.R
import android.content.Intent
import android.support.v4.app.DialogFragment
import android.widget.LinearLayout
import com.firebase.ui.auth.AuthUI
import com.jtoru.project2.Actitivies.HomeActivity
import com.jtoru.project2.Actitivies.ProfileActivity
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jtoru.project2.Model.User
import com.squareup.picasso.Picasso


class FragmentProfile : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var mStorageRef: StorageReference
    private lateinit var profilePic:ImageView
    private lateinit var profileName: TextView
    private lateinit var goToProfile:TextView
    private lateinit var goToFriends:LinearLayout
    private lateinit var goToDelete:LinearLayout
    private lateinit var signOut:LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fragment_profile, container, false)
        profilePic = view.findViewById(R.id.img_profileFragment)
        profileName = view.findViewById(R.id.txt_nameProfileFragment)
        goToProfile = view.findViewById(R.id.txt_goProfileFragment)
        goToFriends = view.findViewById(R.id.lay_goToFriendsProfile)
        goToDelete = view.findViewById(R.id.lay_goToSettingsProfile)
        signOut = view.findViewById(R.id.lay_signOutProfile)

        database = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference

        goToProfile.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val i = Intent(activity, ProfileActivity::class.java)
            i.putExtra("owner", true)
            i.putExtra("id",currentUser?.uid)
            startActivity(i)
        }

        goToFriends.setOnClickListener {
            val vp = activity!!.findViewById(com.jtoru.project2.R.id.viewpager) as ViewPager
            vp.currentItem = 1
        }

        goToDelete.setOnClickListener {
            deleteAccount()
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
                            val i = Intent(activity, HomeActivity::class.java)
                            startActivity(i)
                        }
                        .setNegativeButton("No",null)
                        .create()
                }
            }
            MyDialogFragment().show(fragmentManager!!,"HALP")
        }
        getUser()
        



        // Inflate the layout for this fragment
        return view
    }

    private fun getUser(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = database.child("users").child(currentUser?.uid!!)
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("ProfileActivity",p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user!= null){
                    profileName.text = user.name
                    var pp = user.profilePic
                    if(pp != null && pp.isNotEmpty()) {
                        Picasso.get().load(pp).error(R.drawable.download).into(profilePic)
                    }
                }
            }
        }
        query.addValueEventListener(listener)
    }

    private fun deleteAccount(){
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid?:""
        database.child("posts").child(currentUser).removeValue()
            .addOnSuccessListener {
                Toast.makeText(activity!!, "", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

            }
        database.child("pictures").child(currentUser).removeValue()
            .addOnSuccessListener {
                Toast.makeText(activity!!, "", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

            }
        database.child("education").child(currentUser).removeValue()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
        mStorageRef.child(currentUser).delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

    }


}
