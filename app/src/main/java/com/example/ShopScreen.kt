package com.example.foodino

import ProductAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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

    private lateinit var prefs: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = SharedPrefManager(this)

        setupToolbarAndDrawer()
        setupRecyclerView()
        setupSearchRecycler()
        setupSearchView()
        fetchProducts()
    }

    // ---------------- Toolbar + Drawer (منوی همبرگری) ----------------
    private fun setupToolbarAndDrawer() {
        setupFoodinoDrawer(binding.drawerLayout, binding.toolbar, binding.navView)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // ---------------- RecyclerView ----------------
    private fun setupRecyclerView() {
        recyclerView = binding.recycleview
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupSearchRecycler() {
        searchAdapter = SearchResultAdapter(searchList) { product ->
            addToCart(product)
        }
    }

    // ---------------- افزودن به سبد خرید ----------------
    private fun addToCart(product: Product) {
        val userId = prefs.getUserId()
        if (userId <= 0) {
            showToast("برای خرید ابتدا وارد حساب شوید")
            return
        }
        RetrofitClient.instance.addToCart(userId, product.id, 1)
            .enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    val res = response.body()
                    showToast(res?.message ?: "به سبد اضافه شد")
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    showToast("خطا در افزودن به سبد")
                }
            })
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
                        productAdapter = ProductAdapter(productList) { product ->
                            addToCart(product)
                        }
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
                        if (::productAdapter.isInitialized) recyclerView.adapter = productAdapter
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
