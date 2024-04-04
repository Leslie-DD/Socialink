package com.leslie.socialink.me.datasource

import com.leslie.socialink.network.Constants
import com.leslie.socialink.network.entity.UserInfoBean
import com.leslie.socialink.network.util.AuthorizationInterceptor
import com.leslie.socialink.utils.SharedPreferencesHelp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UserInfoLocalDataSource @Inject constructor() {

    private var _userInfoBeanState: MutableStateFlow<UserInfoBean?> = MutableStateFlow(null)
    var userInfoBeanState = _userInfoBeanState.asStateFlow()

    fun setUserInfoBean(userInfoBean: UserInfoBean) {
        Constants.uid = userInfoBean.id
        Constants.token = SharedPreferencesHelp.getString("token", "")
        AuthorizationInterceptor.cacheToken(token = Constants.token)
        _userInfoBeanState.value = userInfoBean
    }

    fun clearUserInfo() {
        SharedPreferencesHelp.editor
            .putString("phone", "")
            .putString("token", "")
            .putInt("uid", 1)
            .putBoolean("isLogin", false)
            .apply()
        SharedPreferencesHelp.editor
            .putBoolean("isLogin", false)
            .apply()

        Constants.uid = 1
        Constants.token = ""
        _userInfoBeanState.value = null
    }


}