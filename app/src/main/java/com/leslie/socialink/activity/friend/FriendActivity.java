package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.leslie.socialink.R;
import com.leslie.socialink.adapter.MyFragmentPagerAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.fragment.FriendFragment;
import com.leslie.socialink.fragment.NearFragment;
import com.leslie.socialink.fragment.NewFragment;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by madongcheng on 2020/2/28.
 * 交友activity——附近fragment
 */

public class FriendActivity extends NetWorkActivity {
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
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        FriendFiltrate.setOnClickListener(v -> startActivity(new Intent(FriendActivity.this, FriendFiltrate.class)));
        FriendSearch.setOnClickListener(v -> startActivity(new Intent(mContext, FriendSearch.class)));

        FriendSet.setOnClickListener(v -> {
            Intent intent2 = new Intent();
            intent2.putExtra("longitude", longitude + "");
            intent2.putExtra("latitude", latitude + "");
            intent2.setClass(FriendActivity.this, FriendSet.class);
            startActivity(intent2);
        });
        tvnear.setOnClickListener(v -> setTvBg(0));
        tvnew.setOnClickListener(v -> setTvBg(1));
        tvfriend.setOnClickListener(v -> setTvBg(2));
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

}