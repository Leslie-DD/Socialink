package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/4/30.
 */

public class FriendShijuanShow extends NetWorkActivity implements View.OnClickListener {
    private int id;
    private String question_1;
    private String question_2;
    private String question_3;
    private String question_4;
    private String question_5;
    private TextView question1, question2, question3, question4, question5;
    private TextView tvCancel, tvSave;
    private String[] content = {"", "", "", "", ""};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijuanshow);
        //Intent intent1 = getIntent();
//        id = WenConstans.id;
        id = Constants.uid;
        init();
        event();
    }

    private void init() {
        setText("我的试卷");
        question1 = (TextView) findViewById(R.id.question1);
        question2 = (TextView) findViewById(R.id.question2);
        question3 = (TextView) findViewById(R.id.question3);
        question4 = (TextView) findViewById(R.id.question4);
        question5 = (TextView) findViewById(R.id.question5);
        getData();
    }

    private void event() {
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvSave).setOnClickListener(this);

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject value = data.getJSONObject(i);
                //获取到title值
                content[i] = value.getString("content");
            }
            if (content[0] != null) {
                question1.setText(content[0]);
            }
            if (content[1] != null) {
                question2.setText(content[1]);
            }
            if (content[2] != null) {
                question3.setText(content[2]);
            }
            if (content[3] != null) {
                question4.setText(content[3]);
            }
            if (content[4] != null) {
                question5.setText(content[4]);
            }
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
//                Intent intent = new Intent(FriendShijuanShow.this,FriendSet.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.tvSave:
                Intent intent1 = new Intent();
                intent1.setClass(FriendShijuanShow.this, FriendShijuanSet.class);
                startActivity(intent1);
                break;

        }
    }

    private void getData() {
        setBodyParams(new String[]{"user_id"}, new String[]{"" + id});
        sendPost(Constants.base_url + "/api/social/getquest.do", 100, WenConstans.token);
    }


}
