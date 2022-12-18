package com.example.hangmanapp.abductmania

import android.content.Context
import android.preference.PreferenceManager
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel()
{
    private val EMAIL = "email"

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    public fun loadDatabases()
    {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }


    public fun hasLoggedUserAlready() : Boolean
    {
        return firebaseAuth.currentUser != null
    }

    public fun isValidUsername(username : String) : Boolean
    {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    public fun saveSharedPrefsLoggedUser(context : Context, email : String)
    {
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = shared.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

    public fun signIn(context : Context, email : String, password : String,
                      onLoginSuccessCallback : () -> Unit,
                      onLoginFailureCallback : () -> Unit)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                saveSharedPrefsLoggedUser(context, email)
                onLoginSuccessCallback()
            }.addOnFailureListener {
                onLoginFailureCallback()
            }
    }

    public fun signInAsGuest(onLoginSuccessCallback : () -> Unit,
                             onLoginFailureCallback : () -> Unit)
    {
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                onLoginSuccessCallback()
            }
            .addOnFailureListener {
                onLoginFailureCallback()
            }
    }

}