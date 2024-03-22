package com.hnu.heshequ.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.HomeSearchActivity;
import com.hnu.heshequ.activity.WebActivity;
import com.hnu.heshequ.activity.flowview.TeamActivity;
import com.hnu.heshequ.activity.flowview.WenwenActivity;
import com.hnu.heshequ.activity.friend.GPSActivity;
import com.hnu.heshequ.activity.newsencond.SecondHandActivity;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.activity.timetable.TimetableCheckin;
import com.hnu.heshequ.adapter.BannerAdapter;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.adapter.recycleview.RecycleAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.HomeBannerImgsBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefHotActivityEvent;
import com.hnu.heshequ.entity.ScanResultEvent;
import com.hnu.heshequ.secondma.android.CaptureActivity;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CustomViewPager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends NetWorkFragment implements XRecyclerView.LoadingListener {

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
    private BannerAdapter bannerAdapter;
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
            imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBannerImgsBean>>() {
            }.getType());
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
        ivSecondMa.setOnClickListener(v -> startActivity(new Intent(mContext, CaptureActivity.class)));
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), HomeSearchActivity.class)));
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv1.setSelected(true);
        tv1.setOnClickListener(v -> onTv1Clicked());
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv2.setOnClickListener(v -> onTv2Clicked());
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv3.setOnClickListener(v -> onTv3Clicked());
        llInvis = (LinearLayout) view.findViewById(R.id.llInVis);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(getActivity(), true, true, rv);
        initHeadView();
    }

    private void initHeadView() {
        fragmentList = new ArrayList<>();
        ChildFragment2 fragment2 = new ChildFragment2();
        fragment2.setType(3);
        fragmentList.add(new HotTeamFragment());
        fragmentList.add(fragment2);
        fragmentList.add(new HotActivityFragment());

        headView = getActivity().getLayoutInflater().inflate(R.layout.home_head_view, null);
        rollPagerView = (RollPagerView) headView.findViewById(R.id.rp);
        ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
        params.height = ConsTants.screenW * 10 / 22;
        bannerAdapter = new BannerAdapter(rollPagerView, getActivity());
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

        team.setOnClickListener(v -> startActivity(new Intent(mContext, TeamActivity.class)));
        wenwen.setOnClickListener(v -> startActivity(new Intent(mContext, WenwenActivity.class)));
        secondHand.setOnClickListener(v -> startActivity(new Intent(mContext, SecondHandActivity.class)));
        scheduleCard.setOnClickListener(v -> startActivity(new Intent(mContext, TimetableCheckin.class)));
        makeFriends.setOnClickListener(v -> startActivity(new Intent(mContext, GPSActivity.class)));

        // 隐藏 menu
        headView.findViewById(R.id.menu).setVisibility(View.GONE);

        tv4 = (TextView) headView.findViewById(R.id.tv4);
        tv4.setSelected(true);
        tv4.setOnClickListener(v -> onTv1Clicked());
        tv5 = (TextView) headView.findViewById(R.id.tv5);
        tv5.setOnClickListener(v -> onTv2Clicked());
        tv6 = (TextView) headView.findViewById(R.id.tv6);
        tv6.setOnClickListener(v -> onTv3Clicked());


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

        bannerAdapter.setonBannerItemClickListener(position -> {
            Intent intent = new Intent(getActivity(), WebActivity.class).putExtra("url", imgsData.get(position).getLinkUrl());
            startActivity(intent);
        });
    }

    // 获取首页轮播图
    private void getImgs() {
        setBodyParams(new String[]{"category"}, new String[]{"" + 1});
        sendPostConnection(Constants.base_url + "/api/pub/category/advertisement.do", getimgsCode, Constants.token);
    }

    private void onTv3Clicked() {
        if (item == 3) {
            return;
        }
        item = 3;
        setTextBg(item);
    }

    private void onTv2Clicked() {
        if (item == 2) {
            return;
        }
        item = 2;
        setTextBg(item);
    }

    private void onTv1Clicked() {
        if (item == 1) {
            return;
        }
        item = 1;
        setTextBg(item);
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
            HotTeamFragment fragment1 = (HotTeamFragment) pagerAdapter.getItem(0);
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
//        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
//        if (layoutManager == null) {
//            return;
//        }
//        boolean atBottom = false;
//
//        // 在滚动时检查是否滚动到底部
//        int visibleItemCount = layoutManager.getChildCount();
//        int totalItemCount = layoutManager.getItemCount();
//        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//        atBottom = pastVisibleItems + visibleItemCount >= totalItemCount;
//
//        Log.d(TAG, String.format("onLoadMore: %d, visibleItemCount: %d, total: %d, first position: %d", item, visibleItemCount, totalItemCount, pastVisibleItems));
//        if (!atBottom) {
//            Log.d(TAG, "onLoadMore: not bottom");
//            return;
//        }

        Log.d(TAG, "onLoadMore item: " + item);
        if (item == 1) {
            HotTeamFragment fragment1 = (HotTeamFragment) pagerAdapter.getItem(0);
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter("fragment.listener");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getActivity().registerReceiver(brodcast, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            getActivity().registerReceiver(brodcast, filter);
        }
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
                startActivity(new Intent(getActivity(), TeamDetailActivity.class).putExtra("id", id));
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
