package com.hnu.heshequ.network.service

import com.hnu.heshequ.network.entity.UnifyResponse
import com.hnu.heshequ.network.entity.UserInfoBean
import com.hnu.heshequ.network.util.ResponseHandler
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("/xiangyu/api/user/info.do")
//    @ResponseHandler
    suspend fun info(
        @Query("uid") uid: String
    ): UnifyResponse<UserInfoBean>
}