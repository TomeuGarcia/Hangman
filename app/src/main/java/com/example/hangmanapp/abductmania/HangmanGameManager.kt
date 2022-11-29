package com.example.hangmanapp.abductmania

import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max


class HangmanGameManager()
{
    private var hangmanWord : String = ""
    private var gameToken : String = ""
    private var hint : Char = ' '
    private var isGameOver : Boolean = false

    private val CORRECT_LETTER_POINTS : Int = 50
    private val WRONG_LETTER_POINTS : Int = 30
    private val ALL_LETTERS_GUESSED_POINTS : Int = 200
    private var score : Int = 0

    private var wrongGuessesCount : Int = 0
    private val MAX_WRONG_GUESSES : Int = 8

    private val MISSING_LETTER : Char = '_'



}