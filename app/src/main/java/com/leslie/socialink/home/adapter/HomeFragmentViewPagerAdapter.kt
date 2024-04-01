package com.leslie.socialink.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leslie.socialink.home.ui.HotActivitiesFragment
import com.leslie.socialink.questions.ui.QuestionsChildFragment
import com.leslie.socialink.team.ui.TeamsChildFragment

class HomeFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TeamsChildFragment.newInstance(type = "1")
            1 -> QuestionsChildFragment()
            else -> HotActivitiesFragment()
        }
    }

    companion object {
        private const val TAG = "[HomeFragmentViewPagerAdapter]"
    }
}