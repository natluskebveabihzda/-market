package com.deveem.dmarket.data

import android.content.SharedPreferences
import com.deveem.dmarket.data.local.PreferencesKey
import com.deveem.dmarket.core.extention.put
import javax.inject.Inject

class SharedPreferences @Inject constructor(
  private val sharedPreferences: SharedPreferences
) {
  
  var app: String?
    get() = sharedPreferences.getString(PreferencesKey.DMARKET_SHARED_PREFERENCES, "")
    set(value) = sharedPreferences.put(PreferencesKey.DMARKET_SHARED_PREFERENCES, value.toString())
}