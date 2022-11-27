package com.example.hangmanapp.particles

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityParticlesBinding
import com.example.hangmanapp.particles.Particle.Family.*
//simport com.google.firebase.firestore.FirebaseFirestore

class ParticlesActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityParticlesBinding

    //private lateinit var firestore: FirebaseFirestore

    private val PARTICLES_COLLECTION = "particles"

    private var particles = arrayListOf<Particle>().apply {
        add(Particle("Quark Up", QUARK, 2200.0, "2/3", "1/2"))
        add(Particle("Quark Charm", QUARK, 1280.0, "2/3", "1/2"))
        add(Particle("Quark Top", QUARK, 173100.0, "2/3", "1/2"))

        add(Particle("Quark Down", QUARK, 4.7, "-1/3", "1/2"))
        add(Particle("Quark Strange", QUARK, 96.0, "-1/3", "1/2"))
        add(Particle("Quark Bottom", QUARK, 4.18, "-1/3", "1/2"))

        add(Particle("Electron", LEPTON, 0.511, "-1", "1/2"))
        add(Particle("Muon", LEPTON, 105.66, "-1", "1/2"))
        add(Particle("Tau", LEPTON, 1776.8, "-1", "1/2"))

        add(Particle("Electron neutrino", LEPTON, 1.0, "0", "1/2"))
        add(Particle("Muon neutrino", LEPTON, 0.17, "0", "1/2"))
        add(Particle("Tau neutrino", LEPTON, 18.2, "0", "1/2"))

        add(Particle("Gluon", GAUGE_BOSON, 0.0, "0", "1"))
        add(Particle("Photon", GAUGE_BOSON, 0.0, "0", "1"))
        add(Particle("Z boson", GAUGE_BOSON, 91190.0, "0", "1"))
        add(Particle("W boson", GAUGE_BOSON, 80930.0, "±1", "1"))

        add(Particle("Higgs", SCALAR_BOSON, 124970.0, "0", "0"))
    }


    //private val adapter =  ParticlesAdapter(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /*
        firestore = FirebaseFirestore.getInstance()

        //binding.particlesRecycleView.adapter = adapter;


        val particlesCollection = firestore.collection(PARTICLES_COLLECTION)
        */

        /*
        particlesCollection.get()
            .addOnSuccessListener {
                particles = it?.documents?.mapNotNull { dbParticle ->
                    dbParticle.toObject(Particle::class.java)
                } as ArrayList<Particle>

                adapter.updateParticles(particles)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong loading particles", Toast.LENGTH_LONG).show()
            }
         */

    }

    override fun onPause() {
        super.onPause()

        //val particlesCollection = firestore.collection(PARTICLES_COLLECTION)
        /*
        particles.forEach{
            particlesCollection.document(it.name).set(it)
        }
        */
    }

}