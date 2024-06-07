package com.deveem.dmarket.ui.fragment.product.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.deveem.dmarket.core.base.BaseViewModel
import com.deveem.dmarket.data.Resource
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.dto.toCartDto
import com.deveem.dmarket.data.remote.repository.ProductsRepository
import com.deveem.dmarket.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
  private val repository: ProductsRepository,
) : BaseViewModel() {
  
  private var _productsState = mutableUiStateFlow<List<ProductDto>>()
  val productsState = _productsState.asStateFlow()
  
  private val _isProductsByCategorySavedState = mutableUiStateFlow<Boolean>()
  val isProductsByCategorySavedState = _isProductsByCategorySavedState.asStateFlow()
  
  private val _isAllProductsSavedState = mutableUiStateFlow<Boolean>()
  val isAllProductsSavedState = _isAllProductsSavedState.asStateFlow()
  
  private val _isAllCartProductsTotalValue = mutableUiStateFlow<Int>()
  val isAllCartProductsTotalValue = _isAllCartProductsTotalValue.asStateFlow()
  
  private val _createProductsState = MutableStateFlow<UIState<Unit>>(UIState.Idle())
  
  private val _categoriesState = mutableUiStateFlow<List<String>>()
  val categoriesState = _categoriesState.asStateFlow()
  
  private val _isCategoriesSavedState = mutableUiStateFlow<Boolean>()
  val isCategoriesSavedState = _isCategoriesSavedState.asStateFlow()
  
  private val _createCategoryState = MutableStateFlow<UIState<Unit>>(UIState.Idle())
  
  
  fun isALlProductsSaved() {
    val isSaved = repository.isALlProductsSaved()
    _isAllProductsSavedState.value = UIState.Success(isSaved)
  }
  
  fun getAllCartTotalValue() {
    viewModelScope.launch {
      repository.checkIsAllCartProductsTotalValue().collect { resource ->
        if (resource is Resource.Success) {
          Log.d("WWWWWWWWWWWWWWWWWWWWWWW", "getAllCartTotalValue:${ UIState.Success(resource.data?: 0 )} ")
          _isAllCartProductsTotalValue.value = UIState.Success(resource.data?: 0 )
        }
      }
    }
  }
  
  fun getAllProducts() {
    repository.getAllProducts().collectFlow(_productsState)
  }
  
  fun createProducts(products: List<ProductDto>, category: String) {
    products.map { repository.createProduct(it, category).collectFlow(_createProductsState) }
  }
  
  fun createCategories(categories: List<String>) {
    categories.map { repository.createCategory(it).collectFlow(_createCategoryState) }
  }
  
  fun getProductsByCategory(category: String) {
    repository.getProductsByCategory(category).collectFlow(_productsState)
  }
  
  fun isProductsByCategorySaved() {
    val isSaved = repository.isProductsByCategorySaved()
    _isProductsByCategorySavedState.value = UIState.Success(isSaved)
  }
  
  fun isCategoriesSaved() {
    repository.isCategoriesSaved().collectFlow(_isCategoriesSavedState)
  }
  
  fun getCategories() {
    repository.getCategories().collectFlow(_categoriesState)
  }
}