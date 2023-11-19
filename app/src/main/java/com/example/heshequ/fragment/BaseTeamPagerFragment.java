package com.example.heshequ.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.TeamDetailActivity2;
import com.example.heshequ.adapter.recycleview.CommentTeamAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Hulk_Zhang on 2018/5/9 11:22
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class BaseTeamPagerFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, CommentTeamAdapter.OnItemClickListener {

    private View view;
    public XRecyclerView rv;
    public CommentTeamAdapter adapter;
    public ArrayList<TeamBean> list;
    public int nums;
    public TextView tvNoData;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.vp_tim, null);
            init();
        }
        return view;
    }

    private void init() {
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        tvNoData = view.findViewById(R.id.tvNoData);
        ConsTants.initXrecycleView(getActivity(), true, true, rv);
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(getActivity(), list);
        adapter.setListener(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        Log.e("ying", "init");

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    public void setData(ArrayList<TeamBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<TeamBean>());
        }
    }

    public void setData(int num) {
        Log.e("ying", "setData");
        this.nums = num;
        if (adapter != null) {
            adapter.setData(num);
            adapter.setListener(this);
        }
    }

    @Override
    public void OnItemClick(int position) {
        //Log.e("YSF","我运行了"+adapter.getData().get(position).getId());
        int type = adapter.getData().get(position).getItemType();
        Intent intent = new Intent(mContext, TeamDetailActivity2.class);
        intent.putExtra("id", adapter.getData().get(position).getId());
        startActivity(intent);

        switch (type) {
            case 1:
                if (adapter.getData().get(position).getCreator() == Constants.uid) {
                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_myTeamEnterDetail");
                }
                break;
            case 2:
                MobclickAgent.onEvent(mContext, "event_teamEnterHall");
                break;
            case 3:
                MobclickAgent.onEvent(mContext, "event_teamActivityEnterHall");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
        }
    }
}
