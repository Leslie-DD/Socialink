package com.hnu.heshequ.home.ui;

import android.content.Intent;
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
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.HomeSearchActivity;
import com.hnu.heshequ.home.adapter.HomeFragmentViewPagerAdapter;
import com.hnu.heshequ.secondma.android.CaptureActivity;

public class HomeFragment extends Fragment {
    private static final String TAG = "[HomeFragment]";

    private View view;
    TabLayout tabs;
    TabLayout tabsSticky;
    HomeFragmentViewPagerAdapter pagerAdapter;
    ViewPager2 viewPager;

    private final String[] tabTitleList = {"热门团队", "热门问问", "热门活动"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    private void init() {
        ImageView ivSecondMa = view.findViewById(R.id.ivSecondMa);
        ivSecondMa.setOnClickListener(v -> startActivity(new Intent(getActivity(), CaptureActivity.class)));
        LinearLayout llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), HomeSearchActivity.class)));

        pagerAdapter = new HomeFragmentViewPagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());
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
    }

}
