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
            disableButtons()
            showProgress()
        }

        binding.rgGiveUpButton.setOnClickListener {
            onGiveUpCallback()
            disableButtons()
            showProgress()
        }

        hideProgress()
        enableButtons()

        return binding.root
    }

    private fun showProgress()
    {
        binding.rgProgressBar.visibility = View.VISIBLE
    }

    public fun hideProgress()
    {
        binding.rgProgressBar.visibility = View.INVISIBLE
    }

    public fun enableButtons()
    {
        binding.rgWatchAdIcon.isEnabled = true
        binding.rgGiveUpButton.isEnabled = true
    }

    private fun disableButtons()
    {
        binding.rgWatchAdIcon.isEnabled = false
        binding.rgGiveUpButton.isEnabled = false
    }


}