package com.deveem.dmarket.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.deveem.dmarket.R

class CountDrawable(context: Context) : Drawable() {
  
  private val mBadgePaint: Paint = Paint()
  private val mTextPaint: Paint = Paint()
  private val mTxtRect = Rect()
  
  private var mCount: String = ""
  private var mWillDraw: Boolean = false
  
  init {
    val mTextSize = context.resources.getDimension(R.dimen.text_tiny)
    
    mBadgePaint.color = ContextCompat.getColor(context.applicationContext, R.color.red)
    mBadgePaint.isAntiAlias = true
    mBadgePaint.style = Paint.Style.FILL
    
    mTextPaint.color = Color.WHITE
    mTextPaint.typeface = Typeface.DEFAULT
    mTextPaint.textSize = mTextSize
    mTextPaint.isAntiAlias = true
    mTextPaint.textAlign = Paint.Align.CENTER
  }
  
  override fun draw(canvas: Canvas) {
    if (!mWillDraw) {
      return
    }
    val bounds: Rect = bounds
    val width: Float = (bounds.right - bounds.left).toFloat()
    val height: Float = (bounds.bottom - bounds.top).toFloat()
    val radius: Float = (width.coerceAtLeast(height) / 2) / 2
    val centerX: Float = (width - radius - 1) + 5
    val centerY: Float = radius - 5
    if (mCount.length <= 2) {
      canvas.drawCircle(centerX, centerY, (radius + 5.5).toInt().toFloat(), mBadgePaint)
    } else {
      canvas.drawCircle(centerX, centerY, (radius + 6.5).toInt().toFloat(), mBadgePaint)
    }
    
    mTextPaint.getTextBounds(mCount, 0, mCount.length, mTxtRect)
    val textHeight: Float = (mTxtRect.bottom - mTxtRect.top).toFloat()
    val textY: Float = centerY + (textHeight / 2f)
    if (mCount.length > 2)
      canvas.drawText("99+", centerX, textY, mTextPaint)
    else
      canvas.drawText(mCount, centerX, textY, mTextPaint)
  }
  
  fun setCount(count: String) {
    mCount = count
    mWillDraw = !count.equals("0", ignoreCase = true)
    invalidateSelf()
  }
  
  override fun setAlpha(alpha: Int) {
    // do nothing
  }
  
  override fun setColorFilter(cf: android.graphics.ColorFilter?) {
    // do nothing
  }
  
  override fun getOpacity(): Int {
    return android.graphics.PixelFormat.TRANSLUCENT
  }
}
