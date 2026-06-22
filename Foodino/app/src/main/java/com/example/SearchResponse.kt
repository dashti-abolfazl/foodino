package com.example.foodino


data class SearchResponse(
    val success: Boolean,
    val data: List<Product>,
    val message: String?
)