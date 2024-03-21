package com.hnu.heshequ.activity.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/5/9.
 */

public class ShidaZhouSelect extends NetWorkActivity implements View.OnClickListener {
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
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.set:
                week1 = zhoushu.getText().toString();
                Intent intent1 = new Intent();
                intent1.setClass(ShidaZhouSelect.this, ShidaZhouShow.class);
                ShidaZhouShow.week = week1;
                startActivity(intent1);
                finish();
                break;
        }
    }


}
