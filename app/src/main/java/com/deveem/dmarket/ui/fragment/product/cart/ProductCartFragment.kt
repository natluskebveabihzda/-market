package com.deveem.dmarket.ui.fragment.product.cart

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.deveem.dmarket.MainActivity
import com.deveem.dmarket.R
import com.deveem.dmarket.core.base.BaseFragment
import com.deveem.dmarket.data.dto.CartProductDto
import com.deveem.dmarket.data.local.Constants.CART
import com.deveem.dmarket.databinding.FragmentProductsCartBinding
import com.deveem.dmarket.util.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ProductCartFragment : BaseFragment<FragmentProductsCartBinding, ProductCartViewModel>(R.layout.bottom_sheet_dialog_fragment_product_detail) {
  
  override fun inflateBinding() = FragmentProductsCartBinding.inflate(layoutInflater)
  override val viewModel by viewModels<ProductCartViewModel>()
  private val productListAdapter by lazy {
    ProductCartAdapter { model, position -> removeCartProduct(model, position) }
  }
  
  override fun initView() {
    binding.rvCartProducts.adapter = productListAdapter
    initTopAppBarNavigation()
  }
  
  override fun initRequest() {
    getAllCartProducts()
  }
  
  private fun getAllCartProducts() {
    viewModel.getAllCartProducts()
  }
  
  private fun removeCartProduct(model: CartProductDto, position: Int) {
    viewModel.removeCartItem(model)
    productListAdapter.removeItem(position)
  }
  
  override fun initSubscribers() {
    viewModel.cartProductsState.spectateUiState { uiState ->
      when (uiState) {
        is UIState.Success -> {
          initTopAppBarTitle(uiState.data.size)
          productListAdapter.setData(uiState.data)
          (activity as MainActivity).progressLineVisible(false)
        }
        
        is UIState.Error -> (activity as MainActivity).progressLineVisible(false)
        is UIState.Idle -> (activity as MainActivity).progressLineVisible(false)
        is UIState.Loading -> (activity as MainActivity).progressLineVisible(true)
      }
    }
  }
  
  private fun initTopAppBarTitle(cartCount: Int) {
    val bigText = CART
    val cartCountText = cartCount.toString().uppercase(Locale.ROOT)
    val spannable = SpannableString("$bigText ($cartCountText)")
    
    spannable.setSpan(
      StyleSpan(Typeface.NORMAL),
      0,
      bigText.length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    
    spannable.setSpan(
      RelativeSizeSpan(0.6f),
      bigText.length + 1,
      spannable.length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    
    spannable.setSpan(
      ForegroundColorSpan(Color.GRAY),
      bigText.length + 1,
      spannable.length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    (activity as MainActivity).setToolbarTitle(spannable, "")
  }
  
  
  private fun initTopAppBarNavigation() {
    (activity as MainActivity).apply {
      supportActionBar?.run {
        setHomeActionContentDescription(getString(R.string.back))
        setDisplayHomeAsUpEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_arrow_back)
      }
    }
    
    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}
      
      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
          android.R.id.home -> {
            findNavController().navigateUp()
          }
          
          else -> return true
        }
        return false
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }
}