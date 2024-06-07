package com.deveem.dmarket.ui.fragment.product.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.deveem.dmarket.core.base.BaseViewModel
import com.deveem.dmarket.data.Resource
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.dto.toCartDto
import com.deveem.dmarket.data.remote.repository.ProductsRepository
import com.deveem.dmarket.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel  @Inject constructor(
  private val repository: ProductsRepository,
) : BaseViewModel() {
  private val _isThisProductSavedInCard = mutableUiStateFlow<Boolean>()
  val isThisProductSavedInCard = _isThisProductSavedInCard.asStateFlow()
  
  fun addProductToCart(product: ProductDto){
    viewModelScope.launch {
      repository.addCartItem(product.toCartDto()).collect { resource ->
        if (resource is Resource.Success) {
          Log.d("ProductDetailViewModel", "addProductToCart")
          isThisProductInCardSaved(product)
        }
      }
    }
  }
  
  fun isThisProductInCardSaved(product: ProductDto) {
    repository.checkIsSavedSingleProduct(product.toCartDto()).collectFlow(_isThisProductSavedInCard)
  }
}