package com.leslie.socialink.login.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.me.repository.UserInfoRepository
import com.leslie.socialink.network.Constants
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.util.AuthorizationInterceptor
import com.leslie.socialink.utils.SharedPreferencesHelp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val atomicInteger = AtomicInteger(0)
    private val _loginDialog: MutableSharedFlow<Pair<Int, String>> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1
    )
    val logoutDialog = _loginDialog.asSharedFlow()

    private val _logoutResult: MutableSharedFlow<Boolean> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1
    )
    val logoutResult = _logoutResult.asSharedFlow()

    fun onAction(loginAction: LoginAction) = with(loginAction) {
        when (this) {
            is LoginAction.RequestLogin -> viewModelScope.launch {
                val loginResult = RetrofitClient.userService.login(
                    phone = phone,
                    pwd = password
                )
                val loginSuccessResult = loginResult.data
                if (loginSuccessResult == null) {
                    loginResult.msg?.let {
                        _loginDialog.emit(Pair(atomicInteger.incrementAndGet(), it))
                    }
                    return@launch
                }
                Log.i(TAG, "loginSuccessResult: $loginSuccessResult")
                SharedPreferencesHelp.editor
                    .putString("phone", phone)
                    .putString("token", loginSuccessResult.token)
                    .putInt("uid", loginSuccessResult.uid)
                    .putBoolean("isLogin", true)
                    .apply()
                Constants.uid = loginSuccessResult.uid
                Constants.token = loginSuccessResult.token
                AuthorizationInterceptor.cacheToken(loginSuccessResult.token ?: "")
                userInfoRepository.reFetchUserInfo()
                _logoutResult.emit(true)
            }

            else -> {}
        }
    }

    companion object {
        private const val TAG = "[LoginViewModel]"
    }
}