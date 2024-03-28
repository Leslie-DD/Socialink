package com.hnu.heshequ.home.ui;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.HotQuestionsAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.WenwenBean;
import com.hnu.heshequ.constans.ResultUtils;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotQuestionsFragment
        extends NetWorkFragment
        implements HotQuestionsAdapter.DoSaveListener, XRecyclerView.LoadingListener, IListFragment {
    private static final String TAG = "[HotQuestionsFragment]";

    private View view;
    private XRecyclerView mRecyclerView;
    private HotQuestionsAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private List<WenwenBean> newList, moreList;
    private TextView tvTips;
    private int totalPage = 1;
    private boolean hasRefresh;
    private int type = 3;
    private int clickPosition;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }
        try {
            Gson gson = new Gson();
            if (where == 100) {
                if (hasRefresh) {
                    hasRefresh = false;
                }
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        newList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<WenwenBean>>() {
                                }.getType());
                        if (newList == null || newList.isEmpty()) {
                            newList = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            totalPage = data.getInt("totalPage");
                        }
                    } else {
                        newList = new ArrayList<>();
                    }
                } else {
                    newList = new ArrayList<>();
                }
                if (newList.isEmpty()) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 101) {
                mRecyclerView.loadMoreComplete();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<WenwenBean>>() {
                                }.getType());
                        if (moreList == null || moreList.isEmpty()) {
                            moreList = new ArrayList<>();
                        }
                    } else {
                        moreList = new ArrayList<>();
                    }
                } else {
                    moreList = new ArrayList<>();
                }
                newList.addAll(moreList);
                if (newList.isEmpty()) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 1000) {
                Utils.toastShort(mContext, result.getString("msg"));
                int zan = newList.get(clickPosition).likeAmount;
                if (TextUtils.isEmpty(newList.get(clickPosition).userLike)) {
                    newList.get(clickPosition).userLike = "1";
                    zan = zan + 1;
                } else {
                    newList.get(clickPosition).userLike = "";
                    zan = zan - 1;
                }
                newList.get(clickPosition).likeAmount = zan;
                adapter.setData(newList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        if (where == 101) {
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        Log.i(TAG, "(createView)");
        view = inflater.inflate(R.layout.fragment_tim, null);
        tvTips = view.findViewById(R.id.tvTips);
        adapter = new HotQuestionsAdapter(getActivity());
        mRecyclerView = view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(getActivity(), true, false, mRecyclerView);
        mRecyclerView.setLoadingListener(this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, "1", "20"});
        sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
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

    private void setList(List<WenwenBean> list) {
        if (adapter != null) {
            adapter.setData(list);
        }
    }

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            hasRefresh = true;
            pn = 1;
            setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, pn + "", ps + ""});
            sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        } else {
            pn++;
            if (pn > totalPage) {
                mRecyclerView.loadMoreComplete();
            } else {
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, pn + "", ps + ""});
                sendPostConnection(WenConstans.WenwenList, 101, WenConstans.token);
            }
        }
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id});
        sendPostConnection(WenConstans.WwLike, 1000, WenConstans.token);
    }

    @Override
    public boolean isFirstItemVisible() {
        if (mRecyclerView == null) {
            return false;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
            return firstVisiblePosition == 0;
        }
        return false;
    }
}