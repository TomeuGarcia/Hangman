package com.example.hangmanapp.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import com.example.hangmanapp.databinding.ActivityNasaBinding
import com.squareup.picasso.Picasso
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class NasaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNasaBinding
    private var adapter = NasaRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityNasaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nasaRecyclesView.adapter = adapter

        binding.nasaSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query ?: return false)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })


    }

    private fun performSearch(query: String)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://images-api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiNasa::class.java)

        call.performSearch(query).enqueue(object : Callback<NasaImagesList> {

            override fun onResponse(call: Call<NasaImagesList>,response: Response<NasaImagesList>) {
                val images = response.body() ?: return

                val nasaImages = images.collection.items.map{
                    val data = it.data?.getOrNull(0) ?: return
                    val link = it.links?.getOrNull(0)?.href ?: return

                    NasaImage(data.title ?: "", data.description ?: "", link)
                }

                adapter.submitList(nasaImages)
            }

            override fun onFailure(call: Call<NasaImagesList>, t: Throwable) {
                Toast.makeText(this@NasaActivity, "Something went wrong", Toast.LENGTH_LONG)
            }

        })
    }


}