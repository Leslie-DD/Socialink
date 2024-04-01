package com.leslie.socialink.activity.newsencond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.SecondhandgoodBean;
import com.leslie.socialink.classification.ClassificationBean;
import com.leslie.socialink.classification.ClassifySecondaryBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.HomeBanner;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SecondHandActivity extends NetWorkActivity {

    public static final String LOCAL_BROADCAST = "ClassifyPosition";

    private final Gson gson = new Gson();

    private final List<Integer> mTypeList = new ArrayList<>();

    private final List<String> mSearchList = new ArrayList<>();
    private final List<String> mPicList = new ArrayList<>();
    private List<SecondhandgoodBean> mGoodList = new ArrayList<>();
    private final List<String> images = new ArrayList<>();
    private SecondHandAdapter adapter;

    private XRefreshView xRefreshView;

    /**
     * 横向分类RecyclerView
     */
    private final List<ClassifySecondaryBean> classifySecondaryBeanList = new ArrayList<>();

    /**
     * 商品部分参数
     */
    private int category1Id;
    private int category2Id;
    private int pn = 1;
    private int ps = 8;
    private int totalPage = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrefresh_test);

        initParam();
        initSearchData();
        initPicData();

        adapter = new SecondHandAdapter(this, mTypeList);
        RecyclerView rcv_test = findViewById(R.id.rv_xrefresh_test);
        rcv_test.setLayoutManager(new LinearLayoutManager(this));
//        rcv_test.setHasFixedSize(false);
        rcv_test.setAdapter(adapter);

        adapter.setSearchDataList(mSearchList);
        adapter.setPicDataList(mPicList);

        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setPullLoadEnable(true);       //允许加载更多
        xRefreshView.setPullRefreshEnable(true);    //允许下拉刷新
        xRefreshView.setAutoLoadMore(true);         //滑动到底部自动加载更多

        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                category2Id = 0;
                adapter.setPosi(0);
                xRefreshView.stopRefresh();
                pn = 1;
                mGoodList.clear();
                adapter.setGoodDataList(mGoodList);
                getData(100);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pn++;
                if (pn > totalPage) {
                    new Handler().postDelayed(() -> xRefreshView.setLoadComplete(true), 1000);
                } else {
                    getData(103);
                }
                xRefreshView.stopLoadMore();
            }
        });

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        LocalReceiver localReceiver = new LocalReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOCAL_BROADCAST);   //添加action
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        getBannerImages();
        getCategory();
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        switch (where) {
            case 1000:  // 获取轮播图数据
                if (result.optInt("code") == 0) {
                    if (result.has("data") && !result.optString("data").isEmpty()) {
                        ArrayList<HomeBanner> imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBanner>>() {
                        }.getType());
                        if (imgsData != null && imgsData.size() > 0) {
                            for (HomeBanner bannerImgsBean : imgsData) {
                                String img = Constants.BASE_URL + bannerImgsBean.getCoverImage();
                                images.add(img);
                            }

                        } else {
                        }
                        adapter.setBannerDataList(images, imgsData);
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case 102:   // 获取分类数据
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

                category1Id = classifySecondaryBeanList.get(0).getCategory1Id();
                category2Id = classifySecondaryBeanList.get(0).getCategory2Id();
                adapter.setClassifyDataList(classifySecondaryBeanList);
                getData(100);   // 获取最初推荐商品
                break;

            case 100:   // 获取最初推荐商品
                mGoodList.clear();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        mGoodList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (mGoodList == null || mGoodList.size() == 0) {
                            mGoodList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    }
                }
                adapter.setGoodDataList(mGoodList);
                break;
            case 101:   // 获取其他分类的商品
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        mGoodList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (mGoodList == null || mGoodList.size() == 0) {
                            mGoodList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    }
                }
                adapter.setGoodDataList(mGoodList);
                break;

            case 103:   // 获取当前分类的更多商品
                mGoodList.clear();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        mGoodList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (mGoodList == null || mGoodList.size() == 0) {
                            mGoodList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    }
                }

                adapter.addGoodDataList(mGoodList);
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    // 获取首页轮播图
    private void getBannerImages() {
        setBodyParams(new String[]{"category"}, new String[]{"" + 1});
        sendPost(Constants.BASE_URL + "/api/pub/category/advertisement.do", 1000, Constants.token);
    }

    // 获取二级分类
    private void getCategory() {
        sendPost(WenConstans.SecondhandClassify, 102, Constants.token);
    }

    // 获取商品
    private void getData(int where) {
        if (category2Id == 0 && category1Id == 0) {
            sendPost(WenConstans.SecondhandRecommend, where, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "category2_id"}, new String[]{pn + "", ps + "", category2Id + ""});
            sendPost(WenConstans.Secondhand, where, Constants.token);
        }
    }

    private void initParam() {
        mTypeList.add(3);   // Search
        mTypeList.add(2);   // pics
        mTypeList.add(0);   // classifys
        mTypeList.add(1);   // goods
    }

    private void initSearchData() {
        mSearchList.add("搜索商品");
    }

    private void initPicData() {
        mPicList.add("我是Banner1！");
    }

    /**
     * 自定义广播接收者
     */
    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, LOCAL_BROADCAST)) {
                category2Id = intent.getIntExtra("category2Id", 0);
                adapter.setPosi(intent.getIntExtra("position", 0));

                pn = 1;
                xRefreshView.setLoadComplete(false);
                mGoodList.clear();
                adapter.setGoodDataList(mGoodList);
                getData(101);
            }
        }
    }
}