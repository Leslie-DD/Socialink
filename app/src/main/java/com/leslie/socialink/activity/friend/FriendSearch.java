package com.leslie.socialink.activity.friend;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.FriendSearchAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.FriendListBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendSearch extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private int totalPage = 1;
    private List<FriendListBean> allList, moreList, newList;
    private boolean hasRefresh;
    private TextView tvTips;
    private LinearLayout llTip;
    private int clickPosition;
    private LinearLayout llBack, search;
    private XRecyclerView rv;
    private EditText etContent;
    private ArrayList<FriendListBean> goodData;
    private FriendSearchAdapter adapter;
    private String content;
    private int goodtype;
    private int pn = 1;
    private int ps = 20;//总页数

    public FriendSearch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsearch);
        init();


    }

    private void init() {
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXRecycleView(this, true, true, rv);
        adapter = new FriendSearchAdapter(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);


        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(v -> finish());
        search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            content = etContent.getText().toString();
            getFriendData(content);
        });

        llTip = findViewById(R.id.llTip);
        etContent = findViewById(R.id.etContent);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("昵称长度<", s.length() + ">");
                if (s.length() >= 32) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getFriendData(String content) {
        setBodyParams(new String[]{"content"}, new String[]{"" + content});
        sendPost(Constants.base_url + "/api/social/search.do", 10086, Constants.token);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }

        Gson gson = new Gson();
        if (where == 10086) {
            Log.e("s", "ssssssssssssssssssssssssssssssssssssssssss2");
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            if (result.has("data")) {


                Log.e("s", "ssssssssssssssssssssssssssssssssssssssssss1");
                JSONArray data = result.getJSONArray("data");

                if (data != null) {
                    allList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendListBean>>() {
                            }.getType());
                    Log.e("s", "ssssssssssssssssssssssssssssssssssssssssss");
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }

                }
            }

            adapter.setData(allList);
        } else if (where == 101) {

            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONArray data = result.getJSONArray("data");
                if (data != null) {
                    moreList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendListBean>>() {
                            }.getType());
                    if (moreList == null || moreList.size() == 0) {
                        moreList = new ArrayList<>();
                    }

                }
            }
            allList.addAll(moreList);
            if (allList.size() == 0) {
                tvTips.setVisibility(View.VISIBLE);
            } else {
                tvTips.setVisibility(View.GONE);
            }
            adapter.setData(allList);
        } else if (where == 1000) {
            Utils.toastShort(mContext, result.getString("msg") + "");
            adapter.setData(allList);
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

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getFriendData(content);

    }

    @Override
    public void onLoadMore() {

    }
}
