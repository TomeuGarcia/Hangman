package com.example.hangmanapp.abductmania


import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max

class HangmanGameViewModel()
    : ViewModel()
{
    public val hangmanWord = MutableLiveData<String>()
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


    private var isGameOver : Boolean = false

    private lateinit var hangmanApiCommunication : HangmanApiCommunication
    private lateinit var gameKeyboardMap : GameKeyboardMap
    private lateinit var hangmanDrawer : HangmanDrawer


    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TOTAL_TIME_MILLISECONDS : Long = 60000
    private val COUNTDOWN_INTERVAL_TIME_MILLISECONDS : Long = 1000
    public var countDownCurrentTimeSeconds = MutableLiveData<Long>()

    private lateinit var context : Context  // Used to debug with Toast()

    private lateinit var onDisablePausingCallback : () -> Unit
    private lateinit var onVictoryCallback : (String, Int, Long) -> Unit
    private lateinit var onGameOverCallback : (String, Int, Long) -> Unit


    public fun create(context: Context, binding: ActivityHangmanGameBinding)
    {
        this.context = context

        hangmanWord.value = ""
        countDownCurrentTimeSeconds.value = COUNTDOWN_TOTAL_TIME_MILLISECONDS / 1000

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

        createNewHangmanGame()
    }

    public fun initCallbacks(onDisablePausingCallback : () -> Unit,
                             onVictoryCallback : (String, Int, Long) -> Unit,
                             onGameOverCallback : (String, Int, Long) -> Unit)
    {
        this.onDisablePausingCallback = onDisablePausingCallback
        this.onVictoryCallback = onVictoryCallback
        this.onGameOverCallback = onGameOverCallback
    }


    private fun createNewHangmanGame()
    {
        gameKeyboardMap.disableRemainingLetterButtons()
        hangmanApiCommunication.createNewHangmanGame()
    }
    private fun onCreateNewHangmanGameResponse(hangmanNewGame : HangmanNewGame)
    {
        hangmanWord.value = hangmanNewGame.hangman
        gameToken = hangmanNewGame.token

        gameKeyboardMap.reenableRemainingLetterButtons()

        startCountDownTimer(COUNTDOWN_TOTAL_TIME_MILLISECONDS)
    }
    private fun onCreateNewHangmanGameFailure()
    {
        Toast.makeText(context,
            "Something went wrong -> createNewHangmanGame()", Toast.LENGTH_LONG).show()

        gameKeyboardMap.reenableRemainingLetterButtons()
    }



    private fun getSolution()
    {
        hangmanApiCommunication.getSolution(gameToken)
    }
    private fun onGetSolutionResponse(hangmanGameSolution : HangmanGameSolution)
    {
        hangmanWord.value = hangmanGameSolution.solution
        gameToken = hangmanGameSolution.token

        onGameOverSolutionObtained(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS)
    }
    private fun onGetSolutionFailure()
    {
        Toast.makeText(context,
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
        Toast.makeText(context,
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
            hangmanWord.value = hangmanLetterGuessResponse.hangman
            gameToken = hangmanLetterGuessResponse.token

            if (isCorrect) { onGuessedLetterCorrectly(letter) }
            else { onGuessedLetterIncorrectly(letter) }
        }
    }
    private fun onGuessLetterFailure()
    {
        Toast.makeText(context,
            "Something went wrong -> guessLetter()", Toast.LENGTH_LONG).show()

        gameKeyboardMap.reenableRemainingLetterButtons()
    }


    private fun onGuessedLetterCorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterCorrect(letter)
        score += CORRECT_LETTER_POINTS

        if (hasGuessedAllLetters())
            doVictory(END_GAME_FRAGMENT_START_DELAY_MILLISECONDS)
        else
            gameKeyboardMap.reenableRemainingLetterButtons()

    }

    private fun onGuessedLetterIncorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterWrong(letter)
        score -= WRONG_LETTER_POINTS

        hangmanDrawer.drawPart(wrongGuessesCount)

        if (++wrongGuessesCount == MAX_WRONG_GUESSES)
            doGameOver()
        else
            gameKeyboardMap.reenableRemainingLetterButtons()
    }

    private fun hasGuessedAllLetters() : Boolean
    {
        hangmanWord.value?.apply {
            return !this.any {
                it == MISSING_LETTER
            }
        }

        return false
    }


    private fun doVictory(startFragmentDelay : Long)
    {
        countDownCurrentTimeSeconds.value?.apply {
            score += ALL_LETTERS_GUESSED_POINTS + this.toInt()
        }

        gameKeyboardMap.disableRemainingLetterButtons()
        countDownTimer?.cancel()

        onDisablePausingCallback()
        onVictoryCallback(hangmanWord.value ?: "", score, startFragmentDelay)
    }

    private fun doGameOver()
    {
        isGameOver = true
        gameKeyboardMap.disableRemainingLetterButtons()
        countDownTimer?.cancel()

        getSolution() // this is async.... wait until solution received to do real GameOver
        onDisablePausingCallback()
    }

    private fun onGameOverSolutionObtained(startFragmentDelay : Long)
    {
        score = max(0, score) // Make score not negative

        onGameOverCallback(hangmanWord.value ?: "", score, startFragmentDelay)
    }



    private fun startCountDownTimer(startTime : Long)
    {
        countDownTimer = object : CountDownTimer(startTime, COUNTDOWN_INTERVAL_TIME_MILLISECONDS){
            override fun onTick(millisUntilFinished: Long)
            {
                countDownCurrentTimeSeconds.value = millisUntilFinished / 1000
            }
            override fun onFinish()
            {
                if(countDownCurrentTimeSeconds.value as Long <= 0)
                {
                    doGameOver()
                }
            }
        }.start()
    }

    public fun pauseCountDownTimer()
    {
        countDownTimer?.cancel()
    }

    public fun resumeCountDownTimer()
    {
        startCountDownTimer((countDownCurrentTimeSeconds.value ?: 0) * 1000)
    }

}