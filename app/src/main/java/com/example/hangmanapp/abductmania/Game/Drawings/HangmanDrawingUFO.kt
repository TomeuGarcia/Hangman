package com.example.hangmanapp.abductmania.Game.Drawings

import android.view.View
import android.widget.ImageView

class HangmanDrawingUFO(image : ImageView)
    : HangmanDrawingPart(image, View.INVISIBLE, View.VISIBLE)
{
    public override fun setEndVisibility()
    {
        image.visibility = endVisibility
        image.scaleX = 0f
        image.scaleY = 0f
        image.animate().scaleX(1f).scaleY(1f).setDuration(300)
    }
}