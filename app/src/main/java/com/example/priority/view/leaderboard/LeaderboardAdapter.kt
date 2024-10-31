package com.example.priority.view.leaderboard

import User
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.priority.R
import com.example.priority.databinding.ItemRowLeaderboardBinding

class LeaderboardAdapter(private var leaderboardList: List<User>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemRowLeaderboardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = leaderboardList[position]
        holder.bind(user, position)
    }

    override fun getItemCount(): Int = leaderboardList.size

    fun updateData(newLeaderboardList: List<User>) {
        leaderboardList = newLeaderboardList
        notifyDataSetChanged()  // Refresh the list
    }

    class LeaderboardViewHolder(private val binding: ItemRowLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, position: Int) {
            binding.tvRank.text = "${position + 1}"
            binding.tvItemName.text = user.name
            binding.tvItemSkore.text = user.score.toString()
            Glide.with(binding.root.context)
                .load(user.profileImageUrl) // Use Glide to load image from URL
                .into(binding.imgRank)
        }
    }
}