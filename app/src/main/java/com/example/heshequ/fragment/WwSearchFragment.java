package com.example.heshequ.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.HotWenwenAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.WenwenBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2018/6/19 11:07
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class WwSearchFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, HotWenwenAdapter.DoSaveListener {

    private View view;
    private int pn = 1;
    private int ps = 10;
    private XRecyclerView rv;
    private TextView tvTips;
    private String content;
    private boolean hasRefresh;
    private int totalPage;
    private FragmentBrodcast brodcast;
    private List<WenwenBean> newList, moreList;
    private HotWenwenAdapter adapter;
    private int clickPosition;

    private boolean islabel = false;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (ResultUtils.isFail(result, getActivity())) {
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
                                new TypeToken<List<WenwenBean>>() {
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
                if (hasRefresh) {
                    hasRefresh = false;
                    rv.refreshComplete();
                }
                rv.loadMoreComplete();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<WenwenBean>>() {
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
            } else if (where == 1000) {
                Utils.toastShort(mContext, result.getString("msg") + "");
                int zan = newList.get(clickPosition).likeAmount;
                if (TextUtils.isEmpty(newList.get(clickPosition).userLike)) {
                    newList.get(clickPosition).userLike = "1";
                    zan = zan + 1;
                    newList.get(clickPosition).likeAmount = zan;
                } else {
                    newList.get(clickPosition).userLike = "";
                    zan = zan - 1;
                    newList.get(clickPosition).likeAmount = zan;
                }
                adapter.setData(newList);
            }
        } catch (Exception e) {

        }
    }

    private void setList(List<WenwenBean> newList) {
        adapter.setData(newList);
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_only_recycleview, null);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        ConsTants.initXrecycleView(getActivity(), true, true, rv);
        adapter = new HotWenwenAdapter(getActivity());
        adapter.DoSaveListener(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData("");
        setFragmentListener();
        return view;
    }

    public void getData(String s) {
        pn = 1;
        content = s;
        if (!islabel) {
            if (TextUtils.isEmpty(s)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"2", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "name"},
                        new String[]{"2", pn + "", ps + "", s});
            }
            sendPostConnection(WenConstans.WenwenList, 100, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"},
                    new String[]{pn + "", ps + "", content});
            sendPostConnection(Constants.base_url + "/api/ask/base/labelList.do", 101, Constants.token);
        }
    }

    private void getMore() {
        if (!islabel) {
            if (TextUtils.isEmpty(content)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"2", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "name"},
                        new String[]{"2", pn + "", ps + "", content});
            }
            sendPostConnection(WenConstans.WenwenList, 101, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"},
                    new String[]{pn + "", ps + "", content});
            sendPostConnection(Constants.base_url + "/api/ask/base/labelList.do", 101, Constants.token);
        }
    }

    public void getLableWwData(String content) {
        islabel = true;
        pn = 1;
        this.content = content;
        setBodyParams(new String[]{"pn", "ps", "label"},
                new String[]{pn + "", ps + "", content});
        sendPostConnection(Constants.base_url + "/api/ask/base/labelList.do", 100, Constants.token);
    }

    public void setIslabel(boolean islabel) {
        this.islabel = islabel;
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
        /*if (pn > totalPage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv.loadMoreComplete();
                }
            }, 1000);
        } else {
            getMore();
        }*/
        getMore();
    }

    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("fragment.listener");
        brodcast = new FragmentBrodcast();
        getActivity().registerReceiver(brodcast, filter);
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id + ""});
        sendPostConnection(WenConstans.WwLike, 1000, WenConstans.token);
    }

    private class FragmentBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);

            if (items == 1) {    //加载

            } else if (items == 2) {
                getData(content);
            } else if (items == 3) {   //刷新

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (brodcast != null) {
            getActivity().unregisterReceiver(brodcast);
        }
    }
}
