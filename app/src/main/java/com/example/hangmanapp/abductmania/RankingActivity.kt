package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hangmanapp.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRankingBinding
    private var adapter = RankingRecyclerViewAdapter()

    private val rankingViewModel : RankingViewModel by viewModels()



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

        rankingViewModel.rankingUsersData.observe(this) {
            updateRanking(it)
        }

        rankingViewModel.loadRanking()
    }

    private fun updateRanking(rankingUsersData : List<RankingViewModel.RankingUserData>)
    {
        val rankingImages = arrayListOf<RankingItem>()

        rankingUsersData.forEachIndexed { index, e ->
            rankingImages.add(RankingItem(e.username, e.score, index+1))
        }

        adapter.submitList(rankingImages)
    }
}