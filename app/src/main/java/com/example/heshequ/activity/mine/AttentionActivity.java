package com.example.heshequ.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.AttentionAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.AttentionBean;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 佳佳 on 2018/11/18.
 * 我的关注
 */

public class AttentionActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private XRecyclerView rv;
    private TextView tvNoData;
    private AttentionAdapter adapter;
    private ArrayList<AttentionBean> testData;
    private AttentionBean testData2;
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
        setContentView(R.layout.activity_attention);
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
                AttentionActivity.this.finish();
            }
        });
    }

    //数据的初始化，加载数据，请求网络
    private void init() {
        setText("我的关注");
        rv = (XRecyclerView) findViewById(R.id.rv);
        tvNoData = findViewById(R.id.attentionData);
        ConsTants.initXrecycleView(mContext, true, true, rv);
        testData = new ArrayList<>();
        adapter = new AttentionAdapter(mContext, testData);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        pn = 1;
        getData(pn);
    }


    private void getData(int pn) {
        setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, "" + ps});
        sendPost(WenConstans.SearchAttention, getCode, Constants.token);
    }

    //刷新功能
    @Override
    public void onRefresh() {
        new Handler().postDelayed(
                new Runnable() {
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

    public void setData(ArrayList<AttentionBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<AttentionBean>());
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
                            new TypeToken<List<AttentionBean>>() {
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
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
