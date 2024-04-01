package com.leslie.socialink.team.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.network.Constants
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.TeamBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamsChildViewModel : ViewModel() {
    private val _teamsBeanStateFlow = MutableStateFlow<List<TeamBean>>(emptyList())
    val teamsBeanStateFlow = _teamsBeanStateFlow.asStateFlow()

    private val _loadFinish = MutableStateFlow(1)
    val loadFinish = _loadFinish.asStateFlow()

    private var pn = 0
    private var totalPage = 0

    private var initialized = false

    fun initData(type: String) {
        if (initialized) {
            return
        }
        fetchData(refresh = true, type = type)
        initialized = true
    }

    fun fetchData(refresh: Boolean = false, type: String = "1") = viewModelScope.launch(Dispatchers.IO) {
        Log.w(TAG, "(fetchData) refresh ? $refresh, pn = $pn, totalPage = $totalPage")
        if (!refresh && pn >= totalPage) {
            _loadFinish.value = loadFinish.value + 1
            Log.w(TAG, "fetchData: no more data")
            return@launch
        }
        if (refresh) {
            pn = 1
        } else {
            pn += 1
        }
        RetrofitClient.homeService.teams(
            type = type,
            pn = pn.toString(),
            ps = Constants.DEFAULT_PS.toString()
        ).data?.let {
            totalPage = it.totalPage
            if (it.data.isEmpty()) {
                return@launch
            }
            it.data.forEach { teamBean -> teamBean.itemType = 1 }
            _teamsBeanStateFlow.value = if (refresh) {
                it.data
            } else {
                teamsBeanStateFlow.value + it.data
            }

        }
        _loadFinish.value = loadFinish.value + 1
    }

    companion object {
        private const val TAG = "[HotActivitiesViewModel]"
    }
}