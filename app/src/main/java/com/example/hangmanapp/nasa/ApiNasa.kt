package com.example.hangmanapp.nasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNasa
{
    @GET("search")
    fun performSearch(@Query("q") query : String) : Call<NasaImagesList>
}