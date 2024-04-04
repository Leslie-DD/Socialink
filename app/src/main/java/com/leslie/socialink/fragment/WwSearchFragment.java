package com.leslie.socialink.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
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


public class WwSearchFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, QuestionsAdapter.DoSaveListener {

    private View view;
    private int pn = 1;
    private int ps = 10;
    private XRecyclerView rv;
    private TextView tvTips;
    private String content;
    private boolean hasRefresh;
    private int totalPage;
    private FragmentBrodcast brodcast;
    private List<QuestionBean> newList, moreList;
    private QuestionsAdapter adapter;
    private int clickPosition;

    private boolean islabel = false;

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
                    rv.refreshComplete();
                }
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        newList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<QuestionBean>>() {
                                }.getType());
                        if (newList == null || newList.size() == 0) {
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
                if (newList.size() == 0) {
                    tvTips.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setVisibility(View.GONE);
                }
                setList(newList);
            } else if (where == 101) {
                if (hasRefresh) {
                    hasRefresh = false;
                    rv.refreshComplete();
                }
                rv.loadMoreComplete();
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        moreList = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<QuestionBean>>() {
                                }.getType());
                        if (moreList == null || moreList.size() == 0) {
                            moreList = new ArrayList<>();
                        }
                    } else {
                        moreList = new ArrayList<>();
                    }
                } else {
                    moreList = new ArrayList<>();
                }
                newList.addAll(moreList);
                if (newList.size() == 0) {
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

    private void setList(List<QuestionBean> newList) {
        adapter.setData(newList);
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_only_recycleview, null);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        ConsTants.initXRecycleView(getActivity(), true, true, rv);
        adapter = new QuestionsAdapter(getActivity());
        adapter.setSaveListener(this);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData("");
        setFragmentListener();
        return view;
    }

    public void getData(String s) {
        pn = 1;
        content = s;
        if (!islabel) {
            if (TextUtils.isEmpty(s)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"2", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "name"},
                        new String[]{"2", pn + "", ps + "", s});
            }
            sendPostConnection(Constants.QUESTIONS, 100, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"},
                    new String[]{pn + "", ps + "", content});
            sendPostConnection(Constants.BASE_URL + "/api/ask/base/labelList.do", 101, Constants.token);
        }
    }

    private void getMore() {
        if (!islabel) {
            if (TextUtils.isEmpty(content)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"2", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "name"},
                        new String[]{"2", pn + "", ps + "", content});
            }
            sendPostConnection(Constants.QUESTIONS, 101, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"},
                    new String[]{pn + "", ps + "", content});
            sendPostConnection(Constants.BASE_URL + "/api/ask/base/labelList.do", 101, Constants.token);
        }
    }

    public void getLableWwData(String content) {
        islabel = true;
        pn = 1;
        this.content = content;
        setBodyParams(new String[]{"pn", "ps", "label"},
                new String[]{pn + "", ps + "", content});
        sendPostConnection(Constants.BASE_URL + "/api/ask/base/labelList.do", 100, Constants.token);
    }

    public void setIslabel(boolean islabel) {
        this.islabel = islabel;
    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getData(content);
    }

    @Override
    public void onLoadMore() {
        pn++;
        /*if (pn > totalPage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv.loadMoreComplete();
                }
            }, 1000);
        } else {
            getMore();
        }*/
        getMore();
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
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id + ""});
        sendPostConnection(Constants.QUESTION_LIKES, 1000, Constants.token);
    }

    private class FragmentBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int items = intent.getIntExtra("item", 0);

            if (items == 1) {    //加载

            } else if (items == 2) {
                getData(content);
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
