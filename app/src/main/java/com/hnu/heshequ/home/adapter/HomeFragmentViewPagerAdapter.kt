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

    private val teamsFragment: HotTeamsFragment by lazy { HotTeamsFragment() }
    private val questionsFragment: HotQuestionsFragment by lazy { HotQuestionsFragment() }
    private val activitiesFragment: HotActivitiesFragment by lazy { HotActivitiesFragment() }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return getFragment(position)
    }

    private fun getFragment(position: Int): Fragment {
        return when (position) {
            0 -> teamsFragment
            1 -> questionsFragment
            else -> activitiesFragment
        }
    }

    fun refresh(position: Int) {
        when (position) {
            0 -> teamsFragment.refreshData()
            1 -> questionsFragment.getData(true)
            2 -> activitiesFragment.getData(true)
        }
    }

    fun loadMore(position: Int) {
        when (position) {
            0 -> teamsFragment.loaData()
            1 -> questionsFragment.getData(false);
            2 -> activitiesFragment.getData(false)
        }
    }

    companion object {
        private const val TAG = "[HomeFragmentViewPagerAdapter]"
    }
}