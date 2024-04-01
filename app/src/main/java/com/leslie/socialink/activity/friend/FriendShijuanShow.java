package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FriendShijuanShow extends NetWorkActivity {
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
//        id = Constants.uid;
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
        findViewById(R.id.tvCancel).setOnClickListener(v -> finish());
        findViewById(R.id.tvSave).setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setClass(FriendShijuanShow.this, FriendShijuanSet.class);
            startActivity(intent1);
        });

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

    private void getData() {
        setBodyParams(new String[]{"user_id"}, new String[]{"" + id});
        sendPost(Constants.base_url + "/api/social/getquest.do", 100, Constants.token);
    }


}
