package com.example.hangmanapp.abductmania.Config

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.abductmania.MainMenu.MainMenuViewModel
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

        configurationViewModel.areNotificationsOn.observe(this) {
            it?.apply {
                if (this) binding.notificationsButton.text = "Notifications: ON"
                else binding.notificationsButton.text = "Notifications: OFF"
            }

        }

        ConfigurationViewModel.isMusicOn.observe(this) {
            it?.apply {
                if (this) binding.musicButton.text = "Music: ON"
                else binding.musicButton.text = "Music: OFF"

            }

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()
        }

        ConfigurationViewModel.isSoundOn.observe(this) {
            it?.apply {
                if (this) binding.soundButton.text = "Sound: ON"
                else binding.soundButton.text = "Sound: OFF"
            }

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()
        }



        binding.backButtonImage.setOnClickListener {
            // Go To Main Menu
            finish()

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()
        }

        binding.languageButton.setOnClickListener {
            // Change Current Language
            configurationViewModel.iterateCurrentLanguage()

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()
        }

        binding.notificationsButton.setOnClickListener {
            // Turn On/Off Notifications
            configurationViewModel.toggleNotifications()

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()
        }

        binding.musicButton.setOnClickListener {
            // Turn On/Off Music
            configurationViewModel.toggleMusic()

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()
        }

        binding.soundButton.setOnClickListener {
            // Turn On/Off Sound
            configurationViewModel.toggleSound()

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
                MainMenuViewModel.buttonSfxMP?.pause()

        }
    }


    override fun onPause()
    {
        super.onPause()

        configurationViewModel.saveData(this)


    }

    override fun onResume() {
        super.onResume()

        MainMenuViewModel.menuMusicMP?.start()
    }
}