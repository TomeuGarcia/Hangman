package com.example.hangmanapp.abductmania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHangmanGameBinding

    private val hangmanApiUrl : String = "https://hangman-api.herokuapp.com/"

    private var guessWord : String = ""
    private var gameToken : String = ""

    private var solution : String = ""
    private var hint : Char = ' '

    private lateinit var gameKeyboardMap : GameKeyboardMap


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

        binding.xButton.setOnClickListener {
            createNewHangmanGame()
        }

        binding.yButton.setOnClickListener {
            getSolution()
        }

        binding.zButton.setOnClickListener {
            getHint()
        }

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
                guessWord = response.body()?.hangman ?: ""
                gameToken = response.body()?.token ?: ""

                binding.guesswordText.text = guessWord
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

                guessWord = solution
                binding.guesswordText.text = guessWord
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
                guessWord = response.body()?.hangman ?: "";
                gameToken = response.body()?.token ?: ""
                binding.guesswordText.text = guessWord

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
    }

    private fun onGuessedLetterIncorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterWrong(letter)
    }




}