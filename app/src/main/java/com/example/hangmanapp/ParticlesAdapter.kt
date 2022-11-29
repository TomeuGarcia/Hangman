package com.example.hangmanapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangmanapp.databinding.ItemParticleBinding
import com.example.hangmanapp.particles.Particle

class ParticlesAdapter(private val particles: List<Particle>) :
    RecyclerView.Adapter<ParticlesAdapter.ParticlesViewHolder>()
{

    inner class ParticlesViewHolder(binding: ItemParticleBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        val name = binding.particleName
        val image = binding.particleImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticlesViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParticleBinding.inflate(layoutInflater)

        return ParticlesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticlesViewHolder, position: Int) {
        val particle = particles[position]

        holder.name.text = particle.name
        holder.image.setColorFilter(particle.family.color())
    }

    override fun getItemCount(): Int {
        return particles.size
    }


    public fun updateParticles(particles: List<Particle>)
    {

    }

}