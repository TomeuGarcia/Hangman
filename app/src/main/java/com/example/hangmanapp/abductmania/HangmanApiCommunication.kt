package com.example.hangmanapp.abductmania

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanApiCommunication(private val createNewHangmanGameResponseCallback : (HangmanNewGame) -> Unit,
                              private val createNewHangmanGameFailureCallback : () -> Unit,
                              private val getSolutionResponseCallback : (HangmanGameSolution) -> Unit,
                              private val getSolutionFailureCallback : () -> Unit,
                              private val getHintResponseCallback : (HangmanGameHint) -> Unit,
                              private val getHintFailureCallback : () -> Unit,
                              private val guessLetterResponseCallback : (HangmanLetterGuessResponse, Char) -> Unit,
                              private val guessLetterFailureCallback : () -> Unit)
{

    private val HANGMAN_API_URL : String = "https://hangman-api.herokuapp.com/"



    public fun createNewHangmanGame()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.createNewHangmanGame().enqueue(object : Callback<HangmanNewGame> {

            override fun onResponse(call: Call<HangmanNewGame>,
                                    response: Response<HangmanNewGame>)
            {
                val hangmanNewGame =  response.body()
                if (hangmanNewGame != null)
                {
                    createNewHangmanGameResponseCallback(hangmanNewGame)
                }
                else
                {
                    createNewHangmanGameFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanNewGame>, t: Throwable)
            {
                createNewHangmanGameFailureCallback()
            }
        })
    }

    public fun getSolution(gameToken : String)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.getSolution(gameToken).enqueue(object : Callback<HangmanGameSolution> {
            override fun onResponse(call: Call<HangmanGameSolution>,
                                    response: Response<HangmanGameSolution>)
            {
                val hangmanGameSolution = response.body()
                if (hangmanGameSolution != null)
                {
                    getSolutionResponseCallback(hangmanGameSolution)
                }
                else
                {
                    getSolutionFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanGameSolution>, t: Throwable)
            {
                getSolutionFailureCallback()
            }
        })
    }

    public fun getHint(gameToken : String)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.getHint(gameToken).enqueue(object : Callback<HangmanGameHint> {
            override fun onResponse(call: Call<HangmanGameHint>,
                                    response: Response<HangmanGameHint>)
            {
                val hangmanGameHint = response.body()
                if (hangmanGameHint != null)
                {
                    getHintResponseCallback(hangmanGameHint)
                }
                else
                {
                    getHintFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanGameHint>, t: Throwable)
            {
                getHintFailureCallback()
            }
        })
    }

    public fun guessLetter(gameToken : String, letter : Char)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(HANGMAN_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiHangman::class.java)

        call.guessLetter(letter.toString(), gameToken).enqueue(object :
            Callback<HangmanLetterGuessResponse> {
            override fun onResponse(call: Call<HangmanLetterGuessResponse>,
                                    response: Response<HangmanLetterGuessResponse>)
            {
                val hangmanLetterGuessResponse = response.body()
                if (hangmanLetterGuessResponse != null)
                {
                    guessLetterResponseCallback(hangmanLetterGuessResponse, letter)
                }
                else
                {
                    guessLetterFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanLetterGuessResponse>, t: Throwable)
            {
                guessLetterFailureCallback()
            }
        })
    }

}