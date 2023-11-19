package com.example.heshequ.activity.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.statement.EditorialBulletinActivity;
import com.example.heshequ.activity.statement.PublishVoteActivity;
import com.example.heshequ.activity.statement.ReleaseActivitiesActivity;
import com.example.heshequ.base.NetWorkActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class NewAddActivity extends NetWorkActivity implements View.OnClickListener {
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
        findViewById(R.id.ivBack).setOnClickListener(this);
        findViewById(R.id.ll1).setOnClickListener(this);
        findViewById(R.id.ll2).setOnClickListener(this);
        findViewById(R.id.ll3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ll1: //投票
                this.finish();
                MobclickAgent.onEvent(mContext, "event_postVote");
                startActivity(new Intent(mContext, PublishVoteActivity.class));
                break;
            case R.id.ll2: //活动
                this.finish();
                startActivity(new Intent(mContext, ReleaseActivitiesActivity.class).putExtra("type", 1));
                break;
            case R.id.ll3: //公告
                this.finish();
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_enterNoticeVC");
                startActivity(new Intent(mContext, EditorialBulletinActivity.class).putExtra("type", 1));
                break;
        }
    }


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }


}
