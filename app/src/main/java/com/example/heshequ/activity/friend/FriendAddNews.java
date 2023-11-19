package com.example.heshequ.activity.friend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.heshequ.R;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.fragment.News_FriendFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dell on 2020/5/11.
 */

public class FriendAddNews extends NetWorkActivity implements View.OnClickListener {
    private View view;
    private ViewPager vp;
    private ArrayList<Fragment> list;
    private News_FriendFragment ffFragment;
    private MyFragmentPagerAdapter adapter;
    private int status = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendaddnews);

        init();
        event();
    }

    private void init() {
        setText("您收到的好友请求");
        list = new ArrayList<>();
        ffFragment = new News_FriendFragment();
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
                //Intent intent = new Intent(FriendShengriSet.this,FriendSet.class);
                //startActivity(intent);
                finish();
                break;
        }
    }
}


