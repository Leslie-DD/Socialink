package com.hnu.heshequ.teams.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hnu.heshequ.fragment.TeamChildFragment1
import com.hnu.heshequ.fragment.TeamChildFragment2
import com.hnu.heshequ.fragment.TeamChildFragment3

class TeamsFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val asksFragment: TeamChildFragment2 by lazy { TeamChildFragment2() }
    private val activitiesFragment: TeamChildFragment3 by lazy { TeamChildFragment3() }
    private val teamsFragment: TeamChildFragment1 by lazy { TeamChildFragment1() }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return getFragment(position)
    }

    fun getFragment(position: Int): Fragment {
        return when (position) {
            0 -> asksFragment
            1 -> activitiesFragment
            else -> teamsFragment
        }
    }
}