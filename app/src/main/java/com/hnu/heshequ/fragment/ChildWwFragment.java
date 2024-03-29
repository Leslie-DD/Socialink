package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.os.Handler;
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


public class ChildWwFragment extends NetWorkFragment implements QuestionsAdapter.DoSaveListener {
    private static final String TAG = "[ChildWwFragment]";

    private View view;
    private XRecyclerView mRecyclerView;
    private QuestionsAdapter adapter;
    private List<QuestionBean> list;
    private int pn = 1;
    private int ps = 10;
    private List<QuestionBean> newList, moreList;
    private TextView tvTips;
    private int totalPage = 1;
    private boolean hasRefresh;
    private int type = 2;
    private int clickPosition;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.d(TAG, "onSuccess where: " + where + ", result: " + result + ", fromCache: " + fromCache);
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }
        try {
            Gson gson = new Gson();
            if (where == 100) {
                if (hasRefresh) {
                    hasRefresh = false;
                    Intent intent = new Intent();
                    intent.setAction("fragment.listener");
                    intent.putExtra("item", 3);
                    getActivity().sendBroadcast(intent);
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
                /*WenwenBean bean = new WenwenBean();
                bean.type = 1;
                bean.id = "-1";
                newList.add(bean);*/
                setList(newList);
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 1);
                getActivity().sendBroadcast(intent);
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

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_child_ww, null);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        ConsTants.initXRecycleView(getActivity(), false, false, mRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter = new QuestionsAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
        list = new ArrayList<>();
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"1", "1", "10"});
        sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        return view;
    }

    private void setList(List<QuestionBean> list) {
        if (adapter != null) {
            adapter.setData(list);
        }
    }

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            hasRefresh = true;
            pn = 1;
            setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"1", pn + "", ps + ""});
            sendPostConnection(WenConstans.WenwenList, 100, WenConstans.token);
        } else {
            pn++;
            if (pn > totalPage) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent();
                    intent.setAction("fragment.listener");
                    intent.putExtra("item", 1);
                    getActivity().sendBroadcast(intent);
                }, 700);
            } else {
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"1", pn + "", ps + ""});
                sendPostConnection(WenConstans.WenwenList, 101, WenConstans.token);
            }
        }
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id + ""});
        sendPostConnection(WenConstans.WwLike, 1000, WenConstans.token);
    }
}
