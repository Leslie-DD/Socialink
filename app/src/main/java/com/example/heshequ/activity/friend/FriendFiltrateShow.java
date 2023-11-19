package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.heshequ.R;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.fragment.FriendFritrateFragment;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dell on 2020/5/6.
 */

public class FriendFiltrateShow extends NetWorkActivity implements View.OnClickListener {
    private View view;
    private ViewPager vp;
    private ArrayList<Fragment> list;
    private FriendFritrateFragment ffFragment;
    private MyFragmentPagerAdapter adapter;
    private static int distance;
    private static int age;
    private static String college;
    private static String sex;
    private static String interest = "false";
    private int status = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_ffshow);
        Intent intent = getIntent();
        distance = Integer.parseInt(intent.getStringExtra("distance"));
        college = intent.getStringExtra("college");
        sex = intent.getStringExtra("sex");
        age = Integer.parseInt(intent.getStringExtra("age"));
        Log.e("get Bundle", distance + " " + college + " " + sex + " " + age + " " + interest);
        init();
        event();
    }

    private void init() {
        list = new ArrayList<>();
        ffFragment = new FriendFritrateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("juli", distance + "");
        bundle.putString("nianling", "" + age + "");
        bundle.putString("daxue", "" + college + "");
        bundle.putString("xingbie", "" + sex + "");
        bundle.putString("xingqu", interest + "");
        ffFragment.setArguments(bundle);
        list.add(ffFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
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
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
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
