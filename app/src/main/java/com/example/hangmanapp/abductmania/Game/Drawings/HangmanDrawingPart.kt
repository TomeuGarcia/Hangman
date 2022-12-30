package com.example.hangmanapp.abductmania.Game.Drawings

import android.graphics.Matrix
import android.view.View
import android.widget.ImageView
import kotlin.random.Random


abstract class HangmanDrawingPart(protected val image : ImageView,
                                  protected val startVisibility : Int,
                                  protected val endVisibility : Int)
{
    protected var hasBeenDrawn = false


    public fun setStartVisibility()
    {
        image.visibility = startVisibility
    }

    public open fun setEndVisibility()
    {
        hasBeenDrawn = true
        image.visibility = endVisibility
    }

    public open fun resetStartVisibility()
    {
        image.visibility = startVisibility
    }

    public fun hasBeenDrawn() : Boolean
    {
        return hasBeenDrawn
    }

}