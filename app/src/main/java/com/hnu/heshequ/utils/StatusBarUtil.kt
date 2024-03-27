package com.hnu.heshequ.utils

import android.app.Activity
import android.graphics.Color
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
}