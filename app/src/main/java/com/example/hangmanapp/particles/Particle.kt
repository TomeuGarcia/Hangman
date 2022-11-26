package com.example.hangmanapp.particles

import com.example.hangmanapp.R

class Particle(val name: String = "",
               val family: Family = Family.NONE,
               val max: Double = 0.0,
               val charge: String = "",
               val spin: String = "")
{
    enum class Family
    {
        NONE, QUARK, LEPTON, GAUGE_BOSON, SCALAR_BOSON;

        fun color(): Int = when (this) {
            NONE -> R.color.black
            QUARK -> R.color.quarks
            LEPTON -> R.color.leptons
            GAUGE_BOSON -> R.color.gauge_bosons
            SCALAR_BOSON -> R.color.higgs
        }
    }
}