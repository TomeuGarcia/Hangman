package com.example.hangmanapp.abductmania.Login

import android.content.Context
import android.preference.PreferenceManager
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.DatabaseUtils.DatabaseUtils
import com.example.hangmanapp.abductmania.DatabaseUtils.SharedPrefsUtils
import com.example.hangmanapp.abductmania.DatabaseUtils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel()
{
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    init
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

    public fun saveSharedPrefsLoggedUser(context : Context, email : String,
                                         onLoginSuccessCallback : () -> Unit,
                                         onLoginFailureCallback : () -> Unit)
    {
        val usersCollection = firestore.collection(DatabaseUtils.USERS_COLLECTION)
        usersCollection.get()
            .addOnSuccessListener {
                val users = it?.documents?.mapNotNull { dbUser ->
                    dbUser.toObject(User::class.java)
                } as ArrayList<User>

                val currentUser = users.find { itUser ->
                    itUser.email == email
                }

                if (currentUser != null)
                {
                    val shared = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = shared.edit()
                    editor.putString(SharedPrefsUtils.EMAIL, email)
                    editor.putString(SharedPrefsUtils.USERNAME, currentUser.username)
                    editor.apply()

                    onLoginSuccessCallback()
                }
                else
                {
                    onLoginFailureCallback()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, context.getString(R.string.somethingWentWrong),
                    Toast.LENGTH_LONG).show()
                onLoginFailureCallback()
            }

        /*
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = shared.edit()
        editor.putString(SharedPrefsUtils.EMAIL, email)
        editor.apply()
         */
    }

    public fun signIn(context : Context, email : String, password : String,
                      onLoginSuccessCallback : () -> Unit,
                      onLoginFailureCallback : () -> Unit)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                saveSharedPrefsLoggedUser(context, email,
                                          onLoginSuccessCallback, onLoginFailureCallback)
            }.addOnFailureListener {
                onLoginFailureCallback()
            }
    }

    public fun signInAsGuest(context : Context,
                             onLoginSuccessCallback : () -> Unit,
                             onLoginFailureCallback : () -> Unit)
    {
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                saveSharedPrefsGuestUser(context)
                onLoginSuccessCallback()
            }
            .addOnFailureListener {
                onLoginFailureCallback()
            }
    }

    public fun saveSharedPrefsGuestUser(context : Context)
    {
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = shared.edit()
        editor.putString(SharedPrefsUtils.USERNAME, "Guest")
        editor.apply()
    }


}