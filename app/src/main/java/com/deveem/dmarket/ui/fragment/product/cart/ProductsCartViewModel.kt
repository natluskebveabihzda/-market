package com.deveem.dmarket.ui.fragment.product.cart

import androidx.lifecycle.viewModelScope
import com.deveem.dmarket.core.base.BaseViewModel
import com.deveem.dmarket.data.Resource
import com.deveem.dmarket.data.dto.CartProductDto
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.remote.repository.ProductsRepository
import com.deveem.dmarket.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsCartViewModel @Inject constructor(
  private val repository: ProductsRepository,
) : BaseViewModel() {
  
  private var _cartProductsState = mutableUiStateFlow<List<CartProductDto>>()
  val cartProductsState = _cartProductsState.asStateFlow()
  
  fun getAllCartProducts() {
    repository.getAllCartProducts().collectFlow(_cartProductsState)
  }

  fun removeCartItem(cartProductDto: CartProductDto) {
    viewModelScope.launch {
      repository.removeCartItem(cartProductDto).collect { resource ->
        if (resource is Resource.Success) {
          getAllCartProducts()
        }
      }
    }
  }
}