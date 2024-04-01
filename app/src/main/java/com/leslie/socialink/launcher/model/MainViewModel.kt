package com.leslie.socialink.launcher.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.UserInfoBean
import com.leslie.socialink.utils.SharedPreferencesHelp
import kotlinx.coroutines.launch

//@HiltViewModel
class MainViewModel /*@Inject*/ constructor() : ViewModel() {

    init {
        viewModelScope.launch {
            val userInfoBean: UserInfoBean? = RetrofitClient.userService.info(
                SharedPreferencesHelp.getInt("uid", 1).toString()
            ).data
            Log.i(TAG, "userInfoBean: $userInfoBean")
        }
    }

    public fun initialize() {

    }

    companion object {
        const val TAG = "[MainViewModel]"
    }
}