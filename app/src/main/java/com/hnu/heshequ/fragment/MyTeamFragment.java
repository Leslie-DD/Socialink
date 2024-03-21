package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.StatementDetailActivity;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.adapter.recycleview.CommentTeamAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dev06 on 2018/5/31.
 */
public class MyTeamFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, CommentTeamAdapter.OnItemClickListener {

    private XRecyclerView rv;
    private View view;
    private ArrayList<TeamBean> list;
    public CommentTeamAdapter adapter;
    private int pn = 1;
    private int ps;
    private final String TAG = "MyTeamFragment";
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private Gson gson = new Gson();
    private SharedPreferences sp;
    private TeamBean teamBean;
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private JSONArray jsonArray;
    private boolean firstin = true;
    private TextView tvTips;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e(TAG, "" + result);
        int resultType = result.optInt("code");
        switch (where) {
            case GETDATA:
                switch (resultType) {
                    case 0:
                        list = new ArrayList<>();
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getJSONObject(i)
                                                .getJSONObject("obj").toString(), TeamBean.class);
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                        if (teamBean.getSpeak() != null) {
                                            TeamBean teamBean1 = new TeamBean();
                                            teamBean1.setItemType(2);
                                            teamBean1.setSpeak(teamBean.getSpeak());
                                            teamBean1.setId(teamBean.getId());
                                            teamBean1.setName(teamBean.getName());
                                            teamBean1.setLogoImage(teamBean.getLogoImage());
                                            teamBean1.setCreatorName(teamBean.getCreatorName());
                                            teamBean1.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean1.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean1);
                                        }
                                        if (teamBean.getActivity() != null) {
                                            TeamBean teamBean2 = new TeamBean();
                                            teamBean2.setItemType(3);
                                            teamBean2.setActivity(teamBean.getActivity());
                                            teamBean2.setId(teamBean.getId());
                                            teamBean2.setName(teamBean.getName());
                                            teamBean2.setLogoImage(teamBean.getLogoImage());
                                            teamBean2.setCreatorName(teamBean.getCreatorName());
                                            teamBean2.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean2.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean2);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            if (list.size() == 0) {
                                tvTips.setVisibility(View.VISIBLE);
                            } else {
                                tvTips.setVisibility(View.GONE);
                            }
                            setData(list);
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
            case REFDATA:
                switch (resultType) {
                    case 0:
                        list = new ArrayList<>();
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getJSONObject(i)
                                                .getJSONObject("obj").toString(), TeamBean.class);
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                        if (teamBean.getSpeak() != null) {
                                            TeamBean teamBean1 = new TeamBean();
                                            teamBean1.setItemType(2);
                                            teamBean1.setSpeak(teamBean.getSpeak());
                                            teamBean1.setId(teamBean.getId());
                                            teamBean1.setName(teamBean.getName());
                                            teamBean1.setLogoImage(teamBean.getLogoImage());
                                            teamBean1.setCreatorName(teamBean.getCreatorName());
                                            teamBean1.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean1.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean1);
                                        }
                                        if (teamBean.getActivity() != null) {
                                            TeamBean teamBean2 = new TeamBean();
                                            teamBean2.setItemType(3);
                                            teamBean2.setActivity(teamBean.getActivity());
                                            teamBean2.setId(teamBean.getId());
                                            teamBean2.setName(teamBean.getName());
                                            teamBean2.setLogoImage(teamBean.getLogoImage());
                                            teamBean2.setCreatorName(teamBean.getCreatorName());
                                            teamBean2.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean2.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean2);
                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            if (list.size() == 0) {
                                tvTips.setVisibility(View.VISIBLE);
                            } else {
                                tvTips.setVisibility(View.GONE);
                            }
                            setData(list);
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
            case LOADATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getJSONObject(i)
                                                .getJSONObject("obj").toString(), TeamBean.class);
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                        if (teamBean.getSpeak() != null) {
                                            TeamBean teamBean1 = new TeamBean();
                                            teamBean1.setItemType(2);
                                            teamBean1.setSpeak(teamBean.getSpeak());
                                            teamBean1.setId(teamBean.getId());
                                            teamBean1.setName(teamBean.getName());
                                            teamBean1.setLogoImage(teamBean.getLogoImage());
                                            teamBean1.setCreatorName(teamBean.getCreatorName());
                                            teamBean1.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean1.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean1);
                                        }
                                        if (teamBean.getActivity() != null) {
                                            TeamBean teamBean2 = new TeamBean();
                                            teamBean2.setItemType(3);
                                            teamBean2.setActivity(teamBean.getActivity());
                                            teamBean2.setId(teamBean.getId());
                                            teamBean2.setName(teamBean.getName());
                                            teamBean2.setLogoImage(teamBean.getLogoImage());
                                            teamBean2.setCreatorName(teamBean.getCreatorName());
                                            teamBean2.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean2.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean2);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        view = inflater.inflate(R.layout.only_rv_item, null);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        sp = MeetApplication.getInstance().getSharedPreferences();
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(mContext, list);
        adapter.setListener(this);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData(pn, 0);
        return view;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                type = 1;
                getData(pn, type);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pn < ps) {
                    pn++;
                    type = 2;
                    getData(pn, type);
                } else {

                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 2, "" + pn, "" + Constants.default_PS});
                sendPostConnection(WenConstans.MyFoots, GETDATA, sp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"" + 2, "" + pn, "" + Constants.default_PS});
                sendPostConnection(WenConstans.MyFoots, REFDATA, sp.getString("token", ""));
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"" + 2, "" + pn, "" + Constants.default_PS});
                sendPostConnection(WenConstans.MyFoots, LOADATA, sp.getString("token", ""));
                break;
        }
    }

    public void setData(ArrayList<TeamBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<TeamBean>());
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
                //Utils.toastShort(getActivity(),"团言详情");
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
