package com.leslie.socialink.activity.team;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.OtherSayAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.entity.RefStatementEvent;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OtherSayActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private OtherSayAdapter adapter;
    private XRecyclerView rv;
    private TextView tvTitle;
    private final int getSayCode = 1000;
    private ArrayList<TeamBean.SpeakBean> testData;
    private Gson gson = new Gson();
    private boolean isref = true;
    private int pn = 1;
    private int allpn = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_other_say);
        init();
        event();
    }


    private void init() {
        rv = (XRecyclerView) findViewById(R.id.lv);
        //lv = (ListView) findViewById(R.id.lv);
        ConsTants.initXRecycleView(this, true, true, rv);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("他们说");
        getData(1);
        testData = new ArrayList<>();
        adapter = new OtherSayAdapter(mContext, testData, 2);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
    }

    private void getData(int pn) {
        setBodyParams(new String[]{"type", "id", "pn", "ps"}, new String[]{"3", getIntent().getIntExtra("teamid", 0) + "", "" + pn, "" + Constants.DEFAULT_PS});
        sendPost(Constants.BASE_URL + "/api/club/speak/pglist.do", getSayCode, Constants.token);

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                isref = true;
                getData(1);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pn < allpn) {
                    pn++;
                    isref = false;
                    getData(pn);
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getSayCode) {
            try {
                testData = new ArrayList<>();
                JSONObject data = new JSONObject(result.optString("data"));
                if (data != null) {
                    allpn = data.optInt("totalPage");
                    testData = gson.fromJson(data.optString("list"),
                            new TypeToken<List<TeamBean.SpeakBean>>() {
                            }.getType());
                    if (testData != null) {
                        if (isref) {
                            //lvadapter.setData(2,testData);
                            adapter.setData(testData);
                        } else {
                            adapter.setData2(testData);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(this, "网络异常");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addRefresh(RefStatementEvent event) {
        pn = 1;
        isref = true;
        getData(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
