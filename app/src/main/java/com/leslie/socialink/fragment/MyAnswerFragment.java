package com.leslie.socialink.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.QuestionsAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyAnswerFragment extends NetWorkFragment implements QuestionsAdapter.DoSaveListener, XRecyclerView.LoadingListener {
    private View view;
    private XRecyclerView xRecyclerView;
    private QuestionsAdapter adapter;

    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<QuestionBean> allList, moreList;
    private TextView tvTips;
    private int clickPosition;
    private FragmentBrodcast brodcast;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (ResultUtils.isFail(result, getActivity())) {
            return;
        }
        Gson gson = new Gson();
        if (where == 100) {
            allList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                xRecyclerView.refreshComplete();
            }
            if (result.has("data")) {
                JSONObject data = result.optJSONObject("data");
                if (data != null && data.has("list")) {
                    allList = gson.fromJson(data.optJSONArray("list").toString(),
                            new TypeToken<List<QuestionBean>>() {
                            }.getType());
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.optInt("totalPage");
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
            xRecyclerView.loadMoreComplete();
            moreList = new ArrayList<>();
            if (result.has("data")) {
                JSONObject data = result.optJSONObject("data");
                if (data != null && data.has("list")) {
                    moreList = gson.fromJson(data.optJSONArray("list").toString(),
                            new TypeToken<List<QuestionBean>>() {
                            }.getType());
                    if (moreList == null || moreList.size() == 0) {
                        moreList = new ArrayList<>();
                    }
                    if (data.has("totalPage")) {
                        totalPage = data.optInt("totalPage");
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
            Utils.toastShort(mContext, result.optString("msg") + "");
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
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, xRecyclerView);
        adapter = new QuestionsAdapter(mContext);
        adapter.setSaveListener(this);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(this);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        getData(100);
        setFragmentListener();
        return view;
    }

    private void getData(int where) {
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"1", pn + "", ps + ""});
        sendPostConnection(Constants.MY_QUESTIONS, where, Constants.token);
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
                    xRecyclerView.loadMoreComplete();
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
        sendPostConnection(Constants.QUESTION_LIKES, 1000, Constants.token);
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
