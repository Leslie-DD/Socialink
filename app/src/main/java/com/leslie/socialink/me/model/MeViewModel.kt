package com.leslie.socialink.me.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.me.repository.UserInfoRepository
import com.leslie.socialink.network.entity.UserInfoBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    var userInfoBean: UserInfoBean? = null
    private val atomicInteger = AtomicInteger(0)

    private val _logoutDialog: MutableSharedFlow<Int> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1
    )
    val logoutDialog = _logoutDialog.asSharedFlow()

    private val _goToLogin: MutableSharedFlow<Int> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1
    )
    val goToLogin = _goToLogin.asSharedFlow()

    val userInfoFlow: Flow<UserInfoBean?>
        get() = userInfoRepository.userInfoFlow

    init {
        viewModelScope.launch {
            userInfoFlow.collect {
                userInfoBean = it
            }
        }
    }

    fun reFetchUserInfo() = userInfoRepository.reFetchUserInfo()

    fun onAction(action: MeFragmentAction) = with(action) {
        Log.i(TAG, "onAction $action")
        when (this) {
            MeFragmentAction.HeaderClick -> {
                val userInfo = userInfoBean
                if (userInfo == null || userInfo.id == 1) {
                    logout()
                } else {

                }
            }

            MeFragmentAction.LoginOrOut -> {
                val userInfo = userInfoBean
                if (userInfo == null || userInfo.id == 1) {
                    logout()
                } else {
                    _logoutDialog.tryEmit(atomicInteger.incrementAndGet())
                }
            }

            MeFragmentAction.Logout -> logout()

            else -> {}
        }
    }

    private fun logout() {
        Log.i(TAG, "logout")
        userInfoRepository.clearUserInfo()
        _goToLogin.tryEmit(atomicInteger.incrementAndGet())
//        SocialinkApplication.getInstance().finishAll()
//        appCtx.startActivity(Intent(appCtx, LoginActivity::class.java))
    }

    companion object {
        private const val TAG = "[MeViewModel]"
    }

}