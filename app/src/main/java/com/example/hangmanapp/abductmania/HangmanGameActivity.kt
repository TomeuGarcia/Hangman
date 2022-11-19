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

    private lateinit var solution : String
    private var lastHint : Char = ' '



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
            Toast.makeText(this, solution, Toast.LENGTH_LONG).show()
        }

        binding.eButton.setOnClickListener {
            getHint()
            Toast.makeText(this, lastHint.toString(), Toast.LENGTH_LONG).show()
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
                lastHint = response.body()?.letter?.get(0) ?: ' '
            }

            override fun onFailure(call: Call<HangmanGameHint>, t: Throwable)
            {
                Toast.makeText(this@HangmanGameActivity,
                    "Something went wrong -> getHint()", Toast.LENGTH_LONG)
            }
        })
    }


}