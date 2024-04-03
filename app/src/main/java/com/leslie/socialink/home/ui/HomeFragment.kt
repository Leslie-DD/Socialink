package com.leslie.socialink.home.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jude.rollviewpager.RollPagerView
import com.jude.rollviewpager.hintview.ColorPointHintView
import com.leslie.socialink.R
import com.leslie.socialink.activity.HomeSearchActivity
import com.leslie.socialink.adapter.BannerAdapter
import com.leslie.socialink.home.adapter.HomeFragmentViewPagerAdapter
import com.leslie.socialink.home.model.HomeFragmentViewModel
import com.leslie.socialink.network.Constants
import com.leslie.socialink.secondma.android.CaptureActivity
import com.leslie.socialink.utils.StatusBarUtil.setMarginStatusBar
import com.leslie.socialink.widget.RoundViewOutlineProvider
import com.leslie.socialink.widget.pull.PullLayout
import com.leslie.socialink.widget.pull.PullLayout.OnGiveUpTouchEventListener
import com.leslie.socialink.widget.pull.PullLayout.OnHeaderChangedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var view: View

    private lateinit var pullLayout: PullLayout
    private lateinit var stickyHeader: LinearLayout
    private lateinit var pullDownLayout: LinearLayout
    private lateinit var pullUpLayout: LinearLayout

    private lateinit var bannerView: RollPagerView
    private lateinit var bannerAdapter: BannerAdapter

    private lateinit var viewPager: ViewPager2
    private var pagerAdapter: HomeFragmentViewPagerAdapter? = null

    private val tabTitleList = arrayOf("团队", "问问", "活动")

    private val viewModel: HomeFragmentViewModel by viewModels()

    private var headerViewCollapsed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.i(TAG, "onCreateView")
        view = inflater.inflate(R.layout.fragment_home, container, false)
        setMarginStatusBar<LinearLayout.LayoutParams>(view.findViewById(R.id.content))

        view.findViewById<View>(R.id.ivSecondMa).setOnClickListener {
            startActivity(Intent(context, CaptureActivity::class.java))
        }
        view.findViewById<View>(R.id.llSearch).setOnClickListener {
            startActivity(Intent(context, HomeSearchActivity::class.java))
        }

        initPullLayout()
        initStickyLayout()
        initViewPager()
        initBanner()
        return view
    }

    private fun initPullLayout() {
        pullDownLayout = view.findViewById<LinearLayout?>(R.id.pull_down).apply {
            val pullDownParams = layoutParams
            pullDownParams.height = 0
            layoutParams = pullDownParams
        }

        pullUpLayout = view.findViewById<LinearLayout?>(R.id.pull_up).apply {
            val pullUpParams = layoutParams
            pullUpParams.height = 0
            layoutParams = pullUpParams
        }

        pullLayout = view.findViewById<PullLayout>(R.id.rootView).apply {
            setViewId(R.id.pull_down, R.id.sticky_header, R.id.sticky_content, R.id.pull_up)
            // 通过判断 viewpager 当前 fragment 中的 recyclerview 的第一个 item 是否显示来决定是否拦截事件
            setOnGiveUpTouchEventListener(object : OnGiveUpTouchEventListener {
                override fun giveUpTouchEventSinceTopList(event: MotionEvent?): Boolean {
                    val iListFragment = getChildFragmentManager().findFragmentByTag("f${viewPager.currentItem}") as? IListFragment
                        ?: return false
                    return iListFragment.isFirstItemVisible()
                }

                override fun giveUpTouchEventSinceBottomList(event: MotionEvent?): Boolean {
                    val iListFragment = getChildFragmentManager().findFragmentByTag("f${viewPager.currentItem}") as? IListFragment
                        ?: return false
                    return iListFragment.isLastItemVisible()
                }
            })
            // 监听 header view 展开或折叠状态变更，并在 fragment 重新走声明周期时保证 header view 显示正确的折叠或者展开状态
            setOnHeaderChangedListener(object : OnHeaderChangedListener {
                override fun onCollapsed() {
                    headerViewCollapsed = true
                }

                override fun onExpanded() {
                    headerViewCollapsed = false
                }
            })
            setHeaderViewInitCollapsed(headerViewCollapsed)
        }
    }

    private fun initStickyLayout() {
        stickyHeader = view.findViewById<LinearLayout?>(R.id.sticky_header).apply {
            // 当 stickyHeader 的高度确定后需要初始化 stickyLayout 的数据
            post {
                pullLayout.init()
                if (headerViewCollapsed) {
                    pullLayout.setHeaderHeight(0, 0)
                }
            }
        }
    }

    private fun initViewPager() {
        pagerAdapter = HomeFragmentViewPagerAdapter(getChildFragmentManager(), lifecycle)
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.vp)
        viewPager.setAdapter(pagerAdapter)
        viewPager.setOffscreenPageLimit(3)
        TabLayoutMediator(tabs, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.setText(tabTitleList[position])
        }.attach()
    }

    private fun initBanner() {
        bannerView = view.findViewById<RollPagerView?>(R.id.rollPageView).apply {
            setPlayDelay(3000)
            setAnimationDurtion(500)
            // 设置指示器
            setHintView(ColorPointHintView(activity, requireContext().getColor(R.color.colorPrimary), Color.WHITE))

            // 设置圆角
            val roundViewOutlineProvider = RoundViewOutlineProvider(resources.getDimensionPixelOffset(R.dimen.home_banner_radius))
            outlineProvider = roundViewOutlineProvider
            setClipToOutline(true)
        }
        bannerAdapter = BannerAdapter(bannerView, activity)
        bannerView.setAdapter(bannerAdapter)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.homeBannersStateFlow.collect { homeBanners ->
                bannerAdapter.setData(homeBanners.map { homeBanner -> Constants.BASE_URL + homeBanner.coverImage })
            }
        }
    }

    companion object {
        private const val TAG = "[HomeFragment]"
    }
}
