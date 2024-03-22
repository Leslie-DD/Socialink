package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.TeamSearchActivity;
import com.hnu.heshequ.activity.team.AddTeamActivity;
import com.hnu.heshequ.adapter.MytPagersAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.launcher.MainActivity2;
import com.hnu.heshequ.view.NoScrollViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TeamsFragment extends NetWorkFragment {

    private View view;
    private ImageView llSearch;
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
        view = inflater.inflate(R.layout.fragment_teams, null);
        init();
        event();
        return view;
    }

    private void init() {
        MainActivity2 activity = (MainActivity2) getActivity();
//        TeamActivity activity = (TeamActivity) getActivity();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TeamChildFragment1());
        fragments.add(new TeamChildFragment2());
        fragments.add(new TeamChildFragment3());
        //fragments.add(new TeamChildFragment4());
        llSearch = (ImageView) view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> startActivity(new Intent(mContext, TeamSearchActivity.class)));
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
        ivAdd.setOnClickListener(v -> startActivity(new Intent(mContext, AddTeamActivity.class)));
        tvMine.setOnClickListener(v -> setTvBg(0));
        tvNew.setOnClickListener(v -> setTvBg(2));
        tvCollection.setOnClickListener(v -> setTvBg(3));
        tvRecommended.setOnClickListener(v -> setTvBg(1));
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
