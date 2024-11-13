package com.example.priority.view.leaderboard

import User
import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.priority.databinding.ItemRowLeaderboardBinding
import java.text.DecimalFormat

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
            binding.tvRank.text = "${position + 1}" // Rank starts from 1
            binding.tvItemName.text = user.name  // Display user's fullname (mapped as 'name' in User)
            val decimalFormat = DecimalFormat("#.###")
            val formattedDistance = decimalFormat.format(user.points)
            binding.tvItemSkore.text = formattedDistance.toString()  // Display user's points

            // Load image using Glide
            Glide.with(binding.root.context)
                .load(user.profileImageUrl)
                .placeholder(R.drawable.ic_menu_report_image) // Optional placeholder
                .into(binding.imgRank)
        }
    }
}
