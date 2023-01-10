package com.example.hangmanapp.abductmania.Game.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingUFO
import com.example.hangmanapp.abductmania.Game.HangmanGameViewModel
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.abductmania.MainMenu.MainMenuViewModel
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
            MainMenuViewModel.buttonSfxMP?.start()
            MainMenuViewModel.gameMusicMP?.pause()

            activity?.finish()
        }

        binding.ylReplayIcon.setOnClickListener {
            MainMenuViewModel.buttonSfxMP?.start()

            activity?.finish()
            startActivity(activity?.intent)
        }

        drawingUFOs = HangmanDrawingUFO(binding.ylUfosImage, 3000, 30f)
        drawingUFOs.setEndVisibility()


        initViewComponents()
        HangmanGameViewModel.gameOverSfxMP?.start()

        return binding.root
    }


    public override fun initViewComponents()
    {
        binding.ylHangmanWordText.text = hangmanWord
        binding.ylScoreText.text = score.toString()
    }

}