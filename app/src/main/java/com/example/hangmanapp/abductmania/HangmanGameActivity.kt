package com.example.hangmanapp.abductmania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class HangmanGameActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHangmanGameBinding

    private lateinit var hangmanApiUrl : String
    private lateinit var guessWord : String
    private lateinit var gameToken : String

    private var solution : String = ""
    private var hint : Char = ' '



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHangmanGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hangmanApiUrl = "https://hangman-api.herokuapp.com/"
        createNewHangmanGame()


        binding.aButton.setOnClickListener {
            Toast.makeText(this, guessWord, Toast.LENGTH_LONG).show()
        }

        binding.bButton.setOnClickListener {
            Toast.makeText(this, gameToken, Toast.LENGTH_LONG).show()
        }

        binding.cButton.setOnClickListener {
            createNewHangmanGame()
        }

        binding.dButton.setOnClickListener {
            getSolution()
        }

        binding.eButton.setOnClickListener {
            getHint()
        }

        binding.fButton.setOnClickListener {
            guessLetter('a')
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
                Toast.makeText(this@HangmanGameActivity, solution, Toast.LENGTH_LONG).show()
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

                Toast.makeText(this@HangmanGameActivity, letter.toString(), Toast.LENGTH_LONG)

                if (isCorrect) { onGuessedLetterCorrectly(letter) }
                else { onGuessedLetterIncorrectly(letter) }
            }

            override fun onFailure(call: Call<HangmanLetterGuessResponse>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> guessLetter()", Toast.LENGTH_LONG)
            }
        })
    }


    private fun onGuessedLetterCorrectly(letter : Char)
    {
        Toast.makeText(this,("CORRECT: "+letter), Toast.LENGTH_LONG)
    }

    private fun onGuessedLetterIncorrectly(letter : Char)
    {
        Toast.makeText(this,("Pepega Clap: "+letter), Toast.LENGTH_LONG)
    }




}