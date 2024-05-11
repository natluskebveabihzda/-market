package com.deveem.dmarket.ui.fragment.product.detail

import android.util.Log
import androidx.fragment.app.viewModels
import com.deveem.dmarket.R
import com.deveem.dmarket.core.base.BaseFragment
import com.deveem.dmarket.core.extention.getSerializableExt
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.local.Constants.PRODUCT_DETAILS_KEY
import com.deveem.dmarket.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment :
  BaseFragment<FragmentProductDetailBinding, ProductDetailViewModel>(R.layout.fragment_product_detail) {
  
  override fun inflateBinding() = FragmentProductDetailBinding.inflate(layoutInflater)
  override val viewModel by viewModels<ProductDetailViewModel>()
  private val productModel by lazy {
    arguments?.let {
      getSerializableExt(
        it,
        PRODUCT_DETAILS_KEY,
        ProductDto::class.java
      )
    }
  }
  
  override fun initView() {
    Log.d("ProductDetailFragment", "initView: $productModel")
  }
  
}