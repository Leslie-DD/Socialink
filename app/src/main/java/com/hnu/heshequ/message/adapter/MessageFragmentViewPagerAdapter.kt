package com.hnu.heshequ.message.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hnu.heshequ.message.ui.IMessagesFragment
import com.hnu.heshequ.message.ui.MessagesChatFragment
import com.hnu.heshequ.message.ui.MessagesFriendApplyFragment
import com.hnu.heshequ.message.ui.MessagesQuestionFragment
import com.hnu.heshequ.message.ui.MessagesSayFragment
import com.hnu.heshequ.message.ui.MessagesTeamFragment

class MessageFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val teamsFragment: MessagesTeamFragment by lazy { MessagesTeamFragment() }
    private val saysFragment: MessagesSayFragment by lazy { MessagesSayFragment() }
    private val asksFragment: MessagesQuestionFragment by lazy { MessagesQuestionFragment() }
    private val messageFragment: MessagesChatFragment by lazy { MessagesChatFragment() }
    private val friendFragment: MessagesFriendApplyFragment by lazy { MessagesFriendApplyFragment() }

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return getFragment(position)
    }

    private fun getFragment(position: Int): Fragment {
        return when (position) {
            0 -> teamsFragment
            1 -> saysFragment
            2 -> asksFragment
            3 -> messageFragment
            else -> friendFragment
        }
    }

    fun refreshFragmentData(position: Int) {
        (getFragment(position) as? IMessagesFragment)?.refreshData()
    }

    companion object {
        private const val TAG = "[HomeFragmentViewPagerAdapter]"
    }
}