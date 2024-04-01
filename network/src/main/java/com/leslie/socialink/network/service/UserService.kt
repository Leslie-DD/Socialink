package com.leslie.socialink.network.service

import com.leslie.socialink.network.entity.UnifyResponse
import com.leslie.socialink.network.entity.UserInfoBean
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("/xiangyu/api/user/info.do")
    suspend fun info(
        @Query("uid") uid: String
    ): UnifyResponse<UserInfoBean>
}