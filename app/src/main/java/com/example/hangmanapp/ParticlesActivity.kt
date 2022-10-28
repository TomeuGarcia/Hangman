package com.example.hangmanapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityParticlesBinding

class ParticlesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityParticlesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val particles = listOf<ParticleData>(
            ParticleData("Quark Up", Color.BLUE),
            ParticleData("Quark Charm", Color.BLUE),
            ParticleData("Quark Top", Color.BLUE),
            ParticleData("Quark Down", Color.BLUE),
            ParticleData("Quark Strange", Color.BLUE),
            ParticleData("Quark Bottom", Color.BLUE),
            ParticleData("Electron", Color.GREEN),
            ParticleData("Muon", Color.GREEN),
            ParticleData("Tau", Color.GREEN),
            ParticleData("Electron neutrino", Color.GREEN),
            ParticleData("Tau neutrino", Color.GREEN),
            ParticleData("Gluon", Color.MAGENTA),
            ParticleData("Photon", Color.MAGENTA),
            ParticleData("Z boson", Color.MAGENTA),
            ParticleData("W boson", Color.MAGENTA),
            ParticleData("Higgs", Color.RED)
        )
        val particles2 = listOf(
            "Quark Up",
            "Quark Charm",
            "Quark Top",
            "Quark Down",
            "Quark Strange",
            "Quark Bottom",
            "Electron",
            "Muon",
            "Tau",
            "Electron neutrino",
            "Muon neutrino",
            "Tau neutrino",
            "Gluon",
            "Photon",
            "Z boson",
            "W boson",
            "Higgs"
        )
        binding.particlesRecycleView.adapter = ParticlesAdapter(particles);
    }


    inner class ParticleData(val name : String, val color : Int)
    {
    }

}