package com.leslie.socialink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.MyFragmentPagerAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.LableBean;
import com.leslie.socialink.entity.TestBean;
import com.leslie.socialink.fragment.TeamSearchFragment;
import com.leslie.socialink.fragment.WwSearchFragment;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.FlowLayout;
import com.leslie.socialink.view.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeSearchActivity extends NetWorkActivity implements TabLayout.OnTabSelectedListener, TextWatcher {
    private LinearLayout llBack, llData, llFl;
    private EditText etContent;
    private TabLayout tab;
    private NoScrollViewPager vp;
    private MyFragmentPagerAdapter pagerAdapter;
    private Gson gson = new Gson();
    private FlowLayout flowLayout;
    private ArrayList<LableBean> datas;
    private boolean isLable = false;
    private int position = 0;
    private ArrayList<TestBean> testData;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case 10086:
                if (result.optInt("code") == 0) {
                    datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LableBean>>() {
                    }.getType());
                    if (datas != null && datas.size() > 0) {
                        for (LableBean b : datas) {
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
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        init();
    }

    private void init() {
        llData = findViewById(R.id.llData);
        llFl = findViewById(R.id.llFl);
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TeamSearchFragment());
        fragments.add(new WwSearchFragment());
        llBack = (LinearLayout) findViewById(R.id.llBack);
        llBack.setOnClickListener(v -> finish());
        etContent = (EditText) findViewById(R.id.etContent);
        etContent.addTextChangedListener(this);
        tab = (TabLayout) findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("团队"));
        tab.addTab(tab.newTab().setText("问问"));
        vp = (NoScrollViewPager) findViewById(R.id.vp);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(0);
        tab.setOnTabSelectedListener(this);
        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    Utils.hideSoftInput(etContent);
                    //do something;
                    return true;
                }
                return false;
            }
        });
        testData = new ArrayList<TestBean>();
        getLableData();
    }

    private void getLableData() {
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.BASE_URL + "/api/pub/category/list.do", 10086, Constants.token);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vp.setCurrentItem(tab.getPosition());
        position = tab.getPosition();
        //etContent.setText("");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            return;
        }
        if (s.length() == 0) {
            llFl.setVisibility(View.VISIBLE);
            llData.setVisibility(View.GONE);
        } else {
            llFl.setVisibility(View.GONE);
            llData.setVisibility(View.VISIBLE);
        }
        String content = s.toString();
        TeamSearchFragment f1 = (TeamSearchFragment) pagerAdapter.getItem(0);
        WwSearchFragment f2 = (WwSearchFragment) pagerAdapter.getItem(1);
        if (s.length() != 2) {
            isLable = false;
            if (f1 != null) {
                f1.setIslabel(isLable);
            }
            if (f2 != null) {
                f2.setIslabel(isLable);
            }
        }

        if (f1 != null) {
            if (isLable) {
                f1.getLableTeamData(content);
            } else {
                f1.getData(content);
            }

        }
        if (f2 != null) {
            if (isLable) {
                f2.getLableWwData(content);
            } else {
                f2.getData(content);
            }
        }
    }
}
