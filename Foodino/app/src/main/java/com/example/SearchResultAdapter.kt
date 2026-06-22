package com.example.foodino

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodino.databinding.GhalebProductBinding
import com.example.foodino.databinding.ItemSearchResultBinding

class SearchResultAdapter(
    private val products: MutableList<Product>,
    private val onItemClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<SearchResultAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: GhalebProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            // متن‌ها
            binding.textViewName.text = product.name
            binding.textViewPrice.text = product.price?.let { "$it تومان" } ?: "نامشخص"

            // بارگذاری تصویر با Glide
            val imageUrl = product.imageUrl
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ls) // تصویر پیش‌فرض
                .error(R.drawable.wl_pic)   // تصویر جایگزین در صورت خطا
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("GLIDE_ERROR", "تصویر بارگذاری نشد: ${e?.localizedMessage}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("GLIDE_SUCCESS", "تصویر با موفقیت بارگذاری شد")
                        return false
                    }
                })
                .into(binding.imageViewProduct)

            // کلیک روی آیتم
            binding.root.setOnClickListener {
                onItemClick?.invoke(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = GhalebProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    // به‌روزرسانی لیست
    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}
