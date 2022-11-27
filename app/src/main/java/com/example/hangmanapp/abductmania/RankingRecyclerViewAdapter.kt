package com.example.hangmanapp.abductmania

import android.annotation.SuppressLint
import android.service.notification.NotificationListenerService.Ranking
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangmanapp.databinding.ItemLeaderboardBinding
import com.squareup.picasso.Picasso

class RankingRecyclerViewAdapter
    : RecyclerView.Adapter<RankingRecyclerViewAdapter.RankingViewHolder>()
{
    private var rankingImagesList : List<RankingImage>? = null

    inner class RankingViewHolder(binding: ItemLeaderboardBinding)
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
        val binding = ItemLeaderboardBinding.inflate(inflater, parent, false)

        return RankingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RankingViewHolder, position: Int)
    {
        val image = rankingImagesList?.get(position)

        holder.username.text = image?.username ?: ""
        holder.score.text = image?.score ?: ""
        holder.rank.text = image?.rank ?: ""

        Picasso.get()
            .load(image?.link)
            .into(holder.image)
    }

    override fun getItemCount(): Int
    {
        return rankingImagesList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList : List<RankingImage>)
    {
        rankingImagesList = newList
        notifyDataSetChanged()
    }

}