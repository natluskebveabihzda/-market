package com.deveem.dmarket.core.extention

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.FragmentActivity
import com.deveem.dmarket.MainActivity
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

fun View.setupBlurView(activity: FragmentActivity, contextBlur: Context) {
  val decorView = (activity as MainActivity).window.decorView
  val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
  val windowBackground = decorView.background
  
  (this as? BlurView)?.apply {
    setupWith(rootView, RenderScriptBlur(contextBlur))
      .setFrameClearDrawable(windowBackground)
      .setBlurRadius(25f)
      .setBlurAutoUpdate(true)
      .setBlurEnabled(true)
    outlineProvider = ViewOutlineProvider.BACKGROUND
  }
}