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


public class FriendXuexiaoSet extends NetWorkActivity {
    private EditText xuexiao;
    public static String school;
    public static String touxiang;
    public static String nickname;
    public static int settingClub = 1;
    public static int settingAsk = 1;
    public static int sex1;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_xuexiaoset);
        Intent intent1 = getIntent();
        sex1 = Integer.parseInt(intent1.getStringExtra("sex"));
        nickname = intent1.getStringExtra("nickname");
        touxiang = intent1.getStringExtra("file");

        /**
         * 定义未修改学校时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("xuexiao", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("学校设置");
        xuexiao = (EditText) findViewById(R.id.xuexiao);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            school = xuexiao.getText().toString();
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
            i.putExtra("xuexiao", xuexiao.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
//            Intent i=new Intent();
//            i.putExtra("xuexiao", "");
//            setResult(2,i);
        }
    }

    private void getData() {
        setBodyParams(new String[]{"file", "nickname", "sex", "college", "settingClub", "settingAsk"}, new String[]{"" + touxiang, "" + nickname, "" + sex1, "" + school, "" + settingClub, "" + settingAsk});
        sendPost(Constants.BASE_URL + "/api/user/update.do", 100, Constants.token);
    }


}

