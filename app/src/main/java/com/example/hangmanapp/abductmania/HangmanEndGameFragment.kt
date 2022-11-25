package com.example.hangmanapp.abductmania

import androidx.fragment.app.Fragment

abstract class HangmanEndGameFragment : Fragment()
{
    public abstract fun init(hangmanWord : String, score : Int)
}