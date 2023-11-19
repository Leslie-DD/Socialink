package com.example.heshequ.activity.oldsecond;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.SecondhandgoodAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.bean.UserInfoBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsearchActivity extends NetWorkActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
    private int totalPage = 1;
    private List<SecondhandgoodBean> allList, moreList, newList;
    private TextView tvTips;
    private ViewPager vp;
    private boolean hasRefresh;
    private UserInfoBean userInfoBean;
    private LinearLayout llTip;
    private int clickPosition;
    private LinearLayout llBack, search;
    private XRecyclerView rv;
    private EditText etContent;
    private ArrayList<SecondhandgoodBean> goodData;
    private SecondhandgoodAdapter adapter;
    private String content;
    private int goodtype;
    private int pn = 1;
    private int ps = 20;
    private int category2Id;

    public GoodsearchActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsearch);

        /**
         * 可以从商品分类查询页面（HomeItemAdapter）跳转到此页面
         *      需要获取category2Id的值，非0；
         * 也可以从二手商品页面主页（SecondFragment）跳转到此页面
         *      不需要获取categroy2Id的值，为0；
         *
         */
        Bundle bundle = this.getIntent().getExtras();
        category2Id = bundle.getInt("category2_id", 0);
        if (category2Id != 0) {
            getGoodData(pn, content);
        }
        init();

    }

    private void init() {
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXrecycleView(this, true, true, rv);
        adapter = new SecondhandgoodAdapter(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);

        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etContent.getText().toString();
                category2Id = 0;
                getGoodData(pn, content);
            }
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
                Log.e("商品长度<", s.length() + ">");
                if (s.length() >= 32) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getGoodData(int png, String content) {
        if (category2Id == 0) {
            setBodyParams(new String[]{"pn", "ps", "keyword"}, new String[]{"" + png, "" + Constants.default_PS, "" + content});
            sendPost(Constants.base_url + "/api/goods/base/search.do", 10086, Constants.token);
        } else {
            Utils.toastShort(context, "商品搜索 category2Id=" + category2Id);
            setBodyParams(new String[]{"pn", "ps", "category2_id"}, new String[]{"" + pn, "" + ps, category2Id + ""});
            sendPost(WenConstans.Secondhand, 10086, WenConstans.token);
        }
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
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    allList = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<SecondhandgoodBean>>() {
                            }.getType());
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.getInt("totalPage");
                    }
                }
            }

            adapter.setData(allList);
        } else if (where == 101) {
            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    moreList = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<SecondhandgoodBean>>() {
                            }.getType());
                    if (moreList == null || moreList.size() == 0) {
                        moreList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.getInt("totalPage");
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
            int zan = allList.get(clickPosition).likeAmount;
            if (TextUtils.isEmpty(allList.get(clickPosition).userLike)) {
                allList.get(clickPosition).userLike = "1";
                zan = zan + 1;
                allList.get(clickPosition).likeAmount = zan;
            } else {
                allList.get(clickPosition).userLike = "";
                zan = zan - 1;
                allList.get(clickPosition).likeAmount = zan;
            }
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
        getGoodData(100, content);

    }

    @Override
    public void onLoadMore() {
        pn++;
        getGoodData(pn, content);
    }
}
