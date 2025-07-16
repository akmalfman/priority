package com.example.priority.data.response

data class User(
    val userId: String,
    val name: String,
    val totalPoints: Double? = 0.000,
    val profileImageUrl: String? = null
)