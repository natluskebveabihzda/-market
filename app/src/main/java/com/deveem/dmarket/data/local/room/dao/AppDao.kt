package com.deveem.dmarket.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.deveem.dmarket.data.local.room.entity.CartProductsEntity
import com.deveem.dmarket.data.local.room.entity.CategoriesEntity
import com.deveem.dmarket.data.local.room.entity.ProductsEntity

@Dao
interface AppDao {
  
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun createProduct(productsEntity: ProductsEntity)
  
  @Query("SELECT COUNT(*) FROM cart WHERE id = :id")
  suspend fun isCartProductSaved(id: Int): Int
  
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveProductToCart(cartProductsEntity: CartProductsEntity)
  
  @Delete
  suspend fun deleteCartProduct(cartProductsEntity: CartProductsEntity): Int
  
  @Query("SELECT * FROM cart ORDER BY addedAt ASC")
  suspend fun readAllCartProducts(): List<CartProductsEntity>
  
  @Query("SELECT COUNT(*) FROM cart")
  suspend fun isCartProductsListSize(): Int
  
  @Query("SELECT * FROM products WHERE category = :category")
  suspend fun readProductsByCategory(category: String): List<ProductsEntity>
  
  @Query("SELECT COUNT(*) FROM products WHERE category = :category")
  suspend fun isProductsByCategorySaved(category: String): Int
  
  @Query("SELECT COUNT(*) FROM products")
  suspend fun isAllProductsSaved(): Int
  
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun createCategory(categoriesEntity: CategoriesEntity)
  
  @Query("SELECT * FROM categories")
  suspend fun readAllCategories(): List<CategoriesEntity>
  
  @Query("SELECT * FROM products")
  suspend fun readAllProducts(): List<ProductsEntity>
  
  @Update
  suspend fun updateCategory(categoriesEntity: CategoriesEntity)
  
  @Delete
  suspend fun deleteCategory(categoriesEntity: CategoriesEntity)
  
  @Query("SELECT COUNT(*) FROM categories")
  suspend fun isCategoriesSaved(): Int
}