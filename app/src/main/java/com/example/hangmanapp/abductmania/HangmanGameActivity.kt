package com.example.hangmanapp.abductmania

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
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

    private lateinit var pauseFragment : HangmanGamePauseFragment

    private val hangmanGameViewModel: HangmanGameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.guesswordText.text = ""

        binding.pauseIcon.setOnClickListener {
            pauseGame()
        }



        pauseFragment = HangmanGamePauseFragment(this::resumeGame)


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
            setEndGameFragment(HangmanYouWinFragment(hangmanGameViewModel.getHangmanWord(),
                                                     hangmanGameViewModel.getScore()))
        }
    }

    private fun startGameOverFragment()
    {
        CoroutineScope(Dispatchers.Default).launch {
            delay(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS)
            setEndGameFragment(HangmanYouLoseFragment(hangmanGameViewModel.getHangmanWord(),
                                                      hangmanGameViewModel.getScore()))
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

}