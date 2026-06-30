package com.example.foodino

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val role: String?,
    val message: String?,
    @SerializedName("user_id") val userId: Int = -1,
    val username: String?,
    @SerializedName("full_name") val fullName: String?,
    val phone: String?,
    val address: String?
)
