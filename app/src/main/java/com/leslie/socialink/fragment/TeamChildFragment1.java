package com.leslie.socialink.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.leslie.socialink.entity.RefStatementEvent;
import com.leslie.socialink.entity.RefTeamChild1;
import com.leslie.socialink.entity.RefreshBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TeamChildFragment1 extends BaseTeamPagerFragment {
    private int pn = 1;
    private int ps = 0;   //总页数
    private final String TAG = "TeamChildFragment1";
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private Gson gson = new Gson();
    private TeamBean teamBean;
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private JSONArray jsonArray;
    private boolean firstin = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
        if (firstin) {
            firstin = false;
            type = 0;
            pn = 1;
            getData(pn, type);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 3, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do",
                        GETDATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 3, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do",
                        REFDATA, SharedPreferencesHelp.getString("token", ""));
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 3, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do",
                        LOADATA, SharedPreferencesHelp.getString("token", ""));
                break;
        }
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

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e(TAG, "" + result);
        int resultType = result.optInt("code");
        switch (where) {
            case GETDATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    tvNoData.setVisibility(View.GONE);
                                    list = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        if (teamBean.getSpeak() != null) {
                                            TeamBean teamBean1 = new TeamBean();
                                            teamBean1.setItemType(2);
                                            teamBean1.setSpeak(teamBean.getSpeak());
                                            teamBean1.setId(teamBean.getId());
                                            teamBean1.setName(teamBean.getName());
                                            teamBean1.setCollege(teamBean.getCollege());
                                            teamBean1.setLogoImage(teamBean.getLogoImage());
                                            teamBean1.setCreatorName(teamBean.getCreatorName());
                                            teamBean1.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean1.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean1);
                                        } else if (teamBean.getActivity() != null) {
                                            TeamBean teamBean2 = new TeamBean();
                                            teamBean2.setItemType(3);
                                            teamBean2.setActivity(teamBean.getActivity());
                                            teamBean2.setId(teamBean.getId());
                                            teamBean2.setName(teamBean.getName());
                                            teamBean2.setCollege(teamBean.getCollege());
                                            teamBean2.setLogoImage(teamBean.getLogoImage());
                                            teamBean2.setCreatorName(teamBean.getCreatorName());
                                            teamBean2.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean2.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean2);
                                        } else {
                                            teamBean.setItemType(1);
                                            list.add(teamBean);
                                        }
                                    }
                                    setData(list);
                                } else {
                                    list = new ArrayList<>();
                                    setData(list);
                                    tvNoData.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(mContext, result.optString("msg"));
                        break;
                }
                break;
            case REFDATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    tvNoData.setVisibility(View.GONE);
                                    list = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        if (teamBean.getSpeak() != null) {
                                            TeamBean teamBean1 = new TeamBean();
                                            teamBean1.setItemType(2);
                                            teamBean1.setSpeak(teamBean.getSpeak());
                                            teamBean1.setId(teamBean.getId());
                                            teamBean1.setName(teamBean.getName());
                                            teamBean1.setCollege(teamBean.getCollege());
                                            teamBean1.setLogoImage(teamBean.getLogoImage());
                                            teamBean1.setCreatorName(teamBean.getCreatorName());
                                            teamBean1.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean1.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean1);
                                        } else if (teamBean.getActivity() != null) {
                                            TeamBean teamBean2 = new TeamBean();
                                            teamBean2.setItemType(3);
                                            teamBean2.setActivity(teamBean.getActivity());
                                            teamBean2.setId(teamBean.getId());
                                            teamBean2.setName(teamBean.getName());
                                            teamBean2.setCollege(teamBean.getCollege());
                                            teamBean2.setLogoImage(teamBean.getLogoImage());
                                            teamBean2.setCreatorName(teamBean.getCreatorName());
                                            teamBean2.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean2.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean2);
                                        } else {
                                            teamBean.setItemType(1);
                                            list.add(teamBean);
                                        }
                                    }
                                    setData(list);
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    list = new ArrayList<>();
                                    setData(list);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(mContext, result.optString("msg"));
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
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        if (teamBean.getSpeak() != null) {
                                            TeamBean teamBean1 = new TeamBean();
                                            teamBean1.setItemType(2);
                                            teamBean1.setSpeak(teamBean.getSpeak());
                                            teamBean1.setId(teamBean.getId());
                                            teamBean1.setName(teamBean.getName());
                                            teamBean1.setCollege(teamBean.getCollege());
                                            teamBean1.setLogoImage(teamBean.getLogoImage());
                                            teamBean1.setCreatorName(teamBean.getCreatorName());
                                            teamBean1.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean1.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean1);
                                        } else if (teamBean.getActivity() != null) {
                                            TeamBean teamBean2 = new TeamBean();
                                            teamBean2.setItemType(3);
                                            teamBean2.setActivity(teamBean.getActivity());
                                            teamBean2.setId(teamBean.getId());
                                            teamBean2.setName(teamBean.getName());
                                            teamBean2.setCollege(teamBean.getCollege());
                                            teamBean2.setLogoImage(teamBean.getLogoImage());
                                            teamBean2.setCreatorName(teamBean.getCreatorName());
                                            teamBean2.setCollectionNumber(teamBean.getCollectionNumber());
                                            teamBean2.setMemberNumber(teamBean.getMemberNumber());
                                            list.add(teamBean2);
                                        } else {
                                            teamBean.setItemType(1);
                                            list.add(teamBean);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(mContext, result.optString("msg"));
                        break;
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(getActivity(), "网络异常");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addTeam(RefTeamChild1 bean) {
        pn = 1;
        type = 1;
        getData(pn, type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addRefresh(RefStatementEvent event) {
        pn = 1;
        type = 1;
        getData(pn, type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshData(RefreshBean refreshBean) {
        if (refreshBean.type.equals("1")) {
            pn = 1;
            type = 1;
            getData(pn, type);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
