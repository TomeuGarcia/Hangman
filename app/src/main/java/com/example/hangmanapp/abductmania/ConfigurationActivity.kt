package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityConfigurationBinding

class ConfigurationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigurationBinding

    val languages = arrayOf<String>("English", "Spanish", "Catalan")
    var currentLang = 0
    var music = true
    var sound = true

    val sharedPreferences = getSharedPreferences("Config", MODE_PRIVATE)
    var editor = sharedPreferences.edit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            editor.putInt("language", currentLang).apply()
        }

        binding.musicButton.setOnClickListener() {
            // Turn On/Off Music
            music = !music

            if (music) {
                binding.musicButton.text = "Music: ON"
                editor.putBoolean("music", true).apply()
            }
            else {
                binding.musicButton.text = "Music: OFF"
                editor.putBoolean("music", false).apply()
            }
        }

        binding.soundButton.setOnClickListener() {
            // Turn On/Off Sound
            sound = !sound

            if (sound) {
                binding.soundButton.text = "Sound: ON"
                editor.putBoolean("sound", true).apply()
            }
            else {
                binding.soundButton.text = "Sound: OFF"
                editor.putBoolean("sound", false).apply()
            }
        }
    }
}