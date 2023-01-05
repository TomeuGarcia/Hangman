package com.example.hangmanapp.abductmania.Game.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.FragmentHangmanGamePauseBinding

class HangmanGamePauseFragment(private val backToPlayCallback : () -> Unit)
    : Fragment()
{
    private lateinit var binding : FragmentHangmanGamePauseBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanGamePauseBinding.inflate(inflater, container, false)

        binding.pPlayIcon.setOnClickListener {
            MainMenuActivity.audioPlayer?.start()

            backToPlayCallback()
        }

        binding.pHomeIcon.setOnClickListener {

            activity?.finish()

            MainMenuActivity.audioPlayer?.start()
            MainMenuActivity.musicPlayerMenu?.start()
        }

        binding.pauseBackgroundImage.setOnClickListener {  }

        return binding.root
    }

}