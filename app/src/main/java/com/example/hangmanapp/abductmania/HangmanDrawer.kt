package com.example.hangmanapp.abductmania

import android.widget.ImageView

class HangmanDrawer(private val hangmanDrawParts : List<HangmanDrawingPart>)
{
    init {
        hangmanDrawParts.forEach {
            it.setStartVisibility()
        }
    }

    public fun drawPart(index : Int)
    {
        hangmanDrawParts[index].setEndVisibility()
    }
}