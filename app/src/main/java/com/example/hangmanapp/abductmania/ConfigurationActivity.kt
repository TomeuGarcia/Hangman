package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityConfigurationBinding

class ConfigurationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigurationBinding

    val languages = arrayOf<String>("English", "Spanish", "Catalan")
    var currentLang = 0
    var music = true
    var sound = true

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
        }

        binding.musicButton.setOnClickListener() {
            // Turn On/Off Music
            music = !music

            if (music) {
                binding.musicButton.text = "Music: ON"
            }
            else {
                binding.musicButton.text = "Music: OFF"
            }
        }

        binding.soundButton.setOnClickListener() {
            // Turn On/Off Sound
            sound = !sound

            if (sound) {
                binding.soundButton.text = "Sound: ON"
            }
            else {
                binding.soundButton.text = "Sound: OFF"
            }
        }
    }
}