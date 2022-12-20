package com.example.hangmanapp.abductmania.Game.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.FragmentHangmanYouLoseBinding

open class HangmanYouLoseFragment(hangmanWord: String, score: Int)
    : HangmanEndGameFragment(hangmanWord, score)
{
    private lateinit var binding: FragmentHangmanYouLoseBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanYouLoseBinding.inflate(inflater, container, false)


        binding.ylHomeIcon.setOnClickListener {
            activity?.finish()
        }

        binding.ylReplayIcon.setOnClickListener {
            startActivity(activity?.intent)
            activity?.finish()
        }

        initViewComponents()

        return binding.root

    }
    public override fun initViewComponents()
    {
        binding.ylHangmanWordText.text = hangmanWord
        binding.ylScoreText.text = score.toString()
    }
}