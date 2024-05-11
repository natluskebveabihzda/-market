package com.deveem.dmarket.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoriesEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val category: String,
)

fun List<CategoriesEntity>.toList(): List<String> {
  val result = arrayListOf<String>()
  this.map { result.add(it.category) }
  return result.toList()
}