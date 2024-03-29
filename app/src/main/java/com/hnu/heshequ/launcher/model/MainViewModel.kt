package com.hnu.heshequ.launcher.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnu.heshequ.network.RetrofitClient
import com.hnu.heshequ.network.entity.UserInfoBean
import com.hnu.heshequ.utils.SharedPreferencesHelp
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