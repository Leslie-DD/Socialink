package com.leslie.socialink.activity.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class ZhouSelect extends NetWorkActivity {
    private TextView zhoushu;
    private String week1;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhouselect);
        init();
        event();
    }

    private void init() {
        setText("选择周数");
        set = (Button) findViewById(R.id.set);
        zhoushu = (EditText) findViewById(R.id.zhoushu);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            week1 = zhoushu.getText().toString();
            Intent intent1 = new Intent();
            intent1.setClass(ZhouSelect.this, Zhoushow.class);
            Zhoushow.week = week1;
            startActivity(intent1);
            finish();
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }
}
