package com.deveem.dmarket.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deveem.dmarket.data.Resource
import com.deveem.dmarket.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel() : ViewModel() {
  
  protected fun <T> mutableUiStateFlow() = MutableStateFlow<UIState<T>>(UIState.Idle())
  
  protected fun <T> Flow<Resource<T>>.collectFlow(
    state: MutableStateFlow<UIState<T>>,
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      this@collectFlow.collect { res ->
        when (res) {
          is Resource.Error -> {
            if (res.message != null) {
              state.value = UIState.Error(res.message)
            }
          }
          is Resource.Loading -> {
            state.value = UIState.Loading()
          }
          is Resource.Success -> {
            if (res.data != null) {
              state.value = UIState.Success(res.data)
            }
          }
        }
      }
    }
  }
}