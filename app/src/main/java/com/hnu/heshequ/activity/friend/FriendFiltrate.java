package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.AgeBean;
import com.hnu.heshequ.bean.knowledge.DistanceBean;
import com.hnu.heshequ.constans.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendFiltrate extends NetWorkActivity {
    private OptionsPickerView distanceChooseHelper; // 距离选择器
    private OptionsPickerView ageChooseHelper;      // 年龄选择器
    private OptionsPickerView sexChooseHelper;      // 性别选择器
    private OptionsPickerView schoolChooseHelper;   // 学校选择器


    private LinearLayout mLlDistance;
    private LinearLayout mLlAge;
    private LinearLayout mLlSex;
    private LinearLayout mLlSchool;

    private TextView mTvDistance;
    private TextView mTvAge;
    private TextView mTvSex;
    private TextView mTvSchool;

    private DistanceBean theDistance = new DistanceBean();  // 选择的距离
    private ArrayList<DistanceBean> distanceList = new ArrayList<>();   // 距离列表
    private AgeBean theAge = new AgeBean();  // 选择的年龄
    private ArrayList<AgeBean> ageList = new ArrayList<>();   // 年龄列表
    private String theSex = new String();  // 选择的性别
    private ArrayList<String> sexList = new ArrayList<>();   // 性别列表
    private String theCollege = new String();  // 选择的大学
    private ArrayList<String> collegeList = new ArrayList<>();   // 大学列表

    //    private RadioGroup nRg1,nRg2,nRg3,nRg4;
//    private static String juli = "";    // 先赋初值，不然如果用户不选择radiobutton，会导致juli为空，.equals()函数出错
//    private static int distance;
//    private static String college = "不限";
//    private static String sex = "不限";
//    private static String nianling = "";
//    private static int age;
//    private ListView lv;
//    private ArrayList<FriendBean> data;
//    private FriendfiltrateAdapter adapter;
//    private View view;
    private static String interest = "false";
    private Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrate);

//        nRg1 = findViewById(R.id.rg_1);
//        nRg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton radioButton = radioGroup.findViewById(i);
//                juli = radioButton.getText().toString();
//                Toast.makeText(FriendFiltrate.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        nRg2 = findViewById(R.id.rn_1);
//        nRg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton radioButton = radioGroup.findViewById(i);
//                nianling = radioButton.getText().toString();
//                Toast.makeText(FriendFiltrate.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        nRg3 = findViewById(R.id.rx_1);
//        nRg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton radioButton = radioGroup.findViewById(i);
//                sex = radioButton.getText().toString();
//                Toast.makeText(FriendFiltrate.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        nRg4 = findViewById(R.id.rxx_1);
//        nRg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton radioButton = radioGroup.findViewById(i);
//                college = radioButton.getText().toString();
//                Toast.makeText(FriendFiltrate.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
//            }
//        });
        init();
        initList();
        initChooseHelper();
        event();
    }

    private void init() {
        //View view = getLayoutInflater().inflate(R.layout.activity_filtrate, null);
        setText("筛选信息");
        button1 = (Button) findViewById(R.id.button1);

        mTvDistance = (TextView) findViewById(R.id.tvDistance);
        mTvAge = (TextView) findViewById(R.id.tvAge);
        mTvSex = (TextView) findViewById(R.id.tvSex);
        mTvSchool = (TextView) findViewById(R.id.tvSchool);

        mLlDistance = (LinearLayout) findViewById(R.id.llDistance);
        mLlAge = (LinearLayout) findViewById(R.id.llAge);
        mLlSex = (LinearLayout) findViewById(R.id.llSex);
        mLlSchool = (LinearLayout) findViewById(R.id.llSchool);

        theDistance = new DistanceBean("不限距离", 0);
        theAge = new AgeBean("不限", 0);
        theSex = "不限";
        theCollege = "不限";
    }

    private void initList() {
        DistanceBean distanceBean = new DistanceBean("不限距离", 0);
        distanceList.add(distanceBean);
        distanceBean = new DistanceBean("0-1km", 1);
        distanceList.add(distanceBean);
        distanceBean = new DistanceBean("1-2km", 2);
        distanceList.add(distanceBean);
        distanceBean = new DistanceBean("2-3km", 3);
        distanceList.add(distanceBean);
        distanceBean = new DistanceBean("3km以上", 0);
        distanceList.add(distanceBean);

        AgeBean ageBean = new AgeBean("不限", 0);
        ageList.add(ageBean);
        ageBean = new AgeBean("10-20岁", 1);
        ageList.add(ageBean);
        ageBean = new AgeBean("21-25岁", 2);
        ageList.add(ageBean);
        ageBean = new AgeBean("26-30岁", 3);
        ageList.add(ageBean);

        sexList.add("不限");
        sexList.add("男");
        sexList.add("女 ");

        collegeList.add("不限");
        collegeList.add("湖南大学");
        collegeList.add("中南大学");
        collegeList.add("湖南师范大学");
        collegeList.add("湘潭大学");
        collegeList.add("长沙理工大学");
        collegeList.add("湖南农业大学");
        collegeList.add("湖南科技大学");
    }

    private void initChooseHelper() {
        distanceChooseHelper = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                Log.e("选择距离", "-------position-------" + options1);
//                theDistance.setJuli(distanceList.get(options1).getJuli());
//                theDistance.setDistance(distanceList.get(options1).getDistance());
                theDistance = new DistanceBean(distanceList.get(options1).getJuli(), distanceList.get(options1).getDistance());
                mTvDistance.setText(theDistance.getJuli());
            }
        }).setSubmitText("确定")  // 确定按钮文字
                .setCancelText("取消")    // 取消按钮文字
                .setTitleText("选择距离") // 标题
                .setSubCalSize(18)  // 确定和取消文字大小
                .setTitleSize(20)   // 标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#2CD22B"))    // 确定按钮文字颜色
                .setCancelColor(Color.parseColor("#2CD22B"))    // 取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)    // 标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF) // 滚轮背景颜色
                .setContentTextSize(18) // 滚轮文字大小
                .isCenterLabel(false)   // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(true) // 点击外部dismiss default true
                .isDialog(false)    // 是否显示为对话框样式
                .isRestoreItem(true)// 切换时是否还原，设置默认选中第一项。
                .build();
        List<String> results = new ArrayList<>();
        for (int i = 0; i < distanceList.size(); i++) {
            results.add(distanceList.get(i).getJuli());
        }
        distanceChooseHelper.setPicker(results);

        ageChooseHelper = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                Log.e("选择年龄", "-------position-------" + options1);
