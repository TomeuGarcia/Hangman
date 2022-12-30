package com.example.hangmanapp.abductmania.Game.Drawings

class HangmanDrawer(private val hangmanDrawParts : List<HangmanDrawingPart>)
{
    private var lastDrawnIndex = -1

    init {
        hangmanDrawParts.forEach {
            it.setStartVisibility()
        }
    }

    public fun drawPart(index : Int)
    {
        lastDrawnIndex = index
        hangmanDrawParts[index].setEndVisibility()
    }

    public fun undrawParts(startIndex : Int)
    {
        for(i in startIndex..hangmanDrawParts.size-1)
        {
            if (hangmanDrawParts[i].hasBeenDrawn())
            {
                hangmanDrawParts[i].resetStartVisibility()
            }
        }

        lastDrawnIndex = startIndex - 1
    }

    public fun drawRemainingParts()
    {
        for(i in lastDrawnIndex+1..hangmanDrawParts.size-1)
        {
            drawPart(i)
        }
    }

}