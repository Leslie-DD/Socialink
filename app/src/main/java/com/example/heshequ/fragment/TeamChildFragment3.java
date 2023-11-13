package com.example.heshequ.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefTeamChild1;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.MeetApplication;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Hulk_Zhang on 2018/5/9 11:28
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class TeamChildFragment3 extends BaseTeamPagerFragment {
    private int pn = 1;
    private int ps = 0;   //总页数
    private final String TAG = "TeamChildFragment3";
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private Gson gson = new Gson();
    private SharedPreferences sp;
    private TeamBean teamBean;
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private JSONArray jsonArray;
    private boolean firstin = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        sp = MeetApplication.getInstance().getSharedPreferences();
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
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"" + 2, "" + pn, "" + Constants.default_PS});
                sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/base/pglist.do",
                        GETDATA, sp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"" + 2, "" + pn, "" + Constants.default_PS});
                sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/base/pglist.do",
                        REFDATA, sp.getString("token", ""));

                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"" + 2, "" + pn, "" + Constants.default_PS});
                sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/base/pglist.do",
                        LOADATA, sp.getString("token", ""));
                break;
        }
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
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        //Log.e(TAG, "" + result);
        int resultType = result.optInt("ret");
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
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                    }
                                    setData(list);
                                }else{
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
                        Utils.toastShort(mContext,result.optString("msg"));
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
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                    }
                                    setData(list);
                                }else{
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
                        Utils.toastShort(mContext,result.optString("msg"));
                        break;
                }
                break;
            case LOADATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            ps = result.optJSONObject("data").optInt("totalPage");
                            try {
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
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
                        }
                        break;
                    default:
                        Utils.toastShort(mContext,result.optString("msg"));
                        break;
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addTeam(RefTeamChild1 bean) {
        Log.e("DDQ","刷新");
        pn = 1;
        type = 1;
        getData(pn, type);
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(getActivity(), "网络异常");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
