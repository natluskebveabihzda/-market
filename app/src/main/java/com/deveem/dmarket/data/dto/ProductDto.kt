package com.deveem.dmarket.data.dto

import com.deveem.dmarket.data.local.room.entity.ProductsEntity

data class ProductDto(
  val category: String,
  val description: String,
  val image: String,
  val price: Double,
  val rating: RatingDto,
  val title: String,
  val id: Int,
) : java.io.Serializable

data class RatingDto(
  val count: Int,
  val rate: Double,
)

fun ProductDto.toProductEntity() = ProductsEntity(
  category = this.category,
  description = this.description,
  image = this.image,
  price = this.price,
  title = this.title,
  rate = this.rating.rate,
  count = this.rating.count,
  id = this.id
)

fun ProductDto.toCartDto() = CartProductDto(
  category = this.category,
  description = this.description,
  image = this.image,
  price = this.price,
  title = this.title,
  rating = this.rating,
  id = this.id
)
