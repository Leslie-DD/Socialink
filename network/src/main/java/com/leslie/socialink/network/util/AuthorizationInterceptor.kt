package com.leslie.socialink.network.util

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import splitties.init.appCtx
import java.io.IOException

class AuthorizationInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .header(HEADER_TOKEN, token)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    companion object {
        private const val HEADER_TOKEN = "XIANGYU-ACCESS-TOKEN"

        private var _token: String? = null
        private val token: String
            get() {
                return _token ?: (appCtx.getSharedPreferences(
                    "socialink",
                    Context.MODE_PRIVATE
                ).getString("token", "") ?: "").also {
                    _token = it
                }
            }

        @JvmStatic
        fun cacheToken(token: String) {
            this._token = token
        }
    }
}
