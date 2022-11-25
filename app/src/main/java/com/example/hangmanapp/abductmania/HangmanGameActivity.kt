package com.example.hangmanapp.abductmania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.max

class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHangmanGameBinding

    private val hangmanApiUrl : String = "https://hangman-api.herokuapp.com/"

    private var hangmanWord : String = ""
    private var gameToken : String = ""

    private var solution : String = ""
    private var hint : Char = ' '

    private lateinit var gameKeyboardMap : GameKeyboardMap

    private val CORRECT_LETTER_POINTS : Int = 50
    private val WRONG_LETTER_POINTS : Int = 30
    private val ALL_LETTERS_GUESSED_POINTS : Int = 200
    private var score : Int = 0

    private var wrongGuessesCount : Int = 0
    private val MAX_WRONG_GUESSES : Int = 7

    private val MISSING_LETTER : Char = '_'


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        gameKeyboardMap = GameKeyboardMap(binding)
        gameKeyboardMap.initButtonsClickCallback(this::guessLetter)
        gameKeyboardMap.hideOverlapImages()


        createNewHangmanGame()

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
        val retrofit = Retrofit.Builder()
            .baseUrl(hangmanApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.createNewHangmanGame().enqueue(object : Callback<HangmanGame>{

            override fun onResponse(call: Call<HangmanGame>, response: Response<HangmanGame>)
            {
                hangmanWord = response.body()?.hangman ?: ""
                gameToken = response.body()?.token ?: ""

                binding.guesswordText.text = hangmanWord
            }

            override fun onFailure(call: Call<HangmanGame>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> createNewHangmanGame()", Toast.LENGTH_LONG)
            }
        })
    }

    private fun getSolution()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(hangmanApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.getSolution(gameToken).enqueue(object : Callback<HangmanGameSolution>{
            override fun onResponse(call: Call<HangmanGameSolution>,response: Response<HangmanGameSolution>)
            {
                solution = response.body()?.solution ?: ""
                gameToken = response.body()?.token ?: ""
                Toast.makeText(this@HangmanGameActivity, solution, Toast.LENGTH_LONG).show()

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
            .baseUrl(hangmanApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.getHint(gameToken).enqueue(object : Callback<HangmanGameHint>{
            override fun onResponse(call: Call<HangmanGameHint>,response: Response<HangmanGameHint>)
            {
                hint = response.body()?.hint?.get(0) ?: ' '
                gameToken = response.body()?.token ?: ""

                Toast.makeText(this@HangmanGameActivity, hint.toString(), Toast.LENGTH_LONG).show()
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
            .baseUrl(hangmanApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

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
            }
        })
    }


    private fun onGuessedLetterCorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterCorrect(letter)
        score += CORRECT_LETTER_POINTS

        if (hasGuessedAllLetters()) doVictory()
    }

    private fun onGuessedLetterIncorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterWrong(letter)
        score -= WRONG_LETTER_POINTS

        if (++wrongGuessesCount == MAX_WRONG_GUESSES) doGameOver()
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
        Toast.makeText(this, "GUESSED ALL LETTERS", Toast.LENGTH_LONG).show()

        // TODO Open WIN fragment
        val youWinFragment = HangmanYouWinFragment()
        //youWinFragment.Init(hangmanWord, score)
        changeFragment(youWinFragment)
    }

    private fun doGameOver()
    {
        getSolution() // this is async.... wait until solution received to do real GameOver
    }

    private fun onGameOverSolutionObtained()
    {
        score = max(0, score) // Make score not negative
        Toast.makeText(this, "GAME OVER", Toast.LENGTH_LONG).show()

        // TODO Open LOSE fragment
    }

    private fun changeFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().apply{
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


}