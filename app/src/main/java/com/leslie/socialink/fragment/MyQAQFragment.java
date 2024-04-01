package com.leslie.socialink.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.QuestionsAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.network.entity.QuestionBean;

import org.json.JSONObject;

import java.util.ArrayList;


public class MyQAQFragment extends NetWorkFragment {
    private View view;
    private XRecyclerView xRecyclerView;
    private QuestionsAdapter adapter;
    private ArrayList<QuestionBean> data;
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
        ConsTants.initXRecycleView(mContext, true, true, xRecyclerView);
        adapter = new QuestionsAdapter(mContext);
        xRecyclerView.setAdapter(adapter);
        return view;
    }

    private void getData() {

    }
}
