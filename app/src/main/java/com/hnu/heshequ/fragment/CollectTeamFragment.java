package com.hnu.heshequ.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.TeamAndWenwenAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.WenwenBean;
import com.hnu.heshequ.constans.ResultUtils;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class CollectTeamFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, TeamAndWenwenAdapter.DoSaveListener {
    private View view;
    private XRecyclerView rv;
    private TeamAndWenwenAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<WenwenBean> allList, moreList;
    private int clickPosition;
    private TextView tvTips;
    private FragmentBrodcast brodcast;

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        init();
        getData(100);
        setFragmentListener();
        return view;
    }

    private void init() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        adapter = new TeamAndWenwenAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        adapter.DoSaveListener(this);
    }

    private void getData(int where) {
        setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{pn + "", ps + "", "1"});
        sendPostConnection(WenConstans.MySaves, where, WenConstans.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }
        Gson gson = new Gson();
        if (where == 100) {
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            allList = new ArrayList<>();
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    JSONArray array = data.getJSONArray("list");
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject oo = array.getJSONObject(i);
                            if (oo != null) {
                                WenwenBean bean = gson.fromJson(oo
                                        .getJSONObject("obj").toString(), WenwenBean.class);
                                bean.category = oo.getInt("category");
                                allList.add(bean);
                            }
                        }
                    }
//                    allList = gson.fromJson(data.getJSONArray("list").toString(),
//                            new TypeToken<List<WenwenBean>>(){}.getType());
//                    if (allList==null||allList.size()==0){
//                        allList=new ArrayList<>();
//                    }
                    if (data.has("totalPage")) {
                        totalPage = data.getInt("totalPage");
                    }
                }
            }
            if (allList.size() == 0) {
                tvTips.setVisibility(View.VISIBLE);
            } else {
                tvTips.setVisibility(View.GONE);
            }
            adapter.setData(allList);
        } else if (where == 101) {
            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    JSONArray array = data.getJSONArray("list");
                    if (array != null && array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject oo = array.getJSONObject(i);
                            if (oo != null) {
                                WenwenBean bean = gson.fromJson(oo
                                        .getJSONObject("obj").toString(), WenwenBean.class);
                                bean.category = oo.getInt("category");
                                moreList.add(bean);
                            }
                        }
                    }
//                    moreList = gson.fromJson(data.getJSONArray("list").toString(),
//                            new TypeToken<List<WenwenBean>>(){}.getType());
//                    if (moreList==null||moreList.size()==0){
//                        moreList=new ArrayList<>();
//                    }
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
            int zan = allList.get(clickPosition).likeAmount;
            if (TextUtils.isEmpty(allList.get(clickPosition).userLike)) {
                allList.get(clickPosition).userLike = "1";
                zan = zan + 1;
                allList.get(clickPosition).likeAmount = zan;
            } else {
                allList.get(clickPosition).userLike = "";
                zan = zan - 1;
                allList.get(clickPosition).likeAmount = zan;
            }

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
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{allList.get(position).id + ""});
        sendPostConnection(WenConstans.WwLike, 1000, WenConstans.token);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
