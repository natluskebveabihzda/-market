package com.deveem.dmarket.ui.fragment.product.detail

import com.deveem.dmarket.core.base.BaseViewModel
import com.deveem.dmarket.data.remote.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel  @Inject constructor(
  private val repository: ProductsRepository,
) : BaseViewModel() {}