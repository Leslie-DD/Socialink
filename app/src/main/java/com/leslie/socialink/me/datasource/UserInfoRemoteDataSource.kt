package com.leslie.socialink.me.datasource

import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.UserInfoBean
import com.leslie.socialink.utils.SharedPreferencesHelp
import javax.inject.Inject

class UserInfoRemoteDataSource @Inject constructor() {

    suspend fun fetchUserInfo(): UserInfoBean? {
        val uid = SharedPreferencesHelp.getInt("uid", 1);
        val userInfoBean = RetrofitClient.userService.info(uid.toString()).data
        userInfoBean?.money = RetrofitClient.userService.money(uid.toString()).data ?: 0
        return userInfoBean
    }

    companion object {
        const val TAG = "[UserInfoRemoteRepository]"
    }
}