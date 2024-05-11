package com.deveem.dmarket.data.local.room.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.deveem.dmarket.data.local.room.dao.AppDao
import com.deveem.dmarket.data.local.room.entity.CartProductsEntity
import com.deveem.dmarket.data.local.room.entity.CategoriesEntity
import com.deveem.dmarket.data.local.room.entity.ProductsEntity

@Database(
  entities = [CategoriesEntity::class, ProductsEntity::class, CartProductsEntity::class],
  version = 3,
)
abstract class AppDatabase : RoomDatabase() {
  
  abstract fun productDao(): AppDao
  
  companion object {
    private val migration1_2 = object : Migration(1, 2) {
      override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS new_table (cart)")
      }
    }
    
    private val migration2_3 = object : Migration(2, 3) {
      override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS cart_temp (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, category TEXT NOT NULL, description TEXT NOT NULL, image TEXT NOT NULL, price REAL NOT NULL, title TEXT NOT NULL, count INTEGER NOT NULL, rate REAL NOT NULL, addedAt INTEGER NOT NULL DEFAULT 0)")
        db.execSQL("INSERT INTO cart_temp (id, category, description, image, price, title, count, rate) SELECT id, category, description, image, price, title, count, rate FROM cart")
        db.execSQL("DROP TABLE cart")
        db.execSQL("ALTER TABLE cart_temp RENAME TO cart")
      }
    }
    
    
    fun getInstance(context: Context): AppDatabase {
      return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
        .addMigrations(migration1_2,migration2_3)
        .build()
    }
  }
}