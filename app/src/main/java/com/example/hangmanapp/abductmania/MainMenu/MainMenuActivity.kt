package com.example.hangmanapp.abductmania.MainMenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.abductmania.Config.ConfigurationActivity
import com.example.hangmanapp.abductmania.Game.HangmanGameActivity
import com.example.hangmanapp.abductmania.Ranking.RankingActivity
import com.example.hangmanapp.databinding.ActivityMainMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.mainMenuPlay.setOnClickListener{
            disableButtons()
            val intent = Intent(this, HangmanGameActivity::class.java)
            startActivity(intent)
        }

        binding.mainMenuSettings.setOnClickListener{
            disableButtons()
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }
        binding.mainMenuLeaderboard.setOnClickListener{
            disableButtons()
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }

        binding.mainMenuExit.setOnClickListener{
            disableButtons()
            firebaseAuth.signOut()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        enableButtons()
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

}