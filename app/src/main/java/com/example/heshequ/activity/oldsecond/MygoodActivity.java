package com.example.heshequ.activity.oldsecond;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.heshequ.R;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.adapter.recycleview.LableAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.GoodLabel;
import com.example.heshequ.fragment.MygoodFragment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MygoodActivity extends NetWorkActivity implements View.OnClickListener {
    private ViewPager vp;
    private List<GoodLabel> labelList = new ArrayList<>();
    private ArrayList<Fragment> list;
    private MygoodFragment mygoodfragment;

    private MyFragmentPagerAdapter adapter;
    private int status = -1;
    private LableAdapter lableadapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygood);

        lableadapter = new LableAdapter(labelList);
        init();
        event();
    }

    private void init() {
        setText("我的商品");

        list = new ArrayList<>();
        mygoodfragment = new MygoodFragment();

        list.add(mygoodfragment);

        //list.add(wenwenFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
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

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
            Log.e("YSF", "我是vp的设置item" + status);
        }
//        tvTeam.setSelected(status == 0 ? true : false); //这是切换背景显示的
//        tvWw.setSelected(status == 1 ? true : false);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
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

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}