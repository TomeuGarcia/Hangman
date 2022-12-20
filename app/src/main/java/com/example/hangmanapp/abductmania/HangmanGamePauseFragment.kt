package com.example.hangmanapp.abductmania

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            backToPlayCallback()
        }

        binding.pHomeIcon.setOnClickListener {
            val intent = Intent(activity, MainMenuActivity::class.java)
            startActivity(intent)
        }

        binding.pauseBackgroundImage.setOnClickListener {  }

        return binding.root
    }

}