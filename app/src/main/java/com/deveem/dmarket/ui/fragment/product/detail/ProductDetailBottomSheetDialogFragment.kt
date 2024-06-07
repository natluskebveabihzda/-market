package com.deveem.dmarket.ui.fragment.product.detail

import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import com.deveem.dmarket.R
import com.deveem.dmarket.core.base.BaseBottomSheetDialogFragment
import com.deveem.dmarket.databinding.BottomSheetDialogFragmentProductDetailBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.internal.ViewUtils.dpToPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailBottomSheetDialogFragment :
  BaseBottomSheetDialogFragment<BottomSheetDialogFragmentProductDetailBinding, ProductDetailViewModel>(R.layout.bottom_sheet_dialog_fragment_product_detail) {
  override fun inflateBinding() = BottomSheetDialogFragmentProductDetailBinding.inflate(layoutInflater)
  override val viewModel by viewModels<ProductDetailViewModel>()
  private val navArgs: ProductDetailBottomSheetDialogFragmentArgs by navArgs()
  
  override fun initView() {
    viewModel.isThisProductInCardSaved(navArgs.product)
    binding.ivProduct.load(navArgs.product.image) {
      memoryCachePolicy(CachePolicy.DISABLED)
    }
    ratingBar(navArgs.product.rating.rate.toFloat())
    binding.btnCart.setOnClickListener {
      viewModel.addProductToCart(navArgs.product)
    }
    Log.d("BottomSheetFragmentProductDetail", "Data: ${navArgs.product}")
  }
  
  override fun initSubscribers() {
    viewModel.isThisProductSavedInCard.spectateUiState(success = {
      if (it) {
        binding.btnCart.text = getString(R.string.go_to_cart)
        binding.btnCart.setIconResource(R.drawable.ic_cart_go)
        binding.btnCart.setIconTintResource(R.color.white)
        binding.btnCart.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.btnCart.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.black)
      } else {
        binding.btnCart.setIconResource(R.drawable.ic_cart_add)
        binding.btnCart.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        binding.btnCart.strokeWidth = 4
        binding.btnCart.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.black)
        binding.btnCart.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.btnCart.setIconTintResource(R.color.black)
        binding.btnCart.text = getString(R.string.add_to_cart)
      }
    })
  }
  private fun ratingBar(rate: Float) {
    val ratingBar = binding.ratingBar
    for (i in 0 until 5) {
      val star = ratingBar.getChildAt(i) as MaterialButton
      when {
        i < rate.toInt() -> star.setIconResource(R.drawable.ic_star_fill)
        i == rate.toInt() && rate % 1 != 0f -> star.setIconResource(R.drawable.ic_star_half)
        else -> star.setIconResource(R.drawable.ic_star_empty)
      }
    }
  }
}
