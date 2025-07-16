package com.example.priority.data.response

data class Upload(
    val uploadId: String = "", // Nilai default untuk uploadId
    val userId: String = "",   // Nilai default untuk userId
    val distance: Double = 0.0,
    val points: Double = 0.0,
    val imageUrl: String = "",
    val date: String = "",
    val clock: String = ""
)
