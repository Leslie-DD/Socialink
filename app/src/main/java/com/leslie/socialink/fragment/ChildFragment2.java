package com.leslie.socialink.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.QuestionsAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.constans.ResultUtils;

import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.utils.IChildFragment;
import com.leslie.socialink.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildFragment2 extends NetWorkFragment implements QuestionsAdapter.DoSaveListener, IChildFragment {

    private View view;
    private RecyclerView mRecyclerView;
    private QuestionsAdapter adapter;
    private int pn = 1;
    private int ps = 20;
    private List<QuestionBean> newList, moreList;
    private TextView tvTips;
    private int totalPage = 1;
    private boolean hasRefresh;
    private int type = 2;
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

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_tim, null);
        tvTips = view.findViewById(R.id.tvTips);
        adapter = new QuestionsAdapter(getActivity());
        mRecyclerView = view.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);
        adapter.setSaveListener(this);
        setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, "1", "20"});
        sendPostConnection(Constants.QUESTIONS, 100, Constants.token);
        return view;
    }

    public void setType(int num) {
        type = num;
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
            setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, pn + "", ps + ""});
            sendPostConnection(Constants.QUESTIONS, 100, Constants.token);
        } else {
            pn++;
            if (pn > totalPage) {
            } else {
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + type, pn + "", ps + ""});
                sendPostConnection(Constants.QUESTIONS, 101, Constants.token);
            }
        }
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id});
        sendPostConnection(Constants.QUESTION_LIKES, 1000, Constants.token);
    }

    @Override
    public boolean atBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
        return pastVisibleItems + visibleItemCount >= totalItemCount;
    }

}
