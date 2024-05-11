package com.deveem.dmarket.core.base

import android.util.Log
import com.deveem.dmarket.data.Resource
import retrofit2.Response

abstract class BaseDataSource {

    suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null || response.code() in 200..299) return Resource.Success(body)
            } else {
                Log.e("aga", "ERROR: " + response.message())
                Log.e("aga", "RAW: " + response.raw())
                return Resource.Error(response.message(), response.body())
            }
        } catch (e: Exception) {
            Log.e("aga", "Exception: $e", )
            return Resource.Error(e.message ?: e.toString(), null)
        }
        Log.e("aga", "getResult: ${call().message()}", )
        return Resource.Error(call().message(), null)
    }
}