package com.example.hangmanapp.abductmania.Game.Drawings

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

    public fun undrawParts(startIndex : Int)
    {
        for(i in startIndex..hangmanDrawParts.size-1)
        {
            hangmanDrawParts[i].resetStartVisibility()
        }
    }
}