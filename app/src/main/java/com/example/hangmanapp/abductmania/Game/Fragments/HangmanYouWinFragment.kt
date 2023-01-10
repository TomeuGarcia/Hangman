package com.example.hangmanapp.abductmania.Game.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingUFO
import com.example.hangmanapp.abductmania.Game.HangmanGameViewModel
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.abductmania.MainMenu.MainMenuViewModel
import com.example.hangmanapp.databinding.FragmentHangmanYouWinBinding

open class HangmanYouWinFragment()
    : HangmanEndGameFragment()
{
    private lateinit var binding: FragmentHangmanYouWinBinding

    private lateinit var drawingUFOs : HangmanDrawingUFO


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanYouWinBinding.inflate(inflater, container, false)


        binding.ywHomeIcon.setOnClickListener {
            MainMenuViewModel.buttonSfxMP?.start()
            //MainMenuViewModel.menuMusicMP?.start()
            MainMenuViewModel.gameMusicMP?.pause()

            activity?.finish()
        }

        binding.ywReplayIcon.setOnClickListener {
            MainMenuViewModel.buttonSfxMP?.start()

            activity?.finish()
            startActivity(activity?.intent)
        }

        drawingUFOs = HangmanDrawingUFO(binding.ywUfosImage, 3000, 30f)
        drawingUFOs.setEndVisibility()


        initViewComponents()
        HangmanGameViewModel.victorySfxMP?.start()

        return binding.root
    }

    public override fun initViewComponents()
    {
        binding.ywHangmanWordText.text = hangmanWord
        binding.ywScoreText.text = score.toString()
    }




}