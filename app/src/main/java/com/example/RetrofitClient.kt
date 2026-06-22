package com.example.foodino

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2/Foodino/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) //درس اصلی (base URL) سرور API که درخواست‌ها بهش فرستاده می‌شن
            .addConverterFactory(GsonConverterFactory.create()) //تعیین می‌کنه که داده‌هایی که از سرور می‌گیریم یا براش می‌فرستیم به/از JSON تبدیل بشن
            //GsonConverterFactory از کتابخونه Gson استفاده می‌کنه تا این کار رو انجامش بده.
            .build() //تنظیمات بالا رو اعمال می‌کنه و در نهایت یک شیء Retrofit واقعی می‌سازه

        retrofit.create(ApiService::class.java)
        //یعنی متدهایی که توی ApiService تعریف کردیم (مثلاً @POST, @GET, ...) تبدیل به توابع قابل استفاده می‌شن
    }
}
