package com.example.hangmanapp.abductmania

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityConfigurationBinding

class ConfigurationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigurationBinding

    private val languages = arrayOf<String>("English", "Catalan", "Spanish")
    private var currentLang = 0
    private var music = true
    private var sound = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //val sharedPreferences = getSharedPreferences(getString(R.string.preferences_config), MODE_PRIVATE)
        //var editor = sharedPreferences.edit()

        binding.backButtonImage.setOnClickListener() {
            // Go To Main Menu
            val intent = Intent(this, MainMenuActivity::class.java)

            startActivity(intent)
        }

        binding.languageButton.setOnClickListener() {
            // Change Current Language
            currentLang = ++currentLang % languages.size

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