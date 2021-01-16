package com.example.heshequ.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heshequ.activity.wenwen.ZcArticleActivity;
import com.example.heshequ.activity.wenwen.ZcQusetionActivity;
import com.example.heshequ.adapter.listview.ZcWwAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.ZcBean;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2018/5/29 15:07
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ZcFragment extends NetWorkFragment implements View.OnClickListener {

    private View view;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private int item;
    private XRecyclerView rv;
    private ZcWwAdapter adapter;
    private List<ZcBean> zcList1;
    private List<ZcBean> zcList2;
    private List<ZcBean> zcList3;
    private List<ZcBean> zcList4;
    private List<ZcBean> moreList;
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private Intent intent;
    private int pn = 1;
    private int ps = 20;
    private int totalPage, totalPage1=1, totalPage2=1, totalPage3=1, totalPage4=1;
    private int pn1 = 1, pn2 = 1, pn3 = 1, pn4 = 1;
    private boolean hasRefresh;
    private TextView tvTip;
    private Bundle bundle;
    private boolean loadmore;
    private ChangeBrodCast brodCast;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }
        if (where == 1) {
            if (hasRefresh) {
                hasRefresh = false;
                totalPage1 = setData(result);
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 3);
                getActivity().sendBroadcast(intent);
            } else {
                totalPage1 = setData(result);
            }

        } else if (where == 2) {
            if (hasRefresh) {
                hasRefresh = false;
                totalPage2 = setData(result);
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 3);
                getActivity().sendBroadcast(intent);
            } else {
                totalPage2 = setData(result);
            }

        } else if (where == 3) {
            if (hasRefresh) {
                hasRefresh = false;
                totalPage3 = setData(result);
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 3);
                getActivity().sendBroadcast(intent);
            } else {
                totalPage4 = setData(result);
            }

        } else if (where == 4) {
            if (hasRefresh) {
                hasRefresh = false;
                totalPage4 = setData(result);
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 3);
                getActivity().sendBroadcast(intent);
            } else {
                totalPage3 = setData(result);
            }
        } else if (where == 100) {
            Intent intent = new Intent();
            intent.setAction("fragment.listener");
            intent.putExtra("item", 1);
            getActivity().sendBroadcast(intent);
            Gson gson = new Gson();
            if (result.has("data")) {
                try {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<ZcBean>>() {
                                }.getType());
                        if (moreList == null) {
                            moreList = new ArrayList<>();
                        }
                    } else {
                        moreList = new ArrayList<>();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                moreList = new ArrayList<>();
            }
            switch (item) {
                case 0:
                    zcList1.addAll(moreList);
                    adapter.setData(zcList1);
                    break;
                case 1:
                    zcList2.addAll(moreList);
                    adapter.setData(zcList2);
                    break;
                case 2:
                    zcList3.addAll(moreList);
                    adapter.setData(zcList3);
                    break;
                case 3:
                    zcList4.addAll(moreList);
                    adapter.setData(zcList4);
                    break;
            }
        }
    }

    private int setData(JSONObject result) {
        Gson gson = new Gson();
        List<ZcBean> list=new ArrayList<>();
        if (result.has("data")) {
            try {
                JSONObject data = result.getJSONObject("data");
                totalPage = data.getInt("totalPage");
                if (data != null && data.has("list")) {
                    list = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<ZcBean>>() {
                            }.getType());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                } else {
                    list = new ArrayList<>();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            list = new ArrayList<>();
        }
        adapter.setType(item);
        adapter.setData(list);
        if (list.size()==0){
            Log.e("ying","显示");
            tvTip.setVisibility(View.VISIBLE);
        }else{
            Log.e("ying","隐藏");
            tvTip.setVisibility(View.GONE);
        }
        if (item==0){
            zcList1=new ArrayList<>();
            zcList1.addAll(list);
        }else if (item==1){
            zcList2=new ArrayList<>();
            zcList2.addAll(list);
        }else if (item==2){
            zcList4=new ArrayList<>();
            zcList4.addAll(list);
        }else if (item==3){
            zcList3=new ArrayList<>();
            zcList3.addAll(list);
        }
        return totalPage;
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_zc, null);
        init();
        return view;
    }

    private void init() {
        zcList1 = new ArrayList<>();
        zcList2 = new ArrayList<>();
        zcList3 = new ArrayList<>();
        zcList4 = new ArrayList<>();
        tvTip = (TextView) view.findViewById(R.id.tvTips);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv1.setSelected(true);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);
        rl1 = (RelativeLayout) view.findViewById(R.id.rl1);
        rl2 = (RelativeLayout) view.findViewById(R.id.rl2);
        rl3 = (RelativeLayout) view.findViewById(R.id.rl3);
        rl4 = (RelativeLayout) view.findViewById(R.id.rl4);
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        v1 = view.findViewById(R.id.iv1);
        v2 = view.findViewById(R.id.iv2);
        v3 = view.findViewById(R.id.iv3);
        v4 = view.findViewById(R.id.iv4);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXrecycleView(getActivity(),false,false,rv);
        rv.setNestedScrollingEnabled(false);
        adapter = new ZcWwAdapter(getActivity());
        rv.setAdapter(adapter);
        adapter.DoSaveListener(new ZcWwAdapter.DoClickListener() {
            @Override
            public void doSave(int position) {
                switch (item) {
                    case 0:
                        if (zcList1.size()<=position){
                            Utils.toastShort(mContext,"网络请求较慢，请稍后重试");
                            return;
                        }
                        intent = new Intent(mContext, ZcQusetionActivity.class);
                        bundle=new Bundle();
                        bundle.putSerializable("beans",zcList1.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:
                        if (zcList2.size()<=position){
                            Utils.toastShort(mContext,"网络请求较慢，请稍后重试");
                            return;
                        }
                        intent = new Intent(mContext, ZcQusetionActivity.class);
                        bundle=new Bundle();
                        bundle.putSerializable("beans",zcList2.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 3:
                        if (zcList3.size()<=position){
                            Utils.toastShort(mContext,"网络请求较慢，请稍后重试");
                            return;
                        }
                        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_askSpecailRunning");
                        intent = new Intent(mContext, ZcArticleActivity.class);
                        bundle = new Bundle();
                        bundle.putSerializable("beans",zcList3.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 2:
                        if (zcList4.size()<=position){
                            Utils.toastShort(mContext,"网络请求较慢，请稍后重试");
                            return;
                        }
                        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_askArticleEnter");
                        intent = new Intent(mContext, ZcQusetionActivity.class);
                        bundle=new Bundle();
                        bundle.putSerializable("beans",zcList4.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
        getData(1);
        brodCast = new ChangeBrodCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("zcRefresh");
        getActivity().registerReceiver(brodCast,intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl1:
                if (item == 0) {
                    return;
                }
                item = 0;
                clearAllBg();
                tv1.setSelected(true);
                if (zcList1.size()>0){
                    tvTip.setVisibility(View.GONE);
                    adapter.setType(item);
                    adapter.setData(zcList1);
                }else{
                    adapter.setType(item);
                    adapter.setData(zcList1);
                    getData(pn1);
                }
                break;
            case R.id.rl2:
                if (item == 1) {
                    return;
                }
                item = 1;
                clearAllBg();
                tv2.setSelected(true);
                if (zcList2.size()>0){
                    tvTip.setVisibility(View.GONE);
                    adapter.setType(item);
                    adapter.setData(zcList2);
                }else{
                    adapter.setType(item);
                    adapter.setData(zcList2);
                    getData(pn2);
                }
                break;
            case R.id.rl3:
                if (item == 3) {
                    return;
                }
                item = 3;
                clearAllBg();
                tv3.setSelected(true);
                if (zcList3.size()>0){
                    tvTip.setVisibility(View.GONE);
                    adapter.setType(item);
                    adapter.setData(zcList3);
                }else{
                    adapter.setType(item);
                    adapter.setData(zcList3);
                    getData(pn3);
                }
                break;
            case R.id.rl4:
                if (item == 2) {
                    return;
                }
                item = 2;
                clearAllBg();
                tv4.setSelected(true);
                if (zcList4.size()>0){
                    tvTip.setVisibility(View.GONE);
                    adapter.setType(item);
                    adapter.setData(zcList4);
                }else{
                    adapter.setType(item);
                    adapter.setData(zcList4);
                    getData(pn4);
                }
                break;
        }
    }

    private void clearAllBg() {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        tv4.setSelected(false);
    }

    private void getData(int page) {
        setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{page + "", ps + "", item + 1 + ""});
        if (page > 1) {
            sendPost(WenConstans.ZcList, 100, WenConstans.token);
        } else {
            sendPost(WenConstans.ZcList, item + 1, WenConstans.token);
        }
    }

    public void getData(boolean isRefresh) {
        switch (item) {
            case 0:
                if (isRefresh) {
                    pn1 = 1;
                } else {
                    pn1++;
                }
                pn = pn1;
                loadmore = pn>totalPage1;
                break;
            case 1:
                if (isRefresh) {
                    pn2 = 1;
                } else {
                    pn2++;
                }
                pn = pn2;
                loadmore = pn>totalPage2;
                break;
            case 2:
                if (isRefresh) {
                    pn3 = 1;
                } else {
                    pn3++;
                }
                pn = pn3;
                loadmore = pn>totalPage4;
                break;
            case 3:
                if (isRefresh) {
                    pn4 = 1;
                } else {
                    pn4++;
                }
                pn = pn4;
                loadmore = pn>totalPage3;
                break;
        }
        setDatas(isRefresh);
    }

    private void setDatas(boolean isRefresh) {
        if (isRefresh) {
            hasRefresh = true;
            getData(1);
        } else {
            if (loadmore) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setAction("fragment.listener");
                        intent.putExtra("item", 1);
                        getActivity().sendBroadcast(intent);
                    }
                }, 800);
            } else {
                getData(pn);
            }
        }
    }

    private class ChangeBrodCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null){
                if (intent.getBooleanExtra("refresh",true)){
                    getData(true);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(brodCast);
    }
}
