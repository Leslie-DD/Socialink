package com.leslie.socialink.questions.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leslie.socialink.questions.ui.QuestionsChildFragment

class QuestionsFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return QuestionsChildFragment.newInstance((position + 1).toString())
    }

    companion object {
        private const val TAG = "[QuestionsFragmentViewPagerAdapter]"
    }
}