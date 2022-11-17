package com.example.hangmanapp.abductmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangmanapp.databinding.ActivityRankingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRankingBinding
    private var adapter = RankingRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rankingRecyclerView.adapter = adapter

        binding.leaderboardBack.setOnClickListener{
            val intent = Intent(this@RankingActivity, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}