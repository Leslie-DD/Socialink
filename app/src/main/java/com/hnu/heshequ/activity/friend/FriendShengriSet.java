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

public class FriendShengriSet extends NetWorkActivity  {
    private EditText year, month, day;
    private String year1, month1, day1;
    private static String gexing;
    private int id;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_shengriset);
        Intent intent1 = getIntent();
        gexing = intent1.getStringExtra("gexing");
//        id = WenConstans.id;
        id = Constants.uid;

        /**
         * 定义未修改生日时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("shengri", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("生日设置");
        year = (EditText) findViewById(R.id.year);
        month = (EditText) findViewById(R.id.month);
        day = (EditText) findViewById(R.id.day);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            year1 = year.getText().toString();
            month1 = month.getText().toString();
            day1 = day.getText().toString();
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
            i.putExtra("shengri", year.getText().toString() + "-" + month.getText().toString() + "-" + day.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i = new Intent();
            i.putExtra("shengri", "");
            setResult(2, i);
        }
    }

    private void getData() {
        setBodyParams(new String[]{"description", "birthday", "user_id"}, new String[]{"" + gexing, "" + year1 + "-" + month1 + "-" + day1, id + ""});
        sendPost(Constants.base_url + "/api/social/updateinfo.do", 100, WenConstans.token);
    }


}
