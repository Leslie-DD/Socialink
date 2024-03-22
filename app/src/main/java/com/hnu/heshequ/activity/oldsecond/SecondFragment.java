package com.hnu.heshequ.activity.oldsecond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.WebActivity;
import com.hnu.heshequ.activity.newsencond.SecondhandPostActivity;
import com.hnu.heshequ.adapter.BannerAdapter;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.adapter.recycleview.RecycleAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.HomeBannerImgsBean;
import com.hnu.heshequ.classification.ClassifationActivity;
import com.hnu.heshequ.classification.ClassificationBean;
import com.hnu.heshequ.classification.ClassifySecondaryBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CustomViewPager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * FileName: SecondFragment
 * Author: Ding Yifan
 * Data: 2020/9/2
 * Time: 8:47
 * Description: 二手商品主页面
 */
public class SecondFragment extends NetWorkFragment implements XRecyclerView.LoadingListener {

    /**
     * 通过广播 实现ClassifyAdatper调用此Fragment的setTvBg()方法
     */
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;    //本地广播接收者
    private LocalBroadcastManager localBroadcastManager;   //本地广播管理者   可以用来注册广播
    // 发送本地广播的action
    public static final String LOCAL_BROADCAST = "PageView.Click.LOCAL_BROADCAST";
    public static final String HEIGHT_BROADCAST = "PageView.Height.LOCAL_HEIGHTCAST";

    private View view;
    private View headView;
    private XRecyclerView rv;
    private CustomViewPager vp;
    private LinearLayout llVis;
    private LinearLayout llSearch;
    private ArrayList<Fragment> list;

    private MyFragmentPagerAdapter adapter;
    private int status = -1;
    private ImageView ivRight;
    private ClassifyAdapter classifyAdapter;    // 横向商品分类Adapter
    private RecyclerView classifyRecyclerView;  // 横向商品分类
    private ImageButton ib_classifacation;      // 二级商品分类按钮
    private SecondClassifyFragment[] fragments; // 各个二级分类

    private RollPagerView rollPagerView;    // 轮播图
    private BannerAdapter bannerAdapter;
    private final int getimgsCode = 1000;
    private ArrayList<HomeBannerImgsBean> imgsData;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;
    private int position;

