package com.example.hangmanapp.abductmania

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel()
{
    private val USERS_COLLECTION = "users"
    private val EMAIL = "email"

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var emailIsValid = true
    private var usernameIsValid = true
    private var passwordIsValid = true


    init
    {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    public fun registerNewUser(contextActivity : Activity,
                               email : String, username : String, password : String,
                               onRegisterSuccessCallback : () -> Unit,
                               onRegisterFailureCallback : () -> Unit)
    {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(contextActivity) {
                if (it.isSuccessful)
                {
                    saveSharedPrefsRegisteredUser(contextActivity, email)
                    fireStoreRegisterUser(username, email)

                    onRegisterSuccessCallback()
                }
                else
                {
                    onRegisterFailureCallback()
                }
            }
            .addOnFailureListener {
                onRegisterFailureCallback()
            }

    }

    public fun canRegisterUser() : Boolean
    {
        return emailIsValid && usernameIsValid && passwordIsValid
    }


    // EMAIL
    public fun validateEmail(emailText : String?) : String?
    {
        emailIsValid = Patterns.EMAIL_ADDRESS.matcher(emailText).matches()

        if (emailIsValid)
        {
            return null
        }
        return "Invalid Email Address"
    }

    // USERNAME
    public fun validateUsername(usernameText : String?): String?
    {
        val usernameLength = usernameText?.length ?: 0

        usernameIsValid = usernameLength >= 2

        if (usernameIsValid)
        {
            return null
        }
        return "Minimum 2 Characters Username"
    }

    // PASSWORD
    public fun validatePassword(passwordText : String?) : String?
    {
        passwordIsValid = passwordText == null

        if (passwordIsValid)
        {
            return null
        }

        passwordText?.apply {
            if (passwordText.length < 8) {
                return "Minimum 8 Characters Password"
            }
            if (!passwordText.matches(".*[A-Z].*".toRegex())) {
                return "Must Contain 1 Upper-case Character"
            }
            if (!passwordText.matches(".*[a-z].*".toRegex())) {
                return "Must Contain 1 Lower-case Character"
            }
            if (!passwordText.matches(".*[|@#€¬!·$%&/()=?¿¡^*¨´`+{}_:;<>].*".toRegex())) {
                return "Must Contain 1 Special Character"
            }
        }
        return null
    }

    private fun saveSharedPrefsRegisteredUser(context : Context, email : String)
    {
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = shared.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }

    private fun fireStoreRegisterUser(username : String, email :String)
    {
        val usersCollection = firestore.collection(USERS_COLLECTION)
        val user = User(username, email)

        usersCollection.document(user.username).set(user)
    }

}