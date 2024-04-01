package com.leslie.socialink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.SearchTeamAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.LableBean;
import com.leslie.socialink.bean.SearchTeamBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.entity.TestBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TeamSearchActivity extends NetWorkActivity implements XRecyclerView.LoadingListener, TextWatcher {
    private static final String TAG = "[TeamSearchActivity]";

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
    private ArrayList<LableBean> datas;
    private ArrayList<TestBean> testData;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.d(TAG, "onSuccess: " + " where: " + where + ", " + result.toString());
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
                    if (data.has("list")) {
                        newList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SearchTeamBean>>() {
                                }.getType());
                        if (newList == null || newList.isEmpty()) {
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
                if (newList.isEmpty()) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 101) {
                rv.loadMoreComplete();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SearchTeamBean>>() {
                                }.getType());
                        if (moreList == null || moreList.isEmpty()) {
                            moreList = new ArrayList<>();
                        }
                    } else {
                        moreList = new ArrayList<>();
                    }
                } else {
                    moreList = new ArrayList<>();
                }
                newList.addAll(moreList);
                if (newList.isEmpty()) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 10086) {
                if (result.optInt("code") == 0) {
                    datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LableBean>>() {
                    }.getType());
                    if (datas != null && !datas.isEmpty()) {
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
                            view.setOnClickListener(v -> {
                                Utils.hideSoftInput(etContent);
                                String text = view.getText().toString();

                                int index = -1;
                                for (int j = 0; j < testData.size(); j++) {
                                    TestBean bean1 = testData.get(j);
                                    if (bean1.getName().equals(text)) {
                                        bean1.setStatus(Math.abs(bean1.getStatus() - 1));
                                        index = j;
                                        break;
                                    }
                                }
                                isLable = true;
                                //setTvBg(view, testData.get(index).getStatus());
                                etContent.setText(text.replace("#", ""));
                                etContent.setSelection(etContent.getText().toString().trim().length());

                            });
                            flowLayout.addView(view);
                        }
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "error " + e.getMessage());
            e.printStackTrace();
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
        ConsTants.initXRecycleView(this, true, true, rv);
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
        sendPost(Constants.BASE_URL + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void event() {
        llBack.setOnClickListener(v -> TeamSearchActivity.this.finish());

        etContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
                //do something;
                getData(etContent.getText().toString().trim());
                return true;
            }
            return false;
        });
    }


    private void getData(String s) {
        pn = 1;
        content = s;

        if (TextUtils.isEmpty(s)) {
            Log.d(TAG, "search content is empty");
            return;
        }
        if (!isLable) {
//            if (TextUtils.isEmpty(s)) {
//                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"1", pn + "", ps + ""});
//            } else {
            setBodyParams(new String[]{"type", "pn", "ps", "keyword"}, new String[]{"1", pn + "", ps + "", s});
//            }
            sendPost(Constants.BASE_URL + "/api/club/base/pglist.do", 100, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"}, new String[]{pn + "", ps + "", content});
            sendPost(Constants.BASE_URL + "/api/club/base/labelList.do", 100, Constants.token);
        }
    }

    private void getMore() {
        if (!isLable) {
            if (TextUtils.isEmpty(content)) {
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"2", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "keyword"}, new String[]{"2", pn + "", ps + "", content});
            }
            sendPost(Constants.BASE_URL + "/api/club/base/pglist.do", 101, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"}, new String[]{pn + "", ps + "", content});
            sendPost(Constants.BASE_URL + "/api/club/base/labelList.do", 101, Constants.token);
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
}
