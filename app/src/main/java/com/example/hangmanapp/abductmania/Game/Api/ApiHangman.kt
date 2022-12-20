package com.example.hangmanapp.abductmania.Game.Api


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiHangman
{
    @POST("hangman")
    fun createNewHangmanGame() : Call<HangmanNewGame>

    @GET("new")
    fun createNewLangHangmanGame(@Query("lang") lang : String) : Call<HangmanNewGame>

    @GET("hangman")
    fun getSolution(@Query("token") token : String) : Call<HangmanGameSolution>

    @GET("hangman/hint")
    fun getHint(@Query("token") token : String) : Call<HangmanGameHint>

    @PUT("hangman")
    fun guessLetter(@Query("letter") letter : String, @Query("token") token : String) :
            Call<HangmanLetterGuessResponse>
}