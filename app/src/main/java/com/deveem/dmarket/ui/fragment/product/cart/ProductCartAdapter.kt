package com.deveem.dmarket.ui.fragment.product.cart


import android.view.View
import coil.load
import com.deveem.dmarket.core.base.BaseAdapter
import com.deveem.dmarket.data.dto.CartProductDto
import com.deveem.dmarket.databinding.ItemCartProductBinding

class ProductCartAdapter(private val onDeleteItemClick: (CartProductDto, Int) -> Unit) :
  BaseAdapter<CartProductDto, ItemCartProductBinding>(ItemCartProductBinding::inflate) {
  
  override fun bindData(model: CartProductDto, binding: ItemCartProductBinding, adapterPosition: Int, itemView: View) {
    with(binding) {
      tvProductTitle.text = model.title
      tvProductPrice.text = model.price.toString()
      ivProductImage.load(model.image)
      
      btnRemove.setOnClickListener {
        onDeleteItemClick(model, adapterPosition)
      }
    }
  }
}

