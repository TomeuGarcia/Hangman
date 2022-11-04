package com.example.hangmanapp.quotes

import retrofit2.Call
import retrofit2.http.GET

interface ApiQuotes
{
    @GET("random.json/")
    fun GetQuote() : Call<Quote>
}