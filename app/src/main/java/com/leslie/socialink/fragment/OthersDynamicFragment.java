package com.leslie.socialink.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.FriendNewAdapter;
import com.leslie.socialink.adapter.recycleview.LabelsortAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.FriendNewBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OthersDynamicFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, LabelsortAdapter.DoSaveListener {
    private View view;
    private XRecyclerView rv;
    private FriendNewAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<FriendNewBean> allList, moreList;
    private int clickPosition;
    private TextView tvTips;
    private FragmentBrodcast brodcast;
    private int hisid;

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        Bundle bundle = getArguments();
        hisid = Integer.parseInt(bundle.getString("hisid"));
        init();
        getData(100);
        setFragmentListener();
        return view;
    }

    private void init() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        adapter = new FriendNewAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);

    }

    private void getData(int where) {
        setBodyParams(new String[]{"pn", "ps", "uid"}, new String[]{"" + pn, "" + Constants.DEFAULT_PS, "" + hisid});
        sendPostConnection(Constants.GET_ONES_DYNAMIC, where, Constants.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }

        Gson gson = new Gson();
        if (where == 100) {
            Log.e("s", "ssssssssssssssssssssssssssssssssssssssssss2");
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            if (result.has("data")) {


                Log.e("s", "ssssssssssssssssssssssssssssssssssssssssss1");
                JSONArray data = result.getJSONArray("data");

                if (data != null) {
                    allList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendNewBean>>() {
                            }.getType());
                    Log.e("s", "ssssssssssssssssssssssssssssssssssssssssss");
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }

                }
            }

            adapter.setData(allList);
        } else if (where == 101) {

            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONArray data = result.getJSONArray("data");
                if (data != null) {
                    moreList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendNewBean>>() {
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
        getData(100);
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
            getData(101);
        }
    }

    @Override
    public void doSave(int position) {
//        clickPosition = position;
//        setBodyParams(new String[]{"id"}, new String[]{allList.get(position).id + ""});
//        sendPost(Constants.WwLike, 1000, Constants.token);
        //暂时还没写交友的关注功能
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void setFragmentListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("fragment.listener");
        brodcast = new FragmentBrodcast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getActivity().registerReceiver(brodcast, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            getActivity().registerReceiver(brodcast, filter);
        }
    }

    private class FragmentBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);

            if (items == 1) {    //加载

            } else if (items == 2) {
                getData(100);
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

}

