package com.deveem.dmarket.di

import android.content.Context
import androidx.room.Room
import com.deveem.dmarket.data.local.room.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
  
  @Singleton
  @Provides
  fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
    return AppDatabase.getInstance(context)
  }
  
  @Singleton
  @Provides
  fun provideDao(db: AppDatabase) = db.productDao()
}
