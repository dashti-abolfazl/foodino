package com.example.foodino

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.foodino.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var b : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.apply {
            // اجرای یک کار با تاخیر 5 ثانیه روی Thread اصلی
            Handler(Looper.getMainLooper()).postDelayed({
                //ساخت یک Intent برای رفتن از اکتیویتی SplashScreen به اکتیویتی WelcomeScreen.
                var p = Intent(this@SplashScreen,WelcomeScreen::class.java)
                startActivity(p)
            },5000)

        }

    }
}