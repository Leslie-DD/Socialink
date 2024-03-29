package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.TeamSearchActivity;
import com.hnu.heshequ.activity.team.AddTeamActivity;
import com.hnu.heshequ.teams.adapter.TeamsFragmentViewPagerAdapter;
import com.hnu.heshequ.utils.StatusBarUtil;

public class TeamsFragment extends Fragment {
    private static final String TAG = "[TeamsFragment]";

    private View view;

    private final String[] tabTitleList = {"推荐", "最新", "我的"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_teams, container, false);
        init();
        return view;
    }

    private void init() {
        RelativeLayout titleLayout = view.findViewById(R.id.title_layout);
        StatusBarUtil.setMarginStatusBar(titleLayout);

        ImageView llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), TeamSearchActivity.class)));
        ImageView ivAdd = view.findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTeamActivity.class)));

        TabLayout tabs = view.findViewById(R.id.tabs);
        TeamsFragmentViewPagerAdapter pagerAdapter = new TeamsFragmentViewPagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = view.findViewById(R.id.vp);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(tabTitleList[position]);
        }).attach();
    }

}
