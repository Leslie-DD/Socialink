package com.leslie.socialink.team.ui

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
import com.leslie.socialink.home.ui.IListFragment
import com.leslie.socialink.team.model.TeamsChildViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TeamsChildFragment : Fragment(), IListFragment {

    private lateinit var tvTips: TextView
    private lateinit var recyclerView: XRecyclerView
    private var adapter: CommentTeamAdapter? = null

    private val type: String by lazy { arguments?.getString("type") ?: "1" }

    private val viewModel: TeamsChildViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_tim, container, false)
        tvTips = view.findViewById(R.id.tvTips)
        recyclerView = view.findViewById(R.id.rv)
        init()
        initCollect()
        viewModel.initData(type = type)
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
                    viewModel.fetchData(refresh = false, type = type)
                }
            }
        })
        recyclerView.adapter = adapter
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.teamsBeanStateFlow
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

    override fun isLastItemVisible(): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        if (adapter == null || layoutManager == null) {
            return false
        }
        return layoutManager.findLastVisibleItemPosition() == adapter?.itemCount
    }

    companion object {
        private const val TAG = "[HotTeamsFragment]"

        fun newInstance(type: String = "1"): TeamsChildFragment {
            return TeamsChildFragment().apply { arguments = Bundle().apply { putString("type", type) } }
        }
    }
}