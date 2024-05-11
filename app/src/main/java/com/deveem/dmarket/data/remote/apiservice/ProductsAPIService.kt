package com.deveem.dmarket.data.remote.apiservice

import com.deveem.dmarket.data.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsAPIService {
  
  @GET("products/categories")
  suspend fun fetchCategories(
  ): Response<List<String>>
  
  @GET("products/category/{categoryName}")
  suspend fun fetchProductsByCategory(
    @Path("categoryName") categoryName: String,
  ): Response<List<ProductDto>>
  
  @GET("products")
  suspend fun fetchAllProducts(
  ): Response<List<ProductDto>>
  
}