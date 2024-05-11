package com.deveem.dmarket.core.base

import android.util.Log
import com.deveem.dmarket.data.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

abstract class BaseRepository {
  
  protected fun <T> doRemoteRequest(response: suspend () -> Resource<T>): Flow<Resource<T>> = flow {
    emit(Resource.Loading())
    try {
      emit(Resource.Success(response().data))
    } catch (ioException: IOException) {
      Log.e("Error remote request", "RemoteRequest: $ioException", )
      emit(Resource.Error(ioException.localizedMessage ?: "unknown exception"))
    }
  }.flowOn(Dispatchers.IO)
  
  protected inline fun <T> doLocalRequest(crossinline response: suspend () -> T): Flow<Resource<T>> = flow {
    Log.d("doLocalRequest", "doLocalRequest")
    emit(Resource.Loading())
    try {
      val result = response()
      emit(Resource.Success(result))
    } catch (ioException: IOException) {
      Log.e("Error local request", "LocalRequest: $ioException")
      emit(Resource.Error(ioException.localizedMessage ?: "unknown exception"))
    }
  }.flowOn(Dispatchers.IO)

}