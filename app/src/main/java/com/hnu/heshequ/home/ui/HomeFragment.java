package com.hnu.heshequ.home.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.HomeSearchActivity;
import com.hnu.heshequ.adapter.BannerAdapter;
import com.hnu.heshequ.bean.HomeBannerImgsBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.home.adapter.HomeFragmentViewPagerAdapter;
import com.hnu.heshequ.network.HttpRequestUtil;
import com.hnu.heshequ.secondma.android.CaptureActivity;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.widget.StickyLayout;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "[HomeFragment]";

    private static final int FETCH_BANNER_IMAGES = 1000;

    private View view;

    private StickyLayout stickyLayout;
    private LinearLayout stickyHeader;

    private RollPagerView bannerView;
    private BannerAdapter bannerAdapter;

    private TabLayout tabs;
    private TabLayout tabsSticky;
    private HomeFragmentViewPagerAdapter pagerAdapter;
    private ViewPager2 viewPager;

    private final Gson gson = new Gson();

    private final String[] tabTitleList = {"热门团队", "热门问问", "热门活动"};

    private final HttpRequestUtil.RequestCallBack callBack = new HttpRequestUtil.RequestCallBack() {
        @Override
        public void onSuccess(JSONObject result, int where, boolean fromCache) {
            if (where == FETCH_BANNER_IMAGES) {
                if (result.optInt("code") != 0) {
                    Utils.toastShort(getContext(), result.optString("msg"));
                    return;
                }
                if (!result.has("data") || result.optString("data").isEmpty()) {
                    return;
                }
                List<String> imagesUrl = new ArrayList<>();
                List<HomeBannerImgsBean> imagesData = gson.fromJson(result.optString("data"),
                        new TypeToken<ArrayList<HomeBannerImgsBean>>() {
                        }.getType());

                if (imagesData != null && !imagesData.isEmpty()) {
                    imagesData.forEach(bannerImage -> {
                        imagesUrl.add(Constants.base_url + bannerImage.getCoverImage());
                    });
                }
                bannerAdapter.setData(imagesUrl);
            }
        }

        @Override
        public void onFailure(String result, int where) {
            Utils.toastShort(getContext(), "操作失败");
        }
    };

    private final HttpRequestUtil httpRequest = new HttpRequestUtil(callBack, TAG);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
    }

    private void init() {
        stickyHeader = view.findViewById(R.id.sticky_header);

        ImageView ivSecondMa = view.findViewById(R.id.ivSecondMa);
        ivSecondMa.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptureActivity.class)));
        LinearLayout llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), HomeSearchActivity.class)));

        stickyLayout = view.findViewById(R.id.rootView);
        stickyLayout.setHeaderAndContentId(R.id.sticky_header, R.id.sticky_content);
        // 通过判断 viewpager 当前 fragment 中的 recyclerview 的第一个 item 是否显示来决定是否拦截事件
        stickyLayout.setOnGiveUpTouchEventListener(event -> {
            int currentItem = viewPager.getCurrentItem();
            Fragment currentFragment = getChildFragmentManager().findFragmentByTag("f" + currentItem);
            if (currentFragment == null) {
                return false;
            }
            return ((IListFragment) currentFragment).isFirstItemVisible();
        });
        // 当 stickyHeader 的高度确定后需要初始化 stickyLayout 的数据
        stickyHeader.post(() -> stickyLayout.initData());

        bannerView = view.findViewById(R.id.rollPageView);
        bannerAdapter = new BannerAdapter(bannerView, getActivity());
        bannerView.setAdapter(bannerAdapter);
        bannerView.setPlayDelay(3000);
        bannerView.setAnimationDurtion(500);
        // 设置指示器
        bannerView.setHintView(new ColorPointHintView(getActivity(), Color.parseColor("#00bbff"), Color.WHITE));

        pagerAdapter = new HomeFragmentViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        tabs = view.findViewById(R.id.tabs);
        tabsSticky = view.findViewById(R.id.tabs_sticky);
        viewPager = view.findViewById(R.id.vp);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(tabTitleList[position]);
        }).attach();

        new TabLayoutMediator(tabsSticky, viewPager, (tab, position) -> {
            tab.setText(tabTitleList[position]);
        }).attach();

        fetchBannerImages();
    }

    // 获取首页轮播图
    private void fetchBannerImages() {
        httpRequest.setBodyParams(new String[]{"category"}, new String[]{"" + 1});
        httpRequest.sendPostConnection(Constants.base_url + "/api/pub/category/advertisement.do", FETCH_BANNER_IMAGES, Constants.token);
    }

}
