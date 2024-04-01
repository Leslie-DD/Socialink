package com.leslie.socialink.team.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leslie.socialink.team.ui.TeamsChildFragment

class TeamsFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val recommendFragment: TeamsChildFragment by lazy { TeamsChildFragment.newInstance(type = "5") }
    private val latestFragment: TeamsChildFragment by lazy { TeamsChildFragment.newInstance(type = "2") }
    private val mineFragment: TeamsChildFragment by lazy { TeamsChildFragment.newInstance(type = "3") }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return getFragment(position)
    }

    private fun getFragment(position: Int): Fragment {
        return when (position) {
            0 -> recommendFragment
            1 -> latestFragment
            else -> mineFragment
        }
    }
}