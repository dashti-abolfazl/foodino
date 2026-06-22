package com.example.foodino

import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodino.Product
import com.example.foodino.RetrofitClient
import com.example.foodino.SearchResponse
import com.example.foodino.SearchResultAdapter
import com.example.foodino.databinding.ActivitySearchBinding

class Search : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchList = mutableListOf<Product>()
    private lateinit var searchAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // RecyclerView دو ستونه
        binding.recycleview.layoutManager = LinearLayoutManager(this@Search, LinearLayoutManager.VERTICAL, false)

        searchAdapter = SearchResultAdapter(searchList) { product ->
            // روی آیتم کلیک واکنش بده
        }
        binding.recycleview.adapter = searchAdapter

        // دریافت متن سرچ از Intent
        val query = intent.getStringExtra("query") ?: ""
        if (query.isNotEmpty()) {
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        RetrofitClient.instance.searchProduct(query)
            .enqueue(object : retrofit2.Callback<SearchResponse> {
                override fun onResponse(
                    call: retrofit2.Call<SearchResponse>,
                    response: retrofit2.Response<SearchResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        searchAdapter.updateProducts(response.body()!!.data)
                    } else {
                        Toast.makeText(this@Search, "هیچ نتیجه‌ای یافت نشد", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<SearchResponse>, t: Throwable) {
                    Toast.makeText(this@Search, "خطا در اتصال", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
