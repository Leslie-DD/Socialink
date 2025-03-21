package com.leslie.socialink.activity.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dell on 2020/4/27.
 * Description: 课程表模块 学年学期选择页面
 */

public class TimetableSelect extends NetWorkActivity {
    public static String studentId;
    public static String pwd;
    public static String schoolname;
    //studentid和pwd的赋值是通过静态变量直接赋值的。
    private EditText year1, year2, term, today, week;
    private static String year_1, year_2, term_1, today_1, week_1;
    private static String key = "", url = "";
    private TextView tvcancel, tvsave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetableselect);
        Log.e("schoolname", "" + schoolname);
        init();
        event();
    }

    private void init() {
        setText("选择当前学期");
        year1 = (EditText) findViewById(R.id.year1);
        year2 = (EditText) findViewById(R.id.year2);
        term = (EditText) findViewById(R.id.term);
        week = (EditText) findViewById(R.id.week);
        today = (EditText) findViewById(R.id.today);
        tvcancel = (TextView) findViewById(R.id.tvCancel);
        tvsave = (TextView) findViewById(R.id.tvSave);
        //getData();
    }

    private void event() {
        tvcancel.setOnClickListener(v -> finish());
        tvsave.setOnClickListener(v -> {
            if (schoolname.equals("hnu")) {
                year_1 = year1.getText().toString();
                year_2 = year2.getText().toString();
                term_1 = term.getText().toString();
                week_1 = week.getText().toString();
                today_1 = today.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("year1", "" + year_1);
                intent.putExtra("year2", "" + year_2);
                intent.putExtra("term1", "" + term_1);
                intent.putExtra("week", "" + week_1);
                intent.putExtra("today", "" + today_1);
                intent.putExtra("pwd", pwd + "");
                intent.putExtra("studentId", studentId + "");
                intent.putExtra("schoolname", schoolname + "");
                intent.setClass(TimetableSelect.this, TimetableShow.class);
                startActivity(intent);
                finish();
            } else if (schoolname.equals("hunnu")) {
                year_1 = year1.getText().toString();
                year_2 = year2.getText().toString();
                term_1 = term.getText().toString();
                week_1 = week.getText().toString();
                today_1 = today.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("year1", "" + year_1);
                intent.putExtra("year2", "" + year_2);
                intent.putExtra("term1", "" + term_1);
                intent.putExtra("week", "" + week_1);
                intent.putExtra("today", "" + today_1);
                intent.putExtra("pwd", pwd + "");
                intent.putExtra("studentId", studentId + "");
                intent.putExtra("schoolname", schoolname + "");
                intent.setClass(TimetableSelect.this, ShidaTimetableShow.class);
                startActivity(intent);
                finish();
            } else {
                year_1 = year1.getText().toString();
                year_2 = year2.getText().toString();
                term_1 = term.getText().toString();
                week_1 = week.getText().toString();
                today_1 = today.getText().toString();
                OkHttpUtils.post(Constants.ZHONGNAN_VERIFICATION_CODE)
                        .tag(this)
                        .headers(Constants.TOKEN_HEADER, Constants.token)
                        .params("studentId", studentId + "")
                        .params("pwd", "" + pwd)
                        .params("yearFirst", "" + year_1)
                        .params("yearSecond", "" + year_2)
                        .params("num", term_1 + "")
                        .params("Date", "" + today_1)
                        .params("currentWeek", "" + week_1)
                        .params("school", "" + schoolname)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                Log.e("gerenxinxi", "sresult:" + s);
                                try {
                                    JSONObject result = new JSONObject(s);
                                    JSONObject dd = new JSONObject(result.optString("data"));
                                    String key1 = dd.getString("key");
                                    Log.e("keyshiwoao", key1 + "");
                                    String url = dd.optString("URL");
                                    Log.e("urlshiwoao", url + "");
                                    Intent intent1 = new Intent();
                                    intent1.putExtra("key", key1 + "");
                                    intent1.putExtra("url", url + "");
                                    intent1.putExtra("year1", "" + year_1);
                                    intent1.putExtra("year2", "" + year_2);
                                    intent1.putExtra("term1", "" + term_1);
                                    intent1.putExtra("week", "" + week_1);
                                    intent1.putExtra("today", "" + today_1);
                                    intent1.putExtra("pwd", pwd + "");
                                    intent1.putExtra("studentId", studentId + "");
                                    intent1.putExtra("schoolname", schoolname + "");
                                    intent1.setClass(TimetableSelect.this, TimetableSecondCheckin.class);
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

//                    if(!key.equals("")&&!url.equals("")) {
//                        Intent intent1 = new Intent();
//                        intent1.putExtra("key", key + "");
//                        intent1.putExtra("url", url + "");
//                        intent1.putExtra("year1", "" + year_1);
//                        intent1.putExtra("year2", "" + year_2);
//                        intent1.putExtra("term1", "" + term_1);
//                        intent1.putExtra("week", "" + week_1);
//                        intent1.putExtra("today", "" + today_1);
//                        intent1.putExtra("pwd", pwd + "");
//                        intent1.putExtra("studentId", studentId + "");
//                        intent1.putExtra("schoolname", schoolname + "");
//                        intent1.setClass(TimetableSelect.this, TimetableSecondCheckin.class);
//                        startActivity(intent1);
//                    }
//                    Intent intent = new Intent();
//                    intent.putExtra("year1",""+year_1);
//                    intent.putExtra("year2",""+year_2);
//                    intent.putExtra("term1",""+term_1);
//                    intent.putExtra("week",""+week_1);
//                    intent.putExtra("today",""+today_1);
//                    intent.putExtra("pwd",pwd+"");
//                    intent.putExtra("studentId",studentId+"");
//                    intent.putExtra("schoolname",schoolname+"");
//                    intent.setClass(TimetableSelect.this,TimetableSecondCheckin.class);
//                    startActivity(intent);
            }
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    public void getData() {
        setBodyParams(new String[]{"studentId", "pwd", "yearFirst", "yearSecond", "num", "Date", "currentWeek", "school"}, new String[]{studentId + "", pwd + "", year_1 + "", year_2 + "", term_1 + "", today_1 + "", week_1 + "", "" + schoolname});
        sendPost(Constants.ZHONGNAN_VERIFICATION_CODE, 1, Constants.timetableToken);
    }


}
