package com.deveem.dmarket.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.deveem.dmarket.util.UIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(
  @LayoutRes layoutId: Int
) : Fragment(layoutId) {
  private var _binding: Binding? = null
  protected open val binding: Binding
    get() = _binding!!
  
  abstract fun inflateBinding(): Binding
  protected abstract val viewModel: ViewModel
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = inflateBinding()
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (isAdded && activity != null) {
      initView()
      initListeners()
      initRequest()
      initSubscribers()
    }
  }
  
  protected open fun initView() {}
  protected open fun initListeners() {}
  protected open fun initRequest() {}
  protected open fun initSubscribers() {}
  
  protected fun <T> StateFlow<UIState<T>>.spectateUiState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    success: ((data: T) -> Unit)? = null,
    loading: ((data: UIState.Loading<T>) -> Unit)? = null,
    error: ((error: String) -> Unit)? = null,
    idle: ((idle: UIState.Idle<T>) -> Unit)? = null,
    gatherIfSucceed: ((state: UIState<T>) -> Unit)? = null,
  ) {
    safeFlowGather(lifecycleState) {
      collect {
        gatherIfSucceed?.invoke(it)
        when (it) {
          is UIState.Idle -> idle?.invoke(it)
          is UIState.Loading -> loading?.invoke(it)
          is UIState.Error -> error?.invoke(it.error)
          is UIState.Success -> success?.invoke(it.data)
        }
      }
    }
  }
  
  private fun safeFlowGather(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    gather: suspend () -> Unit,
  ) {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
        gather()
      }
    }
  }
}