package com.example.foodino

import ProductAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodino.databinding.ActivityShopScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopScreen : AppCompatActivity() {

    private lateinit var binding: ActivityShopScreenBinding
    private lateinit var recyclerView: RecyclerView

    private lateinit var productAdapter: ProductAdapter
    private lateinit var searchAdapter: SearchResultAdapter

    private var productList: List<Product> = emptyList()
    private val searchList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchRecycler()
        setupSearchView()
        fetchProducts()
    }

    // ---------------- RecyclerView ----------------
    private fun setupRecyclerView() {
        recyclerView = binding.recycleview
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupSearchRecycler() {
        searchAdapter = SearchResultAdapter(searchList) { product ->
            // کلیک روی محصول (در صورت نیاز)
        }
    }

    // ---------------- Products ----------------
    private fun fetchProducts() {
        RetrofitClient.instance.getProducts()
            .enqueue(object : Callback<ProductResponse> {

                override fun onResponse(
                    call: Call<ProductResponse>,
                    response: Response<ProductResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {

                        productList = response.body()!!.products
                        productAdapter = ProductAdapter(productList)
                        recyclerView.adapter = productAdapter

                    } else {
                        showToast("خطا در دریافت محصولات")
                    }
                }

                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    showToast("خطا در اتصال")
                    Log.e("API_ERROR", t.localizedMessage ?: "")
                }
            })
    }

    // ---------------- SearchView ----------------
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { performSearch(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank()) {
                        recyclerView.adapter = productAdapter
                    } else {
                        performSearch(newText)
                    }
                    return true
                }
            })
    }

    private fun performSearch(query: String) {
        RetrofitClient.instance.searchProduct(query)
            .enqueue(object : Callback<SearchResponse> {

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {

                        recyclerView.adapter = searchAdapter
                        searchAdapter.updateProducts(response.body()!!.data)

                    } else {
                        searchAdapter.updateProducts(emptyList())
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showToast("خطا در جستجو")
                }
            })
    }

    // ---------------- Toast ----------------
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
