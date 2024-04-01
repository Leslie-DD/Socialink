package com.leslie.socialink.team.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.leslie.socialink.R
import com.leslie.socialink.activity.TeamSearchActivity
import com.leslie.socialink.activity.team.AddTeamActivity
import com.leslie.socialink.team.adapter.TeamsFragmentViewPagerAdapter
import com.leslie.socialink.utils.StatusBarUtil.setMarginStatusBar

class TeamsFragment : Fragment() {
    private lateinit var view: View

    private lateinit var viewPager: ViewPager2
    private var pagerAdapter: TeamsFragmentViewPagerAdapter? = null

    private val tabTitleList = arrayOf("推荐", "最新", "我的")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView")
        view = inflater.inflate(R.layout.fragment_teams, container, false)
        init()
        return view
    }

    private fun init() {
        val titleLayout = view.findViewById<RelativeLayout>(R.id.title_layout)
        setMarginStatusBar<MarginLayoutParams>(titleLayout)

        view.findViewById<ImageView>(R.id.llSearch)
            .setOnClickListener { startActivity(Intent(activity, TeamSearchActivity::class.java)) }

        view.findViewById<ImageView>(R.id.ivAdd)
            .setOnClickListener { startActivity(Intent(activity, AddTeamActivity::class.java)) }

        val tabs = view.findViewById<TabLayout>(R.id.tabs)
        pagerAdapter = TeamsFragmentViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager = view.findViewById(R.id.vp)
        viewPager.setAdapter(pagerAdapter)
        viewPager.setOffscreenPageLimit(3)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setText(tabTitleList[position])
        }.attach()
    }

    companion object {
        private const val TAG = "[TeamsFragment]"
    }
}
