package com.leslie.socialink.home.ui

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
import com.leslie.socialink.adapter.recycleview.HotActiveAdapter
import com.leslie.socialink.bean.ConsTants
import com.leslie.socialink.home.model.HotActivitiesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HotActivitiesFragment : Fragment(), IListFragment {

    private lateinit var recyclerView: XRecyclerView
    private var adapter: HotActiveAdapter? = null
    private lateinit var tvTips: TextView

    private val viewModel: HotActivitiesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_tim, container, false)
        recyclerView = view.findViewById(R.id.rv)
        recyclerView.setLoadingListener(object : LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    viewModel.fetchData()
                }
            }
        })
        recyclerView.isNestedScrollingEnabled = false
        ConsTants.initXRecycleView(activity, true, false, recyclerView)
        adapter = HotActiveAdapter(activity)
        recyclerView.adapter = adapter

        tvTips = view.findViewById(R.id.tvTips)

        initCollect()
        return view
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hotActivitiesBeanStateFlow
                .collect {
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
    }

    override fun isFirstItemVisible(): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        return if (layoutManager != null) {
            layoutManager.findFirstCompletelyVisibleItemPosition() == 0
        } else {
            false
        }
    }

    override fun isLastItemVisible(): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        if (adapter == null || layoutManager == null) {
            return false
        }
        return layoutManager.findLastVisibleItemPosition() == adapter?.itemCount
    }

    companion object {
        private const val TAG = "[HotActivitiesFragment]"
    }
}