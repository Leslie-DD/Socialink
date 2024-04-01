package com.leslie.socialink.activity.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.QuestionsAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyQuestionActivity1 extends NetWorkActivity implements XRecyclerView.LoadingListener, QuestionsAdapter.DoSaveListener {

    private XRecyclerView rv;
    private QuestionsAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<QuestionBean> allList, moreList;
    private TextView tvTips;
    private int clickPosition;
    private FragmentBrodcast brodcast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        init();
        setFragmentListener();
    }

    private void init() {
        setTitleAndBack("我的问题");
        tvTips = (TextView) findViewById(R.id.tvTips);
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXRecycleView(this, true, true, rv);
        adapter = new QuestionsAdapter(this);
        rv.setAdapter(adapter);
        adapter.setSaveListener(this);
        rv.setLoadingListener(this);
        getData(100);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        Gson gson = new Gson();
        if (where == 100) {
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    allList = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<QuestionBean>>() {
                            }.getType());
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.getInt("totalPage");
                    }
                }
            }
            if (allList.size() == 0) {
                tvTips.setVisibility(View.VISIBLE);
            } else {
                tvTips.setVisibility(View.GONE);
            }
            adapter.setData(allList);
        } else if (where == 101) {
            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    moreList = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<QuestionBean>>() {
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

    private void getData(int where) {
        if (where == 100) {
            pn = 1;
        }
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"1", pn + "", ps + ""});
        sendPost(WenConstans.MyProblems, where, Constants.token);
    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getData(100);
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
            getData(101);
        }
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{allList.get(position).id + ""});
        sendPost(WenConstans.WwLike, 1000, Constants.token);
    }

    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("fragment.listener");
        brodcast = new FragmentBrodcast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(brodcast, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(brodcast, filter);
        }
    }

    private class FragmentBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);

            if (items == 1) {    //加载

            } else if (items == 2) {
                getData(100);
            } else if (items == 3) {   //刷新

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (brodcast != null) {
            unregisterReceiver(brodcast);
        }
    }


}
