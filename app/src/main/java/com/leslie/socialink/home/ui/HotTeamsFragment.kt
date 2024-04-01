package com.leslie.socialink.home.ui

import android.content.Intent
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
import com.leslie.socialink.activity.team.TeamDetailActivity
import com.leslie.socialink.adapter.recycleview.CommentTeamAdapter
import com.leslie.socialink.bean.ConsTants
import com.leslie.socialink.home.model.HotTeamsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HotTeamsFragment : Fragment(), IListFragment {

    private lateinit var tvTips: TextView
    private lateinit var recyclerView: XRecyclerView
    private var adapter: CommentTeamAdapter? = null

    private val viewModel: HotTeamsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_tim, container, false)
        tvTips = view.findViewById(R.id.tvTips)
        recyclerView = view.findViewById(R.id.rv)
        init()
        initCollect()
        return view
    }

    private fun init() {
        ConsTants.initXRecycleView(context, true, false, recyclerView)
        adapter = CommentTeamAdapter(context, emptyList())
        adapter?.setListener { position ->
            val intent = Intent(context, TeamDetailActivity::class.java)
            intent.putExtra("id", adapter!!.data[position].id)
            startActivity(intent)
        }
        recyclerView.setLoadingListener(object : LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    viewModel.fetchData(false)
                }
            }
        })
        recyclerView.adapter = adapter
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hotTeamsBeanStateFlow
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
        if (layoutManager != null) {
            val firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            return firstVisiblePosition == 0
        }
        return false
    }

    companion object {
        private const val TAG = "[HotTeamsFragment]"
    }
}