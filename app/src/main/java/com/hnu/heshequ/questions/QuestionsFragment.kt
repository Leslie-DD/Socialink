package com.hnu.heshequ.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hnu.heshequ.R
import com.hnu.heshequ.questions.adapter.QuestionsFragmentViewPagerAdapter
import com.hnu.heshequ.utils.StatusBarUtil.setMarginStatusBar

class QuestionsFragment : Fragment() {
    private lateinit var view: View

    private val tabsValue = arrayOf("推荐", "最新")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        view = inflater.inflate(R.layout.fragment_questions, container, false)
        val titleLayout = view.findViewById<RelativeLayout>(R.id.title_layout)
        setMarginStatusBar<MarginLayoutParams>(titleLayout)

        initViewPager()
        return view
    }

    private fun initViewPager() {
        val pagerAdapter = QuestionsFragmentViewPagerAdapter(getChildFragmentManager(), lifecycle)
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
