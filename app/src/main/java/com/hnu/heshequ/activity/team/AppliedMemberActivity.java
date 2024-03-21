package com.hnu.heshequ.activity.team;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.listview.AppliedMemberAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.AppliedMemberBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppliedMemberActivity extends NetWorkActivity {
    private static final String TAG = "[AppliedMemberActivity]";
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
        adapter.isTeamOwner = getIntent().getBooleanExtra("isTeamOwner", false);
        Log.i(TAG, "isTeamOwner: " + adapter.isTeamOwner);
        lv.setAdapter(adapter);
        getData();
    }

    private void getData() {
        setBodyParams(new String[]{"id"}, new String[]{"" + id});
        sendPost(Constants.base_url + "/api/club/activity/member.do", getCode, Constants.token);
    }

    private void event() {
        ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.i(TAG, "onSuccess where: " + where + ", " + result.toString());
        if (where != getCode) {
            return;
        }
        if (result.optInt("code") != 0) {
            Utils.toastShort(context, result.optString("msg"));
            return;
        }
        data = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<AppliedMemberBean>>() {
        }.getType());
        Log.i(TAG, "List<AppliedMemberBean>: " + data.toString());
        if (data != null && !data.isEmpty()) {
            adapter.setData(data);
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(context, "网络异常");
    }


}
