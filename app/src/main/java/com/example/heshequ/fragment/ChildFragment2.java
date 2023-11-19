package com.example.heshequ.fragment;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.HotWenwenAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.WenwenBean;
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
 * Hulk_Zhang on 2018/1/10 17:50
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ChildFragment2 extends NetWorkFragment implements HotWenwenAdapter.DoSaveListener {

    private View view;
    private XRecyclerView mRecyclerView;
    private HotWenwenAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private List<WenwenBean> newList, moreList;
    private TextView tvTips;
    private int totalPage = 1;
    private boolean hasRefresh;
    private int type = 2;
    private int clickPosition;
//    private FragmentBrodcast brodcast;

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
                    Intent intent = new Intent();
                    intent.setAction("fragment.listener");
                    intent.putExtra("item", 3);
                    getActivity().sendBroadcast(intent);
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
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 1);
                getActivity().sendBroadcast(intent);
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

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_tim, null);
//        setFragmentListener();
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXrecycleView(getActivity(), false, false, mRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter = new HotWenwenAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.DoSaveListener(this);
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, "1", "20"});
        sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        return view;
    }

    public void setType(int num) {
        type = num;
    }

    private void setList(List<WenwenBean> list) {
        if (adapter != null) {
            adapter.setData(list);
        }
    }

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            hasRefresh = true;
            pn = 1;
            setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, pn + "", ps + ""});
            sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        } else {
            pn++;
            if (pn > totalPage) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setAction("fragment.listener");
                        intent.putExtra("item", 1);
                        getActivity().sendBroadcast(intent);
                    }
                }, 700);
            } else {
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, pn + "", ps + ""});
                sendPostConnection(WenConstans.WenwenList, 101, WenConstans.token);
            }
        }
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id + ""});
        sendPostConnection(WenConstans.WwLike, 1000, WenConstans.token);
    }

//    private void setFragmentListener() {
//        IntentFilter filter=new IntentFilter();
//        filter.addAction("refresh.listener");
//        brodcast = new FragmentBrodcast();
//        getActivity().registerReceiver(brodcast,filter);;
//    }

//    private class FragmentBrodcast extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int items=intent.getIntExtra("item",0);
//            if (items==1){
//
//            }
//        }
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (brodcast!=null){
//            getActivity().unregisterReceiver(brodcast);
//        }
//    }
}
