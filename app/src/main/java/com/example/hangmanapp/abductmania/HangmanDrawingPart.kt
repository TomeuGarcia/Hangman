package com.example.hangmanapp.abductmania

import android.view.View
import android.widget.ImageView
import kotlin.random.Random


abstract class HangmanDrawingPart(protected val image : ImageView,
                                  protected val startVisibility : Int,
                                  protected val endVisibility : Int)
{
    public fun setStartVisibility()
    {
        image.visibility = startVisibility
    }

    public open fun setEndVisibility()
    {
        image.visibility = endVisibility
    }


}