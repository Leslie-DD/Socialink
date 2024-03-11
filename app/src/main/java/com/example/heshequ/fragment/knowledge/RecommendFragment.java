package com.example.heshequ.fragment.knowledge;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.knowledge.RecommendAdapter;
import com.example.heshequ.adapter.recycleview.LabelsortAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.knowledge.RecommendItemBean;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, LabelsortAdapter.DoSaveListener {
    private View view;
    private XRecyclerView rv;
    private RecommendAdapter adapter;
    private int pn = 1;
    private int ps = 100;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<RecommendItemBean> allList, moreList;
    private TextView tvTips;


    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        init();
        getData(pn, ps);
        return view;
    }

    private void init() {
        tvTips = view.findViewById(R.id.tvTips);
        rv = view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        adapter = new RecommendAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
    }

    private void getData(int pn, int ps) {
        sendGetConnection(WenConstans.getKnowledgeRecommend + "?pageSize=" + ps + "&pageNum=" + pn, 100, WenConstans.token);
        Log.e("userid", "" + WenConstans.id);
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
            totalPage = result.getJSONObject("data").getInt("totalPage");
            Log.e("RecommendFragment", "totalPage: " + totalPage);
            if (result.has("data")) {

                JSONArray data = result.getJSONObject("data").getJSONArray("list");

                if (data != null) {
                    allList = gson.fromJson(data.toString(),
                            new TypeToken<List<RecommendItemBean>>() {
                            }.getType());

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
                JSONArray data = result.getJSONObject("data").getJSONArray("list");
                if (data != null) {
                    moreList = gson.fromJson(data.toString(),
                            new TypeToken<List<RecommendItemBean>>() {
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
        getData(pn, ps);
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
            getData(pn, ps);
        }
    }

    @Override
    public void doSave(int position) {
//        clickPosition = position;
//        setBodyParams(new String[]{"id"}, new String[]{allList.get(position).id + ""});
//        sendPost(WenConstans.WwLike, 1000, WenConstans.token);
        //暂时还没写交友的关注功能
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
