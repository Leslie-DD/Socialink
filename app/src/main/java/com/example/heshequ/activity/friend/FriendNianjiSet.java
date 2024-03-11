package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/4/30.
 */

public class FriendNianjiSet extends NetWorkActivity implements View.OnClickListener {
    private EditText nianji;
    private String zhuanye;
    private int id;
    private String jiaxiang;
    private int qingan;
    private String xueyuan;
    private String nianji1;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_nianjiset);
        Intent intent1 = getIntent();
        qingan = Integer.parseInt(intent1.getStringExtra("qingan"));
        xueyuan = intent1.getStringExtra("xueyuan");
        zhuanye = intent1.getStringExtra("zhuanye");
        jiaxiang = intent1.getStringExtra("jiaxiang");
//        id = WenConstans.id;
        id = Constants.uid;
        Utils.toastShort(this, "用户 id:" + id);

        /**
         * 定义未修改年级时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("nianji", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("年级设置");
        nianji = (EditText) findViewById(R.id.nianji);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        nianji.setOnClickListener(this);
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
            i.putExtra("nianji", nianji.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
//                Intent intent = new Intent(FriendNianjiSet.this,FriendSet.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.set:
                nianji1 = nianji.getText().toString();
                getData();
                break;
        }
    }

    private void getData() {
        setBodyParams(new String[]{"hometown", "emotion", "academy", "profession", "schoolgrade", "user_id"}, new String[]{"" + jiaxiang, "" + qingan, "" + xueyuan, "" + zhuanye, "" + nianji1, "" + id});
        sendPost(Constants.base_url + "/api/social/updateinfor.do", 100, WenConstans.token);
    }


}

