package com.example.hangmanapp.abductmania.Ranking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangmanapp.databinding.ItemRankingBinding

class RankingRecyclerViewAdapter
    : RecyclerView.Adapter<RankingRecyclerViewAdapter.RankingViewHolder>()
{
    private var rankingImagesList : List<RankingItem>? = null

    inner class RankingViewHolder(binding: ItemRankingBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        val image = binding.leaderboardProfileImage
        val username = binding.leaderboardUsernameText
        val score = binding.leaderboardScoreText
        val rank = binding.leaderboardRank

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRankingBinding.inflate(inflater, parent, false)

        return RankingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RankingViewHolder, position: Int)
    {
        val image = rankingImagesList?.get(position)

        holder.username.text = image?.username ?: ""
        holder.score.text = image?.score.toString() ?: ""
        holder.rank.text = image?.rank.toString() ?: ""
    }

    override fun getItemCount(): Int
    {
        return rankingImagesList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList : List<RankingItem>)
    {
        rankingImagesList = newList
        notifyDataSetChanged()
    }

}