package com.hnu.heshequ.home.ui;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.HotActiveAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.HotAvtivityBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

public class HotActivitiesFragment extends NetWorkFragment implements XRecyclerView.LoadingListener {

    private static final String TAG = "[HotActivityFragment]";
    private HotActiveAdapter adapter;
    private TextView tvTips;
    XRecyclerView mRecyclerView;
    private final int GetData = 1000;
    private final int LoaMore = 1001;

    private int pn;
    private int totalPage;
    private HotAvtivityBean hotAvtivityBean;
    private final Gson gson = new Gson();
    private ArrayList<HotAvtivityBean.HotBean> data;

    @Override
    protected View createView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_tim, null);
        mRecyclerView = view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(getActivity(), true, false, mRecyclerView);
        mRecyclerView.setLoadingListener(this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvTips = view.findViewById(R.id.tvTips);
        mRecyclerView.setNestedScrollingEnabled(false);

        getData(true);

        adapter = new HotActiveAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onRefresh() {
//        new Handler().postDelayed(() -> mRecyclerView.refreshComplete(), 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(() -> getData(false), 1000);
    }

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            pn = 1;
            setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, Constants.default_PS + ""});
            sendPostConnection(Constants.base_url + "/api/club/activity/hotlist.do", GetData, Constants.token);
        } else if (pn < totalPage) {
            pn++;
            setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, Constants.default_PS + ""});
            sendPostConnection(Constants.base_url + "/api/club/activity/hotlist.do", LoaMore, Constants.token);
        } else {
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.d(TAG, "onSuccess: where: " + where + ", result: " + result.toString());
        if (result.optInt("code") != 0) {
            Utils.toastShort(mContext, result.optString("msg"));
            return;
        }
        switch (where) {
            case GetData:
                hotAvtivityBean = gson.fromJson(result.optString("data"), HotAvtivityBean.class);
                totalPage = hotAvtivityBean.getTotalPage();
                if (hotAvtivityBean.getData() == null) {
                    return;
                }
                if (!hotAvtivityBean.getData().isEmpty()) {
                    tvTips.setVisibility(View.GONE);
                    data = hotAvtivityBean.getData();
                    adapter.setData(data);
                } else {
                    tvTips.setVisibility(View.VISIBLE);
                    data = new ArrayList<>();
                    adapter.setData(data);
                }
                break;
            case LoaMore:
                mRecyclerView.loadMoreComplete();
                hotAvtivityBean = gson.fromJson(result.optString("data"), HotAvtivityBean.class);
                totalPage = hotAvtivityBean.getTotalPage();
                if (hotAvtivityBean.getData() == null) {
                    return;
                }
                if (!hotAvtivityBean.getData().isEmpty()) {
                    data = hotAvtivityBean.getData();
                    adapter.setData2(data);
                } else {
                    data = new ArrayList<>();
                    adapter.setData2(data);
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
    }
}
