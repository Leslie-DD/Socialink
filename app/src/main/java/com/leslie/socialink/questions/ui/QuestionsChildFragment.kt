package com.leslie.socialink.questions.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener
import com.leslie.socialink.R
import com.leslie.socialink.bean.ConsTants
import com.leslie.socialink.home.ui.IListFragment
import com.leslie.socialink.questions.adapter.QuestionsChildAdapter
import com.leslie.socialink.questions.model.QuestionsChildViewModel
import com.leslie.socialink.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class QuestionsChildFragment : Fragment(), IListFragment {
    private lateinit var view: View
    private lateinit var tvTips: TextView

    private lateinit var recyclerView: XRecyclerView
    private var adapter: QuestionsChildAdapter? = null

    private val type: String by lazy { arguments?.getString("type") ?: "1" }

    private val viewModel: QuestionsChildViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        view = inflater.inflate(R.layout.fragment_tim, container, false)
        tvTips = view.findViewById(R.id.tvTips)
        adapter = QuestionsChildAdapter(activity)
        recyclerView = view.findViewById(R.id.rv)
        ConsTants.initXRecycleView(activity, true, false, recyclerView)
        recyclerView.setLoadingListener(object : LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    viewModel.fetchData(refresh = false, type = type)
                }
            }
        })
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        adapter?.setListener { position ->
            viewModel.like(position)
        }
        initCollect()
        viewModel.initData(type = type)
        return view
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.teamsBeanStateFlow.collect {
                tvTips.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
                recyclerView.refreshComplete()
                adapter?.setData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadFinish.collect {
                recyclerView.loadMoreComplete()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.clearLike()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.likeResultErrorMessage
                .filter { !it.second.isNullOrBlank() }
                .collect { Utils.toastShort(context, it.second) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updatedQuestion
                .filter { it.second != -1 && it.third != null }
                .collect { adapter?.update(it.second, it.third!!) }
        }
    }

    override fun isFirstItemVisible(): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            return layoutManager.findFirstCompletelyVisibleItemPosition() == 0
        }
        return false
    }

    companion object {
        private const val TAG = "[HotQuestionsFragment]"

        fun newInstance(type: String = "1"): QuestionsChildFragment {
            return QuestionsChildFragment().apply { arguments = Bundle().apply { putString("type", type) } }
        }
    }
}