package com.example.hangmanapp.abductmania

import com.example.hangmanapp.databinding.ActivityHangmanGameBinding

class LettersMap(binding: ActivityHangmanGameBinding) : HashMap<Char, LetterButtonImage>() {

    init {
        this['a'] = LetterButtonImage(binding.aButton, binding.aOverlapImage)
        this['b'] = LetterButtonImage(binding.bButton, binding.bOverlapImage)
        this['c'] = LetterButtonImage(binding.cButton, binding.cOverlapImage)
        this['d'] = LetterButtonImage(binding.dButton, binding.dOverlapImage)
        this['e'] = LetterButtonImage(binding.eButton, binding.eOverlapImage)
        this['f'] = LetterButtonImage(binding.fButton, binding.fOverlapImage)
        this['g'] = LetterButtonImage(binding.gButton, binding.gOverlapImage)
        this['h'] = LetterButtonImage(binding.hButton, binding.hOverlapImage)
        this['i'] = LetterButtonImage(binding.iButton, binding.iOverlapImage)
        this['j'] = LetterButtonImage(binding.jButton, binding.jOverlapImage)
        this['k'] = LetterButtonImage(binding.kButton, binding.kOverlapImage)
        this['l'] = LetterButtonImage(binding.lButton, binding.lOverlapImage)
        this['m'] = LetterButtonImage(binding.mButton, binding.mOverlapImage)
        this['n'] = LetterButtonImage(binding.nButton, binding.nOverlapImage)
        this['o'] = LetterButtonImage(binding.oButton, binding.oOverlapImage)
        this['p'] = LetterButtonImage(binding.pButton, binding.pOverlapImage)
        this['q'] = LetterButtonImage(binding.qButton, binding.qOverlapImage)
        this['r'] = LetterButtonImage(binding.rButton, binding.rOverlapImage)
        this['s'] = LetterButtonImage(binding.sButton, binding.sOverlapImage)
        this['t'] = LetterButtonImage(binding.tButton, binding.tOverlapImage)
        this['u'] = LetterButtonImage(binding.uButton, binding.uOverlapImage)
        this['v'] = LetterButtonImage(binding.vButton, binding.vOverlapImage)
        this['w'] = LetterButtonImage(binding.wButton, binding.wOverlapImage)
        this['x'] = LetterButtonImage(binding.xButton, binding.xOverlapImage)
        this['y'] = LetterButtonImage(binding.yButton, binding.yOverlapImage)
        this['z'] = LetterButtonImage(binding.zButton, binding.zOverlapImage)
    }



}