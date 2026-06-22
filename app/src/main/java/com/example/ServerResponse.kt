package com.example.foodino

data class ServerResponse(
    val status: String,
    val message: String,
    val success: Boolean,
    val image_url: String? // چون ممکنه null باشه
)

