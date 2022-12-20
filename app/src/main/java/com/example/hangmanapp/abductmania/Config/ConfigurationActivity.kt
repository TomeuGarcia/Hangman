package com.example.hangmanapp.abductmania.Config

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.ActivityConfigurationBinding


class ConfigurationActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityConfigurationBinding

    private val configurationViewModel : ConfigurationViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        configurationViewModel.loadData(this)

        configurationViewModel.currentLang.observe(this) {
            binding.languageButton.text =
                "Language: " + configurationViewModel.languages[it ?: 0]
        }

        configurationViewModel.isMusicOn.observe(this) {
            it?.apply {
                if (this) binding.musicButton.text = "Music: ON"
                else binding.musicButton.text = "Music: OFF"
            }
        }

        configurationViewModel.isSoundOn.observe(this) {
            it?.apply {
                if (this) binding.soundButton.text = "Sound: ON"
                else binding.soundButton.text = "Sound: OFF"
            }
        }



        binding.backButtonImage.setOnClickListener {
            // Go To Main Menu
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

        binding.languageButton.setOnClickListener {
            // Change Current Language
            configurationViewModel.iterateCurrentLanguage()
        }

        binding.musicButton.setOnClickListener {
            // Turn On/Off Music
            configurationViewModel.toggleMusic()
        }

        binding.soundButton.setOnClickListener {
            // Turn On/Off Sound
            configurationViewModel.toggleSound()
        }
    }


    override fun onPause()
    {
        super.onPause()

        configurationViewModel.saveData(this)
    }


}