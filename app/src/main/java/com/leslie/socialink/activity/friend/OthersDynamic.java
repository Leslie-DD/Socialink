package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.leslie.socialink.R;
import com.leslie.socialink.adapter.MyFragmentPagerAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.fragment.OthersDynamicFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OthersDynamic extends NetWorkActivity {
    private View view;
    private ViewPager vp;
    private int hisid;
    private ArrayList<Fragment> list;
    private OthersDynamicFragment othersDynamicFragment;
    private MyFragmentPagerAdapter adapter;
    private int status = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydynamic);
        Intent intent1 = getIntent();
        hisid = Integer.parseInt(intent1.getStringExtra("uid"));
        init();
        event();
    }

    private void init() {
        setText("ta的动态");
        list = new ArrayList<>();
        othersDynamicFragment = new OthersDynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hisid", hisid + "");
        othersDynamicFragment.setArguments(bundle);
        list.add(othersDynamicFragment);
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
