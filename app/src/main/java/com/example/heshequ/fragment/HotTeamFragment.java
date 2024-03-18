package com.example.heshequ.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.TeamDetailActivity2;
import com.example.heshequ.adapter.recycleview.CommentTeamAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.utils.IChildFragment;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotTeamFragment extends NetWorkFragment implements CommentTeamAdapter.OnItemClickListener, IChildFragment {
    private View view;
    public RecyclerView rv;
    public CommentTeamAdapter adapter;
    public ArrayList<TeamBean> list;
    private int pn = 1;
    private int ps = 0;   //总页数
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private Gson gson = new Gson();
    private SharedPreferences sp;
    private TeamBean teamBean;
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private JSONArray jsonArray;
    private TextView tvTips;

    @Override
    protected View createView(LayoutInflater inflater) {

        view = inflater.inflate(R.layout.fragment_tim, null);
        tvTips = view.findViewById(R.id.tvTips);
        sp = MeetApplication.getInstance().getSharedPreferences();
        init();
        return view;
    }

    private void init() {
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        ConsTants.initXRecycleView(getActivity(), true, true, rv);
        list = new ArrayList<>();
        adapter = new CommentTeamAdapter(getActivity(), list);
        adapter.setListener(this);
        rv.setAdapter(adapter);

        type = 0;
        pn = 1;
        getData(pn, type);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", GETDATA, sp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", REFDATA, sp.getString("token", ""));
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps"}, new String[]{"" + 1, "" + pn, "" + Constants.default_PS});
                sendPostConnection(Constants.base_url + "/api/club/base/pglist.do", LOADATA, sp.getString("token", ""));
                break;
        }
    }

    public void refData() {
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
                Intent intent1 = new Intent();
                intent1.setAction("fragment.listener");
                intent1.putExtra("item", 1);
                getActivity().sendBroadcast(intent1);
            }, 2000);
        }

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        //Log.e(TAG,""+result);
        int resultType = result.optInt("code");
        switch (where) {
            case GETDATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                list = new ArrayList<>();
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                    }
                                }
                                TeamBean bbean = new TeamBean();
                                bbean.setItemType(4);
                                bbean.setId(-1);
                                list.add(bbean);
                                setData(list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    default:
                        Utils.toastShort(getActivity(), result.optString("msg"));
                }
                break;
            case REFDATA:
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 3);
                getActivity().sendBroadcast(intent);
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                list = new ArrayList<>();
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        teamBean.setItemType(1);
                                        list.add(teamBean);
                                    }
                                }
                                TeamBean bbean = new TeamBean();
                                bbean.setItemType(4);
                                bbean.setId(-1);
                                list.add(bbean);
                                setData(list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(getActivity(), result.optString("msg"));
                }
                break;
            case LOADATA:
                Intent intent1 = new Intent();
                intent1.setAction("fragment.listener");
                intent1.putExtra("item", 1);
                getActivity().sendBroadcast(intent1);
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
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
                        Utils.toastShort(getActivity(), result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(getActivity(), "网络异常");
    }

    public void setData(ArrayList<TeamBean> list) {
        if (list != null && list.size() > 0) {
            tvTips.setVisibility(View.GONE);

            adapter.setData(list);
        } else {
            tvTips.setVisibility(View.VISIBLE);
            adapter.setData(new ArrayList<TeamBean>());
        }
    }


    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(mContext, TeamDetailActivity2.class);
        intent.putExtra("id", adapter.getData().get(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean atBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
        Log.d("ChildFragment1", String.format("onLoadMore, visibleItemCount: %d, total: %d, first position: %d", visibleItemCount, totalItemCount, pastVisibleItems));

        return pastVisibleItems + visibleItemCount >= totalItemCount;
    }

}
