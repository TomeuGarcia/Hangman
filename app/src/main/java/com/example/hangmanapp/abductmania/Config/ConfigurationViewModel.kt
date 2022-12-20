package com.example.hangmanapp.abductmania.Config

import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.DatabaseUtils.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ConfigurationViewModel : ViewModel()
{
    private val USERS_COLLECTION = "users"
    private val LANGUAGE = "language"
    private val MUSIC = "music"
    private val SOUND = "sound"
    private val EMAIL = "email"

    public val languages = arrayOf<String>("English", "Catalan", "Spanish")
    public var currentLang = MutableLiveData<Int>()
    public var isMusicOn = MutableLiveData<Boolean>()
    public var isSoundOn = MutableLiveData<Boolean>()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var email: String
    private var currentUser : User? = null

    private var users = arrayListOf<User>()
    private lateinit var usersCollection : CollectionReference


    init
    {
        currentLang.value = 0
        isMusicOn.value = true
        isSoundOn.value = true
    }

    public fun loadData(context : Context)
    {
        firestore = FirebaseFirestore.getInstance()
        usersCollection = firestore.collection(USERS_COLLECTION)

        // Shared prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        email = shared.getString(EMAIL, null)?: ""

        updateValues(shared.getInt(LANGUAGE, 0),
                     shared.getBoolean(MUSIC, true),
                     shared.getBoolean(SOUND, true))

        // Firestore
        usersCollection.get()
            .addOnSuccessListener {
                users = it?.documents?.mapNotNull { dbUser ->
                    dbUser.toObject(User::class.java)
                } as ArrayList<User>

                currentUser = users.find { itUser ->
                    isUserCurrentUser(itUser)
                }

                currentUser?.apply{
                    updateValues(language, music, sound)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, context.getString(R.string.somethingWentWrong),
                    Toast.LENGTH_LONG).show()
            }
    }



    public fun saveData(context : Context)
    {
        // Shared Prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = shared.edit()

        val currentLangValue =  currentLang.value ?: 0
        val isMusicOnValue = isMusicOn.value ?: false
        val isSoundOnValue = isSoundOn.value ?: false

        editor.putInt(LANGUAGE, currentLangValue)
        editor.putBoolean(MUSIC, isMusicOnValue)
        editor.putBoolean(SOUND, isSoundOnValue)
        editor.apply()

        // Firestore
        /*
        users.forEach { it ->
            usersCollection.document(it.username).set(it)
                .addOnFailureListener { exception ->
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
        }
         */

        currentUser?.let {
            it.language = currentLangValue
            it.music = isMusicOnValue
            it.sound = isSoundOnValue

            usersCollection.document(it.username).set(it)
        }
    }


    private fun isUserCurrentUser(user : User) : Boolean
    {
        return user.email == email
    }

    private fun updateValues(_lang: Int, _music: Boolean, _sound: Boolean) {
        currentLang.value = _lang
        isMusicOn.value = _music
        isSoundOn.value = _sound
    }


    public fun iterateCurrentLanguage()
    {
        currentLang.value = (currentLang.value?.inc() ?: 0) % languages.size
    }

    public fun toggleMusic()
    {
        isMusicOn.value = isMusicOn.value?.not() ?: false
    }

    public fun toggleSound()
    {
        isSoundOn.value = isSoundOn.value?.not() ?: false
    }

}