package com.hnu.heshequ.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnu.heshequ.network.Constants
import com.hnu.heshequ.network.RetrofitClient
import com.hnu.heshequ.network.entity.HotActivities.HotBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotActivitiesViewModel : ViewModel() {
    private val _hotActivitiesBeanStateFlow = MutableStateFlow<List<HotBean>>(emptyList())
    val hotActivitiesBeanStateFlow = _hotActivitiesBeanStateFlow.asStateFlow()

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
        RetrofitClient.homeService.hotActivities(
            pn = pn.toString(),
            ps = Constants.default_PS.toString()
        ).data?.let {
            totalPage = it.totalPage
            if (it.data.isNotEmpty()) {
                _hotActivitiesBeanStateFlow.value = if (refresh) {
                    it.data
                } else {
                    hotActivitiesBeanStateFlow.value + it.data
                }
            }
        }
        _loadFinish.value = loadFinish.value + 1
    }

    companion object {
        private const val TAG = "[HotActivitiesViewModel]"
    }

}