package com.hnu.heshequ.activity.friend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.fragment.MyDynamicFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyDynamic extends NetWorkActivity {
    private View view;
    private ViewPager vp;
    private ArrayList<Fragment> list;
    private MyDynamicFragment MDfragment;
    private MyFragmentPagerAdapter adapter;
    private int status = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydynamic);
        init();
        event();
    }

    private void init() {
        setText("我的动态");
        list = new ArrayList<>();
        MDfragment = new MyDynamicFragment();
        list.add(MDfragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onFailure(String result, int where) {

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
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

}
