package com.example.hangmanapp.abductmania.Game.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.abductmania.MainMenu.MainMenuViewModel
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
            MainMenuViewModel.buttonSfxMP?.start()

            backToPlayCallback()
        }

        binding.pHomeIcon.setOnClickListener {
            activity?.finish()

            MainMenuViewModel.buttonSfxMP?.start()
        }

        binding.pauseBackgroundImage.setOnClickListener {  }

        return binding.root
    }

}