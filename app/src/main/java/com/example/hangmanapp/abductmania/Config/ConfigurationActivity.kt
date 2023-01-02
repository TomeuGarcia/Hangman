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

    companion object
    {
        var musicPlayer : MediaPlayer? = null
        var audioPlayer : MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (configurationViewModel.isMusicOn.value == true)
        {
            musicPlayer?.start()
        }
        else
            musicPlayer?.pause()

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
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }

        configurationViewModel.isMusicOn.observe(this) {
            it?.apply {
                if (this) binding.musicButton.text = "Music: ON"
                else binding.musicButton.text = "Music: OFF"

            }
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }

        configurationViewModel.isSoundOn.observe(this) {
            it?.apply {
                if (this) binding.soundButton.text = "Sound: ON"
                else binding.soundButton.text = "Sound: OFF"
            }
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }



        binding.backButtonImage.setOnClickListener {
            // Go To Main Menu
            finish()
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }

        binding.languageButton.setOnClickListener {
            // Change Current Language
            configurationViewModel.iterateCurrentLanguage()
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }

        binding.notificationsButton.setOnClickListener {
            // Turn On/Off Notifications
            configurationViewModel.toggleNotifications()
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }

        binding.musicButton.setOnClickListener {
            // Turn On/Off Music
            configurationViewModel.toggleMusic(this)
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()
        }

        binding.soundButton.setOnClickListener {
            // Turn On/Off Sound
            configurationViewModel.toggleSound(this)
            if (configurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
            }
            else
                audioPlayer?.pause()

        }
    }


    override fun onPause()
    {
        super.onPause()

        configurationViewModel.saveData(this)
        musicPlayer?.pause()
    }


}