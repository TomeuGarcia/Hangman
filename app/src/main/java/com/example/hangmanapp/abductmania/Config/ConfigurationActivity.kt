package com.example.hangmanapp.abductmania.Config

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hangmanapp.R
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

        if (configurationViewModel.isMusicOn.value == true)
        {
            configurationViewModel.musicPlayer = MediaPlayer.create(this, R.raw.menus_song)
            configurationViewModel.musicPlayer?.start()
            configurationViewModel.audioPlayer = MediaPlayer.create(this, R.raw.button_click)
        }
        else
            configurationViewModel.musicPlayer?.pause()

        configurationViewModel.loadData(this)


        configurationViewModel.currentLang.observe(this) {
            binding.languageButton.text =
                "Language: " + configurationViewModel.languages[it ?: 0]
        }

        configurationViewModel.areNotificationsOn.observe(this) {
            it?.apply {
                if (this) binding.notificationsButton.text = "Notifications: ON"
                else binding.notificationsButton.text = "Notifications: OFF"
            }

            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }

        configurationViewModel.isMusicOn.observe(this) {
            it?.apply {
                if (this) binding.musicButton.text = "Music: ON"
                else binding.musicButton.text = "Music: OFF"

            }
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }

        configurationViewModel.isSoundOn.observe(this) {
            it?.apply {
                if (this) binding.soundButton.text = "Sound: ON"
                else binding.soundButton.text = "Sound: OFF"
            }
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }



        binding.backButtonImage.setOnClickListener {
            // Go To Main Menu
            finish()
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }

        binding.languageButton.setOnClickListener {
            // Change Current Language
            configurationViewModel.iterateCurrentLanguage()
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }

        binding.notificationsButton.setOnClickListener {
            // Turn On/Off Notifications
            configurationViewModel.toggleNotifications()
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }

        binding.musicButton.setOnClickListener {
            // Turn On/Off Music
            configurationViewModel.toggleMusic(this)
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()
        }

        binding.soundButton.setOnClickListener {
            // Turn On/Off Sound
            configurationViewModel.toggleSound(this)
            if (configurationViewModel.isSoundOn.value == true)
            {
                configurationViewModel.audioPlayer?.start()
            }
            else
                configurationViewModel.audioPlayer?.pause()

        }
    }


    override fun onPause()
    {
        super.onPause()

        configurationViewModel.saveData(this)
        configurationViewModel.musicPlayer?.pause()
    }


}