package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.view.View;

import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class EditorNoticeActivity extends NetWorkActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_notice);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onClick(View view) {

    }


}
