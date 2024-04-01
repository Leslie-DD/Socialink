package com.leslie.socialink.questions.model

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.QuestionBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionsChildViewModel : ViewModel() {

    private val _teamsBeanStateFlow = MutableStateFlow<List<QuestionBean>>(emptyList())
    val teamsBeanStateFlow = _teamsBeanStateFlow.asStateFlow()

    private val _likeResultErrorMessage = MutableStateFlow<Pair<Int, String?>>(Pair(0, null))
    val likeResultErrorMessage = _likeResultErrorMessage.asStateFlow()

    private val _updatedQuestion = MutableStateFlow<Triple<Int, Int, QuestionBean?>>(Triple(0, -1, null))
    val updatedQuestion = _updatedQuestion.asStateFlow()

    private val _loadFinish = MutableStateFlow(1)
    val loadFinish = _loadFinish.asStateFlow()

    private var pn = 0
    private var totalPage = 0

    private var updateCount = 0

    private var initialized = false

    fun initData(type: String) {
        if (initialized) {
            return
        }
        fetchData(refresh = true, type = type)
        initialized = true
    }

    fun fetchData(
        refresh: Boolean = false,
        type: String = "1"
    ) = viewModelScope.launch(Dispatchers.IO) {
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
        RetrofitClient.homeService.hotQuestions(
            type = type,
            pn = pn.toString(),
            ps = "20"
        ).data?.let {
            totalPage = it.totalPage
            if (it.data.isEmpty()) {
                return@launch
            }
            _teamsBeanStateFlow.value = if (refresh) {
                it.data
            } else {
                teamsBeanStateFlow.value + it.data
            }

        }
        _loadFinish.value = loadFinish.value + 1
    }

    fun like(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        val questions = teamsBeanStateFlow.value
        if (position < 0 || position >= questions.size) {
            return@launch
        }

        val result = RetrofitClient.homeService.questionLike(
            id = questions[position].id
        )
        _likeResultErrorMessage.value = Pair(likeResultErrorMessage.value.first + 1, result.msg)

        val targetQuestion = questions[position]
        if (result.code == 0) {
            var zan = targetQuestion.likeAmount
            if (TextUtils.isEmpty(targetQuestion.userLike)) {
                targetQuestion.userLike = "1"
                zan += 1
            } else {
                targetQuestion.userLike = ""
                zan -= 1
            }
            targetQuestion.likeAmount = zan

            updateCount += 1
            _updatedQuestion.value = Triple(updateCount, position, targetQuestion)
        }
    }

    fun clearLike() {
        _likeResultErrorMessage.value = Pair(0, null)
    }

    companion object {
        private const val TAG = "[HotQuestionsViewModel]"
    }
}