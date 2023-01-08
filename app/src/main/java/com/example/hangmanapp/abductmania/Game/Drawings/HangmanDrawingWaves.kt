package com.example.hangmanapp.abductmania.Game.Drawings

import android.view.View
import android.widget.ImageView
import com.example.hangmanapp.abductmania.Game.HangmanGameViewModel

class HangmanDrawingWaves(image : ImageView, duration : Long, yDisplacement : Float)
    : HangmanFloatingDrawing(image, duration, yDisplacement)
{
    public override fun setEndVisibility()
    {
        hasBeenDrawn = true

        image.pivotY = 0f
        image.visibility = endVisibility
        image.scaleY = 0f
        image.animate().scaleY(1f).setDuration(300)
            .withEndAction {
                startFloatingUp()
            }
        HangmanGameViewModel.abductorSfxMP?.seekTo(0)
        HangmanGameViewModel.abductorSfxMP?.start()
    }
}