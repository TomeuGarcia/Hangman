package com.example.hangmanapp.abductmania

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityConfigurationBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class ConfigurationActivity : AppCompatActivity() {
    private val USERS_COLLECTION = "users"
    private val LANGUAGE = "language"
    private val MUSIC = "music"
    private val SOUND = "sound"
    private val EMAIL = "email"

    private lateinit var binding: ActivityConfigurationBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var email: String
    private var currentUser : User? = null

    private val languages = arrayOf<String>("English", "Catalan", "Spanish")
    private var currentLang = 0
    private var music = true
    private var sound = true
    private var users = arrayListOf<User>()
    private lateinit var usersCollection : CollectionReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firestore = FirebaseFirestore.getInstance()
        usersCollection = firestore.collection(USERS_COLLECTION)

        // Shared prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        email = shared.getString(EMAIL, null)?: ""

        updateValues(shared.getInt(LANGUAGE, 0), shared.getBoolean(MUSIC, true), shared.getBoolean(SOUND, true))
        updateButtons()

        // Firestore
        usersCollection.get()
            .addOnSuccessListener {
                users = it?.documents?.mapNotNull { dbUser ->
                    dbUser.toObject(User::class.java)
                } as ArrayList<User>

                currentUser = users.find {
                    isUserCurrentUser(it)
                }

                currentUser?.apply{
                    updateValues(language, music, sound)
                }
                updateButtons()
            }
            .addOnFailureListener {
                Toast.makeText(this@ConfigurationActivity, getString(R.string.somethingWentWrong), Toast.LENGTH_LONG).show()
            }

        binding.backButtonImage.setOnClickListener() {
            // Go To Main Menu
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

        binding.languageButton.setOnClickListener() {
            // Change Current Language
            currentLang = ++currentLang % languages.size

            updateButtons()
        }

        binding.musicButton.setOnClickListener() {
            // Turn On/Off Music
            music = !music

            updateButtons()
        }

        binding.soundButton.setOnClickListener() {
            // Turn On/Off Sound
            sound = !sound

            updateButtons()
        }
    }


    override fun onPause() {
        super.onPause()

        // Shared Prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = shared.edit()

        editor.putInt(LANGUAGE, currentLang)
        editor.putBoolean(MUSIC, music)
        editor.putBoolean(SOUND, sound)
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
            it.language = currentLang
            it.music = music
            it.sound = sound

            usersCollection.document(it.username).set(it)
        }

    }

    private fun isUserCurrentUser(user : User) : Boolean
    {
        return user.email == email
    }

    private fun updateValues(_lang: Int, _music: Boolean, _sound: Boolean) {
        currentLang = _lang
        music = _music
        sound = _sound
    }

    @SuppressLint("SetTextI18n")
    private fun updateButtons() {
        binding.languageButton.text = "Language: " + languages[currentLang]

        if (music) { binding.musicButton.text = "Music: ON" }
        else { binding.musicButton.text = "Music: OFF" }

        if (sound) { binding.soundButton.text = "Sound: ON" }
        else { binding.soundButton.text = "Sound: OFF" }
    }
}