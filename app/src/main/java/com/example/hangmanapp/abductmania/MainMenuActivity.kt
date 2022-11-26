package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityMainBinding
import com.example.hangmanapp.databinding.ActivityMainMenuBinding
import kotlin.system.exitProcess

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.mainMenuPlay.setOnClickListener{
            val intent = Intent(this, HangmanGameActivity::class.java)
            startActivity(intent)
        }

        binding.mainMenuSettings.setOnClickListener{
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }
        binding.mainMenuLeaderboard.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }

        binding.mainMenuExit.setOnClickListener{
            finish()
            exitProcess(0)
        }
    }
}