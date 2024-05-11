package com.deveem.dmarket.core.extention

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.Serializable

fun Fragment.toastShort(strId: Int) {
  context?.let { context ->
    Toast.makeText(context, strId, Toast.LENGTH_SHORT).show()
  }
}

fun Fragment.toastShort(msg: String?) {
  context?.let { context ->
    msg?.let {
      Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
  }
}

fun Fragment.toastLong(msg: String?) {
  context?.let { context ->
    msg?.let {
      Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }
  }
}

fun Context.getColorCompat(@ColorRes color: Int) =
  ContextCompat.getColor(this, color)

fun Context.getDrawableIcon(drawableId: Int): Drawable? {
  return ContextCompat.getDrawable(this, drawableId)
}

fun Int.dpToPx(): Int {
  val scale = Resources.getSystem().displayMetrics.density
  return (this * scale + 0.5f).toInt()
}

fun spToPx(sp: Number): Int {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp.toFloat(),
    Resources.getSystem().displayMetrics
  ).toInt()
}

fun IntRange.random() =
  (Math.random() * ((endInclusive + 1) - start) + start).toInt()

fun <T : Serializable?> getSerializableExt(bundle: Bundle, name: String, clazz: Class<T>): T
{
  return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    bundle.getSerializable(name, clazz)!!
  else
    bundle.getSerializable(name) as T
}