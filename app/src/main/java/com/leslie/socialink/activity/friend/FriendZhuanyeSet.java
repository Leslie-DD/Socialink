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


public class FriendZhuanyeSet extends NetWorkActivity {
    private EditText zhuanye;
    private String zhuanye1;
    private int id;
    private String jiaxiang;
    private int qingan;
    private String xueyuan;
    private String nianji;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_zhuanyeset);
        Intent intent1 = getIntent();
        qingan = Integer.parseInt(intent1.getStringExtra("qingan"));
        xueyuan = intent1.getStringExtra("xueyuan");
        nianji = intent1.getStringExtra("nianji");
//        id = Constants.uid;
        id = Constants.uid;
        jiaxiang = intent1.getStringExtra("jiaxiang");

        /**
         * 定义未修改专业时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("zhuanye", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("专业设置");
        zhuanye = (EditText) findViewById(R.id.zhuanye);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            zhuanye1 = zhuanye.getText().toString();
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
            i.putExtra("zhuanye", zhuanye.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        setBodyParams(new String[]{"hometown", "emotion", "academy", "profession", "schoolgrade", "user_id"}, new String[]{"" + jiaxiang, "" + qingan, "" + xueyuan, "" + zhuanye1, "" + nianji, "" + id});
        sendPost(Constants.base_url + "/api/social/updateinfor.do", 100, Constants.token);
    }


}

