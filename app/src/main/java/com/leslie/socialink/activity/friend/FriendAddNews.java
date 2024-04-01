package com.leslie.socialink.activity.friend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.leslie.socialink.R;
import com.leslie.socialink.adapter.MyFragmentPagerAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.message.ui.MessagesFriendApplyFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FriendAddNews extends NetWorkActivity {
    private View view;
    private ViewPager vp;
    private ArrayList<Fragment> list;
    private MessagesFriendApplyFragment ffFragment;
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
        ffFragment = new MessagesFriendApplyFragment();
        list.add(ffFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
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
}


