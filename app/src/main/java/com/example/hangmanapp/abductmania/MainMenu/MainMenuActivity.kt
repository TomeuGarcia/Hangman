package com.example.hangmanapp.abductmania.MainMenu

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.Config.ConfigurationActivity
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.Game.HangmanGameActivity
import com.example.hangmanapp.abductmania.Ranking.RankingActivity
import com.example.hangmanapp.databinding.ActivityMainMenuBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var firebaseAuth: FirebaseAuth



    companion object {
        const val CHANNEL_ID = "NOTIFICATIONS_CHANNEL_CONTACTS"
        var musicPlayerMenu : MediaPlayer? = null
        var audioPlayer : MediaPlayer? = null
        var wentToMenuActivity : Boolean = false
        var musicPlayerGame : MediaPlayer? = null

    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        musicPlayerMenu = MediaPlayer.create(this, R.raw.menus_song)
        audioPlayer = MediaPlayer.create(this, R.raw.button_click)

        musicPlayerMenu?.start()

        binding.mainMenuPlay.setOnClickListener{
            disableButtons()
            wentToMenuActivity = false


            val intent = Intent(this, HangmanGameActivity::class.java)
            startActivity(intent)

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
                musicPlayerMenu?.stop()
            }
            else
                musicPlayerMenu?.pause()

        }

        binding.mainMenuSettings.setOnClickListener{
            disableButtons()
            wentToMenuActivity = true

            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)

            if (ConfigurationViewModel.isMusicOn.value == true)
            {
                musicPlayerMenu?.start()
            }
            else
                musicPlayerMenu?.pause()
        }
        binding.mainMenuLeaderboard.setOnClickListener{
            disableButtons()
            wentToMenuActivity = true

            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)

            if (ConfigurationViewModel.isSoundOn.value == true)
            {
                audioPlayer?.start()
                musicPlayerMenu?.start()
            }
            else
                musicPlayerMenu?.pause()
        }

        binding.mainMenuExit.setOnClickListener{
            disableButtons()
            wentToMenuActivity = false

            audioPlayer?.start()

            firebaseAuth.signOut()
            finish()
        }

        fireBaseNotification()
    }

    override fun onResume() {
        super.onResume()

        enableButtons()

        musicPlayerMenu?.start()

        wentToMenuActivity = false

    }

    override fun onPause() {
        super.onPause()

        if (!wentToMenuActivity)
        {
            musicPlayerMenu?.pause()
        }

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

    private fun fireBaseNotification()
    {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this,"Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            println(token)
            // Log and toast
            val msg = "SOMETHING"
            Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
        })
    }

}