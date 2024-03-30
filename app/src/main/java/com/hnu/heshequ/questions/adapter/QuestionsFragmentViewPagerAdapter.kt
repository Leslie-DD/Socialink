package com.hnu.heshequ.questions.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hnu.heshequ.fragment.ChildFragment2
import com.hnu.heshequ.fragment.ChildQuestionsFragment

class QuestionsFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return ChildQuestionsFragment.newInstance(position + 1)
//        return when (position) {
//            0 -> ChildQuestionsFragment()
//            else -> ChildFragment2()
////            else -> ZcFragment()
//        }

    }

    companion object {
        private const val TAG = "[QuestionsFragmentViewPagerAdapter]"
    }
}