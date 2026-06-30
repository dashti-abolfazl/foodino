package com.example.foodino

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodino.databinding.ActivityOrderScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/** صفحه‌ی سبد خرید + فرم ثبت سفارش (فرم خرید) */
class OrderScreen : AppCompatActivity() {

    private lateinit var binding: ActivityOrderScreenBinding
    private lateinit var prefs: SharedPrefManager
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = SharedPrefManager(this)
        userId = prefs.getUserId()

        if (userId <= 0) {
            Toast.makeText(this, "ابتدا وارد حساب شوید", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupFoodinoDrawer(binding.drawerLayout, binding.toolbar, binding.navView)

        cartAdapter = CartAdapter(
            cartItems,
            onChangeQuantity = { item, newQty -> changeQuantity(item, newQty) },
            onRemove = { item -> removeItem(item) }
        )
        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.adapter = cartAdapter

        // پیش‌پرکردن اطلاعات تحویل از روی پروفایل
        prefillDelivery()

        binding.btnPlaceOrder.setOnClickListener { placeOrder() }

        loadCart()
    }

    private fun loadCart() {
        RetrofitClient.instance.getCart(userId).enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                val body = response.body()
                if (body?.success == true) {
                    cartAdapter.update(body.items)
                    binding.tvTotal.text = "جمع کل: ${body.total.toLong()} تومان"
                    toggleEmpty(body.items.isEmpty())
                } else {
                    toggleEmpty(true)
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Toast.makeText(this@OrderScreen, "خطا در دریافت سبد خرید", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleEmpty(empty: Boolean) {
        binding.tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.recyclerCart.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun changeQuantity(item: CartItem, newQty: Int) {
        RetrofitClient.instance.updateCart(userId, item.productId, newQty)
            .enqueue(object : Callback<ServerResponse> {
                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    loadCart()
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Toast.makeText(this@OrderScreen, "خطا در بروزرسانی سبد", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun removeItem(item: CartItem) {
        RetrofitClient.instance.removeFromCart(userId, item.productId)
            .enqueue(object : Callback<ServerResponse> {
                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    Toast.makeText(this@OrderScreen, "حذف شد", Toast.LENGTH_SHORT).show()
                    loadCart()
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Toast.makeText(this@OrderScreen, "خطا در حذف آیتم", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun prefillDelivery() {
        RetrofitClient.instance.getProfile(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                val user = response.body()?.user ?: return
                if (binding.etName.text.isNullOrBlank()) binding.etName.setText(user.fullName ?: "")
                if (binding.etPhone.text.isNullOrBlank()) binding.etPhone.setText(user.phone ?: "")
                if (binding.etAddress.text.isNullOrBlank()) binding.etAddress.setText(user.address ?: "")
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {}
        })
    }

    private fun placeOrder() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
            return
        }
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "سبد خرید شما خالی است", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.placeOrder(userId, name, phone, address)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    val res = response.body()
                    if (res?.success == true) {
                        Toast.makeText(
                            this@OrderScreen,
                            "${res.message} (کد سفارش: ${res.orderId})",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(this@OrderScreen, res?.message ?: "خطا در ثبت سفارش", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@OrderScreen, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
