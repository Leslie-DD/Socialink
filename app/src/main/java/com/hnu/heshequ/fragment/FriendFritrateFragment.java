package com.hnu.heshequ.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.FriendNearAdapter;
import com.hnu.heshequ.adapter.recycleview.LabelsortAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.FriendListBean;
import com.hnu.heshequ.constans.ResultUtils;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2020/3/6.
 */

public class FriendFritrateFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, LabelsortAdapter.DoSaveListener {
    private static int distance;
    private static int age;
    private static String college;
    private static String sex;
    private static String interest;
    private View view;
    private XRecyclerView rv;
    private FriendNearAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<FriendListBean> allList, moreList;
    private int clickPosition;
    private TextView tvTips;
    private FragmentBrodcast brodcast;
    double longtitude = 39.92;//维度
    double latitude = 116.46;//经度


    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        Bundle bundle = getArguments();
        distance = Integer.parseInt(bundle.getString("juli"));
        age = Integer.parseInt(bundle.getString("nianling"));
        college = bundle.getString("daxue");
        interest = bundle.getString("xingqu");
        sex = bundle.getString("xingbie");
        init();
        getData();
        setFragmentListener();
        return view;
    }

    private void init() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        adapter = new FriendNearAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);

    }

    private void getData() {
        setBodyParams(new String[]{"age", "sex", "college", "distance", "interest"}, new String[]{"" + age, "" + sex, "" + college, "" + distance, "" + interest});
        sendPostConnection(WenConstans.FriendFritrate, 100, WenConstans.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }

        Gson gson = new Gson();
        if (where == 100) {
            Log.e("where ", "= 100");
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            if (result.has("data")) {


                Log.e("result", "has data");
                JSONArray data = result.getJSONArray("data");

                if (data != null) {
                    allList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendListBean>>() {
                            }.getType());
                    Log.e("data ", "!= null");
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }

                }
            }
            Log.e("allList.size", allList.size() + "");
            adapter.setData(allList);
        } else if (where == 101) {

            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONArray data = result.getJSONArray("data");
                if (data != null) {
                    moreList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendListBean>>() {
                            }.getType());
                    if (moreList == null || moreList.size() == 0) {
                        moreList = new ArrayList<>();
                    }

                }
            }
            allList.addAll(moreList);
            if (allList.size() == 0) {
                tvTips.setVisibility(View.VISIBLE);
            } else {
                tvTips.setVisibility(View.GONE);
            }
            adapter.setData(allList);
        } else if (where == 1000) {
            Utils.toastShort(mContext, result.getString("msg") + "");
            adapter.setData(allList);
        }
    }


    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        pn++;
        if (pn > totalPage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv.loadMoreComplete();
                }
            }, 1000);
        } else {
            getData();
        }
    }

    @Override
    public void doSave(int position) {
//        clickPosition = position;
//        setBodyParams(new String[]{"id"}, new String[]{allList.get(position).id + ""});
//        sendPost(WenConstans.WwLike, 1000, WenConstans.token);
        //暂时还没写交友的关注功能
    }

    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("fragment.listener");
        brodcast = new FragmentBrodcast();
        getActivity().registerReceiver(brodcast, filter);
    }

    private class FragmentBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);

            if (items == 1) {    //加载

            } else if (items == 2) {
                getData();
            } else if (items == 3) {   //刷新

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (brodcast != null) {
            getActivity().unregisterReceiver(brodcast);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
