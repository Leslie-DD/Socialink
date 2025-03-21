package com.leslie.socialink.activity.timetable;

import android.os.Bundle;
import android.widget.Button;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class TimetableAddCourse extends NetWorkActivity {
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
        set = (Button) findViewById(R.id.set);

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

}

