package com.leslie.socialink.activity.mine;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.PullTheBlackAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.PullTheBlackBean;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 佳佳 on 2018/11/18.
 * 我的拉黑页面
 */

public class MyPullTheBlackActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private XRecyclerView rv;
    private TextView tvNoData;
    private PullTheBlackAdapter adapter;
    private ArrayList<PullTheBlackBean> testData;
    private PullTheBlackBean testData2;
    private TextView attentiondata;
    private final int getCode = 1000;
    private Gson gson = new Gson();
    private boolean isref = true;
    private int pn = 1;
    private int ps = 0;
    private int allpn = 0;
    private AlertDialog deldialog;
    private final int DELETE = 1001;

    //继承父类初始化
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_the_black);
//        EventBus.getDefault().register(this);
        init();
        event();
    }

    //从此函数返回
    private void event() {
        rv.setLoadingListener(this);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPullTheBlackActivity.this.finish();
            }
        });
    }

    //数据的初始化，加载数据，请求网络
    private void init() {
        setText("我的黑名单");
        rv = (XRecyclerView) findViewById(R.id.rv);
        tvNoData = findViewById(R.id.attentionData);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        testData = new ArrayList<>();
        adapter = new PullTheBlackAdapter(mContext, testData);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        pn = 1;
        getData(pn);
    }


    private void getData(int pn) {
        setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, "" + ps});
        sendPost(WenConstans.SearchPullTheBlack, getCode, Constants.token);
    }

    //刷新功能
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                isref = true;
                getData(1);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    //上拉加载功能
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pn < allpn) {
                    pn++;
                    isref = false;
                    getData(pn);
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    public void setData(ArrayList<PullTheBlackBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<PullTheBlackBean>());
        }
    }


    //网络请求成功，取数据并将其放到item中
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where == getCode) {
            try {
                testData = new ArrayList<>();
                JSONObject data = new JSONObject(result.optString("data"));
                if (data != null) {
                    allpn = data.optInt("totalPage");
                    testData = gson.fromJson(data.optString("list"),
                            new TypeToken<List<PullTheBlackBean>>() {
                            }.getType());
                    if (testData != null) {
                        if (isref) {
                            //lvadapter.setData(2,testData);
                            adapter.setData(testData);
                            tvNoData.setVisibility(testData.size() > 0 ? View.GONE : View.VISIBLE);
                        } else {
                            testData.add(testData2);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (where == DELETE) {
            //取消关注弹窗
            pn = 1;
            getData(pn);
            Utils.toastShort(mContext, "已取消关注");
            onRefresh();
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }


    protected void onFailure(String result, int where) {
        Utils.toastShort(this, "网络异常");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
