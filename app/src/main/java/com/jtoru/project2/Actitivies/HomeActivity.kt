package com.jtoru.project2.Actitivies

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.jtoru.project2.R
import com.google.firebase.auth.FirebaseUserMetadata



class HomeActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        FirebaseApp.initializeApp(this)
        authScreen()

    }

    private fun authScreen(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                /*val metadata = user?.getMetadata()
                Log.e("CREATION",metadata?.creationTimestamp.toString())
                Log.e("LAST",metadata?.lastSignInTimestamp.toString())
                if (metadata?.creationTimestamp == metadata?.lastSignInTimestamp) {
                    // The user is new, show them a fancy intro screen!
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // This is an existing user, show them a welcome back screen.
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }*/
                // ...
            } else {
                //Toast.makeText(this,"Error to signIn",Toast.LENGTH_LONG).show()
                finish()
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}
