package com.leslie.socialink.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.OtherSayAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySayActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private XRecyclerView rv;
    private TextView tvNoData;
    private OtherSayAdapter adapter;
    private ArrayList<TeamBean.SpeakBean> testData;
    private final int getSayCode = 1000;
    private Gson gson = new Gson();
    private boolean isref = true;
    private int pn = 1;
    private int allpn = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_say);
        init();
        event();
    }

    private void init() {
        setText("我的团言");
        rv = (XRecyclerView) findViewById(R.id.rv);
        tvNoData = findViewById(R.id.tvNoData);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        testData = new ArrayList<>();
        adapter = new OtherSayAdapter(mContext, testData, 1);
        rv.setAdapter(adapter);
        pn = 1;
        getData(pn);
    }

    private void event() {
        rv.setLoadingListener(this);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySayActivity.this.finish();
            }
        });
    }

    private void getData(int pn) {
        setBodyParams(new String[]{"pn", "ps"}, new String[]{"" + pn, "" + Constants.DEFAULT_PS});
        sendPost(Constants.BASE_URL + "/api/user/mySpeak.do", 1000, Constants.token);
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
                            tvNoData.setVisibility(testData.size() > 0 ? View.GONE : View.VISIBLE);
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


}
