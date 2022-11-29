package com.example.hangmanapp.abductmania

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityConfigurationBinding
import com.google.firebase.firestore.FirebaseFirestore


class ConfigurationActivity : AppCompatActivity() {
    private val USERS_COLLECTION = "users"
    private val CURRENT_LANGUAGE = "language"
    private val MUSIC = "music"
    private val SOUND = "sound"

    private lateinit var binding: ActivityConfigurationBinding
    private lateinit var firestore: FirebaseFirestore

    private val languages = arrayOf<String>("English", "Catalan", "Spanish")
    private var currentLang = 0
    private var music = true
    private var sound = true

    private var users = arrayListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection(USERS_COLLECTION)

        // Shared prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        var signature = shared.getString(USERS_COLLECTION, null) // CHANGE TO A VAL
        currentLang = shared.getInt(CURRENT_LANGUAGE, 0)
        music = shared.getBoolean(MUSIC, true)
        sound = shared.getBoolean(SOUND, true)

        updateButtons()

        if (signature == null) { signature = "a" }
        // Firestore
        if (signature != null) {
            usersCollection.get()
                .addOnSuccessListener {
                    users = it?.documents?.mapNotNull { dbUser ->
                        dbUser.toObject(User::class.java)
                    } as ArrayList<User>

                    Toast.makeText(this@ConfigurationActivity, "Hello " + users.get(0).username, Toast.LENGTH_SHORT).show()

                    updateValues(users.get(0).language, users.get(0).music, users.get(0).sound)
                    updateButtons()
                }
                .addOnFailureListener {
                    Toast.makeText(this@ConfigurationActivity, getString(R.string.somethingWentWrong), Toast.LENGTH_LONG).show()
                }
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

        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = shared.edit()

        editor.putInt("language", currentLang)
        editor.putBoolean("music", music)
        editor.putBoolean("sound", sound)
        editor.apply()
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