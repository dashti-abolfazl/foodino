package com.example.foodino

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("cart_id") val cartId: Int,
    @SerializedName("product_id") val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val subtotal: Double,
    @SerializedName("image_url") val imageUrl: String?
)

data class CartResponse(
    val success: Boolean,
    val items: List<CartItem>,
    val total: Double,
    val message: String?
)

data class OrderResponse(
    val success: Boolean,
    val message: String?,
    @SerializedName("order_id") val orderId: Int = -1,
    val total: Double = 0.0
)
