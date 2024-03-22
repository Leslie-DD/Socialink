package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.fragment.FriendFritrateFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FriendFiltrateShow extends NetWorkActivity {
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
