package com.example.heshequ.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.fragment.MyTeamFragment;
import com.example.heshequ.fragment.MyWenWenFragment;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的足迹页面
 */
public class MyFootprintActivity extends NetWorkActivity implements View.OnClickListener {
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
        findViewById(R.id.ivBack).setOnClickListener(this);
        tvTeam.setOnClickListener(this);
        tvWw.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvTeam:
                setTvBg(0);
                break;
            case R.id.tvWw:
                setTvBg(1);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
