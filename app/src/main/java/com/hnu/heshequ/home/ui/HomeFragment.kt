package com.hnu.heshequ.home.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hnu.heshequ.R
import com.hnu.heshequ.activity.HomeSearchActivity
import com.hnu.heshequ.adapter.BannerAdapter
import com.hnu.heshequ.home.adapter.HomeFragmentViewPagerAdapter
import com.hnu.heshequ.home.model.HomeFragmentViewModel
import com.hnu.heshequ.network.Constants
import com.hnu.heshequ.secondma.android.CaptureActivity
import com.hnu.heshequ.utils.StatusBarUtil.setMarginStatusBar
import com.hnu.heshequ.widget.RoundViewOutlineProvider
import com.hnu.heshequ.widget.StickyLayout
import com.jude.rollviewpager.RollPagerView
import com.jude.rollviewpager.hintview.ColorPointHintView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var view: View

    private lateinit var stickyLayout: StickyLayout
    private lateinit var stickyHeader: LinearLayout

    private lateinit var bannerView: RollPagerView
    private lateinit var bannerAdapter: BannerAdapter

    private lateinit var viewPager: ViewPager2
    private var pagerAdapter: HomeFragmentViewPagerAdapter? = null

    private val tabTitleList = arrayOf("热门团队", "热门问问", "热门活动")

    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.i(TAG, "onCreateView")
        view = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<View>(R.id.ivSecondMa).setOnClickListener {
            startActivity(Intent(context, CaptureActivity::class.java))
        }
        view.findViewById<View>(R.id.llSearch).setOnClickListener {
            startActivity(Intent(context, HomeSearchActivity::class.java))
        }
        initStickyLayout()
        initViewPager()
        initBanner()
        return view
    }

    private fun initStickyLayout() {
        stickyLayout = view.findViewById(R.id.rootView)
        stickyLayout.setHeaderAndContentId(R.id.sticky_header, R.id.sticky_content)
        // 通过判断 viewpager 当前 fragment 中的 recyclerview 的第一个 item 是否显示来决定是否拦截事件
        stickyLayout.setOnGiveUpTouchEventListener {
            val currentItem = viewPager.currentItem
            val currentFragment = getChildFragmentManager().findFragmentByTag("f$currentItem")
                ?: return@setOnGiveUpTouchEventListener false
            (currentFragment as IListFragment).isFirstItemVisible()
        }
        stickyHeader = view.findViewById(R.id.sticky_header)
        setMarginStatusBar<LinearLayout.LayoutParams>(stickyHeader)
        // 当 stickyHeader 的高度确定后需要初始化 stickyLayout 的数据
        stickyHeader.post { stickyLayout.initData() }
    }

    private fun initViewPager() {
        pagerAdapter = HomeFragmentViewPagerAdapter(getChildFragmentManager(), lifecycle)
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        val tabsSticky: TabLayout = view.findViewById(R.id.tabs_sticky)
        viewPager = view.findViewById(R.id.vp)
        viewPager.setAdapter(pagerAdapter)
        viewPager.setOffscreenPageLimit(3)
        TabLayoutMediator(tabs, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.setText(tabTitleList[position])
        }.attach()
        TabLayoutMediator(tabsSticky, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.setText(tabTitleList[position])
        }.attach()
    }

    private fun initBanner() {
        bannerView = view.findViewById(R.id.rollPageView)
        bannerAdapter = BannerAdapter(bannerView, activity)
        bannerView.setAdapter(bannerAdapter)
        bannerView.setPlayDelay(3000)
        bannerView.setAnimationDurtion(500)
        // 设置指示器
        bannerView.setHintView(ColorPointHintView(activity, requireContext().getColor(R.color.colorPrimary), Color.WHITE))

        // 设置圆角
        val outlineProvider = RoundViewOutlineProvider(resources.getDimensionPixelOffset(R.dimen.home_banner_radius))
        bannerView.outlineProvider = outlineProvider
        bannerView.setClipToOutline(true)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.homeBannersStateFlow.collect { homeBanners ->
                bannerAdapter.setData(homeBanners.map { homeBanner -> Constants.base_url + homeBanner.coverImage })
            }
        }
    }

    companion object {
        private const val TAG = "[HomeFragment]"
    }
}
