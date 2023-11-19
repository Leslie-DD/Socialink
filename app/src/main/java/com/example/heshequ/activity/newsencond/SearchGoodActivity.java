package com.example.heshequ.activity.newsencond;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.andview.refreshview.XRefreshView;
import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchGoodActivity extends NetWorkActivity implements View.OnClickListener {

    private int beforeType;
    private int category2Id;
    private String content;

    private int pn = 1;
    private int ps = 10;
    private int totalPage = 1;

    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;

    private LinearLayout llBack;
    private LinearLayout llSearch;
    private EditText etContent;
    private LinearLayout llTip;

    private GoodAdapter goodAdapter;
    private List<SecondhandgoodBean> secondhandgoodBeanList = new ArrayList<>();

    private Gson gson = new Gson();

    private static final int INDEX_TYPE = 0;
    private static final int CLASS_TYPE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_good);

        initView();

        Bundle bundle = this.getIntent().getExtras();
        beforeType = bundle.getInt("type", 0);
//        Utils.toastShort(context, beforeType==CLASS_TYPE?"beforeType: CLASS_TYPE":"beforeType: INDEX_TYPE");
        if (beforeType == CLASS_TYPE) {
            category2Id = bundle.getInt("category2_id", 0);
            getClassifyData();
        }

    }

    private void initView() {

        xRefreshView = findViewById(R.id.search_good_xrv);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setPullLoadEnable(true);       //允许加载更多
        xRefreshView.setPullRefreshEnable(true);    //允许下拉刷新
//        xRefreshView.setAutoLoadMore(true);         //滑动到底部自动加载更多

        recyclerView = findViewById(R.id.search_good_rv);
        etContent = findViewById(R.id.search_good_et);
        llBack = findViewById(R.id.search_good_llBack);
        llBack.setOnClickListener(this);
        llSearch = findViewById(R.id.search_good_llSearch);
        llSearch.setOnClickListener(this);

        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {

                pn = 1;
                xRefreshView.setLoadComplete(false);
                secondhandgoodBeanList.clear();
                goodAdapter.notifyDataSetChanged();
                if (beforeType == CLASS_TYPE) {
                    getClassifyData();
                } else {
                    content = etContent.getText().toString();
                    getSearchData();
                }
                xRefreshView.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pn++;
                if (pn > totalPage) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            xRefreshView.setLoadComplete(true);
//                            xRefreshView.stopLoadMore();
                        }
                    }, 1000);
                } else {
                    if (beforeType == CLASS_TYPE) {
                        getMoreClassifyData();
                    } else {
                        content = etContent.getText().toString();
                        getMoreData();
                    }
                }
                xRefreshView.stopLoadMore();
            }
        });

    }


    private void getSearchData() {
//        Utils.toastShort(context, "商品搜索："+content);
        setBodyParams(new String[]{"pn", "ps", "keyword"}, new String[]{"" + pn, "" + ps, "" + content});
        sendPost(Constants.base_url + "/api/goods/base/search.do", 100, Constants.token);
    }

    private void getClassifyData() {
//        Utils.toastShort(context, "分类搜索 category2Id=" + category2Id);
        setBodyParams(new String[]{"pn", "ps", "category2_id"}, new String[]{"" + pn, "" + ps, category2Id + ""});
        sendPost(WenConstans.Secondhand, 100, WenConstans.token);
    }

    private void getMoreData() {
//        Utils.toastShort(context, "更多搜索商品" + category2Id);
        setBodyParams(new String[]{"pn", "ps", "keyword"}, new String[]{"" + pn, "" + ps, "" + content});
        sendPost(Constants.base_url + "/api/goods/base/search.do", 102, Constants.token);
    }

    private void getMoreClassifyData() {
//        Utils.toastShort(context, "更多分类商品 category2Id=" + category2Id);
        setBodyParams(new String[]{"pn", "ps", "category2_id"}, new String[]{"" + pn, "" + ps, category2Id + ""});
        sendPost(WenConstans.Secondhand, 102, WenConstans.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_good_llBack:
                finish();
                break;
            case R.id.search_good_llSearch:
                content = etContent.getText().toString();
                category2Id = 0;
                beforeType = INDEX_TYPE;
                pn = 1;
                getSearchData();
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        switch (where) {
            case 100:   // 分类商品和搜索商品
                secondhandgoodBeanList.clear();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        secondhandgoodBeanList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (secondhandgoodBeanList == null || secondhandgoodBeanList.size() == 0) {
                            secondhandgoodBeanList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    }
                }
                goodAdapter = new GoodAdapter(this, secondhandgoodBeanList);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(goodAdapter);
//                Utils.toastShort(context, "Goods.size(): " + secondhandgoodBeanList.size());
                goodAdapter.notifyDataSetChanged();
                break;

//            case 101:   // 搜索商品
//                secondhandgoodBeanList.clear();
//                if (result.has("data")) {
//                    JSONObject data = result.getJSONObject("data");
//                    if (data != null && data.has("list")) {
//                        secondhandgoodBeanList = gson.fromJson(data.getJSONArray("list").toString(),
//                                new TypeToken<List<SecondhandgoodBean>>() {
//                                }.getType());
//                        if (secondhandgoodBeanList == null || secondhandgoodBeanList.size() == 0) {
//                            secondhandgoodBeanList = new ArrayList<>();
//                        }
//                        if (data.has("totalPage")) {
//                            totalPage = data.getInt("totalPage");
//                        }
//                    }
//                }
//                goodAdapter = new GoodAdapter(this,secondhandgoodBeanList);
//                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setAdapter(goodAdapter);
//                Utils.toastShort(context, "Goods.size(): " + secondhandgoodBeanList.size());
//                goodAdapter.notifyDataSetChanged();
//                break;

            case 102:   // 更多搜索商品和更多分类商品
                List<SecondhandgoodBean> tempList = new ArrayList<>();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        tempList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (tempList == null || tempList.size() == 0) {
                            tempList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    }
                }
                int start = secondhandgoodBeanList.size();
                int itemsCount = tempList.size();
                secondhandgoodBeanList.addAll(tempList);
//                Utils.toastShort(context, "Goods.size(): " + secondhandgoodBeanList.size());
                goodAdapter.notifyItemRangeInserted(start, itemsCount);
                break;
//            case 103:   // 更多分类商品
//                List<SecondhandgoodBean> tempList = new ArrayList<>();
//                if (result.has("data")) {
//                    JSONObject data = result.getJSONObject("data");
//                    if (data != null && data.has("list")) {
//                        tempList = gson.fromJson(data.getJSONArray("list").toString(),
//                                new TypeToken<List<SecondhandgoodBean>>() {
//                                }.getType());
//                        if (tempList == null || tempList.size() == 0) {
//                            tempList = new ArrayList<>();
//                        }
//                        if (data.has("totalPage")) {
//                            totalPage = data.getInt("totalPage");
//                        }
//                    }
//                }
//                int start2 = secondhandgoodBeanList.size();
//                int itemsCount2 = tempList.size();
//                secondhandgoodBeanList.addAll(tempList);
//                Utils.toastShort(context, "Goods.size(): " + secondhandgoodBeanList.size());
//                goodAdapter.notifyItemRangeInserted(start2, itemsCount2);
//                break;
        }

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
