package com.example.foodino

/**
 * منطق ساده‌ی اعتبارسنجی ورود — یک تابع خالص (pure) که در یونیت‌تست بررسی می‌شود.
 */
object LoginValidator {

    /** اعتبار اولیه‌ی نام کاربری و رمز: هیچ‌کدام نباید خالی باشند. */
    fun isValid(username: String, password: String): Boolean =
        username.isNotBlank() && password.isNotBlank()
}
