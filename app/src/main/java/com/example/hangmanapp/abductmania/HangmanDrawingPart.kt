package com.example.hangmanapp.abductmania

import android.widget.ImageView

class HangmanDrawingPart(private val image : ImageView,
                         private val startVisibility : Int,
                         private val endVisibility : Int)
{
    public fun setStartVisibility()
    {
        image.visibility = startVisibility
    }

    public fun setEndVisibility()
    {
        image.visibility = endVisibility
    }
}