package com.hnu.heshequ.activity.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.network.Constants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class TimetableSecondCheckin extends NetWorkActivity {
    private String year_1, year_2, term_1, today_1, week_1;
    public static String studentId;
    public static String pwd;
    public static String schoolname;
    public static String url;
    public static String key;
    public static String verification;
    public static String key1;
    public EditText yanzheng;
    public ImageView yanzhengtupian;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetablesecondcheckin);
        Intent intent = getIntent();
        year_1 = intent.getStringExtra("year1");
        year_2 = intent.getStringExtra("year2");
        term_1 = intent.getStringExtra("term1");
        week_1 = intent.getStringExtra("week");
        today_1 = intent.getStringExtra("today");
        studentId = intent.getStringExtra("studentId");
        schoolname = intent.getStringExtra("schoolname");
        pwd = intent.getStringExtra("pwd");
        url = intent.getStringExtra("url");
        key = intent.getStringExtra("key");
        init();
        event();
    }

    private void init() {
        setText("中南验证码二次验证");
        yanzheng = (EditText) findViewById(R.id.yanzheng);
        yanzhengtupian = (ImageView) findViewById(R.id.yanzhengtupian);
        if (url == null) {
            Log.e("shownearnull", "" + Constants.base_url + url);
            yanzhengtupian.setImageResource(R.mipmap.head3);
        } else {
            Glide.with(context).load(Constants.base_url + url).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(yanzhengtupian);
            Log.e("shownear", "" + Constants.base_url + url);
        }
    }

    private void event() {
        findViewById(R.id.tvCancel).setOnClickListener(v -> finish());
        findViewById(R.id.tvSave).setOnClickListener(v -> {
            verification = yanzheng.getText().toString();
            Log.e("key", "" + key);
            Log.e("verification", "" + verification);
            OkHttpUtils.post(WenConstans.ZhongnanGetYanzheng)
                    .tag(this)
                    .params("key", key + "")
                    .params("verification", "" + verification)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("gerenxinxi", "sresult:" + s);
                            try {
                                JSONObject result = new JSONObject(s);

                                key1 = result.getString("data");
                                Log.e("key", key1 + "");
                                Intent intent1 = new Intent();
                                intent1.putExtra("key", key1 + "");
                                intent1.putExtra("year1", "" + year_1);
                                intent1.putExtra("year2", "" + year_2);
                                intent1.putExtra("term1", "" + term_1);
                                intent1.putExtra("week", "" + week_1);
                                intent1.putExtra("today", "" + today_1);
                                intent1.putExtra("pwd", pwd + "");
                                intent1.putExtra("studentId", studentId + "");
                                intent1.putExtra("schoolname", schoolname + "");
                                intent1.setClass(TimetableSecondCheckin.this, ZhongnanShow.class);
                                startActivity(intent1);
                                finish();

                            } catch (JSONException e) {
                                Log.e("gerenxinxi", "JSONException: " + e.toString());
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Log.e("gerenxinxi", "onError Exception: " + e.toString());
                        }
                    });
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

}
