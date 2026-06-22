package com.example.foodino
//در تابع ازین مدل برای پاسخگویی استافده شده
interface DeleteCallback {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}

