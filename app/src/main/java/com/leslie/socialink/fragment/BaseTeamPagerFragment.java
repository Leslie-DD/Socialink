package com.leslie.socialink.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.TeamDetailActivity;
import com.leslie.socialink.adapter.recycleview.CommentTeamAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;

import org.json.JSONObject;

import java.util.ArrayList;


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
            view = inflater.inflate(R.layout.fragment_teams_child, null);
            init();
        }
        return view;
    }

    private void init() {
        rv = view.findViewById(R.id.rv);
        tvNoData = view.findViewById(R.id.tvNoData);
        ConsTants.initXRecycleView(getActivity(), true, true, rv);
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(getActivity(), list);
        adapter.setListener(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        Log.e("ying", "init");

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> rv.refreshComplete(), 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(() -> rv.loadMoreComplete(), 1000);
    }

    public void setData(ArrayList<TeamBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<>());
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
        int type = adapter.getData().get(position).getItemType();
        Intent intent = new Intent(mContext, TeamDetailActivity.class);
        intent.putExtra("id", adapter.getData().get(position).getId());
        startActivity(intent);

        switch (type) {
            case 1:
                if (adapter.getData().get(position).getCreator() == Constants.uid) {
                }
                break;
            case 2:
                break;
            case 3:
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
