package com.example.hangmanapp.abductmania.Game.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.FragmentHangmanYouWinBinding

open class HangmanYouWinFragment(hangmanWord: String, score: Int)
    : HangmanEndGameFragment(hangmanWord, score)
{
    private lateinit var binding: FragmentHangmanYouWinBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanYouWinBinding.inflate(inflater, container, false)


        binding.ywHomeIcon.setOnClickListener {
            val intent = Intent(activity, MainMenuActivity::class.java)
            startActivity(intent)
        }

        binding.ywReplayIcon.setOnClickListener {
            activity?.finish()
            startActivity(activity?.intent);
        }

        initViewComponents()

        return binding.root
    }

    public override fun initViewComponents()
    {
        binding.ywHangmanWordText.text = hangmanWord
        binding.ywScoreText.text = score.toString()
    }



}