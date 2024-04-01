package com.leslie.socialink.questions.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.leslie.socialink.R
import com.leslie.socialink.questions.adapter.QuestionsFragmentViewPagerAdapter
import com.leslie.socialink.utils.StatusBarUtil.setMarginStatusBar

class QuestionsFragment : Fragment() {
    private lateinit var view: View

    private var pagerAdapter: QuestionsFragmentViewPagerAdapter? = null

    private val tabsValue = arrayOf("推荐", "最新")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView")
        view = inflater.inflate(R.layout.fragment_questions, container, false)
        val titleLayout = view.findViewById<RelativeLayout>(R.id.title_layout)
        setMarginStatusBar<MarginLayoutParams>(titleLayout)
        val fragmentManager = requireActivity().supportFragmentManager.fragments.size
        Log.i(TAG, "onCreateView activity fragment size: $fragmentManager")
        initViewPager()
        return view
    }

    private fun initViewPager() {
        pagerAdapter = QuestionsFragmentViewPagerAdapter(childFragmentManager, lifecycle)
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        val viewPager: ViewPager2 = view.findViewById(R.id.vp)
        viewPager.setAdapter(pagerAdapter)
        viewPager.setOffscreenPageLimit(2)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setText(tabsValue[position])
        }.attach()
    }

    // TODO: add question listener

    companion object {
        private const val TAG = "[QuestionsFragment]"
    }
}
