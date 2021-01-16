package com.example.heshequ.activity.timetable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/5/10.
 */

public class TimetableAddCourse extends NetWorkActivity implements View.OnClickListener  {
    private Button set;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_addcourse);

        init();
        event();
    }
    private void init() {
        setText("添加课程");
        set=(Button) findViewById(R.id.set);

    }
    private void event(){
        findViewById(R.id.ivBack).setOnClickListener(this);
        set.setOnClickListener(this);
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

