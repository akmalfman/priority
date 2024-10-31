package com.example.priority.view.leaderboard

import com.google.gson.annotations.SerializedName

data class LeaderboardItem(
    @SerializedName ("name") val name: String,
    @SerializedName ("rank") val rank: Int,
    @SerializedName ("score") val score: Double,
    @SerializedName ("imageResId") val imageResId: Int
)
