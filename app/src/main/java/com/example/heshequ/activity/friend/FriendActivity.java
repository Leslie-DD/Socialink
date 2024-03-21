package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

/**
 * Created by madongcheng on 2020/2/28.
 * 交友activity——附近fragment
 */

public class FriendActivity extends NetWorkActivity implements View.OnClickListener {
    private View view;
    private ViewPager vp;
    private static double longitude;
    private static double latitude;
    private TextView tvnear, tvnew, tvfriend;
    private ArrayList<Fragment> list;
    private LinearLayout FriendFiltrate, FriendSet, FriendSearch;
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
        longitude = intent1.getDoubleExtra("longitude", 0f);
        latitude = intent1.getDoubleExtra("latitude", 0f);
        Log.i("FriendActivity", "经纬度：" + longitude + "， " + latitude);
        init();
        event();
    }

    private void init() {
        setText("交友");
        FriendSearch = (LinearLayout) findViewById(R.id.FriendSearch);

        TextView searchContent = findViewById(R.id.search_content);
        Drawable drawable = getResources().getDrawable(R.drawable.search, null);
        // 图片宽度和高度
        int paddingStart = Utils.dip2px(this, 2);
        int paddingEnd = Utils.dip2px(this, 6);
        int heightWidth = Utils.dip2px(this, 24);
        drawable.setBounds(paddingStart, paddingStart, heightWidth, heightWidth);
        searchContent.setCompoundDrawables(drawable, null, null, null); // 左上右下

        list = new ArrayList<>();
        FriendFiltrate = (LinearLayout) findViewById(R.id.FriendFiltrate);
        FriendSet = (LinearLayout) findViewById(R.id.FriendSet);    //点击此处进入交友设置界面
        tvnear = (TextView) findViewById(R.id.tvNear);
        tvnew = (TextView) findViewById(R.id.tvNew);
        tvfriend = (TextView) findViewById(R.id.tvFriend);
        getData();
        nearFragment = new NearFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
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
            Log.i("FriendActivity", "我是vp的设置item " + status);
        }
        tvnear.setSelected(status == 0);
        tvnew.setSelected(status == 1);
        tvfriend.setSelected(status == 2);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.d("FriendActivity", "onSuccess " + where + " " + result.toString());
    }

    private void getData() {
        Log.d("FriendActivity", "setBodyParams 经度纬度 WenConstants.token:  " + WenConstans.token + ", Constants.token: " + Constants.token);
        setBodyParams(new String[]{"longitude", "latitude"}, new String[]{"" + longitude, "" + latitude});
        sendPost(Constants.base_url + "/api/social/updatePosition.do", 100, WenConstans.token);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.FriendSearch:
                startActivity(new Intent(mContext, FriendSearch.class));
                break;
            case R.id.FriendSet:
                Intent intent2 = new Intent();
                intent2.putExtra("longitude", longitude + "");
                intent2.putExtra("latitude", latitude + "");
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
//                Intent intent3 = new Intent();
//                intent3.putExtra("longitude", longitude + "");
//                intent3.putExtra("latitude", latitude + "");
//                intent3.setClass(FriendActivity.this, FriendActivity2.class);
//                startActivity(intent3);
//                overridePendingTransition(0, 0);
//                finish();
                setTvBg(1);
                break;

            case R.id.tvFriend:
                setTvBg(2);
                break;
        }
    }
}