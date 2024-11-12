package com.example.priority.data.response

data class User(
    val id: String,
    val name: String,
    val points: Double? = 0.000,
    val profileImageUrl: String? = null
)