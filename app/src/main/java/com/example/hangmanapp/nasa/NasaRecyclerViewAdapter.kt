package com.example.hangmanapp.nasa

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangmanapp.databinding.ItemNasaImageBinding
import com.squareup.picasso.Picasso

class NasaRecyclerViewAdapter
    : RecyclerView.Adapter<NasaRecyclerViewAdapter.NasaViewHolder>()
{
    private var imagesList: List<NasaImage>? = null


    inner class NasaViewHolder(binding: ItemNasaImageBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        val image = binding.nasaImage
        val title = binding.nasaTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNasaImageBinding.inflate(inflater, parent, false)

        return NasaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NasaViewHolder, position: Int)
    {
        val image = imagesList?.get(position)

        holder.title.text = image?.title ?: ""
        Picasso.get()
            .load(image?.link)
            .into(holder.image)
    }

    override fun getItemCount(): Int
    {
        return imagesList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<NasaImage>)
    {
        imagesList = newList
        notifyDataSetChanged()
    }

}