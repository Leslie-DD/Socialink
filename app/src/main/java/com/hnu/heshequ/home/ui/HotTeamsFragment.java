package com.hnu.heshequ.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.adapter.recycleview.CommentTeamAdapter;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.network.HttpRequestUtil;
import com.hnu.heshequ.utils.SharedPreferencesHelp;
import com.hnu.heshequ.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotTeamsFragment
        extends Fragment
        implements CommentTeamAdapter.OnItemClickListener,
        XRecyclerView.LoadingListener,
        IListFragment {
    private static final String TAG = "[HotTeamsFragment]";

    private static final int GET_DATA = 1000;
    private static final int REF_DATA = 1001;
    private static final int LOA_DATA = 1002;

    private static final int TYPE_INIT = 0;
    private static final int TYPE_REFRESH = 1;
    private static final int TYPE_LOAD_MORE = 2;

    private final Gson gson = new Gson();

    public XRecyclerView mRecyclerView;
    public CommentTeamAdapter adapter;
    public ArrayList<TeamBean> list;
    private int pn = 1;
    private int ps = 0;
    private int type;
    private TextView tvTips;

    private final HttpRequestUtil.RequestCallBack callBack = new HttpRequestUtil.RequestCallBack() {
        @Override
        public void onSuccess(JSONObject result, int where, boolean fromCache) {
            Log.i(TAG, "onSuccess: where = " + where + ", result" + result.toString());
            if (where == LOA_DATA) {
                mRecyclerView.loadMoreComplete();
            }
            if (result.optInt("code") != 0) {
                Utils.toastShort(getActivity(), result.optString("msg"));
                return;
            }
            if (result.optString("data").isEmpty()) {
                return;
            }
            TeamBean teamBean;
            JSONArray jsonArray;
            switch (where) {
                case GET_DATA:
                    try {
                        ps = result.optJSONObject("data").optInt("totalPage");
                        list = new ArrayList<>();
                        jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                        Log.i(TAG, "GET_DATA size: " + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                            teamBean.setItemType(1);
                            list.add(teamBean);
                        }
                        setData(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case REF_DATA:
                    try {
                        ps = result.optJSONObject("data").optInt("totalPage");
                        list = new ArrayList<>();
                        jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                        Log.i(TAG, "REF_DATA size: " + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                            teamBean.setItemType(1);
                            list.add(teamBean);
                        }
                        setData(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOA_DATA:
                    try {
                        ps = result.optJSONObject("data").optInt("totalPage");
                        jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                        Log.i(TAG, "LOA_DATA size: " + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                            teamBean.setItemType(1);
                            list.add(teamBean);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFailure(String result, int where) {
            Utils.toastShort(getActivity(), "网络异常");
            if (where == REF_DATA) {
                mRecyclerView.loadMoreComplete();
            }
        }
    };

    private final HttpRequestUtil httpRequest = new HttpRequestUtil(callBack, TAG);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG, "hashcode: " + hashCode() + " (onCreateView)");
        View view = inflater.inflate(R.layout.fragment_tim, container, false);
        tvTips = view.findViewById(R.id.tvTips);
        mRecyclerView = view.findViewById(R.id.rv);
        init();
        return view;
    }

    private void init() {
        ConsTants.initXRecycleView(getActivity(), true, false, mRecyclerView);
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(getActivity(), list);
        adapter.setListener(this);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setAdapter(adapter);

        type = TYPE_INIT;
        pn = 1;
        getData(pn, type);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> mRecyclerView.refreshComplete(), 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(this::loadMoreData, 1000);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                httpRequest.setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                httpRequest.sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", GET_DATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 1:
                httpRequest.setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                httpRequest.sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", REF_DATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 2:
                httpRequest.setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                httpRequest.sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", LOA_DATA, SharedPreferencesHelp.getString("token", ""));
                break;
        }
    }

    public void refreshData() {
        pn = 1;
        type = TYPE_REFRESH;
        getData(pn, type);
    }

    public void loadMoreData() {
        if (pn < ps) {
            pn++;
            type = TYPE_LOAD_MORE;
            getData(pn, type);
        } else {
            mRecyclerView.loadMoreComplete();
        }
    }

    public void setData(@NonNull ArrayList<TeamBean> list) {
        if (!list.isEmpty()) {
            tvTips.setVisibility(View.GONE);
            adapter.setData(list);
        } else {
            tvTips.setVisibility(View.VISIBLE);
            adapter.setData(new ArrayList<>());
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getContext(), TeamDetailActivity.class);
        intent.putExtra("id", adapter.getData().get(position).getId());
        startActivity(intent);
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