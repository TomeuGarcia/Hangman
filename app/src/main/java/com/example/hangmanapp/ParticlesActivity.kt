package com.example.hangmanapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityParticlesBinding

class ParticlesActivity : AppCompatActivity() {

    enum class ParticleFamilies{
        FAMILY_1, FAMILY_2, FAMILY_3, FAMILY_4
    }

    companion object {
        val families = listOf(ParticleFamilies.FAMILY_1,
                                ParticleFamilies.FAMILY_2,
                                ParticleFamilies.FAMILY_3,
                                ParticleFamilies.FAMILY_4)
        val familyToColor = mapOf<ParticleFamilies, Int>(families[0] to Color.MAGENTA,
            families[1] to Color.GREEN, families[2] to Color.YELLOW,
            families[3] to Color.RED)

    }

    private lateinit var binding : ActivityParticlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val particles = listOf<ParticleData>(
            ParticleData("Quark Up", families[0]),
            ParticleData("Quark Charm", families[0]),
            ParticleData("Quark Top", families[0]),
            ParticleData("Quark Down", families[0]),
            ParticleData("Quark Strange", families[0]),
            ParticleData("Quark Bottom", families[0]),
            ParticleData("Electron", families[1]),
            ParticleData("Muon", families[1]),
            ParticleData("Tau", families[1]),
            ParticleData("Electron neutrino", families[1]),
            ParticleData("Tau neutrino", families[1]),
            ParticleData("Gluon", families[2]),
            ParticleData("Photon", families[2]),
            ParticleData("Z boson", families[2]),
            ParticleData("W boson", families[2]),
            ParticleData("Higgs", families[3])
        )

        binding.particlesRecycleView.adapter = ParticlesAdapter(particles);
    }


    inner class ParticleData(val name : String, val family : ParticleFamilies)
    {

    }

}