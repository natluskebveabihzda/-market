package com.deveem.dmarket.data.remote.repository

import android.util.Log
import com.deveem.dmarket.core.base.BaseRepository
import com.deveem.dmarket.data.Resource
import com.deveem.dmarket.data.dto.CartProductDto
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.dto.toCartProductEntity
import com.deveem.dmarket.data.dto.toProductEntity
import com.deveem.dmarket.data.local.room.dao.AppDao
import com.deveem.dmarket.data.local.room.entity.CategoriesEntity
import com.deveem.dmarket.data.local.room.entity.toDto
import com.deveem.dmarket.data.local.room.entity.toList
import com.deveem.dmarket.data.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

class ProductsRepository @Inject constructor(
  private val appDao: AppDao,
  private val remoteDataSource: RemoteDataSource,
) : BaseRepository() {
  private var isProductSaved = false
  private var isAllProductSaved = false
  private var isCartProductSaved = false
  
  fun isProductsByCategorySaved() = isProductSaved
  fun isALlProductsSaved() = isAllProductSaved
  
  fun getProductsByCategory(category: String): Flow<Resource<List<ProductDto>>> {
    val isSaved = runBlocking { checkIsSaved(category) }
    isProductSaved = isSaved
    return if (isSaved) {
      doLocalRequest { appDao.readProductsByCategory(category).map { it.toDto() } }
    } else {
      doRemoteRequest { remoteDataSource.fetchProductsByCategory(category) }
    }
  }
  
  fun getAllProducts(): Flow<Resource<List<ProductDto>>> {
    val isAllProductSavedState = runBlocking { checkIsSavedAllProducts() }
    isAllProductSaved = isAllProductSavedState
    return if (isAllProductSavedState) {
      doLocalRequest { appDao.readAllProducts().map { it.toDto() } }
    } else {
      doRemoteRequest { remoteDataSource.fetchAllProducts() }
    }
  }
  
  fun getAllCartProducts(): Flow<Resource<List<CartProductDto>>> {
    return doLocalRequest { appDao.readAllCartProducts().map { it.toDto() } }
  }
  
  fun removeCartItem(cartProductDto: CartProductDto): Flow<Resource<Unit>> = flow {
    try {
      val deleted = appDao.deleteCartProduct(cartProductDto.toCartProductEntity())
      if (deleted > 0) {
        emit(Resource.Success(Unit))
      } else {
        emit(Resource.Error("Failed to delete item"))
      }
    } catch (ioException: IOException) {
      Log.e("Error local request", "LocalRequest: $ioException")
      emit(Resource.Error(ioException.localizedMessage ?: "unknown exception"))
    }
  }.flowOn(Dispatchers.IO)
  
  fun createProduct(productDto: ProductDto, category: String): Flow<Resource<Unit>> = flow {
    val isSaved = checkIsSaved(category)
    if (!isSaved) {
      emit(Resource.Loading())
      try {
        appDao.createProduct(productDto.toProductEntity())
        emit(Resource.Success(Unit))
      } catch (ioException: IOException) {
        Log.e("Error local request", "LocalRequest: $ioException")
        emit(Resource.Error(ioException.localizedMessage ?: "unknown exception"))
      }
    }
  }.flowOn(Dispatchers.IO)
  
  fun addCartItem(cartProductDto: CartProductDto): Flow<Resource<Unit>> = flow {
    val isSaved = checkIsSingleCartSaved(cartProductDto)
    isCartProductSaved = isSaved
    if (!isSaved) {
      emit(Resource.Loading())
      try {
        appDao.saveProductToCart(cartProductDto.toCartProductEntity())
        checkIsAllCartProductsTotalValue()
        emit(Resource.Success(Unit))
      } catch (ioException: IOException) {
        Log.e("Error local request", "LocalRequest: $ioException")
        emit(Resource.Error(ioException.localizedMessage ?: "unknown exception"))
      }
    }
  }.flowOn(Dispatchers.IO)
  
  private suspend fun checkIsSaved(category: String): Boolean {
    return appDao.isProductsByCategorySaved(category) > 0
  }
  
  private suspend fun checkIsSingleCartSaved(cartProductDto: CartProductDto): Boolean {
    return appDao.isCartProductSaved(cartProductDto.id) > 0
  }
  
  private suspend fun checkIsSavedAllProducts(): Boolean {
    return appDao.isAllProductsSaved() > 0
  }
  
  fun checkIsAllCartProductsTotalValue(): Flow<Resource<Int>> = flow {
    try {
      val size = appDao.isCartProductsListSize()
      emit(Resource.Success(size))
    } catch (ioException: IOException) {
      Log.e("Error local request", "LocalRequest: $ioException")
      emit(Resource.Error(ioException.localizedMessage ?: "unknown exception"))
    }
  }.flowOn(Dispatchers.IO)

  
  fun isCategoriesSaved() = doLocalRequest { appDao.isCategoriesSaved() > 0 }
  
  fun createCategory(category: String) =
    doLocalRequest { appDao.createCategory(CategoriesEntity(category = category)) }
  
  fun getCategories(): Flow<Resource<List<String>>> {
    val isSaved = runBlocking { checkIsSaved() }
    return if (isSaved) {
      doLocalRequest { appDao.readAllCategories().toList() }
    } else {
      doRemoteRequest { remoteDataSource.fetchCategories() }
    }
  }
  
  private suspend fun checkIsSaved(): Boolean {
    return appDao.isCategoriesSaved() > 0
  }
}