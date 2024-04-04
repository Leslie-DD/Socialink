package com.leslie.socialink.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.StatementDetailActivity;
import com.leslie.socialink.activity.team.TeamDetailActivity;
import com.leslie.socialink.adapter.recycleview.CommentTeamAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyTeamFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, CommentTeamAdapter.OnItemClickListener {
    private static final String TAG = "[MyTeamFragment]";

    private XRecyclerView rv;
    private ArrayList<TeamBean> list;
    public CommentTeamAdapter adapter;
    private int pn = 1;
    private int ps;
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private final Gson gson = new Gson();
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private TextView tvTips;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e(TAG, "" + result);
        int resultType = result.optInt("code");
        TeamBean teamBean1;
        JSONArray jsonArray;
        switch (where) {
            case GETDATA:
            case REFDATA:
                switch (resultType) {
                    case 0:
                        list = new ArrayList<>();
                        if (result.optString("data").isEmpty()) {
                            break;
                        }
                        try {
                            JSONObject data = result.optJSONObject("data");
                            if (data == null) {
                                break;
                            }
                            ps = data.optInt("totalPage");
                            jsonArray = new JSONArray(data.optString("list"));
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    teamBean1 = gson.fromJson(jsonArray.getJSONObject(i)
                                            .getJSONObject("obj").toString(), TeamBean.class);
                                    teamBean1.setItemType(1);
                                    list.add(teamBean1);
                                    TeamBean teamBean = new TeamBean();
                                    if (teamBean.getSpeak() != null) {
                                        teamBean.setItemType(2);
                                        teamBean.setSpeak(teamBean.getSpeak());
                                        teamBean.setId(teamBean.getId());
                                        teamBean.setName(teamBean.getName());
                                        teamBean.setLogoImage(teamBean.getLogoImage());
                                        teamBean.setCreatorName(teamBean.getCreatorName());
                                        teamBean.setCollectionNumber(teamBean.getCollectionNumber());
                                        teamBean.setMemberNumber(teamBean.getMemberNumber());
                                        list.add(teamBean);
                                    }
                                    if (teamBean.getActivity() != null) {
                                        teamBean.setItemType(3);
                                        teamBean.setActivity(teamBean.getActivity());
                                        teamBean.setId(teamBean.getId());
                                        teamBean.setName(teamBean.getName());
                                        teamBean.setLogoImage(teamBean.getLogoImage());
                                        teamBean.setCreatorName(teamBean.getCreatorName());
                                        teamBean.setCollectionNumber(teamBean.getCollectionNumber());
                                        teamBean.setMemberNumber(teamBean.getMemberNumber());
                                        list.add(teamBean);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        if (list.isEmpty()) {
                            tvTips.setVisibility(View.VISIBLE);
                        } else {
                            tvTips.setVisibility(View.GONE);
                        }
                        setData(list);
                        break;
                    case 1:
                        Utils.toastShort(getActivity(), "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(getActivity(), result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(getActivity(), "您没有该功能操作权限");
                        break;
                }
                break;
            case LOADATA:
                switch (resultType) {
                    case 0:
                        if (result.optString("data").isEmpty()) {
                            break;
                        }
                        try {
                            JSONObject data = result.optJSONObject("data");
                            if (data == null) {
                                break;
                            }
                            ps = data.optInt("totalPage");
                            jsonArray = new JSONArray(data.optString("list"));
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    teamBean1 = gson.fromJson(jsonArray.getJSONObject(i)
                                            .getJSONObject("obj").toString(), TeamBean.class);
                                    teamBean1.setItemType(1);
                                    list.add(teamBean1);
                                    TeamBean teamBean = new TeamBean();
                                    if (teamBean.getSpeak() != null) {
                                        teamBean.setItemType(2);
                                        teamBean.setSpeak(teamBean1.getSpeak());
                                        teamBean.setId(teamBean1.getId());
                                        teamBean.setName(teamBean1.getName());
                                        teamBean.setLogoImage(teamBean1.getLogoImage());
                                        teamBean.setCreatorName(teamBean1.getCreatorName());
                                        teamBean.setCollectionNumber(teamBean1.getCollectionNumber());
                                        teamBean.setMemberNumber(teamBean1.getMemberNumber());
                                        list.add(teamBean);
                                    }
                                    if (teamBean.getActivity() != null) {
                                        teamBean.setItemType(3);
                                        teamBean.setActivity(teamBean1.getActivity());
                                        teamBean.setId(teamBean1.getId());
                                        teamBean.setName(teamBean1.getName());
                                        teamBean.setLogoImage(teamBean1.getLogoImage());
                                        teamBean.setCreatorName(teamBean1.getCreatorName());
                                        teamBean.setCollectionNumber(teamBean1.getCollectionNumber());
                                        teamBean.setMemberNumber(teamBean1.getMemberNumber());
                                        list.add(teamBean);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        Utils.toastShort(getActivity(), "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(getActivity(), result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(getActivity(), "您没有该功能操作权限");
                        break;
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.only_rv_item, null);
        tvTips = view.findViewById(R.id.tvTips);
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(mContext, list);
        adapter.setListener(this);
        rv = view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData(pn, 0);
        return view;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            pn = 1;
            type = 1;
            getData(pn, type);
            rv.refreshComplete();
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(() -> {
            if (pn < ps) {
                pn++;
                type = 2;
                getData(pn, type);
            }
            rv.loadMoreComplete();
        }, 1000);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 2, "" + pn, "" + Constants.DEFAULT_PS});
                sendPostConnection(Constants.MY_FOOT_PRINT, GETDATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 2, "" + pn, "" + Constants.DEFAULT_PS});
                sendPostConnection(Constants.MY_FOOT_PRINT, REFDATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 2, "" + pn, "" + Constants.DEFAULT_PS});
                sendPostConnection(Constants.MY_FOOT_PRINT, LOADATA, SharedPreferencesHelp.getString("token", ""));
                break;
        }
    }

    public void setData(ArrayList<TeamBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<>());
        }
    }

    @Override
    public void OnItemClick(int position) {
        int type = adapter.getData().get(position).getItemType();
        switch (type) {
            case 1:
                Intent intent = new Intent(mContext, TeamDetailActivity.class);
                intent.putExtra("id", adapter.getData().get(position).getId());
                startActivity(intent);
                break;
            case 2:
                Intent intent1 = new Intent(mContext, StatementDetailActivity.class);
                intent1.putExtra("bean", adapter.getData().get(position));
                startActivity(intent1);
                break;
            case 3:
                Utils.toastShort(getActivity(), "活动详情");
                break;
        }
    }
}
