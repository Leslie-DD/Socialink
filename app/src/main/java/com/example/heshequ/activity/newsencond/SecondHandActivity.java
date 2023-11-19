package com.example.heshequ.activity.newsencond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.HomeBannerImgsBean;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.classification.ClassificationBean;
import com.example.heshequ.classification.ClassifySecondaryBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @FileName: XRefreshViewTestActivity
 * @Author: Ding Yifan
 * @CreateDate: 2021/1/3
 * @CreateTime: 19:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/3
 * @UpdateTime: 19:45
 * @UpdateRemark: 更新说明
 * @Description:
 */
public class SecondHandActivity extends NetWorkActivity implements View.OnClickListener {

    /************** 广播 *****************************************************************************/
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    public static final String LOCAL_BROADCAST = "ClassifyPosition";
    /*************************************************************************************************/

    private Gson gson = new Gson();

    private List<Integer> mTypeList = new ArrayList<>();

    private List<String> mSearchList = new ArrayList<>();
    private List<String> mPicList = new ArrayList<>();
    private List<SecondhandgoodBean> mGoodList = new ArrayList<>();
    private ArrayList<HomeBannerImgsBean> imgsData;
    private List<String> imgs = new ArrayList<>();
    private SecondHandAdapter adapter;

    private XRefreshView xRefreshView;
    private int mLoadCount = 0;

    /*************************** 横向分类RecyclerView ************************************************/
    private List<ClassifySecondaryBean> classifySecondaryBeanList = new ArrayList<>();
    private int position = 0;

    /*************************** 商品部分参数 ********************************************************/
    private int category1Id;
    private int category2Id;
    private int pn = 1;
    private int ps = 8;
    private int totalPage = 1;

    /*************************************************************************************************/

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
        rcv_test.setHasFixedSize(false);
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
                    new Handler().postDelayed(() -> {
                        xRefreshView.setLoadComplete(true);
//                            xRefreshView.stopLoadMore();
                    }, 1000);
                } else {
                    getData(103);
                }
                xRefreshView.stopLoadMore();
            }
        });


        /**
         * 注册广播
         */
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localReceiver = new LocalReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCAL_BROADCAST);   //添加action
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);   //注册本地广播

        getImgs();
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
                        imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBannerImgsBean>>() {
                        }.getType());
                        if (imgsData != null && imgsData.size() > 0) {
                            for (HomeBannerImgsBean bannerImgsBean : imgsData) {
                                String img = Constants.base_url + bannerImgsBean.getCoverImage();
                                imgs.add(img);
                            }

                        } else {
                        }
                        adapter.setBannerDataList(imgs, imgsData);
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
                    if (data != null && data.has("list")) {
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
                    if (data != null && data.has("list")) {
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
                    if (data != null && data.has("list")) {
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

    //获取首页轮播图
    private void getImgs() {
        setBodyParams(new String[]{"category"}, new String[]{"" + 1});
        sendPost(Constants.base_url + "/api/pub/category/advertisement.do", 1000, Constants.token);
    }

    // 获取二级分类
    private void getCategory() {
        sendPost(WenConstans.SecondhandClassify, 102, WenConstans.token);
    }

    // 获取商品
    private void getData(int where) {
        if (category2Id == 0 && category1Id == 0) {
            sendPost(WenConstans.SecondhandRecommend, where, WenConstans.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "category2_id"}, new String[]{pn + "", ps + "", category2Id + ""});
            sendPost(WenConstans.Secondhand, where, WenConstans.token);
        }
    }

    @Override
    public void onClick(View view) {

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
            if (action.equals(LOCAL_BROADCAST)) {
                category2Id = intent.getIntExtra("category2Id", 0);
                position = intent.getIntExtra("position", 0);

                adapter.setPosi(position);

                pn = 1;
                xRefreshView.setLoadComplete(false);
                mGoodList.clear();
                adapter.setGoodDataList(mGoodList);
                getData(101);
            }
        }
    }
}