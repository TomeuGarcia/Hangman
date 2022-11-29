package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityRankingBinding
import com.example.hangmanapp.nasa.NasaImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRankingBinding
    private var adapter = RankingRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.rankingRecyclerView.adapter = adapter

        binding.leaderboardBack.setOnClickListener{
            val intent = Intent(this@RankingActivity, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
        loadRanking()
    }

    fun loadRanking()
    {
        val rankingImages = listOf<RankingImage>(RankingImage("Juan", 334434342,1 ),
            RankingImage("xXPussyDickstroyer69Xx", 69,2 ),
            RankingImage("Ju", 10,3 ),
            RankingImage("Na", 9,4 ),
            RankingImage("Nau", 8,5 ),
            RankingImage("Napalm", 7,6 ),
            RankingImage("Enemy UAV", 6,7 ),
            RankingImage("VTOL", 5,8 ),
            RankingImage("Dron Bomba", 4,9 ))

        adapter.submitList(rankingImages)
    }
}