package com.example.foodino

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodino.RetrofitClient
import com.example.foodino.databinding.ActivitySignupScreenBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupScreen : AppCompatActivity() {
    lateinit var binding: ActivitySignupScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            // رفتن به صفحه‌ی ورود با زدن روی تب Login
            tabLogin.setOnClickListener {
                startActivity(Intent(this@SignupScreen, LoginScreen::class.java))
                finish()
            }

            btnSignup.setOnClickListener {
                val username = userField.text.toString()
                val password = passwordField.text.toString()
                val role = if (rbAdmin.isChecked) "admin" else "customer"

                RetrofitClient.instance.registerUsrer(username, password, role).enqueue(object :
                    Callback<ServerResponse> {
                    override fun onResponse(
                        call: Call<ServerResponse>,
                        response: Response<ServerResponse>
                    ) {
                        if (response.body()?.success == true) {
                            Toast.makeText(this@SignupScreen, "ثبت نام موفق", Toast.LENGTH_SHORT).show()
                            finish() // برمی‌گرده به صفحه لاگین
                        } else {
                            Toast.makeText(
                                this@SignupScreen,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        Toast.makeText(this@SignupScreen, t.message, Toast.LENGTH_LONG).show()
                        t.printStackTrace()
                        Log.e("SIGNUP_ERROR", t.message.toString())
                    }
                })
            }

        }
    }
}