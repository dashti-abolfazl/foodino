package com.example.foodino

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodino.databinding.ActivityWelcomeScreenBinding

class WelcomeScreen : AppCompatActivity() {
    lateinit var b : ActivityWelcomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.apply {
            btnContinue.setOnClickListener {
                //ساخت یک Intent برای رفتن از اکتیویتی WelcomeScreen به اکتیویتی LS.
                var p = Intent(this@WelcomeScreen,LS::class.java)
                startActivity(p)
            }
        }

    }
}