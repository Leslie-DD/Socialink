package com.example.heshequ.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.adapter.recycleview.HotActiveAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.HotAvtivityBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefHotActivityEvent;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Hulk_Zhang on 2018/1/10 17:50
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ChildFragment3 extends NetWorkFragment {
    private View view;
    private XRecyclerView mRecyclerView;
    private HotActiveAdapter adapter;
    private TextView tvTips;
    private final int GetData = 1000;
    private final int LoaMore = 1001;

    private int pn;
    private int totalPage;
    private int type;
    private HotAvtivityBean hotAvtivityBean;
    private Gson gson = new Gson();
    private ArrayList<HotAvtivityBean.HotBean> data;
    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_tim, null);
        EventBus.getDefault().register(this);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        ConsTants.initXrecycleView(getActivity(),false,false,mRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);

        pn = 1;
        type = 1;
        getData(pn,type);

        adapter = new HotActiveAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        //adapter.setData();
        return view;
    }

    private void getData(int pn, int type) {
        if (type == 1) {
            setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, Constants.default_PS + ""});
            sendPost(Constants.base_url + "/api/club/activity/hotlist.do", GetData, Constants.token);
        }else if (type == 2){
            setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, Constants.default_PS + ""});
            sendPost(Constants.base_url + "/api/club/activity/hotlist.do", LoaMore, Constants.token);
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void refload(RefHotActivityEvent event){
        if (event.getType() == 1){
            pn = 1;
            type = 1;
            getData(pn,type);
        }else if(event.getType() == 2){
            if (pn<totalPage){
                pn++;
                type = 2;
                getData(pn,type);
            }
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        LogUtils.e("DDQ:-》"+result.toString());
        switch (where){
            case GetData:
                if (result.optInt("code") == 0){
                    hotAvtivityBean = gson.fromJson(result.optString("data"),HotAvtivityBean.class);
                    totalPage = hotAvtivityBean.getTotalPage();
                    if (hotAvtivityBean.getData() != null){
                        if (hotAvtivityBean.getData().size()>0){
                            tvTips.setVisibility(View.GONE);
                            data = hotAvtivityBean.getData();
                            adapter.setData(data);
                        }else {
                            tvTips.setVisibility(View.VISIBLE);
                            data = new ArrayList<>();
                            adapter.setData(data);
                        }
                    }
                }else{
                    Utils.toastShort(mContext,result.optString("msg"));
                }
                break;
            case LoaMore:
                if (result.optInt("code") == 0){
                    hotAvtivityBean = gson.fromJson(result.optString("data"),HotAvtivityBean.class);
                    totalPage = hotAvtivityBean.getTotalPage();
                    if (hotAvtivityBean.getData() != null){
                        if (hotAvtivityBean.getData().size()>0){
                            data = hotAvtivityBean.getData();
                            adapter.setData2(data);
                        }else {
                            data = new ArrayList<>();
                            adapter.setData2(data);
                        }
                    }
                }else{
                    Utils.toastShort(mContext,result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext,"网络异常");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
