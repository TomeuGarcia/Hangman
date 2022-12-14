package com.example.hangmanapp.abductmania.Game.Api

data class HangmanNewGame(val hangman: String,
                          val token: String)
{
}

data class HangmanGameSolution(val solution: String,
                               val token: String)
{
}

data class HangmanGameHint(val hint: String,
                           val token: String)
{
}

data class HangmanLetterGuessResponse(val hangman: String,
                                      val correct: Boolean,
                                      val token: String)
{
}