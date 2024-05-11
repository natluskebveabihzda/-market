package com.deveem.dmarket.data.dto

import com.deveem.dmarket.data.local.room.entity.CartProductsEntity

data class CartProductDto(
  val category: String,
  val description: String,
  val image: String,
  val price: Double,
  val rating: RatingDto,
  val title: String,
  val addedAt: Long = System.currentTimeMillis(),
  val id: Int = 0
) : java.io.Serializable

fun CartProductDto.toCartProductEntity() = CartProductsEntity(
  category = this.category,
  description = this.description,
  image = this.image,
  price = this.price,
  title = this.title,
  rate = this.rating.rate,
  count = this.rating.count,
  addedAt = this.addedAt,
  id = this.id
)
