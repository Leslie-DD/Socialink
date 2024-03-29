package com.hnu.heshequ.network.util

import com.google.gson.Gson

object Gsons {
    private val _gson: Gson by lazy {
        Gson()
    }

    @JvmStatic
    val gson: Gson
        get() = _gson
}