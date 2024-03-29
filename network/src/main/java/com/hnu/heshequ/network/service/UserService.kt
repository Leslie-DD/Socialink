package com.hnu.heshequ.network.service

import com.hnu.heshequ.network.entity.UnifyResponse
import com.hnu.heshequ.network.entity.UserInfoBean
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("/xiangyu/api/user/info.do")
    suspend fun info(
        @Query("uid") uid: String
    ): UnifyResponse<UserInfoBean>
}