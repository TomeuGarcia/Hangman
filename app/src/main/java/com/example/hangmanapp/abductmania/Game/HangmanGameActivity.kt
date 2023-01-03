package com.example.hangmanapp.abductmania.Game

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.Game.Fragments.*
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityHangmanGameBinding

    private val COUNTDOWN_ANIM_START_TIME_SECONDS : Long = 30
    private val COUNTDOWN_ANIM_TICK_TIME_MILLISECONDS : Long = 1000
    private val END_GAME_FRAGMENT_START_DELAY_MILLISECONDS : Long = 3000

    //private val RETRY_AD_ID = "ca-app-pub-7847710980451886/1215117009"
    private val RETRY_AD_ID = "ca-app-pub-3940256099942544/5224354917" // TEST
    private var retryAd : RewardedAd? = null
    private var canRetry : Boolean = false

    private lateinit var pauseFragment : HangmanGamePauseFragment
    private lateinit var retryFragment : HangmanRetryGameFragment
    private lateinit var youWinFragment : HangmanYouWinFragment
    private lateinit var youLoseFragment : HangmanYouLoseFragment

    private val hangmanGameViewModel: HangmanGameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        pauseFragment = HangmanGamePauseFragment(this::resumeGame)
        retryFragment = HangmanRetryGameFragment(this::onRetryWatchAd, this::onRetryGiveUp)
        youWinFragment = HangmanYouWinFragment()
        youLoseFragment = HangmanYouLoseFragment()

        MobileAds.initialize(this)
        loadRetryAd()

        binding.guesswordText.text = ""

        binding.pauseIcon.setOnClickListener {
            pauseGame()
        }

        hangmanGameViewModel.isPausingDisabled.observe(this) {
            if (it) disablePausing()
        }
        hangmanGameViewModel.hasVictoryHappened.observe(this) {
            if (it) startWinFragment()
        }
        hangmanGameViewModel.hasGameOverHappened.observe(this) {
            if (it) startGameOverFragment()
        }
        hangmanGameViewModel.countDownCurrentTimeSeconds.observe(this) {
            updateCountDownText(it)
        }
        hangmanGameViewModel.hangmanWord.observe(this) {
            updateGuessWord(it)
        }
        hangmanGameViewModel.createGame(this, binding)

    }

    override fun onResume()
    {
        super.onResume()

        val request = AdRequest.Builder().build()
        binding.adView.loadAd(request)

        if (canRetry) {
            canRetry = false
            retryGame()
        }
    }


    private fun updateGuessWord(hangmanWord : String)
    {
        binding.guesswordText.text = hangmanWord
    }


    private fun disablePausing()
    {
        binding.pauseIcon.isEnabled = false
    }

    private fun startWinFragment()
    {
        CoroutineScope(Dispatchers.Default).launch {
            delay(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS)

            youWinFragment.setWordAndScore(hangmanGameViewModel.getHangmanWord(),
                                           hangmanGameViewModel.getScore())
            setEndGameFragment(youWinFragment)
        }
    }

    private fun startGameOverFragment()
    {
        CoroutineScope(Dispatchers.Default).launch {
            delay(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS)

            if (hangmanGameViewModel.hasRetriesLeft()){
                setRetryGameFragment()
            }
            else {
                youLoseFragment.setWordAndScore(hangmanGameViewModel.getHangmanWord(),
                                                hangmanGameViewModel.getScore())
                setEndGameFragment(youLoseFragment)
            }
        }
    }


    private fun setEndGameFragment(fragment: HangmanEndGameFragment)
    {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach {
                hide(it)
            }

            if (fragment.isAdded)
            {
                show(fragment)
            }
            else
            {
                replace(binding.fragmentFrameLayout.id, fragment)
            }

            commit()
        }
    }

    private fun pauseGame()
    {
        binding.pauseIcon.isEnabled = false
        hangmanGameViewModel.pauseCountDownTimer()

        supportFragmentManager.beginTransaction().apply {
            if (pauseFragment.isAdded)
            {
                show(pauseFragment)
            }
            else
            {
                replace(binding.fragmentFrameLayout.id, pauseFragment)
            }
            commit()
        }
    }

    private fun resumeGame()
    {
        binding.pauseIcon.isEnabled = true
        hangmanGameViewModel.resumeCountDownTimer()

        supportFragmentManager.beginTransaction().apply {
            hide(pauseFragment)
            commit()
        }
    }

    private fun onRetryWatchAd()
    {
        // TODO make ad here
        hangmanGameViewModel.pauseCountDownTimer()
        showRetryAd()

        //retryGame() // TODO call this after watching ad
    }

    private fun retryGame()
    {
        binding.pauseIcon.isEnabled = true
        hangmanGameViewModel.retryGameReset()

        //retryFragment.hideProgress()
        supportFragmentManager.beginTransaction().apply {
            hide(retryFragment)
            commit()
        }
    }

    private fun onRetryGiveUp()
    {
        hangmanGameViewModel.doGameOver()
        hangmanGameViewModel.disableRetries()
    }

    private fun setRetryGameFragment()
    {
        hangmanGameViewModel.pauseCountDownTimer()

        supportFragmentManager.beginTransaction().apply {
            if (retryFragment.isAdded)
            {
                show(retryFragment)
            }
            else
            {
                replace(binding.fragmentFrameLayout.id, retryFragment)
            }
            commit()
        }
    }



    private fun updateCountDownText(currentTime: Long)
    {
        binding.countDownText.text = currentTime.toString() + "s"

        if (currentTime <= COUNTDOWN_ANIM_START_TIME_SECONDS)
        {
            playCountDownTextColorAnim()
        }
    }

    private fun playCountDownTextColorAnim()
    {
        ObjectAnimator.ofArgb(binding.countDownText, "textColor",
            resources.getColor(R.color.error_red), resources.getColor(R.color.green_soft))
            .apply {
                duration = COUNTDOWN_ANIM_TICK_TIME_MILLISECONDS
                start()
            }
    }

    private fun loadRetryAd()
    {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this,RETRY_AD_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                retryAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                retryAd = rewardedAd
            }
        })
    }

    private fun showRetryAd()
    {
        if (retryAd == null)
        {
            retryGame()
            return
        }

        retryAd?.show(this,
            OnUserEarnedRewardListener() {
                canRetry = true
        })


    }

}