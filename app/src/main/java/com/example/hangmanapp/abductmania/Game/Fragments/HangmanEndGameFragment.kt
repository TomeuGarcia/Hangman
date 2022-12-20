package com.example.hangmanapp.abductmania.Game.Fragments

import androidx.fragment.app.Fragment

abstract class HangmanEndGameFragment(protected var hangmanWord : String,
                                      protected var score : Int)
    : Fragment()
{
    protected abstract fun initViewComponents()
}