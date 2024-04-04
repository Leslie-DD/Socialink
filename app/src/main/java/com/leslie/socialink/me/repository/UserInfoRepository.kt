package com.leslie.socialink.me.repository

import com.leslie.socialink.me.datasource.UserInfoLocalDataSource
import com.leslie.socialink.me.datasource.UserInfoRemoteDataSource
import com.leslie.socialink.network.entity.UserInfoBean
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class UserInfoRepository(
    private val localDataSource: UserInfoLocalDataSource,
    private val remoteDataSource: UserInfoRemoteDataSource
) {

    init {
        fetchUserInfo()
    }

    val userInfoFlow: Flow<UserInfoBean?>
        get() = localDataSource.userInfoBeanState

    private fun fetchUserInfo() = GlobalScope.launch {
        remoteDataSource.fetchUserInfo()?.let {
            localDataSource.setUserInfoBean(userInfoBean = it)
        }
    }

    fun reFetchUserInfo() = fetchUserInfo()

    fun clearUserInfo() = localDataSource.clearUserInfo()

}