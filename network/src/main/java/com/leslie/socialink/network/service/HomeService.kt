package com.leslie.socialink.network.service

import com.leslie.socialink.network.entity.HomeBanner
import com.leslie.socialink.network.entity.HotActivities
import com.leslie.socialink.network.entity.HotQuestions
import com.leslie.socialink.network.entity.HotTeams
import com.leslie.socialink.network.entity.UnifyResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeService {

    @POST("/xiangyu/api/pub/category/advertisement.do")
    suspend fun advertisement(
        @Query("category") category: String
    ): UnifyResponse<List<HomeBanner>>

    @POST("/xiangyu/api/club/activity/hotlist.do")
    suspend fun hotActivities(
        @Query("pn") pn: String,
        @Query("ps") ps: String
    ): UnifyResponse<HotActivities>

    @POST("/xiangyu/api/club/base/pglist.do")
    suspend fun teams(
        @Query("type") type: String,
        @Query("pn") pn: String,
        @Query("ps") ps: String
    ): UnifyResponse<HotTeams>

    @POST("/xiangyu/api/ask/base/pglist.do")
    suspend fun hotQuestions(
        @Query("type") type: String,
        @Query("pn") pn: String,
        @Query("ps") ps: String
    ): UnifyResponse<HotQuestions>

    @POST("/xiangyu/api/ask/base/likes.do")
    suspend fun questionLike(
        @Query("id") id: String,
    ): UnifyResponse<String>
}