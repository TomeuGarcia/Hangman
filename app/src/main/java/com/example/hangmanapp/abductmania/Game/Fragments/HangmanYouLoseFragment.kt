package com.example.hangmanapp.abductmania.Game.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingUFO
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.FragmentHangmanYouLoseBinding

open class HangmanYouLoseFragment()
    : HangmanEndGameFragment()
{
    private lateinit var binding: FragmentHangmanYouLoseBinding

    private lateinit var drawingUFOs : HangmanDrawingUFO


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanYouLoseBinding.inflate(inflater, container, false)


        binding.ylHomeIcon.setOnClickListener {
            MainMenuActivity.audioPlayer?.start()
            MainMenuActivity.musicPlayerMenu?.start()

            activity?.finish()
        }

        binding.ylReplayIcon.setOnClickListener {
            startActivity(activity?.intent)
            activity?.finish()
        }

        drawingUFOs = HangmanDrawingUFO(binding.ylUfosImage, 3000, 30f)
        drawingUFOs.setEndVisibility()


        initViewComponents()

        return binding.root
    }


    public override fun initViewComponents()
    {
        binding.ylHangmanWordText.text = hangmanWord
        binding.ylScoreText.text = score.toString()
    }

}