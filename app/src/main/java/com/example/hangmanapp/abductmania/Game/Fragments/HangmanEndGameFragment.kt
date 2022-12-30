package com.example.hangmanapp.abductmania.Game.Fragments

import androidx.fragment.app.Fragment

abstract class HangmanEndGameFragment()
    : Fragment()
{
    protected var hangmanWord : String? = null
    protected var score : Int? = null

    public fun setWordAndScore(hangmanWord : String, score : Int)
    {
        this.hangmanWord = hangmanWord
        this.score = score
    }

    protected abstract fun initViewComponents()
}