package com.example.hangmanapp.abductmania

data class HangmanGame(val hangman: String,
                       val token: String)
{
}

data class HangmanGameSolution(val solution: String,
                               val token: String)
{
}

data class HangmanGameHint(val letter: String,
                           val token: String)
{
}