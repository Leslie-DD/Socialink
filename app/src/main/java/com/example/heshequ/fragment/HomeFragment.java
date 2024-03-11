package com.example.heshequ.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.heshequ.R;
import com.example.heshequ.activity.HomeSearchActivity;
import com.example.heshequ.activity.MainActivity;
import com.example.heshequ.activity.WebActivity;
import com.example.heshequ.activity.flowview.TeamActivity;
import com.example.heshequ.activity.flowview.WenwenActivity;
import com.example.heshequ.activity.friend.GPSActivity;
import com.example.heshequ.activity.newsencond.SecondHandActivity;
import com.example.heshequ.activity.team.TeamDetailActivity2;
import com.example.heshequ.activity.timetable.TimetableCheckin;
import com.example.heshequ.adapter.MyBannerAdapter;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.adapter.recycleview.RecycleAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.HomeBannerImgsBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefHotActivityEvent;
import com.example.heshequ.entity.ScanResultEvent;
import com.example.heshequ.secondma.android.CaptureActivity;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CustomViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2018/5/8 14:12
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class HomeFragment extends NetWorkFragment implements View.OnClickListener, XRecyclerView.LoadingListener {

    private static final String TAG = "HomeFragment";

    private View view;
    private ImageView ivSecondMa;
    private XRecyclerView rv;
    private LinearLayout llSearch;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private LinearLayout llInvis;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private LinearLayout llVis;
    private View headView;
    private int item = 1;
    private CustomViewPager vp;
    private MyFragmentPagerAdapter pagerAdapter;

    private RollPagerView rollPagerView;
    private MyBannerAdapter bannerAdapter;
    private List<Fragment> fragmentList;
    private FragmentBroadcast brodcast = new FragmentBroadcast();
    private final int getimgsCode = 1000;
    private ArrayList<HomeBannerImgsBean> imgsData;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private LinearLayout team;
    private LinearLayout wenwen;
    private LinearLayout scheduleCard;
    private LinearLayout secondHand;
    private LinearLayout makeFriends;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where == getimgsCode) {
            if (result.optInt("code") != 0) {
                Utils.toastShort(mContext, result.optString("msg"));
                return;
            }
            if (!result.has("data") || result.optString("data").isEmpty()) {
                return;
            }
            imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBannerImgsBean>>() {}.getType());
            if (imgsData != null && imgsData.size() > 0) {
                imgsData.forEach(bannerImgsBean -> {
                    imgs.add(Constants.base_url + bannerImgsBean.getCoverImage());
                });
            }
            bannerAdapter.setData(imgs);
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        EventBus.getDefault().register(this);
        init();
        return view;
    }

    private void init() {
        setFragmentListener();
        ivSecondMa = (ImageView) view.findViewById(R.id.ivSecondMa);
        ivSecondMa.setOnClickListener(this);
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(this);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv1.setSelected(true);
        tv1.setOnClickListener(this);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv2.setOnClickListener(this);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv3.setOnClickListener(this);
        llInvis = (LinearLayout) view.findViewById(R.id.llInVis);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXrecycleView(getActivity(), true, true, rv);
        initHeadView();
    }

    private void initHeadView() {
        fragmentList = new ArrayList<>();
        ChildFragment2 fragment2 = new ChildFragment2();
        fragment2.setType(3);
        fragmentList.add(new ChildFragment1());
        fragmentList.add(fragment2);
        fragmentList.add(new ChildFragment3());

        headView = getActivity().getLayoutInflater().inflate(R.layout.home_head_view, null);
        rollPagerView = (RollPagerView) headView.findViewById(R.id.rp);
        ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
        params.height = ConsTants.screenW * 10 / 22;
        bannerAdapter = new MyBannerAdapter(rollPagerView, getActivity());
        imgs = new ArrayList<>();
        rollPagerView.setAdapter(bannerAdapter);
        getImgs();
        // 设置播放时间间隔
        rollPagerView.setPlayDelay(3000);
        // 设置透明度
        rollPagerView.setAnimationDurtion(500);
        // 设置指示器（顺序依次）
        rollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.parseColor("#00bbff"), Color.WHITE));

        team = (LinearLayout) headView.findViewById(R.id.team);
        wenwen = (LinearLayout) headView.findViewById(R.id.wenwen);
        secondHand = (LinearLayout) headView.findViewById(R.id.secondhand);
        scheduleCard = (LinearLayout) headView.findViewById(R.id.schedulecard);
        makeFriends = (LinearLayout) headView.findViewById(R.id.makefriends);
        makeFriends.setVisibility(View.GONE);
        secondHand.setVisibility(View.GONE);
        scheduleCard.setVisibility(View.GONE);
        team.setOnClickListener(this);
        wenwen.setOnClickListener(this);
        secondHand.setOnClickListener(this);
        scheduleCard.setOnClickListener(this);
        makeFriends.setOnClickListener(this);

        tv4 = (TextView) headView.findViewById(R.id.tv4);
        tv4.setSelected(true);
        tv4.setOnClickListener(this);
        tv5 = (TextView) headView.findViewById(R.id.tv5);
        tv5.setOnClickListener(this);
        tv6 = (TextView) headView.findViewById(R.id.tv6);
        tv6.setOnClickListener(this);


        llVis = (LinearLayout) headView.findViewById(R.id.llVis);

        rv.setAdapter(new RecycleAdapter(getActivity()));
        rv.setLoadingListener(this);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llVis != null) {
                    if (getScrollYDistance() + llVis.getTop() < 0) {
                        llInvis.setVisibility(View.VISIBLE);
                    } else {
                        llInvis.setVisibility(View.GONE);
                    }
                }
            }
        });

        vp = (CustomViewPager) headView.findViewById(R.id.vp);
        vp.setCanScroll(true);
        pagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        rv.addHeaderView(headView);
        vp.setAdapter(pagerAdapter);
        vp.setOffscreenPageLimit(2);
        vp.setId(fragmentList.get(0).hashCode());
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                item = position + 1;
                setTextBg(position + 1);
                vp.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        bannerAdapter.setonBanneritemClickListener(position -> {
            Intent intent = new Intent(getActivity(), WebActivity.class).putExtra("url", imgsData.get(position).getLinkUrl());
            startActivity(intent);
        });
    }

    // 获取首页轮播图
    private void getImgs() {
        setBodyParams(new String[]{"category"}, new String[]{"" + 1});
        sendPostConnection(Constants.base_url + "/api/pub/category/advertisement.do", getimgsCode, Constants.token);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.team:
                startActivity(new Intent(mContext, TeamActivity.class));
                break;
            case R.id.wenwen:
                startActivity(new Intent(mContext, WenwenActivity.class));
                break;
            case R.id.secondhand:
                startActivity(new Intent(mContext, SecondHandActivity.class));
                break;
            case R.id.schedulecard:
                startActivity(new Intent(mContext, TimetableCheckin.class));
                break;
            case R.id.makefriends:
                startActivity(new Intent(mContext, GPSActivity.class));
                break;
            case R.id.llSearch:
                Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ivSecondMa:
                //
                startActivity(new Intent(mContext, CaptureActivity.class));
                break;
            case R.id.tv1:
            case R.id.tv4:
                if (item == 1) {
                    return;
                }
                item = 1;
                setTextBg(item);
                break;
            case R.id.tv2:
            case R.id.tv5:
                if (item == 2) {
                    return;
                }
                item = 2;
                setTextBg(item);
                break;
            case R.id.tv3:
            case R.id.tv6:
                if (item == 3) {
                    return;
                }
                item = 3;
                setTextBg(item);
                break;
        }
    }

    private void setTextBg(int item) {
        clearAllBg();
        vp.setCurrentItem(item - 1);
        if (item == 1) {
            tv1.setSelected(true);
            tv4.setSelected(true);
        } else if (item == 2) {
            tv2.setSelected(true);
            tv5.setSelected(true);
        } else if (item == 3) {
            tv3.setSelected(true);
            tv6.setSelected(true);
        }
    }

    private void clearAllBg() {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        tv4.setSelected(false);
        tv5.setSelected(false);
        tv6.setSelected(false);
    }

    public int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        return firstVisiableChildView.getTop();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: " + item);
        if (item == 1) {
            ChildFragment1 fragment1 = (ChildFragment1) pagerAdapter.getItem(0);
            fragment1.refData();
        } else if (item == 2) {
            ChildFragment2 fragment2 = (ChildFragment2) pagerAdapter.getItem(1);
            fragment2.getData(true);
        } else if (item == 3) {
            new Handler().postDelayed(() -> {
                EventBus.getDefault().post(new RefHotActivityEvent(1));
                rv.refreshComplete();
            }, 1000);
        }
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore: " + item);
        if (item == 1) {
            ChildFragment1 fragment1 = (ChildFragment1) pagerAdapter.getItem(0);
            fragment1.loaData();
        } else if (item == 2) {
            ChildFragment2 fragment2 = (ChildFragment2) pagerAdapter.getItem(1);
            fragment2.getData(false);
        } else if (item == 3) {
            new Handler().postDelayed(() -> {
                EventBus.getDefault().post(new RefHotActivityEvent(2));
                rv.loadMoreComplete();
            }, 1000);
        }
    }

    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter("fragment.listener");
        getActivity().registerReceiver(brodcast, filter);
    }

    private class FragmentBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);
            if (items == 1) {    //加载
                if (rv != null) {
                    rv.loadMoreComplete();
                }
            } else if (items == 2) {
                ChildFragment2 fragment2 = (ChildFragment2) pagerAdapter.getItem(1);
                fragment2.getData(true);
            } else if (items == 3) {   //刷新
                if (rv != null) {
                    rv.refreshComplete();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanResultEvent(ScanResultEvent event) {
        if (event.getResult() == null) {
            return;
        }
        try {
            /*int id = Integer.parseInt(event.getResult());
            startActivity(new Intent(getActivity(),ApplyJoinTDActivity.class).putExtra("id",id));*/
            if (event.getResult().contains("XYTeam_")) {
                int id = Integer.parseInt(event.getResult().replace("XYTeam_", ""));
                startActivity(new Intent(getActivity(), TeamDetailActivity2.class).putExtra("id", id));
            } else {
                Utils.toastShort(mContext, "请扫描高校联盟团队的二维码");
            }
        } catch (Exception e) {
            Utils.toastShort(mContext, "二维码不合法，识别失败");
        }
    }

    @Override
    public void onDestroy() {
        if (brodcast != null) {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                fragmentActivity.unregisterReceiver(brodcast);
            }
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
