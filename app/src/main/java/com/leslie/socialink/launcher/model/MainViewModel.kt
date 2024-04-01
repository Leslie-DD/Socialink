package com.leslie.socialink.launcher.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.network.Constants
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.UserInfoBean
import com.leslie.socialink.utils.SharedPreferencesHelp
import kotlinx.coroutines.launch

//@HiltViewModel
class MainViewModel /*@Inject*/ constructor() : ViewModel() {

    init {
        viewModelScope.launch {
            val uid = SharedPreferencesHelp.getInt("uid", 1);
            val userInfoBean: UserInfoBean? = RetrofitClient.userService.info(
                SharedPreferencesHelp.getInt("uid", 1).toString()
            ).data
            Constants.uid = uid
            Constants.token = SharedPreferencesHelp.getString("token", "")
            Log.i(TAG, "$userInfoBean \n\t token: ${Constants.token}")
        }
    }

    fun initialize() {

    }

    companion object {
        const val TAG = "[MainViewModel]"
    }
}