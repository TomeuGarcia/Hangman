package com.example.hangmanapp.abductmania.Game.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hangmanapp.databinding.FragmentHangmanRetryGameBinding


class HangmanRetryGameFragment(private val onWatchAdCallback : () -> Unit,
                               private val onGiveUpCallback : () -> Unit)
    : Fragment()
{

    private lateinit var binding : FragmentHangmanRetryGameBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHangmanRetryGameBinding.inflate(inflater, container, false)

        binding.rgBackgroundImage.setOnClickListener {  }

        binding.rgWatchAdIcon.setOnClickListener {
            onWatchAdCallback()
        }

        binding.rgGiveUpButton.setOnClickListener {
            onGiveUpCallback()
        }

        return binding.root
    }
}