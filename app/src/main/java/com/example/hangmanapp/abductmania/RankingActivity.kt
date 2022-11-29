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
        val namesScores = listOf<Pair<String, Int>>(
            Pair("Juan", 900000), Pair("xXEricAkaYuukiasXx", 696969), Pair("Ju", 1243),
            Pair("NaCl", 434), Pair("Naplm", 77), Pair("Enemy UAV", 60),
            Pair("VTOL", 59), Pair("Dron Bomba", 2)
        )

        val rankingImages = arrayListOf<RankingImage>()
        namesScores.forEachIndexed { index, pair ->
            rankingImages.add(RankingImage(pair.first, pair.second, index))
        }

        adapter.submitList(rankingImages)
    }
}