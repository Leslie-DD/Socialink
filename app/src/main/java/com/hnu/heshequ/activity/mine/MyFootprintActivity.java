package com.hnu.heshequ.activity.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.fragment.MyTeamFragment;
import com.hnu.heshequ.fragment.MyWenWenFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的足迹页面
 */
public class MyFootprintActivity extends NetWorkActivity {
    private ViewPager vp;
    private TextView tvTeam, tvWw;
    private ArrayList<Fragment> list;
    private MyTeamFragment teamFragment;
    private MyWenWenFragment wenwenFragment;
    private MyFragmentPagerAdapter adapter;
    private int status = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint);
        init();
        event();
    }

    private void init() {
        setText("我的足迹");
        tvTeam = (TextView) findViewById(R.id.tvTeam);
        tvWw = (TextView) findViewById(R.id.tvWw);
        list = new ArrayList<>();
        wenwenFragment = new MyWenWenFragment();
        teamFragment = new MyTeamFragment();
        list.add(teamFragment);
        list.add(wenwenFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        tvTeam.setOnClickListener(v -> setTvBg(0));
        tvWw.setOnClickListener(v -> setTvBg(1));
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

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
            Log.e("YSF", "我是vp的设置item" + status);
        }
        tvTeam.setSelected(status == 0 ? true : false);
        tvWw.setSelected(status == 1 ? true : false);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

}
