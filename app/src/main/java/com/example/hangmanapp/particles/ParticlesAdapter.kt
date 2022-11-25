package com.example.hangmanapp.particles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangmanapp.databinding.ItemParticleBinding

class ParticlesAdapter(private val context: Context)
    : RecyclerView.Adapter<ParticlesAdapter.ParticlesViewHolder>()
{
    private var particles: List<Particle> = listOf()


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
        holder.image.setColorFilter(context.getColor(particle.family.color()))
    }

    override fun getItemCount(): Int {
        return particles.size
    }

    public fun updateParticles(particles : List<Particle>)
    {
        this.particles = particles
        notifyDataSetChanged()
    }

}