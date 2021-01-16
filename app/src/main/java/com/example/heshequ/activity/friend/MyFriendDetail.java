package com.example.heshequ.activity.friend;

import android.os.Bundle;
import android.view.View;

import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/5/1.
 */

public class MyFriendDetail extends NetWorkActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetablecheckin);
        init();
        event();
    }
    private void init() {

    }
    private void event(){

    }
    @Override
    protected void onFailure(String result, int where) {

    }
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}

