package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class FriendQinganSet extends NetWorkActivity {
    private RadioGroup nRg1;
    private int id;
    public static String qingancontent;
    public static String jiaxiang;
    public static int qingan;
    public static String xueyuan;
    public static String zhuanye;
    public static String nianji;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_qinganset);
        nRg1 = findViewById(R.id.rg_1);
        nRg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                qingancontent = radioButton.getText().toString();
                Utils.toastShort(FriendQinganSet.this, qingancontent);
            }
        });
        Intent intent1 = getIntent();
        nianji = intent1.getStringExtra("nianji");
        xueyuan = intent1.getStringExtra("xueyuan");
        zhuanye = intent1.getStringExtra("zhuanye");
        jiaxiang = intent1.getStringExtra("jiaxiang");
//        id = WenConstans.id;
        id = Constants.uid;

        /**
         * 定义未修改情感时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("qinggan", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("情感状态设置");
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            Log.e("qinggancontent", "" + qingancontent);
            if (qingancontent.equals("保密")) {
                qingan = 0;
            } else if (qingancontent.equals("单身")) {
                qingan = 1;
            } else {
                qingan = 2;
            }

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
            i.putExtra("qinggan", qingan);
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        setBodyParams(new String[]{"hometown", "emotion", "academy", "profession", "schoolgrade", "user_id"}, new String[]{"" + jiaxiang, "" + qingan, "" + xueyuan, "" + zhuanye, "" + nianji, "" + id});
        sendPost(Constants.base_url + "/api/social/updateinfor.do", 100, WenConstans.token);
    }


}

