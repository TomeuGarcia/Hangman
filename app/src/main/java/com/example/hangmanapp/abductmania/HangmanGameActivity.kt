package com.example.hangmanapp.abductmania

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max


class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityHangmanGameBinding

    private var hangmanWord : String = ""
    private var gameToken : String = ""
    private var hint : Char = ' '

    private val CORRECT_LETTER_POINTS : Int = 50
    private val WRONG_LETTER_POINTS : Int = 30
    private val ALL_LETTERS_GUESSED_POINTS : Int = 200
    private var score : Int = 0

    private val MAX_WRONG_GUESSES : Int = 8
    private var wrongGuessesCount : Int = 0

    private val MISSING_LETTER : Char = '_'

    private val END_GAME_FRAGMENT_START_DELAY_MILLISECONDS : Long = 3000

    private val COUNTDOWN_TOTAL_TIME_MILLISECONDS : Long = 60000
    private val COUNTDOWN_INTERVAL_TIME_MILLISECONDS : Long = 1000
    private val COUNTDOWN_ANIM_START_TIME_MILLISECONDS : Long = COUNTDOWN_TOTAL_TIME_MILLISECONDS / 2
    private var countDownCurrentTimeSeconds : Long = COUNTDOWN_TOTAL_TIME_MILLISECONDS / 1000

    private var isGameOver : Boolean = false

    private lateinit var hangmanApiCommunication : HangmanApiCommunication
    private lateinit var gameKeyboardMap : GameKeyboardMap
    private lateinit var hangmanDrawer : HangmanDrawer
    private lateinit var pauseFragment : HangmanGamePauseFragment
    private lateinit var countDownTimer : CountDownTimer

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

        updateCountDownText(COUNTDOWN_TOTAL_TIME_MILLISECONDS / 1000)


        hangmanApiCommunication = HangmanApiCommunication(
            this::onCreateNewHangmanGameResponse, this::onCreateNewHangmanGameFailure,
            this::onGetSolutionResponse,          this::onGetSolutionFailure,
            this::onGetHintResponse,              this::onGetHintFailure,
            this::onGuessLetterResponse,          this::onGuessLetterFailure
        )

        gameKeyboardMap = GameKeyboardMap(binding)
        gameKeyboardMap.initButtonsClickCallback(this::guessLetter)
        gameKeyboardMap.hideOverlapImages()

        hangmanDrawer = HangmanDrawer(
            listOf(
                HangmanDrawingPart(binding.ufoCenterImage, View.INVISIBLE, View.VISIBLE),
                HangmanDrawingPart(binding.ufoLeftImage,   View.INVISIBLE, View.VISIBLE),
                HangmanDrawingPart(binding.ufoRightImage,  View.INVISIBLE, View.VISIBLE),
                HangmanDrawingPart(binding.ufoWavesImage,  View.INVISIBLE, View.VISIBLE),
                HangmanDrawingPart(binding.building2Image, View.VISIBLE,   View.INVISIBLE),
                HangmanDrawingPart(binding.building4Image, View.VISIBLE,   View.INVISIBLE),
                HangmanDrawingPart(binding.building1Image, View.VISIBLE,   View.INVISIBLE),
                HangmanDrawingPart(binding.building3Image, View.VISIBLE,   View.INVISIBLE)
            )
        )

        pauseFragment = HangmanGamePauseFragment(this::resumeGame)

        createNewHangmanGame()

    }



    private fun createNewHangmanGame()
    {
        gameKeyboardMap.disableRemainingLetterButtons()
        hangmanApiCommunication.createNewHangmanGame()
    }
    private fun onCreateNewHangmanGameResponse(hangmanNewGame : HangmanNewGame)
    {
        hangmanWord = hangmanNewGame.hangman
        gameToken = hangmanNewGame.token

        updateGuessWord(hangmanWord)

        gameKeyboardMap.reenableRemainingLetterButtons()

        startCountDownTimer(COUNTDOWN_TOTAL_TIME_MILLISECONDS)
    }
    private fun onCreateNewHangmanGameFailure()
    {
        Toast.makeText(this@HangmanGameActivity,
            "Something went wrong -> createNewHangmanGame()", Toast.LENGTH_LONG)

        gameKeyboardMap.reenableRemainingLetterButtons()
    }



    private fun getSolution()
    {
        hangmanApiCommunication.getSolution(gameToken)
    }
    private fun onGetSolutionResponse(hangmanGameSolution : HangmanGameSolution)
    {
        hangmanWord = hangmanGameSolution.solution
        gameToken = hangmanGameSolution.token

        updateGuessWord(hangmanWord)

        onGameOverSolutionObtained()
    }
    private fun onGetSolutionFailure()
    {
        Toast.makeText(this@HangmanGameActivity,
            "Something went wrong -> getSolution()", Toast.LENGTH_LONG)
    }



    private fun getHint()
    {
        hangmanApiCommunication.getHint(gameToken)
    }
    private fun onGetHintResponse(hangmanGameHint : HangmanGameHint)
    {
        hint = hangmanGameHint.hint[0]
        gameToken = hangmanGameHint.token

        guessLetter(hint)
    }
    private fun onGetHintFailure()
    {
        Toast.makeText(this@HangmanGameActivity,
            "Something went wrong -> getHint()", Toast.LENGTH_LONG)
    }



    private fun guessLetter(letter : Char)
    {
        gameKeyboardMap.disableRemainingLetterButtons()
        hangmanApiCommunication.guessLetter(gameToken, letter)
    }
    private fun onGuessLetterResponse(hangmanLetterGuessResponse : HangmanLetterGuessResponse,
                                      letter : Char)
    {
        if (!isGameOver)
        {
            val isCorrect : Boolean = hangmanLetterGuessResponse.correct
            hangmanWord = hangmanLetterGuessResponse.hangman
            gameToken = hangmanLetterGuessResponse.token
            updateGuessWord(hangmanWord)

            if (isCorrect) { onGuessedLetterCorrectly(letter) }
            else { onGuessedLetterIncorrectly(letter) }
        }
    }
    private fun onGuessLetterFailure()
    {
        Toast.makeText(this@HangmanGameActivity,
            "Something went wrong -> guessLetter()", Toast.LENGTH_LONG).show()

        gameKeyboardMap.reenableRemainingLetterButtons()
    }



    private fun updateGuessWord(hangmanWord : String)
    {
        binding.guesswordText.text = hangmanWord
    }

    private fun onGuessedLetterCorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterCorrect(letter)
        score += CORRECT_LETTER_POINTS

        if (hasGuessedAllLetters()) doVictory()
        else gameKeyboardMap.reenableRemainingLetterButtons()

    }

    private fun onGuessedLetterIncorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterWrong(letter)
        score -= WRONG_LETTER_POINTS

        hangmanDrawer.drawPart(wrongGuessesCount)

        if (++wrongGuessesCount == MAX_WRONG_GUESSES) doGameOver()
        else gameKeyboardMap.reenableRemainingLetterButtons()
    }

    private fun hasGuessedAllLetters() : Boolean
    {
        return !hangmanWord.any {
            it == MISSING_LETTER
        }
    }

    private fun doVictory()
    {
        score += ALL_LETTERS_GUESSED_POINTS + countDownCurrentTimeSeconds.toInt()

        gameKeyboardMap.disableRemainingLetterButtons()
        binding.pauseIcon.isEnabled = false
        countDownTimer.cancel()

        Timer().schedule(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS) {
            setEndGameFragment(HangmanYouWinFragment(hangmanWord, score))
        }
    }

    private fun doGameOver()
    {
        isGameOver = true
        gameKeyboardMap.disableRemainingLetterButtons()
        binding.pauseIcon.isEnabled = false
        countDownTimer.cancel()

        getSolution() // this is async.... wait until solution received to do real GameOver
    }

    private fun onGameOverSolutionObtained()
    {
        score = max(0, score) // Make score not negative

        CoroutineScope(Dispatchers.Default).launch {
            delay(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS)
            setEndGameFragment(HangmanYouLoseFragment(hangmanWord, score))
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
        gameKeyboardMap.disableRemainingLetterButtons()
        binding.pauseIcon.isEnabled = false
        countDownTimer.cancel()

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
        gameKeyboardMap.reenableRemainingLetterButtons()
        binding.pauseIcon.isEnabled = true
        startCountDownTimer(countDownCurrentTimeSeconds * 1000)

        supportFragmentManager.beginTransaction().apply {
            hide(pauseFragment)
            commit()
        }
    }

    private fun startCountDownTimer(startTime : Long)
    {
        countDownTimer = object : CountDownTimer(startTime, COUNTDOWN_INTERVAL_TIME_MILLISECONDS){
            override fun onTick(millisUntilFinished: Long)
            {
                countDownCurrentTimeSeconds = millisUntilFinished / 1000
                updateCountDownText(countDownCurrentTimeSeconds)
                if (millisUntilFinished <= COUNTDOWN_ANIM_START_TIME_MILLISECONDS) playCountDownTextColorAnim()
            }
            override fun onFinish(){
                if(countDownCurrentTimeSeconds <= 0)
                {
                    doGameOver()
                }
            }
        }.start()
    }

    private fun updateCountDownText(currentTime: Long)
    {
        binding.countDownText.text = currentTime.toString() + "s"
    }

    private fun playCountDownTextColorAnim()
    {
        ObjectAnimator.ofArgb(binding.countDownText, "textColor",
            resources.getColor(R.color.error_red), resources.getColor(R.color.green_soft))
            .apply {
                duration = COUNTDOWN_INTERVAL_TIME_MILLISECONDS
                start()
            }
    }

}