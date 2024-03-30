package com.hnu.heshequ.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.QuestionsAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.constans.ResultUtils;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.network.entity.QuestionBean;
import com.hnu.heshequ.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChildQuestionsFragment extends NetWorkFragment {
    private static final String TAG = "[ChildWwFragment]";

    private View view;
    private XRecyclerView mRecyclerView;
    private QuestionsAdapter adapter;
    private int pn = 1;
    private int ps = 10;
    private List<QuestionBean> newList, moreList;
    private TextView tvTips;
    private int totalPage = 1;
    private boolean hasRefresh;
    private int type;
    private int clickPosition;
    Gson gson = new Gson();

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.d(TAG, "onSuccess where: " + where + ", result: " + result + ", fromCache: " + fromCache);
        if (ResultUtils.isFail(result, getActivity())) {
            if (where == 100) {
                mRecyclerView.refreshComplete();
            } else if (where == 101) {
                mRecyclerView.loadMoreComplete();
            }
            return;
        }
        try {
            if (where == 100) {
                mRecyclerView.refreshComplete();
                if (hasRefresh) {
                    hasRefresh = false;
                }
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        newList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<QuestionBean>>() {
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
                QuestionBean bean = new QuestionBean();
                bean.type = 1;
                bean.id = "-1";
                newList.add(bean);
                setList(newList);
            } else if (where == 101) {
                mRecyclerView.loadMoreComplete();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<QuestionBean>>() {
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
                Utils.toastShort(mContext, result.getString("msg") + "");
                int zan = newList.get(clickPosition).likeAmount;
                if (TextUtils.isEmpty(newList.get(clickPosition).userLike)) {
                    newList.get(clickPosition).userLike = "1";
                    zan = zan + 1;
                    newList.get(clickPosition).likeAmount = zan;
                } else {
                    newList.get(clickPosition).userLike = "";
                    zan = zan - 1;
                    newList.get(clickPosition).likeAmount = zan;
                }
                adapter.setData(newList);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onFailure(String result, int where) {
        if (where == 100) {
            mRecyclerView.refreshComplete();
        } else if (where == 101) {
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_child_ww, null);
        mRecyclerView = view.findViewById(R.id.rv);
        tvTips = view.findViewById(R.id.tvTips);
        ConsTants.initXRecycleView(getActivity(), true, true, mRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter = new QuestionsAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                view.postDelayed(() -> getData(true), 1000);
            }

            @Override
            public void onLoadMore() {
                view.postDelayed(() -> getData(false), 1000);
            }
        });
        adapter.setSaveListener(position -> {
            clickPosition = position;
            setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id + ""});
            sendPostConnection(WenConstans.WwLike, 1000, WenConstans.token);
        });
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{type + "", "1", "10"});
        sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        return view;
    }

    private void setList(List<QuestionBean> list) {
        if (adapter != null) {
            adapter.setData(list);
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            hasRefresh = true;
            pn = 1;
            setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{type + "", pn + "", ps + ""});
            sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        } else {
            pn++;
            if (pn > totalPage) {
                mRecyclerView.loadMoreComplete();
            } else {
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{type + "", pn + "", ps + ""});
                sendPostConnection(WenConstans.WenwenList, 101, WenConstans.token);
            }
        }
    }

    public static ChildQuestionsFragment newInstance(int type) {
        ChildQuestionsFragment fragment = new ChildQuestionsFragment();
        fragment.setType(type);
        return fragment;
    }
}
