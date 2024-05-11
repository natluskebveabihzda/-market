package com.deveem.dmarket

import android.os.Bundle
import android.text.Spannable
import android.view.Gravity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.deveem.dmarket.data.SharedPreferences
import com.deveem.dmarket.data.local.Constants.PRODUCTS
import com.deveem.dmarket.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  @Inject lateinit var sharedPreferences: SharedPreferences
  private lateinit var appBarConfiguration: AppBarConfiguration
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    enableEdgeToEdge(SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT))
    setContentView(binding.root)
    with(binding) {
      toolbar.apply {
        setSupportActionBar(this)
        setContentInsetStartWithNavigation(0)
        contentInsetEndWithActions = 0
        progress.hide()
        
      }
    }
    
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
    appBarConfiguration = AppBarConfiguration(navController.graph)
    toolbarState(navController)
    setupActionBarWithNavController(navController, appBarConfiguration)
  }
  
  private fun toolbarState(navController: NavController) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
      if (destination.id == R.id.productsCartFragment) {
        binding.collapsingToolbar.collapsedTitleGravity = Gravity.START
        binding.toolbar.titleMarginStart = dpToPx(12f, resources.displayMetrics).toInt()
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_arrow_back)
      } else {
        with(binding) {
          collapsingToolbar.collapsedTitleGravity = Gravity.CENTER
          val initialX = tvCategoryBottomUnderTitle.x
          appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offset = (appBarLayout.y / appBar.totalScrollRange)
            tvCategoryBottomUnderTitle.setAlpha(1 - (offset * - 1))
            
         
            val range = binding.root.width - tvCategoryBottomUnderTitle.width - 100
            val newX = initialX + 180 - offset * range / 2.toFloat()
            tvCategoryBottomUnderTitle.x = newX
            
            val isCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange
            
           
            if (isCollapsed) {
              appBarLayout.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.gray)
              tvCategoryTopUnderTitle.isVisible = true
              tvCategoryBottomUnderTitle.isVisible = false
            } else {
              appBarLayout.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.platinum)
              tvCategoryTopUnderTitle.isVisible = false
              tvCategoryBottomUnderTitle.isVisible = true
            }
          }
        }
      }
    }
  }
  
  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.navigation_host_fragment)
    return navController.navigateUp(appBarConfiguration)
      || super.onSupportNavigateUp()
  }
  
  fun setToolbarTitle(title: Spannable, category: String) {
    binding.collapsingToolbar.title = title
    binding.tvCategoryTopUnderTitle.text = category
    binding.tvCategoryBottomUnderTitle.text = category
  }
  
  fun setToolbarTitle(title: String) {
    binding.collapsingToolbar.title = title
  }
  
  fun progressLineVisible(visible: Boolean) {
    if (visible) {
      binding.progress.show()
    } else {
      binding.progress.hide()
    }
  }
  
}