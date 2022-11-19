package com.example.hangmanapp.abductmania


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiHangman
{
    @POST("hangman")
    fun createNewHangmanGame() : Call<HangmanGame>

    @GET("hangman")
    fun getSolution(@Query("token") token : String) : Call<HangmanGameSolution>

    @GET("hangman/hint")
    fun getHint(@Query("token") token : String) : Call<HangmanGameHint>
}