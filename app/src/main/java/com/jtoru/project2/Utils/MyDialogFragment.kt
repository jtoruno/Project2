package com.jtoru.project2.Utils

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog


class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
            .setTitle("Exit")
            .setMessage("Do you want to close session?")
            .setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()

            }
            .setNegativeButton("No",null)
            .create()
    }
}