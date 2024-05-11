package com.deveem.dmarket.ui.fragment.product.list


import android.view.View
import coil.load
import com.deveem.dmarket.core.base.BaseAdapter
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.databinding.ItemProductBinding

class ProductListAdapter(private val onItemClick: (ProductDto) -> Unit) :
  BaseAdapter<ProductDto, ItemProductBinding>(ItemProductBinding::inflate) {
  
  override fun bindData(model: ProductDto, binding: ItemProductBinding, adapterPosition: Int, itemView: View) {
    with(binding) {
      tvProductTitle.text = model.title
      tvProductPrice.text = model.price.toString()
      ivProductImage.load(model.image)
    }
    itemView.setOnClickListener { onItemClick(model) }
  }
}
