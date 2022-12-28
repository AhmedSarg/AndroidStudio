package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */

class AuthenticationActivity : AppCompatActivity() {

    companion object {
        const val TAG = "AuthenticationActivity"
        const val SIGN_IN_RESULT = 1001
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    private val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
        button.setOnClickListener { launchSignInFlow() }

//          TODO: If the user was authenticated, send him to RemindersActivity
        val reminderIntent = Intent(this, RemindersActivity::class.java)
        authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    startActivity(reminderIntent)
                    finish()
                }
                else -> Log.i(TAG, "Error Logging in")
            }
        })

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private fun launchSignInFlow() {
        Log.i(TAG, "sign in flow")
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), SIGN_IN_RESULT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT) {
            val response = IdpResponse.fromResultIntent(data)
            if (requestCode == Activity.RESULT_OK) {
                Log.i(TAG, "Successfully Signed In -> User : ${FirebaseAuth.getInstance().currentUser?.displayName}")
            } else {
                Log.i(TAG, "Sign in Unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
