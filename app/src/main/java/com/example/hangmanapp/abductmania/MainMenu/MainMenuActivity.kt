package com.example.hangmanapp.abductmania.MainMenu

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.Config.ConfigurationActivity
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.Game.HangmanGameActivity
import com.example.hangmanapp.abductmania.Ranking.RankingActivity
import com.example.hangmanapp.databinding.ActivityMainMenuBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private val mainMenuViewModel : MainMenuViewModel by viewModels()
    private val configurationViewModel : ConfigurationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.mainMenuProgressBar.visibility = View.INVISIBLE


        mainMenuViewModel.canPlayGame.observe(this) {
            if (it) startPlayGame()
        }
        mainMenuViewModel.noApiCommunication.observe(this) {
            if (it) logNoApiCommunication()
        }
        mainMenuViewModel.init(this, configurationViewModel)

        binding.mainMenuPlay.setOnClickListener{
            disableButtons()
            mainMenuViewModel.testApiCommunication()
            binding.mainMenuProgressBar.visibility = View.VISIBLE
        }

        binding.mainMenuSettings.setOnClickListener{
            disableButtons()

            MainMenuViewModel.goingToAnotherMenu = true

            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)

            if (ConfigurationViewModel.isMusicOn.value == true)
            {
                MainMenuViewModel.menuMusicMP?.start()
            }
            else
            {
                MainMenuViewModel.menuMusicMP?.pause()
            }
        }
        binding.mainMenuLeaderboard.setOnClickListener{
            disableButtons()

            MainMenuViewModel.goingToAnotherMenu = true

            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                MainMenuViewModel.buttonSfxMP?.start()
            }
            else
            {
                MainMenuViewModel.buttonSfxMP?.pause()
            }


            if (ConfigurationViewModel.isMusicOn.value == true)
            {
                MainMenuViewModel.menuMusicMP?.start()
            }
            else
            {
                MainMenuViewModel.menuMusicMP?.pause()
            }
        }

        binding.mainMenuExit.setOnClickListener{
            disableButtons()

            MainMenuViewModel.buttonSfxMP?.start()

            mainMenuViewModel.signOut()
            finish()
        }

        mainMenuViewModel.fireBaseNotification(this)
    }

    override fun onResume() {
        super.onResume()

        enableButtons()

        MainMenuViewModel.menuMusicMP?.start()
        binding.mainMenuProgressBar.visibility = View.INVISIBLE
    }

    override fun onPause() {
        super.onPause()

        if (!MainMenuViewModel.goingToAnotherMenu)
        {
            MainMenuViewModel.menuMusicMP?.pause()
        }
        MainMenuViewModel.goingToAnotherMenu = false
    }

    private fun enableButtons()
    {
        binding.mainMenuPlay.isEnabled = true
        binding.mainMenuSettings.isEnabled = true
        binding.mainMenuLeaderboard.isEnabled = true
        binding.mainMenuExit.isEnabled = true
    }

    private fun disableButtons()
    {
        binding.mainMenuPlay.isEnabled = false
        binding.mainMenuSettings.isEnabled = false
        binding.mainMenuLeaderboard.isEnabled = false
        binding.mainMenuExit.isEnabled = false
    }

    private fun startPlayGame()
    {
        val intent = Intent(this, HangmanGameActivity::class.java)
        startActivity(intent)

        MainMenuViewModel.buttonSfxMP?.start()
        MainMenuViewModel.menuMusicMP?.pause()

        binding.mainMenuProgressBar.visibility = View.INVISIBLE
    }
    private fun logNoApiCommunication()
    {
        Toast.makeText(this, "No API communication...", Toast.LENGTH_LONG).show()
        enableButtons()
        binding.mainMenuProgressBar.visibility = View.INVISIBLE
    }

}