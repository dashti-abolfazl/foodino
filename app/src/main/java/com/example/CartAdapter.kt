package com.example.foodino

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodino.databinding.ItemCartBinding

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onChangeQuantity: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.tvCartName.text = item.name
            binding.tvCartPrice.text = "${item.price.toLong()} تومان × ${item.quantity}"
            binding.tvQty.text = item.quantity.toString()

            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ls)
                .error(R.drawable.wl_pic)
                .into(binding.imgCartProduct)

            binding.btnPlus.setOnClickListener {
                onChangeQuantity(item, item.quantity + 1)
            }
            binding.btnMinus.setOnClickListener {
                onChangeQuantity(item, item.quantity - 1)
            }
            binding.btnRemove.setOnClickListener {
                onRemove(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
