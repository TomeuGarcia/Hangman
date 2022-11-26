package com.example.hangmanapp.abductmania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max

class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHangmanGameBinding

    private val HANGMAN_API_URL : String = "https://hangman-api.herokuapp.com/"

    private var hangmanWord : String = ""
    private var gameToken : String = ""

    private var solution : String = ""
    private var hint : Char = ' '

    private val CORRECT_LETTER_POINTS : Int = 50
    private val WRONG_LETTER_POINTS : Int = 30
    private val ALL_LETTERS_GUESSED_POINTS : Int = 200
    private var score : Int = 0

    private var wrongGuessesCount : Int = 0
    private val MAX_WRONG_GUESSES : Int = 8

    private val MISSING_LETTER : Char = '_'

    private val END_GAME_FRAGMENT_START_DELAY : Long = 3000

    private lateinit var gameKeyboardMap : GameKeyboardMap
    private lateinit var hangmanDrawer : HangmanDrawer
    private lateinit var pauseFragment : HangmanGamePauseFragment


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        gameKeyboardMap = GameKeyboardMap(binding)
        gameKeyboardMap.initButtonsClickCallback(this::guessLetter)
        gameKeyboardMap.hideOverlapImages()

        binding.guesswordText.text = ""

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

        binding.pauseIcon.setOnClickListener {
            pauseGame()
        }

        /*
        binding.xButton.setOnClickListener {
            createNewHangmanGame()
        }

        binding.yButton.setOnClickListener {
            getSolution()
        }

        binding.zButton.setOnClickListener {
            getHint()
        }
        */

    }


    private fun createNewHangmanGame()
    {
        gameKeyboardMap.disableRemainingLetterButtons()

        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.createNewHangmanGame().enqueue(object : Callback<HangmanGame>{

            override fun onResponse(call: Call<HangmanGame>, response: Response<HangmanGame>)
            {
                hangmanWord = response.body()?.hangman ?: ""
                gameToken = response.body()?.token ?: ""

                binding.guesswordText.text = hangmanWord

                gameKeyboardMap.reenableRemainingLetterButtons()
            }

            override fun onFailure(call: Call<HangmanGame>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> createNewHangmanGame()", Toast.LENGTH_LONG)

                gameKeyboardMap.reenableRemainingLetterButtons()
            }
        })
    }

    private fun getSolution()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.getSolution(gameToken).enqueue(object : Callback<HangmanGameSolution>{
            override fun onResponse(call: Call<HangmanGameSolution>,response: Response<HangmanGameSolution>)
            {
                solution = response.body()?.solution ?: ""
                gameToken = response.body()?.token ?: ""

                hangmanWord = solution
                binding.guesswordText.text = hangmanWord

                onGameOverSolutionObtained()
            }

            override fun onFailure(call: Call<HangmanGameSolution>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> getSolution()", Toast.LENGTH_LONG)
            }
        })
    }

    private fun getHint()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.getHint(gameToken).enqueue(object : Callback<HangmanGameHint>{
            override fun onResponse(call: Call<HangmanGameHint>,response: Response<HangmanGameHint>)
            {
                hint = response.body()?.hint?.get(0) ?: ' '
                gameToken = response.body()?.token ?: ""

                guessLetter(hint)
            }

            override fun onFailure(call: Call<HangmanGameHint>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> getHint()", Toast.LENGTH_LONG)
            }
        })
    }

    private fun guessLetter(letter : Char)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        gameKeyboardMap.disableRemainingLetterButtons()

        call.guessLetter(letter.toString(), gameToken).enqueue(object : Callback<HangmanLetterGuessResponse>{
            override fun onResponse(call: Call<HangmanLetterGuessResponse>,response: Response<HangmanLetterGuessResponse>)
            {
                val isCorrect : Boolean = response.body()?.correct ?: false
                hangmanWord = response.body()?.hangman ?: "";
                gameToken = response.body()?.token ?: ""
                binding.guesswordText.text = hangmanWord

                if (isCorrect) { onGuessedLetterCorrectly(letter) }
                else { onGuessedLetterIncorrectly(letter) }
            }

            override fun onFailure(call: Call<HangmanLetterGuessResponse>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> guessLetter()", Toast.LENGTH_LONG).show()

                gameKeyboardMap.reenableRemainingLetterButtons()
            }
        })
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
        return !binding.guesswordText.text.any {
            it == MISSING_LETTER
        }
    }

    private fun doVictory()
    {
        score += ALL_LETTERS_GUESSED_POINTS

        gameKeyboardMap.disableRemainingLetterButtons()
        binding.pauseIcon.isEnabled = false

        Timer().schedule(END_GAME_FRAGMENT_START_DELAY) {
            setEndGameFragment(HangmanYouWinFragment(hangmanWord, score))
        }
    }

    private fun doGameOver()
    {
        gameKeyboardMap.disableRemainingLetterButtons()
        binding.pauseIcon.isEnabled = false

        getSolution() // this is async.... wait until solution received to do real GameOver
    }

    private fun onGameOverSolutionObtained()
    {
        score = max(0, score) // Make score not negative

        CoroutineScope(Dispatchers.Default).launch {
            delay(END_GAME_FRAGMENT_START_DELAY)
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

        supportFragmentManager.beginTransaction().apply {
            hide(pauseFragment)
            commit()
        }
    }


}