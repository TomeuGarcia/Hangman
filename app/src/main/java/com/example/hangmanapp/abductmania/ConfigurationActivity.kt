package com.example.hangmanapp.abductmania

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityConfigurationBinding
import com.google.firebase.firestore.FirebaseFirestore

class ConfigurationActivity : AppCompatActivity() {
    private val USERS_COLLECTION = "users"

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

        usersCollection.get()
            .addOnSuccessListener {
                users = it?.documents?.mapNotNull { dbUser ->
                    dbUser.toObject(User::class.java)
                } as ArrayList<User>

                Toast.makeText(this, users.size, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_LONG).show()
            }

        //val sharedPreferences = getSharedPreferences(getString(R.string.preferences_config), MODE_PRIVATE)
        //var editor = sharedPreferences.edit()

        binding.backButtonImage.setOnClickListener() {
            // Go To Main Menu
            val intent = Intent(this, MainMenuActivity::class.java)

            startActivity(intent)
        }

        binding.languageButton.setOnClickListener() {
            // Change Current Language
            currentLang++
            if (currentLang > languages.size) { currentLang = 0 }

            binding.languageButton.text = "Language: " + languages[currentLang]

            //editor.putInt(getString(R.string.prefLanguage), currentLang).apply()
        }

        binding.musicButton.setOnClickListener() {
            // Turn On/Off Music
            music = !music

            if (music) {
                binding.musicButton.text = "Music: ON"
                //editor.putBoolean(getString(R.string.prefMusic), true).apply()
            }
            else {
                binding.musicButton.text = "Music: OFF"
                //editor.putBoolean(getString(R.string.prefMusic), false).apply()
            }
        }

        binding.soundButton.setOnClickListener() {
            // Turn On/Off Sound
            sound = !sound

            if (sound) {
                binding.soundButton.text = "Sound: ON"
                //editor.putBoolean(getString(R.string.prefSound), true).apply()
            }
            else {
                binding.soundButton.text = "Sound: OFF"
                //editor.putBoolean(getString(R.string.prefSound), false).apply()
            }
        }
    }
}