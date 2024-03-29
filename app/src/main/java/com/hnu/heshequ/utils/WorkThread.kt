package com.hnu.heshequ.utils

import android.os.HandlerThread
import android.os.Looper

class WorkThread {
    companion object {
        @JvmStatic
        val workLooper: Looper by lazy {
            HandlerThread("socialink-work-th").run {
                start()
                looper
            }
        }
    }
}