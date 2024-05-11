package com.deveem.dmarket.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.dto.RatingDto

@Entity(tableName = "products")
data class ProductsEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val category: String,
  val description: String,
  val image: String,
  val price: Double,
  val title: String,
  val count: Int,
  val rate: Double,
)

fun ProductsEntity.toDto() =
  ProductDto(
    category = this.category,
    description = this.description,
    image = this.image,
    price = this.price,
    title = this.title,
    rating = RatingDto(count = this.count, rate = this.rate),
    id = this.id
  )

