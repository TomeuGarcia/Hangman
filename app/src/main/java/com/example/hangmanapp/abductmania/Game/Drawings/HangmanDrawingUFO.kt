package com.example.hangmanapp.abductmania.Game.Drawings

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.example.hangmanapp.abductmania.Game.HangmanGameViewModel

class HangmanDrawingUFO(image : ImageView, duration : Long, yDisplacement : Float)
    : HangmanFloatingDrawing(image, duration, yDisplacement)
{
    public override fun setEndVisibility()
    {
        hasBeenDrawn = true

        image.visibility = endVisibility
        image.scaleX = 0f
        image.scaleY = 0f
        image.animate().scaleX(1f).scaleY(1f).setDuration(300)
            .withEndAction{
            startFloatingUp()
        }
        HangmanGameViewModel.appearSfxMP?.start()

    }





}