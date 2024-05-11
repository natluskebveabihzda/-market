package com.deveem.dmarket.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deveem.dmarket.data.dto.CartProductDto
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.dto.RatingDto

@Entity(tableName = "cart")
data class CartProductsEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val category: String,
  val description: String,
  val image: String,
  val price: Double,
  val title: String,
  val count: Int,
  val rate: Double,
  @ColumnInfo(name = "addedAt", defaultValue = "0")
  val addedAt: Long = System.currentTimeMillis()
)

fun CartProductsEntity.toDto() =
  CartProductDto(
    category = this.category,
    description = this.description,
    image = this.image,
    price = this.price,
    title = this.title,
    rating = RatingDto(count = this.count, rate = this.rate),
    addedAt = this.addedAt,
    id = this.id
  )

fun CartProductsEntity.toProductDto() =
  ProductDto(
    category = this.category,
    description = this.description,
    image = this.image,
    price = this.price,
    title = this.title,
    rating = RatingDto(count = this.count, rate = this.rate),
    id = this.id
  )
