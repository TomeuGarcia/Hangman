package com.example.hangmanapp.abductmania.Ranking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.abductmania.MainMenu.MainMenuViewModel
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
            finish()
            MainMenuViewModel.buttonSfxMP?.start()
        }

        rankingViewModel.rankingUsersData.observe(this) {
            updateRanking(it)
        }
        rankingViewModel.isRankingDataReady.observe(this) {
            if (it) updateRanking(rankingViewModel.getArrayRankingData())
        }

        rankingViewModel.loadRanking(this)
    }

    private fun updateRanking(rankingUsersData : List<RankingViewModel.RankingUserData>)
    {
        val rankingImages = arrayListOf<RankingItem>()

        rankingUsersData.forEachIndexed { index, e ->
            rankingImages.add(RankingItem(e.username ?: "", e.score ?: 0, index+1))
        }

        adapter.submitList(rankingImages)
    }

    override fun onPause()
    {
        super.onPause()

        MainMenuViewModel.menuMusicMP?.pause()
    }

    override fun onResume() {
        super.onResume()

        MainMenuViewModel.menuMusicMP?.start()
    }
}