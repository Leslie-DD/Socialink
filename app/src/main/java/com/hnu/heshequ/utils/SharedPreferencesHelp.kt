package com.hnu.heshequ.utils

import android.content.Context
import android.content.SharedPreferences
import splitties.init.appCtx

object SharedPreferencesHelp {

    private val sharedPreferences: SharedPreferences by lazy {
        appCtx.getSharedPreferences("meet", Context.MODE_PRIVATE)
    }

    private val _editor: SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
    }

    @JvmStatic
    public val editor: SharedPreferences.Editor
        get() = _editor

    @JvmStatic
    @JvmOverloads
    public fun getString(key: String, defValue: String? = null): String? = sharedPreferences.getString(key, defValue)

    @JvmStatic
    @JvmOverloads
    public fun getInt(key: String, defValue: Int = 0): Int = sharedPreferences.getInt(key, defValue)
}