package com.hnu.heshequ.utils

import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager

object StatusBarUtil {

    private const val TAG = "[StatusBarUtil]"

    @JvmStatic
    fun hackInStatusBar(activity: Activity) {
        val window = activity.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = Color.TRANSPARENT
    }

    @JvmStatic
    @JvmOverloads
    fun hackOutStatusBar(activity: Activity, color: Int = Color.WHITE) {
        val window = activity.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = color
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : MarginLayoutParams> setMarginStatusBar(viewGroup: ViewGroup) {
        val layoutParams = viewGroup.layoutParams as T
        layoutParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0)
        viewGroup.setLayoutParams(layoutParams)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : MarginLayoutParams, V : ViewGroup> V.setMarginStatusBarKt() {
        val lp = layoutParams as T
        lp.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0)
        setLayoutParams(lp)
    }
}