package com.hnu.heshequ.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnu.heshequ.network.Constants
import com.hnu.heshequ.network.RetrofitClient
import com.hnu.heshequ.network.entity.TeamBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotTeamsViewModel : ViewModel() {
    private val _hotTeamsBeanStateFlow = MutableStateFlow<List<TeamBean>>(emptyList())
    val hotTeamsBeanStateFlow = _hotTeamsBeanStateFlow.asStateFlow()

    private val _loadFinish = MutableStateFlow(1)
    val loadFinish = _loadFinish.asStateFlow()

    private var pn = 0
    private var totalPage = 0

    init {
        fetchData(refresh = true)
    }

    fun fetchData(refresh: Boolean = false) = viewModelScope.launch(Dispatchers.IO) {
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
        RetrofitClient.homeService.hotTeams(
            type = "1",
            pn = pn.toString(),
            ps = Constants.default_PS.toString()
        ).data?.let {
            totalPage = it.totalPage
            if (it.data.isEmpty()) {
                return@launch
            }
            it.data.forEach { teamBean -> teamBean.itemType = 1 }
            _hotTeamsBeanStateFlow.value = if (refresh) {
                it.data
            } else {
                hotTeamsBeanStateFlow.value + it.data
            }

        }
        _loadFinish.value = loadFinish.value + 1
    }

    companion object {
        private const val TAG = "[HotActivitiesViewModel]"
    }
}