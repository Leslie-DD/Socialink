package com.leslie.socialink.network.service

import com.leslie.socialink.network.entity.LoginSuccessResult
import com.leslie.socialink.network.entity.UnifyResponse
import com.leslie.socialink.network.entity.UserInfoBean
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {


    @POST("/xiangyu/api/account/login.do")
    suspend fun login(
        @Query("phone") phone: String,
        @Query("pwd") pwd: String
    ): UnifyResponse<LoginSuccessResult>

    @POST("/xiangyu/api/user/info.do")
    suspend fun info(
        @Query("uid") uid: String
    ): UnifyResponse<UserInfoBean>

    @POST("/xiangyu/api/user/cornAmount.do")
    suspend fun money(
        @Query("uid") uid: String
    ): UnifyResponse<Int>
}