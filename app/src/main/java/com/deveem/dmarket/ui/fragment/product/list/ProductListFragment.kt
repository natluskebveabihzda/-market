package com.deveem.dmarket.ui.fragment.product.list

import android.content.Context
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.deveem.dmarket.MainActivity
import com.deveem.dmarket.R
import com.deveem.dmarket.core.base.BaseFragment
import com.deveem.dmarket.core.extention.setupBlurView
import com.deveem.dmarket.data.dto.ProductDto
import com.deveem.dmarket.data.local.Constants.CALL_DISMISS_KEY
import com.deveem.dmarket.data.local.Constants.PRODUCTS
import com.deveem.dmarket.databinding.FragmentProductListBinding
import com.deveem.dmarket.util.CountDrawable
import com.deveem.dmarket.util.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding, ProductListViewModel>(R.layout.fragment_product_list) {
  
  override fun inflateBinding() = FragmentProductListBinding.inflate(layoutInflater)
  override val viewModel by viewModels<ProductListViewModel>()
  private val productListAdapter by lazy { ProductListAdapter(this::toDetails) }
  private val category by lazy { "men's clothing" }
  private var isProductSaved = false
  private var isAllProductsSaved = false
  private var isCategorySaved = false
  private var cartCount = 0
  
  
  override fun initView() {
    binding.rvProducts.adapter = productListAdapter
    initTopAppBarNavigation()
    initTopAppBarTitle(category)
  }
  
  override fun initListeners() {}
  
  @RequiresApi(Build.VERSION_CODES.S)
  override fun initRequest() {
    
    /*
    viewModel.getCategories()
    viewModel.isCategoriesSaved()*/
    viewModel.getAllCartTotalValue()
    viewModel.getAllProducts()
    viewModel.isALlProductsSaved()/*
   
   
    category.let { viewModel.getProductsByCategory(it) }
    viewModel.isProductsByCategorySaved()*/
    
  }
  
  override fun onResume() {
    super.onResume()
    viewModel.getAllCartTotalValue()
  }
  
  override fun initSubscribers() {
    viewModel.categoriesState.spectateUiState(success = {
      if (!isCategorySaved) {
        viewModel.createCategories(it)
      }
    })
    
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
      CALL_DISMISS_KEY
    )?.observe(viewLifecycleOwner) {
      it?.let {
        this@ProductListFragment.onResume()
      }
    }
    
    viewModel.productsState.spectateUiState(success = {
      Log.d("TAN_LOG/PRODUCTS_SIZE", " ${it.size}")
      
      productListAdapter.setData(it)
      if (!isAllProductsSaved) {
        Log.d("TAN_LOG/isProductSaved", "$isProductSaved")
        Log.d("TAN_LOG/PRODUCTS_LIST", " $it")
        viewModel.createProducts(it, category)
      }
    })
    
    viewModel.isProductsByCategorySavedState.spectateUiState(success = {
      Log.d("TAN_LOG/isProductsByCategorySavedState", "isProductsByCategorySavedState: $it")
      isProductSaved = it
      Log.d("TAN_LOG/isProductSaved2", "222$isProductSaved")
    })
    
    viewModel.isAllProductsSavedState.spectateUiState(success = {
      Log.d("TAN_LOG/isAllProductsSavedState", "isAllProductsSavedState: $it")
      isAllProductsSaved = it
    })
    
    viewModel.isCategoriesSavedState.spectateUiState(success = {
      isCategorySaved = it
    })
    
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.isAllCartProductsTotalValue.collect { uiState ->
        when (uiState) {
          is UIState.Success -> {
            setCartCount(uiState.data)
            Log.d("DFASSFFSAFS", "initSubscribers: ${uiState.data}")
          }
          
          is UIState.Error -> (activity as MainActivity).progressLineVisible(false)
          is UIState.Idle -> (activity as MainActivity).progressLineVisible(false)
          is UIState.Loading -> (activity as MainActivity).progressLineVisible(false)
        }
      }
    }
  }
  
  private fun toDetails(model: ProductDto) {
    this@ProductListFragment.onPause()
    findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToProductDetailBottomSheetDialogFragment(model))
  }
  
  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    viewModel.getAllCartTotalValue()
  }
  private fun initTopAppBarTitle(category: String) {
    val bigText = PRODUCTS
    val spannable = SpannableString(bigText)
    
    (activity as MainActivity).setToolbarTitle(spannable, category)
  }
  
  private fun initTopAppBarNavigation() {
    (activity as MainActivity).apply {
      supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setHomeActionContentDescription(R.string.category)
        setHomeAsUpIndicator(R.drawable.ic_category_fill)
      }
      setToolbarTitle(getString(R.string.products))
    }
    
    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.cart_action, menu)
      }
      
      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
          android.R.id.home -> {
            Log.d("ProductListFragment", getString(R.string.category))
          }
          
          R.id.cart -> {
            
            findNavController().navigate(
              ProductListFragmentDirections
                .actionProductListFragmentToProductsCartFragment()
            )
          }
          
          else -> return true
        }
        return false
      }
      
      override fun onPrepareMenu(menu: Menu) {
        val icon = menu.findItem(R.id.cart).icon
        val badgeDrawable = if (icon is LayerDrawable) {
          icon
        } else {
          val layers = arrayOfNulls<Drawable>(2)
          layers[0] = icon
          layers[1] = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cart_fill)
          LayerDrawable(layers.requireNoNulls())
        }
        
        val badge: CountDrawable =
          (badgeDrawable.findDrawableByLayerId(R.id.ic_group_count) as? CountDrawable) ?: CountDrawable(requireContext())
        badge.setCount(cartCount.toString())
        badgeDrawable.mutate()
        badgeDrawable.setDrawableByLayerId(R.id.ic_group_count, badge)
        menu.findItem(R.id.cart).icon = badgeDrawable
        return super.onPrepareMenu(menu)
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }
  
  
  private fun setCartCount(count: Int) {
    cartCount = count
    requireActivity().invalidateOptionsMenu()
  }
}