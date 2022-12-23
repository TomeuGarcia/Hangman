package com.example.hangmanapp.abductmania.Game.Drawings

import android.view.View
import android.widget.ImageView
import kotlin.random.Random

class HangmanDrawingBuilding(image : ImageView,
                            private val endMaxRotation : Float)
    : HangmanDrawingPart(image, View.VISIBLE, View.INVISIBLE)
{
    private var imageStartX : Float = 0f
    private var imageStartY : Float = 0f
    private var rotation : Float = 0f


    public override fun setEndVisibility()
    {
        imageStartX = image.x
        imageStartY = image.y
        rotation = getRandomRotationValue()

        val endY = image.y - 200f

        image.animate()
            .scaleX(0f).scaleY(1.3f)
            .y(endY)
            .rotationBy(rotation)
            .setDuration(300).withEndAction {
                image.visibility = endVisibility
            }
    }

    private fun getRandomRotationValue() : Float
    {
        return Random.nextFloat() * endMaxRotation
    }

    public override fun resetStartVisibility()
    {
        image.visibility = startVisibility

        image.animate()
            .scaleX(1f).scaleY(1f)
            .x(imageStartX).y(imageStartY)
            .rotationBy(-rotation)
            .setDuration(450)
    }
}