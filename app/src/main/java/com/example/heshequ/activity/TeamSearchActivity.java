package com.example.heshequ.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.activity.login.LabelSelectionActivity;
import com.example.heshequ.adapter.recycleview.SearchTeamAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.SearchTeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.entity.TestBean;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2018/6/19 14:44
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class TeamSearchActivity extends NetWorkActivity implements XRecyclerView.LoadingListener, TextWatcher {
    private LinearLayout llBack, llFl;
    private EditText etContent;
    private XRecyclerView rv;
    private TextView tvTips;
    private SearchTeamAdapter adapter;
    private RelativeLayout rlData;
    private boolean isLable = false;
    private int i = 0;
    private int pn = 1;
    private int ps = 10;
    private String content;
    private boolean hasRefresh;
    private int totalPage;
    private List<SearchTeamBean> newList, moreList;
    private int clickPosition;
    private FlowLayout flowLayout;
    private ArrayList<LabelSelectionActivity.LableBean> datas;
    private ArrayList<TestBean> testData;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        try {
            Gson gson = new Gson();
            if (where == 100) {
                if (hasRefresh) {
                    hasRefresh = false;
                    rv.refreshComplete();
                }
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        newList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SearchTeamBean>>() {
                                }.getType());
                        if (newList == null || newList.size() == 0) {
                            newList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    } else {
                        newList = new ArrayList<>();
                    }
                } else {
                    newList = new ArrayList<>();
                }
                if (newList.size() == 0) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 101) {
                rv.loadMoreComplete();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SearchTeamBean>>() {
                                }.getType());
                        if (moreList == null || moreList.size() == 0) {
                            moreList = new ArrayList<>();
                        }
                    } else {
                        moreList = new ArrayList<>();
                    }
                } else {
                    moreList = new ArrayList<>();
                }
                newList.addAll(moreList);
                if (newList.size() == 0) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 10086) {
                if (result.optInt("code") == 0) {
                    datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LabelSelectionActivity.LableBean>>() {
                    }.getType());
                    if (datas != null && datas.size() > 0) {
                        for (LabelSelectionActivity.LableBean b : datas) {
                            // 循环添加TextView到容器
                            TestBean bean = new TestBean();
                            bean.setName(b.getValue());
                            testData.add(bean);
                            final TextView view = new TextView(this);
                            view.setText("#" + b.getValue() + "#");
                            view.setTextColor(Color.parseColor("#999999"));
                            view.setHeight(Utils.dip2px(this, 34));
                            view.setPadding(Utils.dip2px(this, 17), 0, Utils.dip2px(this, 17), 0);
                            view.setGravity(Gravity.CENTER);
                            view.setTextSize(14);
                            //view.setBackgroundResource(R.drawable.e6e6e6_17);
                            view.setTag(b.getValue());
                            // 设置点击事件
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.hideSoftInput(etContent);
                                    String text = view.getText().toString();

                                    int index = -1;
                                    for (int j = 0; j < testData.size(); j++) {
                                        TestBean bean = testData.get(j);
                                        if (bean.getName().equals(text)) {
                                            bean.setStatus(Math.abs(bean.getStatus() - 1));
                                            index = j;
                                            break;
                                        }
                                    }
                                    isLable = true;
                                    //setTvBg(view, testData.get(index).getStatus());
                                    etContent.setText(text.replace("#", ""));
                                    etContent.setSelection(etContent.getText().toString().trim().length());

                                }
                            });
                            flowLayout.addView(view);
                        }
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
            }
        } catch (Exception e) {

        }
    }

    private void setList(List<SearchTeamBean> newList) {
        adapter.setData(newList);
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ww_search);
        init();
        event();
    }

    private void init() {
        rlData = findViewById(R.id.rlData);
        llFl = findViewById(R.id.llFl);
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        etContent = (EditText) findViewById(R.id.etContent);
        tvTips = (TextView) findViewById(R.id.tvTips);
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXrecycleView(this, true, true, rv);
        adapter = new SearchTeamAdapter(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        etContent.addTextChangedListener(this);
        getData("");
        testData = new ArrayList<TestBean>();
        //stringList = new ArrayList<>();
        getLableData();
    }

    private void getLableData() {
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void event() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamSearchActivity.this.finish();
            }
        });

        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
                    //do something;
                    getData(etContent.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }


    private void getData(String s) {
        pn = 1;
        content = s;
        if (!isLable) {
            if (TextUtils.isEmpty(s)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"1", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "keyword"},
                        new String[]{"1", pn + "", ps + "", s});
            }
            sendPost(Constants.base_url + "/api/club/base/pglist.do", 100, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"}, new String[]{pn + "", ps + "", content});
            sendPost(Constants.base_url + "/api/club/base/labelList.do", 100, Constants.token);
        }
    }

    private void getMore() {
        if (!isLable) {
            if (TextUtils.isEmpty(content)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"2", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "keyword"},
                        new String[]{"2", pn + "", ps + "", content});
            }
            sendPost(Constants.base_url + "/api/club/base/pglist.do", 101, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"}, new String[]{pn + "", ps + "", content});
            sendPost(Constants.base_url + "/api/club/base/labelList.do", 101, Constants.token);
        }
    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getData(content);
    }

    @Override
    public void onLoadMore() {
        pn++;
        if (pn > totalPage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv.loadMoreComplete();
                }
            }, 1000);
        } else {
            getMore();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null) {
            if (s.length() != 2) {
                isLable = false;
            }
            getData(s.toString());
        }
        if (s.length() == 0) {
            llFl.setVisibility(View.VISIBLE);
            rlData.setVisibility(View.INVISIBLE);
        } else {
            llFl.setVisibility(View.GONE);
            rlData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
