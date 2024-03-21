package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.adapter.recycleview.CommentTeamAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

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
            view = inflater.inflate(R.layout.vp_tim, null);
            init();
        }
        return view;
    }

    private void init() {
        rv = (XRecyclerView) view.findViewById(R.id.rv);
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