    private ArrayList<ClassifySecondaryBean> classifySecondaryBeanList = new ArrayList<>();

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case getimgsCode:
                if (result.optInt("code") == 0) {
                    if (result.has("data") && !result.optString("data").isEmpty()) {
                        imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBannerImgsBean>>() {
                        }.getType());
                        if (imgsData != null && imgsData.size() > 0) {
                            for (HomeBannerImgsBean bannerImgsBean : imgsData) {
                                imgs.add(Constants.base_url + bannerImgsBean.getCoverImage());
                            }
                        }
                        bannerAdapter.setData(imgs);
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case 102:
                String json = result.toString();
                ClassificationBean classificationBean = com.alibaba.fastjson.JSONObject.parseObject(json, ClassificationBean.class);
                ClassifySecondaryBean classifySecondaryBean = new ClassifySecondaryBean("", "推荐", 0, 0);
                classifySecondaryBeanList.add(classifySecondaryBean);
                for (int i = 0; i < classificationBean.getData().size(); i++) {
                    String category1Name = classificationBean.getData().get(i).getCategory1Name();
                    String category2Name = classificationBean.getData().get(i).getCategory2List().get(0).getCategory2Name();
                    int category1Id = classificationBean.getData().get(i).getCategory1Id();
                    int category2Id = classificationBean.getData().get(i).getCategory2List().get(0).getCategory2Id();
                    classifySecondaryBean = new ClassifySecondaryBean(category1Name, category2Name, category1Id, category2Id);
                    classifySecondaryBeanList.add(classifySecondaryBean);
                }
                classifyAdapter = new ClassifyAdapter(classifySecondaryBeanList, getContext());
                classifyRecyclerView.setAdapter(classifyAdapter);
                fragments = new SecondClassifyFragment[classifySecondaryBeanList.size()];

                for (int i = 0; i < classifySecondaryBeanList.size(); i++) {
                    int category1Id = classifySecondaryBeanList.get(i).getCategory1Id();
                    int category2Id = classifySecondaryBeanList.get(i).getCategory2Id();
                    fragments[i] = new SecondClassifyFragment(category1Id, category2Id);
                    list.add(fragments[i]);
                }

                adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), list);
                vp.setAdapter(adapter);
                vp.setCurrentItem(0);
                setTvBg(0);
                event();

        }
    }

    @Override
    protected void onFailure(String result, int where) {
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_second, null);

        // second_head_view 包含轮播图和分类框部分
        headView = getActivity().getLayoutInflater().inflate(R.layout.second_head_view, null);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localReceiver = new LocalReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCAL_BROADCAST);
        intentFilter.addAction(HEIGHT_BROADCAST);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        classifyRecyclerView = (RecyclerView) headView.findViewById(R.id.recycler_view);
        classifyRecyclerView.setLayoutManager(layoutManager);

        init();
        return view;
    }

    private void init() {
        ivRight = view.findViewById(R.id.ivRight);
        ivRight.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.kj2));
        llSearch = view.findViewById(R.id.llSearch);
        llSearch.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("category2_id", 0);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        ivRight.setOnClickListener(v -> startActivity(new Intent(mContext, SecondhandPostActivity.class)));

        rv = (XRecyclerView) view.findViewById(R.id.rv);    // rv是除搜索框外的其他部件范围
        ConsTants.initXRecycleView(getActivity(), true, true, rv);

        rv.setAdapter(new RecycleAdapter(getActivity()));
        rv.setLoadingListener(this);

        rv.addHeaderView(headView);
        vp = (CustomViewPager) headView.findViewById(R.id.vp);    // CustomViewPage  自定义viewpage

        list = new ArrayList<>();
        initHeadView();
    }

    private void initHeadView() {
        rollPagerView = headView.findViewById(R.id.rp);
        ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
        params.height = ConsTants.screenW * 10 / 22;
        bannerAdapter = new BannerAdapter(rollPagerView, getContext());
        imgs = new ArrayList<>();
        rollPagerView.setAdapter(bannerAdapter);
        getImgs();
        getCategory();
        // 设置播放时间间隔、透明度、指示器（顺序依次）
        rollPagerView.setPlayDelay(3000);
        rollPagerView.setAnimationDurtion(500);
        rollPagerView.setHintView(new ColorPointHintView(getContext(), Color.parseColor("#00bbff"), Color.WHITE));
        bannerAdapter.setonBannerItemClickListener(position ->
                startActivity(new Intent(getContext(), WebActivity.class)
                        .putExtra("url", imgsData.get(position).getLinkUrl())
                ));

        llVis = (LinearLayout) headView.findViewById(R.id.llVis);

        ib_classifacation = (ImageButton) headView.findViewById(R.id.second_classifation);
        ib_classifacation.setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), ClassifationActivity.class);
            startActivity(intent2);
        });
    }

    //获取首页轮播图
    private void getImgs() {
        setBodyParams(new String[]{"category"}, new String[]{"" + 1});
        sendPostConnection(Constants.base_url + "/api/pub/category/advertisement.do", getimgsCode, Constants.token);
    }

    // 获取二级分类
    private void getCategory() {
        sendPostConnection(WenConstans.SecondhandClassify, 102, WenConstans.token);
    }

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
        }
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }

    private void event() {
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                vp.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 自定义广播接收者
     */
    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, LOCAL_BROADCAST)) {
                position = intent.getIntExtra("position", 0);
                classifyRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                setTvBg(position);
            } else if (TextUtils.equals(action, HEIGHT_BROADCAST)) {
            }
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(() -> {
            Utils.toastShort(mContext, "父Fragment通知子Fragment加载更多");
            fragments[position].loadMore();
            rv.loadMoreComplete();
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);    //取消广播的注册
    }
}
