package com.example.heshequ.fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.activity.team.TeamDetailActivity2;
import com.example.heshequ.adapter.recycleview.OtherSayAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefStatementEvent;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev06 on 2018/5/15.
 */
public class StatementFragment extends NetWorkFragment implements View.OnClickListener {
    private XRecyclerView rv;
    private TextView tvTips;
    private int status = -1;
    private OtherSayAdapter adapter;
    private ArrayList<TeamBean.SpeakBean> data;
    private View view;
    private TeamDetailActivity2 mActivity;
    private int index = 1;
    private final int getCode = 1000;
    private final int delSpeak = 1001;
    private boolean isfresh;
    private int totalPage = 0;
    private boolean loadmore;


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where == getCode) {
            Log.e("YSF", "团言：" + result.toString());
            try {
                JSONObject jsonObject = new JSONObject(result.optString("data"));
                if (data != null) {
                    totalPage = jsonObject.optInt("totalPage");
                    Log.e("StatementFragment",totalPage+"页");
                    data = new Gson().fromJson(jsonObject.optString("list"), new TypeToken<List<TeamBean.SpeakBean>>() {}.getType());
                    if (isfresh){
                        adapter.setData(data);
                    }else{
                        adapter.setData2(data);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isfresh) {
                if (mActivity!=null){
                    mActivity.setFinish(0);
                }
//                rv.refreshComplete();
            }
            if (loadmore){
                loadmore=false;
                if (mActivity!=null){
                    mActivity.setFinish(1);
                }
            }
            tvTips.setVisibility(data.size()>0?View.GONE:View.VISIBLE);
        }else if (where == delSpeak){
            switch (result.optInt("code")) {
                case 0:
                    Utils.toastShort(getActivity(), result.optString("msg"));
                    isfresh = true;
                    index = 1;
                    getData();
                    break;
                case 1:
                    Utils.toastShort(getActivity(), "您还没有登录或登录已过期，请重新登录");
                    break;
                case 2:
                    Utils.toastShort(getActivity(), result.optString("msg"));
                    break;
                case 3:
                    Utils.toastShort(getActivity(), "您没有该操作权限");
                    break;

            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        if (where == getCode) {
            if (isfresh) {
                if (mActivity!=null){
                    mActivity.setFinish(0);
                }
            } else {
                if (mActivity!=null){
                    mActivity.setFinish(1);
                }
            }
        }
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.statementfragment, null);
        init();
        event();
        return view;
    }

    private void getData() {
        if (mActivity.id != 0) {
            setBodyParams(new String[]{"type", "id", "pn", "ps"}, new String[]{"1", mActivity.id + ""
                    , index + "", Constants.default_PS + ""});
            sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/speak/pglist.do", getCode, Constants.token);
        }
    }

    private void init() {
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = view.findViewById(R.id.tvTips);
        ConsTants.initXrecycleView(mContext, false, false, rv);
        rv.setNestedScrollingEnabled(false);
        mActivity = (TeamDetailActivity2) getActivity();
        getData();
        data = new ArrayList<>();
        adapter = new OtherSayAdapter(mContext, data,1);
        rv.setAdapter(adapter);
        setBg(0);
    }


    private void event() {
        adapter.setDelItemListener(new OtherSayAdapter.DelItemListener() {
            @Override
            public void onDel(int position) {
                setBodyParams(new String[]{"speakId"}, new String[]{"" + data.get(position).getId()});
                sendPost(Constants.base_url + "/api/club/speak/delete.do", delSpeak, Constants.token);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNew:
                setBg(0);
                break;
        }
    }

    private void setBg(int status) {
        if (status == this.status) {
            return;
        }
        this.status = status;
    }


    @Subscribe (threadMode = ThreadMode.MAIN)
    public void addRefresh(RefStatementEvent event){
        isfresh = true;
        index = 1;
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void refreshData() {
        isfresh = true;
        index = 1;
        getData();
    }

    public void loadmoreData(){
        isfresh = false;
        loadmore = true;
        if (index < totalPage) {
            index++;
            getData();
        } else {
            if (mActivity!=null){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.setFinish(1);
                    }
                },800);
            }
        }
    }
}
