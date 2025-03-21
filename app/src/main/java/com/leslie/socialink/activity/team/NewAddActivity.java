package com.leslie.socialink.activity.team;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.activity.statement.EditorialBulletinActivity;
import com.leslie.socialink.activity.statement.PublishVoteActivity;
import com.leslie.socialink.activity.statement.ReleaseActivitiesActivity;
import com.leslie.socialink.base.NetWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class NewAddActivity extends NetWorkActivity {
    private TextView tvTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add);
        inits();
        event();
    }

    private void inits() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("选择新建类型");
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.ll1).setOnClickListener(v -> {
            startActivity(new Intent(mContext, PublishVoteActivity.class));
            this.finish();
        });
        findViewById(R.id.ll2).setOnClickListener(v -> {
            startActivity(new Intent(mContext, ReleaseActivitiesActivity.class).putExtra("type", 1));
            this.finish();
        });
        findViewById(R.id.ll3).setOnClickListener(v -> {
            startActivity(new Intent(mContext, EditorialBulletinActivity.class).putExtra("type", 1));
            this.finish();
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

}
