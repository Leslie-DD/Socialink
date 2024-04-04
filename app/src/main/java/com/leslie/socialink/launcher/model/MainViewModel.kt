package com.leslie.socialink.launcher.model

import androidx.lifecycle.ViewModel
import com.leslie.socialink.me.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    init {
//        viewModelScope.launch {
//            val uid = SharedPreferencesHelp.getInt("uid", 1);
//            val userInfoBean: UserInfoBean? = RetrofitClient.userService.info(
//                SharedPreferencesHelp.getInt("uid", 1).toString()
//            ).data
//            Constants.uid = uid
//            Constants.token = SharedPreferencesHelp.getString("token", "")
//            Log.i(TAG, "$userInfoBean \n\t token: ${Constants.token}")
//        }
    }

    fun initialize() {

    }

    companion object {
        const val TAG = "[MainViewModel]"
    }
}