package com.hnu.heshequ.questions;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.WebActivity;
import com.hnu.heshequ.activity.WwSearchActivity;
import com.hnu.heshequ.activity.wenwen.SendQuestionActivity;
import com.hnu.heshequ.adapter.BannerAdapter;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.adapter.recycleview.RecycleAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.HomeBannerImgsBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.fragment.ChildFragment2;
import com.hnu.heshequ.fragment.ChildWwFragment;
import com.hnu.heshequ.fragment.ZcFragment;
import com.hnu.heshequ.utils.StatusBarUtil;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CustomViewPager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionsFragment extends NetWorkFragment implements XRecyclerView.LoadingListener {
    private static final String TAG = "[QuestionsFragment]";
    private View view;
    private ImageView addWenwen;
    private XRecyclerView rv;
    private ImageView llSearch;
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
    private FragmentBrodcast brodcast;
    private Intent intentActivity;
    private final int getimgsCode = 1000;
    private ArrayList<HomeBannerImgsBean> imgsData;
    private Gson gson;
    ArrayList<String> imgs;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where != getimgsCode) {
            return;
        }
        if (result.optInt("code") != 0) {
            Utils.toastShort(mContext, result.optString("msg"));
            return;
        }
        if (!result.has("data") || result.optString("data").isEmpty()) {
            return;
        }
        imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBannerImgsBean>>() {
        }.getType());

        Log.d(TAG, "imgsData" + imgsData.size());
        if (imgsData != null && !imgsData.isEmpty()) {
            Log.d(TAG, "imgsData" + imgsData.size());
            for (HomeBannerImgsBean bannerImgsBean : imgsData) {
                imgs.add(Constants.base_url + bannerImgsBean.getCoverImage());
            }
        }
        bannerAdapter.setData(imgs);

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_questions, null);
        RelativeLayout titleLayout = view.findViewById(R.id.title_layout);
        StatusBarUtil.setMarginStatusBar(titleLayout);
        init();
        getBanner();
        return view;
    }

    private void init() {
        gson = new Gson();
        setFragmentListener();
        addWenwen = (ImageView) view.findViewById(R.id.add_wenwen);
        addWenwen.setOnClickListener(v -> {
            intentActivity = new Intent(getActivity(), SendQuestionActivity.class);
            startActivity(intentActivity);
        });
        llSearch = (ImageView) view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> {
            intentActivity = new Intent(getActivity(), WwSearchActivity.class);
            startActivity(intentActivity);
        });
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv1.setSelected(true);
        tv1.setOnClickListener(v -> {
            if (item == 1) {
                return;
            }
            item = 1;
            setTextBg(item);
        });
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv2.setOnClickListener(v -> {
            if (item == 2) {
                return;
            }
            item = 2;
            setTextBg(item);
        });
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv3.setOnClickListener(v -> {
            if (item == 3) {
                return;
            }
            item = 3;
            setTextBg(item);
        });
        llInvis = (LinearLayout) view.findViewById(R.id.llInVis);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(getActivity(), true, true, rv);
        initHeadView();
        rv.setAdapter(new RecycleAdapter(getActivity()));
        rv.setLoadingListener(this);
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (getScollYDistance() + llVis.getTop() < 0) {
                    llInvis.setVisibility(View.VISIBLE);
                } else {
                    llInvis.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getBanner() {
        setBodyParams(new String[]{"category"}, new String[]{"3"});
        sendPostConnection(WenConstans.Advantice, 100, WenConstans.token);
    }

    private void initHeadView() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ChildWwFragment());
        fragmentList.add(new ChildFragment2());
        fragmentList.add(new ZcFragment());
        headView = getActivity().getLayoutInflater().inflate(R.layout.wenwen_head_view, null);
        rollPagerView = (RollPagerView) headView.findViewById(R.id.rp);
        ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
        params.height = ConsTants.screenW * 10 / 22;
        bannerAdapter = new BannerAdapter(rollPagerView, getActivity());
        imgs = new ArrayList<>();
        rollPagerView.setAdapter(bannerAdapter);
        // 设置播放时间间隔
        rollPagerView.setPlayDelay(3000);
        // 设置透明度
        rollPagerView.setAnimationDurtion(500);
        // 设置指示器（顺序依次）
        rollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.parseColor("#00bbff"), Color.WHITE));
        tv4 = (TextView) headView.findViewById(R.id.tv4);
        tv4.setSelected(true);
        tv4.setOnClickListener(v -> {
            if (item == 1) {
                return;
            }
            item = 1;
            setTextBg(item);
        });
        tv5 = (TextView) headView.findViewById(R.id.tv5);
        tv5.setOnClickListener(v -> {
            if (item == 2) {
                return;
            }
            item = 2;
            setTextBg(item);
        });
        tv6 = (TextView) headView.findViewById(R.id.tv6);
        tv6.setOnClickListener(v -> {
            if (item == 3) {
                return;
            }
            item = 3;
            setTextBg(item);
        });

        llVis = (LinearLayout) headView.findViewById(R.id.llVis);
        vp = (CustomViewPager) headView.findViewById(R.id.vp);
        vp.setCanScroll(true);
        pagerAdapter = new MyFragmentPagerAdapter(requireActivity().getSupportFragmentManager(), fragmentList);

        //vp.setId(fragmentList.get(2).hashCode());
        rv.addHeaderView(headView);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vp.resetHeight(position);
                item = position + 1;
                setTextBg(item);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Handler().postDelayed(() -> {
            vp.setAdapter(pagerAdapter);
            vp.setOffscreenPageLimit(2);
            vp.setCurrentItem(0);
        }, 100);

        getImgs();

        bannerAdapter.setonBannerItemClickListener(position -> {
            startActivity(new Intent(getActivity(), WebActivity.class)
                    .putExtra("url", imgsData.get(position).getLinkUrl()));
        });
    }

    //获取首页轮播图
    private void getImgs() {
        setBodyParams(new String[]{"category"}, new String[]{"" + 3});
        sendPostConnection(Constants.base_url + "/api/pub/category/advertisement.do", getimgsCode, Constants.token);
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

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        return firstVisiableChildView.getTop();
    }

    @Override
    public void onRefresh() {
        if (item == 1) {
            ChildWwFragment childWwFragment = (ChildWwFragment) pagerAdapter.getItem(0);
            childWwFragment.getData(true);
        } else if (item == 2) {
            ChildFragment2 fragment2 = (ChildFragment2) pagerAdapter.getItem(1);
            fragment2.setType(2);
            fragment2.getData(true);
        } else if (item == 3) {
            ZcFragment fragment3 = (ZcFragment) pagerAdapter.getItem(2);
            fragment3.getData(true);
            //            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    rv.refreshComplete();
//                }
//            },1000);
        }

    }

    @Override
    public void onLoadMore() {
        if (item == 1) {
            ChildWwFragment childWwFragment = (ChildWwFragment) pagerAdapter.getItem(0);
            childWwFragment.getData(false);
        } else if (item == 2) {
            ChildFragment2 fragment2 = (ChildFragment2) pagerAdapter.getItem(1);
            fragment2.getData(false);
        } else if (item == 3) {
            ZcFragment fragment3 = (ZcFragment) pagerAdapter.getItem(2);
            fragment3.getData(false);
            //            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    rv.refreshComplete();
//                }
//            },1000);
        }
    }

    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("fragment.listener");
        brodcast = new FragmentBrodcast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getActivity().registerReceiver(brodcast, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            getActivity().registerReceiver(brodcast, filter);
        }
    }

    private class FragmentBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);

            if (items == 1) {    //加载
                rv.loadMoreComplete();
            } else if (items == 2) {
                ChildFragment2 fragment2 = (ChildFragment2) pagerAdapter.getItem(1);
                fragment2.getData(true);
                ChildWwFragment childWwFragment = (ChildWwFragment) pagerAdapter.getItem(0);
                childWwFragment.getData(true);
            } else if (items == 3) {   //刷新
                rv.refreshComplete();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (brodcast != null) {
            getActivity().unregisterReceiver(brodcast);
        }
    }
}
