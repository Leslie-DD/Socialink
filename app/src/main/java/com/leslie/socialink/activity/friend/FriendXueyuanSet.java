package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class FriendXueyuanSet extends NetWorkActivity {
    private EditText xueyuan;
    private String jiaxiang;
    private int qingan;
    private String zhuanye;
    private String nianji;
    private String xueyuan1;
    private int id;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_xueyuanset);
        Intent intent1 = getIntent();
        qingan = Integer.parseInt(intent1.getStringExtra("qingan"));
        jiaxiang = intent1.getStringExtra("jiaxiang");
        zhuanye = intent1.getStringExtra("zhuanye");
        nianji = intent1.getStringExtra("nianji");
//        id = WenConstans.id;
        id = Constants.uid;
        Utils.toastShort(this, "用户 id:" + id);

        /**
         * 定义未修改学院时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("xueyuan", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("学院设置");
        xueyuan = (EditText) findViewById(R.id.xueyuan);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            xueyuan1 = xueyuan.getText().toString();
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
            i.putExtra("xueyuan", xueyuan.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        setBodyParams(new String[]{"hometown", "emotion", "academy", "profession", "schoolgrade", "user_id"}, new String[]{"" + jiaxiang, "" + qingan, "" + xueyuan1, "" + zhuanye, "" + nianji, "" + id});
        sendPost(Constants.base_url + "/api/social/updateinfor.do", 100, WenConstans.token);
    }


}

