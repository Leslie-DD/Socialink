package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class FriendJiaxiangSet extends NetWorkActivity {
    private EditText jiaxiang;
    private String zhuanye;
    private int id;
    private String jiaxiang1;
    private int qingan;
    private String xueyuan;
    private String nianji;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_jiaxiangset);
        Intent intent1 = getIntent();
        qingan = Integer.parseInt(intent1.getStringExtra("qingan"));
        xueyuan = intent1.getStringExtra("xueyuan");
        zhuanye = intent1.getStringExtra("zhuanye");
//        id = WenConstans.id;
        id = Constants.uid;
        nianji = intent1.getStringExtra("nianji");


        /**
         * 定义未修改家乡时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("jiaxiang", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("家乡设置");
        jiaxiang = (EditText) findViewById(R.id.jiaxiang);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            jiaxiang1 = jiaxiang.getText().toString();
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
            i.putExtra("jiaxiang", jiaxiang.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        setBodyParams(new String[]{"hometown", "emotion", "academy", "profession", "schoolgrade", "user_id"}, new String[]{"" + jiaxiang1, "" + qingan, "" + xueyuan, "" + zhuanye, "" + nianji, "" + id});
        sendPost(Constants.base_url + "/api/social/updateinfor.do", 100, WenConstans.token);
    }
}

