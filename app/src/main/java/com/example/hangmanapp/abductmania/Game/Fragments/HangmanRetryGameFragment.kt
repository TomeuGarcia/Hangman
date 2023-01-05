package com.example.hangmanapp.abductmania.Game.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.FragmentHangmanRetryGameBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class HangmanRetryGameFragment(private val onWatchAdCallback : () -> Unit,
                               private val onGiveUpCallback : () -> Unit)
    : Fragment()
{

    private lateinit var binding : FragmentHangmanRetryGameBinding

    private val NEW_CHANCE = "new_chance"
    private val NEW_CHANCE_PARAM = "NEW_CHANCE_PARAM"
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHangmanRetryGameBinding.inflate(inflater, container, false)

        binding.rgBackgroundImage.setOnClickListener {  }

        binding.rgWatchAdIcon.setOnClickListener {
            analyticsPlayerChoice(true)
            onWatchAdCallback()
            disableButtons()
            showProgress()
        }

        binding.rgGiveUpButton.setOnClickListener {
            analyticsPlayerChoice(false)
            onGiveUpCallback()
            disableButtons()
            showProgress()

            MainMenuActivity.audioPlayer?.start()
            MainMenuActivity.musicPlayerMenu?.start()
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

    private fun analyticsPlayerChoice(showAd : Boolean)
    {
        firebaseAnalytics.logEvent(
            NEW_CHANCE,
            bundleOf(
                NEW_CHANCE_PARAM to showAd
            )
        )
    }
}