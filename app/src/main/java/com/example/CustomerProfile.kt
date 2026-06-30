package com.example.foodino

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodino.databinding.ActivityCustomerProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerProfile : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerProfileBinding
    private lateinit var prefs: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFoodinoDrawer(binding.drawerLayout, binding.toolbar, binding.navView)

        prefs = SharedPrefManager(this)

        val userId = prefs.getUserId()
        if (userId <= 0) {
            Toast.makeText(this, "ابتدا وارد حساب شوید", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvUsername.text = prefs.getUsername() ?: "کاربر"
        binding.tvRole.text = "نقش: ${prefs.getRole() ?: "مهمان"}"

        loadProfile(userId)

        binding.btnSaveProfile.setOnClickListener { saveProfile(userId) }
        binding.btnGoCart.setOnClickListener {
            startActivity(Intent(this, OrderScreen::class.java))
        }
    }

    private fun loadProfile(userId: Int) {
        RetrofitClient.instance.getProfile(userId)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    val user = response.body()?.user
                    if (response.body()?.success == true && user != null) {
                        binding.tvUsername.text = user.username ?: "کاربر"
                        binding.tvRole.text = "نقش: ${user.role ?: "مهمان"}"
                        binding.etFullName.setText(user.fullName ?: "")
                        binding.etPhone.setText(user.phone ?: "")
                        binding.etAddress.setText(user.address ?: "")
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@CustomerProfile, "خطا در دریافت پروفایل", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveProfile(userId: Int) {
        val fullName = binding.etFullName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        RetrofitClient.instance.updateProfile(userId, fullName, phone, address)
            .enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    Toast.makeText(
                        this@CustomerProfile,
                        response.body()?.message ?: "ذخیره شد",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Toast.makeText(this@CustomerProfile, "خطا در ذخیره پروفایل", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
