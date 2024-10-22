package com.example.priority.view.leaderboard

import com.google.android.material.color.utilities.Score
import com.google.gson.annotations.SerializedName

data class LedearboardItem(
    @SerializedName("name") val name:String,
    @SerializedName("rank") val rank: String,
    @SerializedName("score") val score: Int
)
