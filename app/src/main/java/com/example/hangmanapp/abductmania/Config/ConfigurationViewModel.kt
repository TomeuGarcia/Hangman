package com.example.hangmanapp.abductmania.Config

import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.DatabaseUtils.DatabaseUtils
import com.example.hangmanapp.abductmania.DatabaseUtils.SharedPrefsUtils
import com.example.hangmanapp.abductmania.DatabaseUtils.User
import com.example.hangmanapp.abductmania.Game.HangmanGameViewModel
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ConfigurationViewModel : ViewModel()
{
    private val LANGUAGE = "language"
    private val MUSIC = "music"
    private val SOUND = "sound"
    private val NOTIFICATIONS = "notifications"



    public val languages = arrayOf<String>("English", "Catalan", "Spanish")
    public var currentLang = MutableLiveData<Int>()
    public var areNotificationsOn = MutableLiveData<Boolean>()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var email: String
    private var currentUser : User? = null

    private var users = arrayListOf<User>()
    private lateinit var usersCollection : CollectionReference


    companion object
    {
        var isMusicOn = MutableLiveData<Boolean>()
        var isSoundOn = MutableLiveData<Boolean>()
    }

    init
    {
        currentLang.value = 0
        areNotificationsOn.value = true
        isMusicOn.value = true
        isSoundOn.value = true
    }


    public fun loadData(context : Context)
    {
        firestore = FirebaseFirestore.getInstance()
        usersCollection = firestore.collection(DatabaseUtils.USERS_COLLECTION)

        // Shared prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        email = shared.getString(SharedPrefsUtils.EMAIL, null) ?: ""

        updateValues(shared.getInt(LANGUAGE, 0),
                     shared.getBoolean(NOTIFICATIONS, true),
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
                    updateValues(language, notifications, music, sound)
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
        val areNotisOnValue = areNotificationsOn.value ?: false
        val isMusicOnValue = isMusicOn.value ?: false
        val isSoundOnValue = isSoundOn.value ?: false

        editor.putInt(LANGUAGE, currentLangValue)
        editor.putBoolean(NOTIFICATIONS, areNotisOnValue)
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
            it.notifications = areNotisOnValue
            it.music = isMusicOnValue
            it.sound = isSoundOnValue

            usersCollection.document(it.username).set(it)
        }
    }


    private fun isUserCurrentUser(user : User) : Boolean
    {
        return user.email == email
    }

    private fun updateValues(_lang: Int, _notifications: Boolean, _music: Boolean, _sound: Boolean) {
        currentLang.value = _lang
        areNotificationsOn.value = _notifications
        isMusicOn.value = _music
        isSoundOn.value = _sound
    }


    public fun iterateCurrentLanguage()
    {
        currentLang.value = (currentLang.value?.inc() ?: 0) % languages.size
    }

    public fun toggleNotifications()
    {
        areNotificationsOn.value = areNotificationsOn.value?.not() ?: false
    }

    public fun toggleMusic(context : Context)
    {
        isMusicOn.value = isMusicOn.value?.not() ?: false

        if (isMusicOn.value == true)
        {
            MainMenuActivity.menuMusicMP?.setVolume(1.0f,1.0f)
            MainMenuActivity.gameMusicMP?.setVolume(1.0f,1.0f)
        }
        else
        {
            MainMenuActivity.menuMusicMP?.setVolume(0.0f, 0.0f)
            MainMenuActivity.gameMusicMP?.setVolume(0.0f, 0.0f)
        }
    }

    public fun toggleSound(context: Context)
    {
        isSoundOn.value = isSoundOn.value?.not() ?: false

        if (isSoundOn.value == true)
        {
            MainMenuActivity.buttonSfxMP?.setVolume(1.0f,1.0f)
            HangmanGameViewModel.victorySfxMP?.setVolume(1.0f,1.0f)
            HangmanGameViewModel.abductorSfxMP?.setVolume(1.0f,1.0f)
            HangmanGameViewModel.appearSfxMP?.setVolume(1.0f,1.0f)
            HangmanGameViewModel.gameOverSfxMP?.setVolume(1.0f,1.0f)
            HangmanGameViewModel.buildingSfxMP?.setVolume(1.0f,1.0f)
            HangmanGameViewModel.correctLetterSfxMP?.setVolume(1.0f,1.0f)
        }
        else
        {
            MainMenuActivity.buttonSfxMP?.setVolume(0.0f, 0.0f)
            HangmanGameViewModel.victorySfxMP?.setVolume(0.0f,0.0f)
            HangmanGameViewModel.abductorSfxMP?.setVolume(0.0f,0.0f)
            HangmanGameViewModel.appearSfxMP?.setVolume(0.0f,0.0f)
            HangmanGameViewModel.gameOverSfxMP?.setVolume(0.0f,0.0f)
            HangmanGameViewModel.buildingSfxMP?.setVolume(0.0f,0.0f)
            HangmanGameViewModel.correctLetterSfxMP?.setVolume(0.0f,0.0f)
        }
    }

}