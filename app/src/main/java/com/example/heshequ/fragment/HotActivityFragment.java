package com.example.heshequ.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.HotActiveAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.HotAvtivityBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefHotActivityEvent;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotActivityFragment extends NetWorkFragment {

    private static final String TAG = "[HotActivityFragment]";
    private View view;
    private RecyclerView mRecyclerView;
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvTips = (TextView) view.findViewById(R.id.tvTips);
//        ConsTants.initXRecycleView(getActivity(), false, false, mRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);

        pn = 1;
        type = 1;
        getData(pn, type);

        adapter = new HotActiveAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        //adapter.setData();
        return view;
    }

    private void getData(int pn, int type) {
        if (type == 1) {
            setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, Constants.default_PS + ""});
            sendPostConnection(Constants.base_url + "/api/club/activity/hotlist.do", GetData, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, Constants.default_PS + ""});
            sendPostConnection(Constants.base_url + "/api/club/activity/hotlist.do", LoaMore, Constants.token);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refload(RefHotActivityEvent event) {
        if (event.getType() == 1) {
            pn = 1;
            type = 1;
            getData(pn, type);
        } else if (event.getType() == 2) {
            if (pn < totalPage) {
                pn++;
                type = 2;
                getData(pn, type);
            }
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
                if (hotAvtivityBean.getData().size() > 0) {
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
                hotAvtivityBean = gson.fromJson(result.optString("data"), HotAvtivityBean.class);
                totalPage = hotAvtivityBean.getTotalPage();
                if (hotAvtivityBean.getData() == null) {
                    return;
                }
                if (hotAvtivityBean.getData().size() > 0) {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
