package com.hnu.heshequ.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hnu.heshequ.home.ui.HotActivitiesFragment
import com.hnu.heshequ.home.ui.HotQuestionsFragment
import com.hnu.heshequ.home.ui.HotTeamsFragment

class HomeFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HotTeamsFragment()
            1 -> HotQuestionsFragment()
            else -> HotActivitiesFragment()
        }
    }

    companion object {
        private const val TAG = "[HomeFragmentViewPagerAdapter]"
    }
}