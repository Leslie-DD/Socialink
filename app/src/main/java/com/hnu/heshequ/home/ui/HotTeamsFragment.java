package com.hnu.heshequ.home.ui;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.adapter.recycleview.CommentTeamAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.utils.SharedPreferencesHelp;
import com.hnu.heshequ.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotTeamsFragment extends NetWorkFragment implements CommentTeamAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private static final String TAG = "[HotTeamFragment]";
    private View view;
    public XRecyclerView rv;
    public CommentTeamAdapter adapter;
    public ArrayList<TeamBean> list;
    private int pn = 1;
    private int ps = 0;   //总页数
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private Gson gson = new Gson();
    private TeamBean teamBean;
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private JSONArray jsonArray;
    private TextView tvTips;

    @Override
    protected View createView(LayoutInflater inflater) {

        view = inflater.inflate(R.layout.fragment_tim, null);
        tvTips = view.findViewById(R.id.tvTips);
        rv = view.findViewById(R.id.rv);
        init();
        return view;
    }

    private void init() {
        ConsTants.initXRecycleView(getActivity(), true, false, rv);
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(getActivity(), list);
        adapter.setListener(this);
        rv.setLoadingListener(this);
        rv.setAdapter(adapter);

        type = 0;
        pn = 1;
        getData(pn, type);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> rv.refreshComplete(), 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(() -> rv.loadMoreComplete(), 1000);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", GETDATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", REFDATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", LOADATA, SharedPreferencesHelp.getString("token", ""));
                break;
        }
    }

    public void refreshData() {
        pn = 1;
        type = 1;
        getData(pn, type);
    }

    public void loaData() {
        if (pn < ps) {
            pn++;
            type = 2;
            getData(pn, type);
        } else {
            new Handler().postDelayed(() -> {
            }, 2000);
        }

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.i(TAG, "onSuccess: where = " + where + ", result" + result.toString());
        int resultType = result.optInt("code");
        switch (where) {
            case GETDATA:
                if (resultType != 0) {
                    Utils.toastShort(getActivity(), result.optString("msg"));
                    break;
                }
                if (result.optString("data").isEmpty()) {
                    break;
                }
                try {
                    ps = result.optJSONObject("data").optInt("totalPage");
                    list = new ArrayList<>();
                    jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                    Log.i(TAG, "GETDATA size: " + jsonArray.length());
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                            teamBean.setItemType(1);
                            list.add(teamBean);
                        }
                    }
//                    TeamBean bbean = new TeamBean();
//                    bbean.setItemType(4);
//                    bbean.setId(-1);
//                    list.add(bbean);
                    setData(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case REFDATA:
                if (resultType != 0) {
                    Utils.toastShort(getActivity(), result.optString("msg"));
                    break;
                }
                if (result.optString("data").isEmpty()) {
                    break;
                }
                try {
                    ps = result.optJSONObject("data").optInt("totalPage");
                    list = new ArrayList<>();
                    jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                    Log.i(TAG, "REFDATA size: " + jsonArray.length());
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                            teamBean.setItemType(1);
                            list.add(teamBean);
                        }
                    }
//                    TeamBean bbean = new TeamBean();
//                    bbean.setItemType(4);
//                    bbean.setId(-1);
//                    list.add(bbean);
                    setData(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case LOADATA:
                if (resultType != 0) {
                    Utils.toastShort(getActivity(), result.optString("msg"));
                    break;
                }
                if (result.optString("data").isEmpty()) {
                    break;
                }
                try {
                    ps = result.optJSONObject("data").optInt("totalPage");
                    jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                    Log.i(TAG, "LOADATA size: " + jsonArray.length());
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                            teamBean.setItemType(1);
                            list.add(teamBean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(getActivity(), "网络异常");
        switch (where) {
            case GETDATA:
                break;
            case REFDATA:
                break;
            case LOADATA:
                break;
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
        Intent intent = new Intent(mContext, TeamDetailActivity.class);
        intent.putExtra("id", adapter.getData().get(position).getId());
        startActivity(intent);
    }

}
