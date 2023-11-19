package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.heshequ.R;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.fragment.FriendFragment;
import com.example.heshequ.fragment.NearFragment;
import com.example.heshequ.fragment.NewFragment;
import com.example.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by madongcheng on 2020/2/28.
 * 交友activity——附近fragment
 */

public class FriendActivity extends NetWorkActivity implements View.OnClickListener {
    private View view;
    private ViewPager vp;
    private static double longtitude;
    private static double latitude;
    private static double longtitude2, latitude2;
    private TextView tvnear, tvnew, tvfriend;
    private String longtitude1, latitude1;
    private ArrayList<Fragment> list;
    private LinearLayout FriendFiltrate, FriendSet, FriendSearch;
    private LinearLayout llInvis;
    private NearFragment nearFragment;
    private NewFragment newFragment;
    private FriendFragment friendFragment;

    private MyFragmentPagerAdapter adapter;

    private int status = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_friend);
        Intent intent1 = getIntent();
        longtitude1 = intent1.getStringExtra("longtitude");
        latitude1 = intent1.getStringExtra("latitude");
        DecimalFormat df = new DecimalFormat("#.000000");
        longtitude = Double.parseDouble(longtitude1);
        latitude = Double.parseDouble(latitude1);
        Log.e("FriendActivity 经度", "" + longtitude);
        Log.e("FriendActivity 纬度", "" + latitude);
        init();
        event();
    }


    private void init() {
        //  View view = getLayoutInflater().inflate(R.layout.activity_menu_friend, null);
        setText("交友");
        FriendSearch = (LinearLayout) findViewById(R.id.FriendSearch);

        list = new ArrayList<>();
        FriendFiltrate = (LinearLayout) findViewById(R.id.FriendFiltrate);
        FriendSet = (LinearLayout) findViewById(R.id.FriendSet);    //点击此处进入交友设置界面
        tvnear = (TextView) findViewById(R.id.tvNear);
        tvnew = (TextView) findViewById(R.id.tvNew);
        tvfriend = (TextView) findViewById(R.id.tvFriend);
        getData();
        nearFragment = new NearFragment();
        Bundle bundle = new Bundle();
        bundle.putString("jingdu", longtitude + "");
        bundle.putString("weidu", "" + latitude);
        nearFragment.setArguments(bundle);
        newFragment = new NewFragment();

        friendFragment = new FriendFragment();
        list.add(nearFragment);
        list.add(newFragment);
        list.add(friendFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        FriendFiltrate.setOnClickListener(this);
        FriendSearch.setOnClickListener(this);

        FriendSet.setOnClickListener(this);
        tvnear.setOnClickListener(this);
        tvnew.setOnClickListener(this);
        tvfriend.setOnClickListener(this);
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
        tvnear.setSelected(status == 0 ? true : false);
        tvnew.setSelected(status == 1 ? true : false);
        tvfriend.setSelected(status == 2 ? true : false);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            Utils.toastShort(mContext, result.optString("msg"));
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        Log.e("FriendActivity", "setBodyParams 经度纬度");
        setBodyParams(new String[]{"longitude", "latitude"}, new String[]{"" + longtitude, "" + latitude});
        Log.e("WenConstans.token", WenConstans.token);
        Log.e("Constants.token", Constants.token);
        sendPost(Constants.base_url + "/api/social/updatePosition.do", 100, WenConstans.token);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.FriendSearch:
                Log.e("sea", "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
                startActivity(new Intent(mContext, FriendSearch.class));
                break;
            case R.id.FriendSet:
                Intent intent2 = new Intent();
                intent2.putExtra("longtitude", longtitude1 + "");
                intent2.putExtra("latitude", "" + latitude1);
                intent2.setClass(FriendActivity.this, FriendSet.class);
                startActivity(intent2);
                break;
            case R.id.FriendFiltrate:
                Intent intent1 = new Intent(FriendActivity.this, FriendFiltrate.class);
                startActivity(intent1);
                break;
            case R.id.tvNear:
                setTvBg(0);
                break;
            case R.id.tvNew:
                Intent intent3 = new Intent();
                intent3.putExtra("longtitude", "" + longtitude);
                intent3.putExtra("latitude", "" + latitude);
                intent3.setClass(FriendActivity.this, FriendActivity2.class);
                startActivity(intent3);
                overridePendingTransition(0, 0);
                finish();

                break;

            case R.id.tvFriend:
                setTvBg(2);
                break;
        }
    }
}