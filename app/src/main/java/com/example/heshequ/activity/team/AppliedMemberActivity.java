package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.listview.AppliedMemberAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.AppliedMemberBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppliedMemberActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivBack;
    private ListView lv;
    private int id;
    private final int getCode = 1000;
    private ArrayList<AppliedMemberBean> data;
    private Gson gson = new Gson();
    private AppliedMemberAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_member);
        init();
        event();
    }

    private void init() {
        id = getIntent().getIntExtra("id", 0);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("已报名成员");
        lv = (ListView) findViewById(R.id.lv);
        data = new ArrayList<>();
        adapter = new AppliedMemberAdapter(this, data);
        lv.setAdapter(adapter);
        getData();
    }

    private void getData() {
        setBodyParams(new String[]{"id"}, new String[]{"" + id});
        sendPost(Constants.base_url + "/api/club/activity/member.do", getCode, Constants.token);
    }

    private void event() {
        ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.e("ddq", result.toString());
        if (where == getCode) {
            if (result.optInt("code") == 0) {
                data = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<AppliedMemberBean>>() {
                }.getType());
                if (data != null && data.size() > 0) {
                    adapter.setData(data);
                }
            } else {
                Utils.toastShort(context, result.optString("msg"));
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(context, "网络异常");
    }


}
