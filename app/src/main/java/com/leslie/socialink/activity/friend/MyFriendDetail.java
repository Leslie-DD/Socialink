package com.leslie.socialink.activity.friend;

import android.os.Bundle;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFriendDetail extends NetWorkActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetablecheckin);
        init();
        event();
    }

    private void init() {

    }

    private void event() {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }


}

