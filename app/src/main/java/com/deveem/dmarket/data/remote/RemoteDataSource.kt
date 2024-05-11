package com.deveem.dmarket.data.remote

import com.deveem.dmarket.core.base.BaseDataSource
import com.deveem.dmarket.data.remote.apiservice.ProductsAPIService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ProductsAPIService) : BaseDataSource() {
  
  suspend fun fetchCategories() = getResult { apiService.fetchCategories() }
  
  suspend fun fetchProductsByCategory(categoryName: String) = getResult { apiService.fetchProductsByCategory(categoryName) }
  
  suspend fun fetchAllProducts() = getResult { apiService.fetchAllProducts() }
}