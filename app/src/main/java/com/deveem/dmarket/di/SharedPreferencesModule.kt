package com.deveem.dmarket.di

import android.content.Context
import com.deveem.dmarket.data.SharedPreferences
import com.deveem.dmarket.data.local.PreferencesKey.DMARKET_SHARED_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
  
  @Singleton
  @Provides
  fun generateSharedPreferences(@ApplicationContext context: Context): android.content.SharedPreferences =
    context.getSharedPreferences(
      DMARKET_SHARED_PREFERENCES,
      Context.MODE_PRIVATE
    )
  
  @Singleton
  @Provides
  fun generateUserPreferences(sharedPreferences: android.content.SharedPreferences) =
    SharedPreferences(sharedPreferences)
}