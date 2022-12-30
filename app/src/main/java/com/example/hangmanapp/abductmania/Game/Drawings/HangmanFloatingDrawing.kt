package com.example.hangmanapp.abductmania.Game.Drawings

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import androidx.core.animation.doOnEnd

abstract class HangmanFloatingDrawing(image : ImageView,
                                      private val yDispDuration : Long,
                                      private val yDisplacement : Float)
    : HangmanDrawingPart(image, View.INVISIBLE, View.VISIBLE)
{
    protected fun startFloatingUp()
    {
        val startY = image.y
        val endY = startY - yDisplacement

        ObjectAnimator.ofFloat(
            image, "y",
            startY,
            endY
        ).apply {
            duration = yDispDuration
            start()
        }.doOnEnd {
            startFloatingDown()
        }
    }

    protected fun startFloatingDown()
    {
        val startY = image.y
        val endY = startY + yDisplacement

        ObjectAnimator.ofFloat(
            image, "y",
            startY,
            endY
        ).apply {
            duration = yDispDuration
            start()
        }.doOnEnd {
            startFloatingUp()
        }
    }
}