//                theAge.setNianling(ageList.get(options1).getNianling());
//                theAge.setAge(ageList.get(options1).getAge());
                theAge = new AgeBean(ageList.get(options1).getNianling(), ageList.get(options1).getAge());
                mTvAge.setText(theAge.getNianling());
            }
        }).setSubmitText("确定")  // 确定按钮文字
                .setCancelText("取消")    // 取消按钮文字
                .setTitleText("选择年龄") // 标题
                .setSubCalSize(18)  // 确定和取消文字大小
                .setTitleSize(20)   // 标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#2CD22B"))    // 确定按钮文字颜色
                .setCancelColor(Color.parseColor("#2CD22B"))    // 取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)    // 标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF) // 滚轮背景颜色
                .setContentTextSize(18) // 滚轮文字大小
                .isCenterLabel(false)   // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(true) // 点击外部dismiss default true
                .isDialog(false)    // 是否显示为对话框样式
                .isRestoreItem(true)// 切换时是否还原，设置默认选中第一项。
                .build();
        List<String> ageResults = new ArrayList<>();
        for (int i = 0; i < ageList.size(); i++) {
            ageResults.add(ageList.get(i).getNianling());
        }
        ageChooseHelper.setPicker(ageResults);

        sexChooseHelper = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                Log.e("选择性别", "-------position-------" + options1);
                theSex = sexList.get(options1);
                mTvSex.setText(theSex);
            }
        }).setSubmitText("确定")  // 确定按钮文字
                .setCancelText("取消")    // 取消按钮文字
                .setTitleText("选择性别") // 标题
                .setSubCalSize(18)  // 确定和取消文字大小
                .setTitleSize(20)   // 标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#2CD22B"))    // 确定按钮文字颜色
                .setCancelColor(Color.parseColor("#2CD22B"))    // 取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)    // 标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF) // 滚轮背景颜色
                .setContentTextSize(18) // 滚轮文字大小
                .isCenterLabel(false)   // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(true) // 点击外部dismiss default true
                .isDialog(false)    // 是否显示为对话框样式
                .isRestoreItem(true)// 切换时是否还原，设置默认选中第一项。
                .build();
        List<String> sexResults = new ArrayList<>();
        for (int i = 0; i < sexList.size(); i++) {
            sexResults.add(sexList.get(i));
        }
        sexChooseHelper.setPicker(sexResults);


        schoolChooseHelper = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                Log.e("选择大学", "-------position-------" + options1);
                theCollege = collegeList.get(options1);
                mTvSchool.setText(theCollege);
            }
        }).setSubmitText("确定")  // 确定按钮文字
                .setCancelText("取消")    // 取消按钮文字
                .setTitleText("选择大学") // 标题
                .setSubCalSize(18)  // 确定和取消文字大小
                .setTitleSize(20)   // 标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#2CD22B"))    // 确定按钮文字颜色
                .setCancelColor(Color.parseColor("#2CD22B"))    // 取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)    // 标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF) // 滚轮背景颜色
                .setContentTextSize(18) // 滚轮文字大小
                .isCenterLabel(false)   // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(true) // 点击外部dismiss default true
                .isDialog(false)    // 是否显示为对话框样式
                .isRestoreItem(true)// 切换时是否还原，设置默认选中第一项。
                .build();
        List<String> collegeResults = new ArrayList<>();
        for (int i = 0; i < collegeList.size(); i++) {
            collegeResults.add(collegeList.get(i));
        }
        schoolChooseHelper.setPicker(collegeResults);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        mLlDistance.setOnClickListener(v -> distanceChooseHelper.show());
        mLlAge.setOnClickListener(v -> ageChooseHelper.show());
        mLlSex.setOnClickListener(v -> sexChooseHelper.show());
        mLlSchool.setOnClickListener(v -> schoolChooseHelper.show());

        button1.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.putExtra("distance", theDistance.getDistance() + "");
            intent1.putExtra("college", theCollege + "");
            intent1.putExtra("age", theAge.getAge() + "");
            intent1.putExtra("sex", theSex + "");
            intent1.putExtra("interest", interest);
            intent1.setClass(FriendFiltrate.this, FriendFiltrateShow.class);
            startActivity(intent1);
            finish();
        });
    }

    @Override
    protected void onFailure(String result, int where) {
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
    }

    private void getFiltrateData() {
        sendPost(Constants.base_url + " /api/social/getScreen.do", 10086, Constants.token);
    }

}
