package com.deveem.dmarket.core.base

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.deveem.dmarket.MainActivity
import com.deveem.dmarket.data.local.Constants
import com.deveem.dmarket.data.local.Constants.CALL_DISMISS_KEY
import com.deveem.dmarket.util.UIState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.function.Consumer


abstract class BaseBottomSheetDialogFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(
  @LayoutRes layoutId: Int
) : BottomSheetDialogFragment() {
  private var _binding: Binding? = null
  protected open val binding: Binding
    get() = _binding!!
  
  abstract fun inflateBinding(): Binding
  protected abstract val viewModel: ViewModel
  
  @RequiresApi(Build.VERSION_CODES.S)
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    dialog.setOnShowListener { dialogInterface ->
      val bottomSheetDialog = dialogInterface as BottomSheetDialog
      val bottomSheet =
        bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
      if (bottomSheet != null) {
        bottomSheet.backgroundTintList = ColorStateList(arrayOf(intArrayOf(Color.WHITE)), intArrayOf(Color.WHITE))
        /*  bottomSheet.setBackgroundResource(R.drawable.bg_bottom_sheet_choose_photo)*/
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        
      }
      bottomSheetDialog.setCancelable(true)
      bottomSheetDialog.window?.findViewById<View>(Window.ID_ANDROID_CONTENT)?.setOnTouchListener { _, _ -> true }
      bottomSheetDialog.window?.apply {
        
        setDimAmount(0.8f)
      }
      
      initView()
    }
    return dialog
  }
  
  override fun onStart() {
    super.onStart()
    dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.layoutParams?.height =
      ViewGroup.LayoutParams.WRAP_CONTENT
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = inflateBinding()
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView()
    initListeners()
    initRequest()
    initSubscribers()
  }
  
  protected open fun initView() {}
  protected open fun initListeners() {}
  protected open fun initRequest() {}
  protected open fun initSubscribers() {}
  
  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
  
  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    findNavController().previousBackStackEntry?.savedStateHandle?.set(CALL_DISMISS_KEY, true)
  }
  
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