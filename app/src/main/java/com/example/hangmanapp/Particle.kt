package com.example.hangmanapp

class Particle(
    val name: String="",
    val family: Family = Family.LEPTON,
    val mass: Double = 0.0,
    val charge: String="",
    val spin: String="") {

    enum class Family {
        QUARK, LEPTON, GAUGE_BOSON,SCALAR_BOSON;

        fun color(): Int = when (this) {
            QUARK -> R.color.quarks
            LEPTON -> R.color.leptons
            GAUGE_BOSON -> R.color.gauge_bosons
            SCALAR_BOSON -> R.color.higgs
        }
    }
}