package com.example.heshequ.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.HotWenwenAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.WenwenBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dev06 on 2018/5/31.
 */
public class MyQAQFragment extends NetWorkFragment {
    private View view;
    private XRecyclerView xRecyclerView;
    private HotWenwenAdapter adapter;
    private ArrayList<WenwenBean> data;
    private TextView tvTips;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        ConsTants.initXrecycleView(mContext, true, true, xRecyclerView);
        adapter = new HotWenwenAdapter(mContext);
        xRecyclerView.setAdapter(adapter);
        return view;
    }

    private void getData() {

    }
}
