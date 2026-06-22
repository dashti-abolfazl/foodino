package com.example.foodino

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,

    val name: String,

    val price: String,

    @SerializedName("image_url")
    val imageUrl: String?
)