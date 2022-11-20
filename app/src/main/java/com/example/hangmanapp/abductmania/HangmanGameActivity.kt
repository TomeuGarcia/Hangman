package com.example.hangmanapp.abductmania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding

class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHangmanGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}