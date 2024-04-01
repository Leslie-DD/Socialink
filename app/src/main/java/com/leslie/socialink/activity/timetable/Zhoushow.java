package com.leslie.socialink.activity.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Zhoushow extends NetWorkActivity {
    private TextView zhouri_1, zhouri_2, zhouri_3, zhouri_4, zhouri_5;
    private TextView zhouyi_1, zhouyi_2, zhouyi_3, zhouyi_4, zhouyi_5;
    private TextView zhouer_1, zhouer_2, zhouer_3, zhouer_4, zhouer_5;
    private TextView zhousan_1, zhousan_2, zhousan_3, zhousan_4, zhousan_5;
    private TextView zhousi_1, zhousi_2, zhousi_3, zhousi_4, zhousi_5;
    private TextView zhouwu_1, zhouwu_2, zhouwu_3, zhouwu_4, zhouwu_5;
    private TextView zhouliu_1, zhouliu_2, zhouliu_3, zhouliu_4, zhouliu_5;
    private TextView xuanzezhanghao, xuanzezhou;
    public static String week;
    public static String course[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    public static String color[] = {
            "#90e86a", "#f1a1d5", "#f6c665", "#8faff8", "#86edaa",
            "#8c00ff", "#ff008c", "#7c0e00", "#9b910e", "#2a9b0e", "#0e9b8f",
            "#0e349b", "#5c0e9b", "#9b0e71", "#d17373", "#d19973", "#c8d173",
            "#73d17b", "#73c3d1", "#FFFF00", "#7384d1", "#b673d1", "#d173a7",
            "#00BFFF", "#FFFFE0", "#FF69B4", "#DB7093", "#FFFFF0", "#607B8B"};
    public int num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetableshow);
        init();
        event();
    }

    private void init() {
        setText("课程表");
        zhouri_1 = (TextView) findViewById(R.id.zhouri_1);
        zhouri_2 = (TextView) findViewById(R.id.zhouri_2);
        zhouri_3 = (TextView) findViewById(R.id.zhouri_3);
        zhouri_4 = (TextView) findViewById(R.id.zhouri_4);
        zhouri_5 = (TextView) findViewById(R.id.zhouri_5);
        zhouyi_1 = (TextView) findViewById(R.id.zhouyi_1);
        zhouyi_2 = (TextView) findViewById(R.id.zhouyi_2);
        zhouyi_3 = (TextView) findViewById(R.id.zhouyi_3);
        zhouyi_4 = (TextView) findViewById(R.id.zhouyi_4);
        zhouyi_5 = (TextView) findViewById(R.id.zhouyi_5);
        zhouer_1 = (TextView) findViewById(R.id.zhouer_1);
        zhouer_2 = (TextView) findViewById(R.id.zhouer_2);
        zhouer_3 = (TextView) findViewById(R.id.zhouer_3);
        zhouer_4 = (TextView) findViewById(R.id.zhouer_4);
        zhouer_5 = (TextView) findViewById(R.id.zhouer_5);
        zhousan_1 = (TextView) findViewById(R.id.zhousan_1);
        zhousan_2 = (TextView) findViewById(R.id.zhousan_2);
        zhousan_3 = (TextView) findViewById(R.id.zhousan_3);
        zhousan_4 = (TextView) findViewById(R.id.zhousan_4);
        zhousan_5 = (TextView) findViewById(R.id.zhousan_5);
        zhousi_1 = (TextView) findViewById(R.id.zhousi_1);
        zhousi_2 = (TextView) findViewById(R.id.zhousi_2);
        zhousi_3 = (TextView) findViewById(R.id.zhousi_3);
        zhousi_4 = (TextView) findViewById(R.id.zhousi_4);
        zhousi_5 = (TextView) findViewById(R.id.zhousi_5);
        zhouwu_1 = (TextView) findViewById(R.id.zhouwu_1);
        zhouwu_2 = (TextView) findViewById(R.id.zhouwu_2);
        zhouwu_3 = (TextView) findViewById(R.id.zhouwu_3);
        zhouwu_4 = (TextView) findViewById(R.id.zhouwu_4);
        zhouwu_5 = (TextView) findViewById(R.id.zhouwu_5);
        zhouliu_1 = (TextView) findViewById(R.id.zhouliu_1);
        zhouliu_2 = (TextView) findViewById(R.id.zhouliu_2);
        zhouliu_3 = (TextView) findViewById(R.id.zhouliu_3);
        zhouliu_4 = (TextView) findViewById(R.id.zhouliu_4);
        zhouliu_5 = (TextView) findViewById(R.id.zhouliu_5);
        xuanzezhanghao = (TextView) findViewById(R.id.xuanzezhanghao);
        xuanzezhou = (TextView) findViewById(R.id.xuanzezhou);
        getData();
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.ivRight).setOnClickListener(v -> {
            Intent intent3 = new Intent();
            intent3.setClass(Zhoushow.this, TimetableAddCourse.class);
            startActivity(intent3);
            finish();
        });
        xuanzezhou.setOnClickListener(v -> {
            Intent intent2 = new Intent();
            intent2.setClass(Zhoushow.this, ZhouSelect.class);
            startActivity(intent2);
            finish();
        });
        xuanzezhanghao.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setClass(Zhoushow.this, TimetableCheckin.class);
            startActivity(intent1);
            finish();
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        Log.e("fanhuixinxi", result.toString() + "");
        if (ret == 0) {
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject value = data.getJSONObject(i);
                //获取到title值
                String dayinfo = value.getString("dayInfo");
                String begintime = value.getString("beginTime");
                String spantime = value.getString("spanTime");
                String course_name = value.getString("course_name");
                String course_location = value.getString("course_location");
                String course_teacher = value.getString("course_teacher");
                String course_class = value.getString("course_class");
                String course_color = "1";
//                int temp = 0;
//                for(int j = 0;j<num;j++){
//                   if(course_name.equals(course[j])){
//                       temp=1;
//                   }
//                }
//                if(temp==0){
//                    course[num+1] = course_name;
//                }
//                num = num+1;
//                for(int j = 0;j<num;j++){
//                    if(course_name.equals(course[j])){
//                        course_color=color[j];
//                    }
//                }
//                Log.e("course_color",""+course_color);
//                for(int j =0;j<num;j++){
//                    Log.e("course_name",""+course[j]);
//                }
                String content = "" + course_name + " " + course_location + " " + course_teacher;
                if (dayinfo.equals("1")) {
                    if (begintime.equals("1")) {
                        zhouyi_1.setText(content);
                        //zhouyi_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhouyi_2.setText(content);
                        //zhouyi_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhouyi_3.setText(content);
                        //zhouyi_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhouyi_4.setText(content);
                        //zhouyi_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhouyi_5.setText(content);
                        //zhouyi_5.setTextColor(Color.parseColor(course_color));
                    }
                } else if (dayinfo.equals("2")) {
                    if (begintime.equals("1")) {
                        zhouer_1.setText(content);
                        //zhouer_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhouer_2.setText(content);
                        //zhouer_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhouer_3.setText(content);
                        //zhouer_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhouer_4.setText(content);
                        //zhouer_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhouer_5.setText(content);
                        //zhouer_5.setTextColor(Color.parseColor(course_color));
                    }
                } else if (dayinfo.equals("3")) {
                    if (begintime.equals("1")) {
                        zhousan_1.setText(content);
                        //zhousan_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhousan_2.setText(content);
                        //zhousan_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhousan_3.setText(content);
                        //zhousan_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhousan_4.setText(content);
                        //zhousan_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhousan_5.setText(content);
                        //zhousan_5.setTextColor(Color.parseColor(course_color));
                    }
                } else if (dayinfo.equals("4")) {
                    if (begintime.equals("1")) {
                        zhousi_1.setText(content);
                        //zhousi_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhousi_2.setText(content);
                        //zhousi_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhousi_3.setText(content);
                        //zhousi_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhousi_4.setText(content);
                        //zhousi_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhousi_5.setText(content);
                        //zhousi_5.setTextColor(Color.parseColor(course_color));
                    }
                } else if (dayinfo.equals("5")) {
                    if (begintime.equals("1")) {
                        zhouwu_1.setText(content);
                        //zhouwu_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhouwu_2.setText(content);
                        //zhouwu_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhouwu_3.setText(content);
                        //zhouwu_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhouwu_4.setText(content);
                        //zhouwu_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhouwu_5.setText(content);
                        //zhouwu_5.setTextColor(Color.parseColor(course_color));
                    }
                } else if (dayinfo.equals("6")) {
                    if (begintime.equals("1")) {
                        zhouliu_1.setText(content);
                        //zhouliu_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhouliu_2.setText(content);
                        //zhouliu_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhouliu_3.setText(content);
                        //zhouliu_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhouliu_4.setText(content);
                        //zhouliu_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhouliu_5.setText(content);
                        //zhouliu_5.setTextColor(Color.parseColor(course_color));
                    }
                } else {
                    if (begintime.equals("1")) {
                        zhouri_1.setText(content);
                        //zhouri_1.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("3")) {
                        zhouri_2.setText(content);
                        //zhouri_2.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("5")) {
                        zhouri_3.setText(content);
                        //zhouri_3.setTextColor(Color.parseColor(course_color));
                    } else if (begintime.equals("7")) {
                        zhouri_4.setText(content);
                        //zhouri_4.setTextColor(Color.parseColor(course_color));
                    } else {
                        zhouri_5.setText(content);
                        //zhouri_5.setTextColor(Color.parseColor(course_color));
                    }
                }
            }

        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    public void getData() {
        setBodyParams(new String[]{"week"}, new String[]{week});
        sendPost(WenConstans.WeekTimetable, 1, WenConstans.Timetabletoken);
        Log.e("token", WenConstans.Timetabletoken + "");
    }


}
