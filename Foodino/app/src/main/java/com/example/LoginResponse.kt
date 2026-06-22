package com.example.foodino

data class LoginResponse (
    val success: Boolean,
    val role: String?,
    val messsage: String?
)