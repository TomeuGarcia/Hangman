package com.example.hangmanapp.abductmania.DatabaseUtils

data class User (val username : String = "",
                 val email : String = "",
                 var language : Int = 0,
                 var notifications: Boolean = true,
                 var music : Boolean = true,
                 var sound : Boolean = true)
{

}