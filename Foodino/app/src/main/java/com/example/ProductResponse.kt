package com.example.foodino

import com.example.foodino.Product

data class ProductResponse(
    val success: Boolean, //مقدار بازگشتی سرور را به صورت ترو فالس تو این متغیر میریزه که بعدا توی صفحه وقتی فراخوانی شد وضعیت مشخص بشه
    val products: List<Product> //محصولات را از پایگاه داده واکشی میکنه  و توی این متغیر ریخته میشود
    //Product: کلاس کاتلینی که برای ذخیره سازی محصول ساختیم
)