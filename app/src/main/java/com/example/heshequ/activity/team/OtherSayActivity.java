package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.OtherSayAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefStatementEvent;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OtherSayActivity extends NetWorkActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
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
        setBodyParams(new String[]{"type", "id", "pn", "ps"}, new String[]{"3", getIntent().getIntExtra("teamid", 0) + "", "" + pn, "" + Constants.default_PS});
        sendPost(Constants.base_url + "/api/club/speak/pglist.do", getSayCode, Constants.token);

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ivSearch:

                break;
        }
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
