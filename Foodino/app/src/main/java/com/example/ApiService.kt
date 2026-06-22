package com.example.foodino


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("insert_product.php")
    fun insertProduct(
        @Part("code") code : RequestBody,
        @Part("name") name : RequestBody,
        @Part("price") price : RequestBody,
        @Part image : MultipartBody.Part?
    ):Call<ServerResponse>

    @Multipart
    @POST("update_product.php")
    fun updateProduct(
        @Part("id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ):Call<ServerResponse>

    @GET("get_products.php")
    fun getProducts():Call<ProductResponse>

    @FormUrlEncoded
    @POST("delete_product.php")
    fun deleteProduct(
        @Field("id") id:String
    ):Call<ServerResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun registerUsrer(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") rol: String
    ):Call<ServerResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("search_product.php")
    fun searchProduct(@Query("query") query: String): Call<SearchResponse>


}