package com.example.heshequ.activity.oldsecond;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.LabelsortAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: SecondClassifyFragment
 * Author: Ding Yifan
 * Data: 2020/9/7
 * Time: 9:30
 * Description: 二手商品主页面的各个二级分类fragment
 */

@SuppressLint("ValidFragment")
public class SecondClassifyFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, LabelsortAdapter.DoSaveListener {
    private LocalBroadcastManager localBroadcastManager;

    private int category1Id;
    private int category2Id;

    private View view;
    private XRecyclerView rv;
    private LabelsortAdapter adapter;
    private int pn = 1;
    private int ps = 5;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<SecondhandgoodBean> allList, moreList;
    private int clickPosition;
    private TextView tvTips;
    private FragmentBrodcast brodcast;

    @SuppressLint("ValidFragment")
    public SecondClassifyFragment(int category1_Id, int category2_Id) {
        category1Id = category1_Id;
        category2Id = category2_Id;
    }

    public static Fragment newInstance(int category1_id, int category2_id) {
        return new SecondClassifyFragment(category1_id, category2_id);
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
//        Utils.toastShort(mContext, "默认加载category2ID: "+category2Id);
        init();
        getData(100);
        setFragmentListener();
        return view;
    }

    private void init() {
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXrecycleView(mContext, true, true, rv);
        adapter = new LabelsortAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        adapter.DoSaveListener(this);
    }

    private void getData(int where) {
        if (category2Id == 0 && category1Id == 0) {
            sendPostConnection(WenConstans.SecondhandRecommend, where, WenConstans.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "category2_id"}, new String[]{pn + "", ps + "", category2Id + ""});
            sendPostConnection(WenConstans.Secondhand, where, WenConstans.token);
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }
        Gson gson = new Gson();
        if (where == 100) {
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                rv.refreshComplete();
            }
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    allList = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<SecondhandgoodBean>>() {
                            }.getType());
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.getInt("totalPage");
                    }
                }
            }
            adapter.setData(allList);

        } else if (where == 101) {
            rv.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONObject data = result.getJSONObject("data");
                if (data != null && data.has("list")) {
                    moreList = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<SecondhandgoodBean>>() {
                            }.getType());
//                    Utils.toastShort(mContext, "下拉加载更多 "+moreList.size());
                    if (moreList == null || moreList.size() == 0) {
                        moreList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.getInt("totalPage");
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
            localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
            final Intent intent = new Intent(SecondFragment.HEIGHT_BROADCAST);
            intent.putExtra("category2ID", category2Id);   //通知fragment，让它去调用vp.resetHeight(position);方法
            localBroadcastManager.sendBroadcast(intent);   //发送本地广播，通知fragment该刷新了

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
        Utils.toastShort(mContext, "@Override下拉加载更多 ");
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

    public void loadMore() {
//        Utils.toastShort(mContext, "SecondClassifyFragment new loadMore()");
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
