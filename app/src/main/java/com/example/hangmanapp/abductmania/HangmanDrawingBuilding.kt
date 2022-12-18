package com.example.hangmanapp.abductmania

import android.view.View
import android.widget.ImageView
import kotlin.random.Random

class HangmanDrawingBuilding(image : ImageView,
                            private val endMaxRotation : Float)
    : HangmanDrawingPart(image, View.VISIBLE, View.INVISIBLE)
{
    public override fun setEndVisibility()
    {
        image.animate().scaleX(0f).scaleY(1.3f)
            .rotationBy(getRandomRotationValue())
            .setDuration(300).withEndAction {
                image.visibility = endVisibility
            }
    }

    private fun getRandomRotationValue() : Float
    {
        return Random.nextFloat() * endMaxRotation
    }
}