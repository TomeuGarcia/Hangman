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

        binding.languageButtonImage.setOnClickListener() {
            // Change Current Language
            currentLang++
            if (currentLang > languages.size) { currentLang = 0 }

            binding.languageTextView.text = "Language: " + languages[currentLang]
        }

        binding.musicButtonImage.setOnClickListener() {
            // Turn On/Off Music
            music = !music

            if (music) {
                binding.musicTextView.text = "Music: ON"
            }
            else {
                binding.musicTextView.text = "Music: OFF"
            }
        }

        binding.soundButtonImage.setOnClickListener() {
            // Turn On/Off Sound
            sound = !sound

            if (sound) {
                binding.soundTextView.text = "Sound: ON"
            }
            else {
                binding.soundTextView.text = "Sound: OFF"
            }
        }
    }
}