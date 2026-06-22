package com.example.foodino

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodino.databinding.ActivityLsBinding

class LS : AppCompatActivity() {
    lateinit var b : ActivityLsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b= ActivityLsBinding.inflate(layoutInflater)
        setContentView(b.root)

        val txt_guide : TextView = findViewById(R.id.txt_guide)
        b.apply {
            btnLogin.setOnClickListener {
                //ساخت یک Intent برای رفتن از اکتیویتی LS به اکتیویتی LoginScreen.
                var p = Intent(this@LS,LoginScreen::class.java)
                startActivity(p)
            }
            btnSignup.setOnClickListener {
                //ساخت یک Intent برای رفتن از اکتیویتی LS به اکتیویتی SignupScreen.
                var p = Intent(this@LS,SignupScreen::class.java)
                startActivity(p)
            }
            txtGuide.setOnClickListener {
                //ساخت AlertDialog
                val builder = AlertDialog.Builder(this@LS)
                //عنوان
                builder.setTitle("Application Guide")
                //متن مورد نظر
                builder.setMessage("Hello,  \n" +
                        "Welcome to Foodino!  \n" +
                        "If this is your first time using the app, please sign up first and then try loging in.  \n" +
                        "Otherwise, you can log in with your username and password.")
                //دکمه بستن
                    .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, i ->
                        dialog.dismiss()
                    })
                //نمایش
                val dialog = builder.create()
                dialog.show()
            }

        }
    }
}