package com.leslie.socialink.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.HomeBanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    private val _homeBannersStateFlow = MutableStateFlow<List<HomeBanner>>(emptyList())
    val homeBannersStateFlow = _homeBannersStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val homeBanners = RetrofitClient.homeService.advertisement("1").data
            homeBanners?.let {
                _homeBannersStateFlow.value = it
            }
        }
    }

}