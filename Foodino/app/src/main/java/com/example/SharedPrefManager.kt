package com.example.foodino

import android.content.Context

class SharedPrefManager(context: Context){

    private val sharedPreferences =context.getSharedPreferences("MyAppPrefs",Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveLoginInfo(username: String,role: String){
        editor.putString("USERNAME",username)
        editor.putString("ROLE",role)
        editor.apply()
    }

    fun getUsername():String?{
        return sharedPreferences.getString("USERNAME",null)
    }

    fun getRole():String?{
        return sharedPreferences.getString("ROLE",null)
    }

    fun clear(){
        editor.clear()
        editor.apply()
    }

    fun logout(){
        clear()
    }
}