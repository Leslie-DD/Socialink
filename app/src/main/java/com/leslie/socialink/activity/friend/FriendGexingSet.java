package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class FriendGexingSet extends NetWorkActivity {
    private EditText gexing;
    private String gexing1;
    private static String shengri;
    private int id;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_gexingset);
        Intent intent1 = getIntent();
        shengri = intent1.getStringExtra("shengri");
//        id = Constants.uid;
        id = Constants.uid;

        /**
         * 定义未修改个性时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("gexing", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("个人描述设置");
        gexing = (EditText) findViewById(R.id.gexing);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            gexing1 = gexing.getText().toString();
            getData();
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i = new Intent();
            i.putExtra("gexing", gexing.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        setBodyParams(new String[]{"descroption", "birthday", "user_id"}, new String[]{"" + gexing1, "" + shengri, id + ""});
        sendPost(Constants.BASE_URL + "/api/social/updateinfo.do", 100, Constants.token);
    }
}

