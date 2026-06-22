package com.example.foodino


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodino.databinding.ActivityLoginScreenBinding
import com.example.foodino.SignupScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginScreen : AppCompatActivity() {
    lateinit var binding: ActivityLoginScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {

            btnLogin.setOnClickListener {
                val username = userField.text.toString().trim()
                val password = passwordField.text.toString().trim()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginScreen, "نام کاربری و رمز را وارد کنید", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                RetrofitClient.instance.loginUser(username, password).enqueue(object :
                    Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val loginResponse = response.body()
                        if (loginResponse?.success == true) {

                            // بررسی نقش
                            when (loginResponse.role) {
                                "admin" -> startActivity(Intent(this@LoginScreen, AdminProductManagement::class.java))
                                "customer" -> startActivity(Intent(this@LoginScreen, ShopScreen::class.java))


                                else -> Toast.makeText(
                                    this@LoginScreen,
                                    "نقش نامعتبر",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginScreen,
                                "خطا: ${loginResponse?.messsage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginScreen, "خطا در اتصال", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            tabSignup.setOnClickListener {
                startActivity(Intent(this@LoginScreen, SignupScreen::class.java))
            }
        }
    }
}