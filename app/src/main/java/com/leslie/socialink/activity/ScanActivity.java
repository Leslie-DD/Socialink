package com.leslie.socialink.activity;

import android.content.Intent;
import android.os.Bundle;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanActivity extends NetWorkActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        init();

    }

    private void init() {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
