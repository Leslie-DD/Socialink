package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/4/30.
 */

public class FriendGexingSet extends NetWorkActivity implements View.OnClickListener {
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
//        id = WenConstans.id;
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
        findViewById(R.id.ivBack).setOnClickListener(this);
        gexing.setOnClickListener(this);
        set.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
//                Intent intent = new Intent(FriendGexingSet.this,FriendSet.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.set:
                gexing1 = gexing.getText().toString();
                getData();
                break;
        }
    }

    private void getData() {
        setBodyParams(new String[]{"descroption", "birthday", "user_id"}, new String[]{"" + gexing1, "" + shengri, id + ""});
        sendPost(Constants.base_url + "/api/social/updateinfo.do", 100, WenConstans.token);
    }
}

