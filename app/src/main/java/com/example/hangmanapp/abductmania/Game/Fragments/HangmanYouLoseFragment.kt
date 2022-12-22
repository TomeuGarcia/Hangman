package com.example.hangmanapp.abductmania.Game.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingUFO
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.FragmentHangmanYouLoseBinding

open class HangmanYouLoseFragment(private val backToPlayCallback : () -> Unit)
    : HangmanEndGameFragment()
{
    private lateinit var binding: FragmentHangmanYouLoseBinding

    private lateinit var drawingUFOs : HangmanDrawingUFO

    private var retriesEnabled : Boolean = true


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        binding = FragmentHangmanYouLoseBinding.inflate(inflater, container, false)


        binding.ylWatchAdIcon.setOnClickListener {
            backToPlayCallback()
        }

        binding.ylHomeIcon.setOnClickListener {
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

    override fun onResume() {
        super.onResume()

        if (retriesEnabled)
        {
            binding.ylWatchAdIcon.visibility = View.VISIBLE

            binding.ylHangmanWordText.visibility = View.INVISIBLE
            binding.ylHangmanWordText.text = hangmanWord

            binding.ylTheWordWasText.visibility = View.INVISIBLE

            Toast.makeText(activity, "RETRIES", Toast.LENGTH_SHORT).show()
        }
        else
        {
            binding.ylWatchAdIcon.visibility = View.GONE

            binding.ylHangmanWordText.visibility = View.VISIBLE
            binding.ylHangmanWordText.text = hangmanWord

            binding.ylTheWordWasText.visibility = View.VISIBLE

            Toast.makeText(activity, "NO retries", Toast.LENGTH_SHORT).show()
        }

        binding.ylScoreText.text = score.toString()
    }

    public override fun initViewComponents()
    {
        binding.ylHangmanWordText.text = hangmanWord
        binding.ylScoreText.text = score.toString()
    }

    public fun enableRetries()
    {
        retriesEnabled = true
    }

    public fun disableRetries()
    {
        retriesEnabled = false
    }

}