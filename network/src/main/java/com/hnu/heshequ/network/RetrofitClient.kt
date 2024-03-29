package com.hnu.heshequ.network

import android.util.Log
import com.hnu.heshequ.network.service.HomeService
import com.hnu.heshequ.network.service.UserService
import com.hnu.heshequ.network.util.AuthorizationInterceptor
import com.hnu.heshequ.network.util.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://182.92.84.79:8081/"
//    private const val BASE_URL = "http://8.138.85.81:6000/"

    private val authorizedOkHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .configNetworkInterceptor()
            .addInterceptor(LoggingInterceptor())
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authorizedOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val homeService: HomeService by lazy { retrofit.create(HomeService::class.java) }

    fun OkHttpClient.Builder.configNetworkInterceptor(): OkHttpClient.Builder {
        addNetworkInterceptor(HttpLoggingInterceptor { message -> Log.i("Http logging", "$message.") }
            .apply { setLevel(HttpLoggingInterceptor.Level.HEADERS) })
        return this
    }


}