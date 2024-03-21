package com.hnu.heshequ.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.SearchTeamAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.SearchTeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TeamSearchFragment extends NetWorkFragment implements XRecyclerView.LoadingListener {

    private View view;
    private XRecyclerView rv;
    private TextView tvTips;
    private int pn = 1;
    private int ps = 10;
    private boolean hasRefresh;
    private int totalPage;
    private String content;
    private List<SearchTeamBean> newList, moreList;
    private SearchTeamAdapter adapter;
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
                                new TypeToken<List<SearchTeamBean>>() {
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
                                new TypeToken<List<SearchTeamBean>>() {
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
            }
        } catch (Exception e) {

        }
    }

    private void setList(List<SearchTeamBean> newList) {
        adapter.setData(newList);
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_only_recycleview, null);
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        ConsTants.initXRecycleView(getActivity(), true, true, rv);
        adapter = new SearchTeamAdapter(getActivity());
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        tvTips = (TextView) view.findViewById(R.id.tvTips);
        getData("");
        return view;
    }

    public void getData(String s) {
        pn = 1;
        content = s;
        if (!islabel) {
            if (TextUtils.isEmpty(s)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"1", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "keyword"},
                        new String[]{"1", pn + "", ps + "", s});
            }
            sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", 100, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"},
                    new String[]{pn + "", ps + "", content});
            sendPostConnection(Constants.base_url + "/api/club/base/labelList.do", 101, Constants.token);
        }
    }

    private void getMore() {
        if (!islabel) {
            if (TextUtils.isEmpty(content)) {
                setBodyParams(new String[]{"type", "pn", "ps"},
                        new String[]{"1", pn + "", ps + ""});
            } else {
                setBodyParams(new String[]{"type", "pn", "ps", "keyword"},
                        new String[]{"1", pn + "", ps + "", content});
            }
            sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", 101, Constants.token);
        } else {
            setBodyParams(new String[]{"pn", "ps", "label"},
                    new String[]{pn + "", ps + "", content});
            sendPostConnection(Constants.base_url + "/api/club/base/labelList.do", 101, Constants.token);
        }
    }

    public void getLableTeamData(String content) {
        islabel = true;
        pn = 1;
        this.content = content;
        setBodyParams(new String[]{"pn", "ps", "label"},
                new String[]{pn + "", ps + "", content});
        sendPostConnection(Constants.base_url + "/api/club/base/labelList.do", 100, Constants.token);
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
            Log.e("ying","走了这里");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("ying","结束了");
                    rv.loadMoreComplete();
                }
            }, 1000);
        } else {
            getMore();
        }*/
        getMore();
    }


}
