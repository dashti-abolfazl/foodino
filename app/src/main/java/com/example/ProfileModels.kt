package com.example.foodino

import com.google.gson.annotations.SerializedName

data class UserInfo(
    val id: Int,
    val username: String?,
    val role: String?,
    @SerializedName("full_name") val fullName: String?,
    val phone: String?,
    val address: String?,
    @SerializedName("created_at") val createdAt: String?
)

data class ProfileResponse(
    val success: Boolean,
    val user: UserInfo?,
    val message: String?
)
