package com.deveem.dmarket.di

import android.util.Log.VERBOSE
import com.deveem.dmarket.data.local.Constants
import com.deveem.dmarket.data.local.Constants.BASE_URL
import com.deveem.dmarket.data.remote.apiservice.ProductsAPIService
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
  
  @Singleton
  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }
  
  @Singleton
  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    val client = OkHttpClient.Builder()
    client.addInterceptor(
      LoggingInterceptor.Builder()
      .setLevel(Level.BASIC)
      .log(VERBOSE)
      .build())
    client.callTimeout(60, TimeUnit.SECONDS)
      .connectTimeout(60, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
    return client.build()
  }
  
  @Singleton
  @Provides
  fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val levelType: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
    return HttpLoggingInterceptor().apply {
      level = levelType
    }
  }
  
  @Singleton
  @Provides
  fun provideProductsAPIService(retrofit: Retrofit): ProductsAPIService {
    return retrofit.create(ProductsAPIService::class.java)
  }
}