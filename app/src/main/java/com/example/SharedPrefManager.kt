package com.example.foodino

import android.content.Context

class SharedPrefManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveLoginInfo(userId: Int, username: String, role: String) {
        editor.putInt("USER_ID", userId)
        editor.putString("USERNAME", username)
        editor.putString("ROLE", role)
        editor.apply()
    }

    fun getUserId(): Int = sharedPreferences.getInt("USER_ID", -1)

    fun getUsername(): String? = sharedPreferences.getString("USERNAME", null)

    fun getRole(): String? = sharedPreferences.getString("ROLE", null)

    fun isLoggedIn(): Boolean = getUserId() > 0

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun logout() = clear()
}
