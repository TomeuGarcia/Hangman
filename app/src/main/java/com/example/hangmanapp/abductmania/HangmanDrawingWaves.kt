package com.example.hangmanapp.abductmania

import android.view.View
import android.widget.ImageView

class HangmanDrawingWaves(image : ImageView)
    : HangmanDrawingPart(image, View.INVISIBLE, View.VISIBLE)
{
    public override fun setEndVisibility()
    {
        image.pivotY = 0f
        image.visibility = endVisibility
        image.scaleY = 0f
        image.animate().scaleY(1f).setDuration(300)
    }
}