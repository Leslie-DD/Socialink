package com.leslie.socialink.message.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.FriendAddNewsAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.FriendAddNewsBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MessagesFriendApplyFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, IMessagesFragment {
    private View view;
    private XRecyclerView rv;
    private FriendAddNewsAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<FriendAddNewsBean> allList, moreList;
    private int clickPosition;
    private TextView tvTips;
    private FragmentBrodcast brodcast;


    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        init();
        getData(pn);
        setFragmentListener();
        return view;
    }

    private void init() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        adapter = new FriendAddNewsAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);

    }

    private void getData(int pn) {
        setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pn, "" + Constants.default_PS, "" + 4});
        sendPostConnection(WenConstans.ShowFriendAdd, 100, WenConstans.token);
        Log.e("token", "" + WenConstans.token);
        Log.e("userid", "" + WenConstans.id);
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
                JSONObject data1 = result.getJSONObject("data");
                JSONArray data = data1.getJSONArray("list");
                Log.e("friendadd", "" + data.toString());
                if (data != null) {
                    allList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendAddNewsBean>>() {
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
                JSONObject data1 = result.getJSONObject("data");
                JSONArray data = data1.getJSONArray("list");
                if (data != null) {
                    moreList = gson.fromJson(data.toString(),
                            new TypeToken<List<FriendAddNewsBean>>() {
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
        getData(pn);
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
            getData(pn++);
        }
    }

    @Override
    public void refreshData() {
        if (view == null) {
            return;
        }

        pn = 1;
        getData(pn);
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
                getData(pn);
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