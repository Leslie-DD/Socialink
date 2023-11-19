package com.example.heshequ.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.heshequ.R;
import com.example.heshequ.activity.TeamSearchActivity;
import com.example.heshequ.activity.flowview.TeamActivity;
import com.example.heshequ.activity.team.AddTeamActivity;
import com.example.heshequ.adapter.MytPagersAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.view.NoScrollViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2018/5/8 14:12
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class TimsFragment extends NetWorkFragment implements View.OnClickListener {

    private View view;
    private LinearLayout llSearch;
    private NoScrollViewPager vp;
    private MytPagersAdapter pagerAdapter;
    private TextView tvMine, tvRecommended, tvNew, tvCollection;
    private int status = -1;
    private ImageView ivAdd;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_tims, null);
        init();
        event();
        return view;
    }

    private void init() {
//        MainActivity activity = (MainActivity) getActivity();
        TeamActivity activity = (TeamActivity) getActivity();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TeamChildFragment1());
        fragments.add(new TeamChildFragment2());
        fragments.add(new TeamChildFragment3());
        //fragments.add(new TeamChildFragment4());
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        tvNew = (TextView) view.findViewById(R.id.tvNew);
        tvRecommended = (TextView) view.findViewById(R.id.tvRecommended);
        tvMine = (TextView) view.findViewById(R.id.tvMine);
        tvCollection = (TextView) view.findViewById(R.id.tvCollection);
        vp = (NoScrollViewPager) view.findViewById(R.id.vp);
        vp.setNoScroll(false);
        ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        pagerAdapter = new MytPagersAdapter
                (activity.getSupportFragmentManager(), fragments);
        vp.setAdapter(pagerAdapter);
        vp.setId(fragments.get(0).hashCode());
        vp.setCurrentItem(0);
        setTvBg(0);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTvBg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void event() {
        llSearch.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        tvMine.setOnClickListener(this);
        tvNew.setOnClickListener(this);
        tvCollection.setOnClickListener(this);
        tvRecommended.setOnClickListener(this);
    }
//

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
        }
        tvMine.setSelected(status == 0 ? true : false);
        tvRecommended.setSelected(status == 1 ? true : false);
        tvNew.setSelected(status == 2 ? true : false);
        tvCollection.setSelected(status == 3 ? true : false);
        if (status == 0) {
            ivAdd.setVisibility(View.VISIBLE);
        } else {
            ivAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                break;
            case R.id.llSearch:
                startActivity(new Intent(mContext, TeamSearchActivity.class));
                break;
            case R.id.ivAdd:
                startActivity(new Intent(mContext, AddTeamActivity.class));
                break;
            case R.id.tvMine:
                setTvBg(0);
                break;
            case R.id.tvRecommended:
                setTvBg(1);
                break;
            case R.id.tvNew:
                setTvBg(2);
                break;
            case R.id.tvCollection:
                setTvBg(3);
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
