package com.example.heshequ.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.fragment.MyAnswerFragment;
import com.example.heshequ.fragment.MyQAQFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的问题页面
 */
public class MyQuestionActivity extends NetWorkActivity implements View.OnClickListener {

    private ViewPager vp;
    private TextView tvQAQ, tvAns;
    private ArrayList<Fragment> list;
    private MyQAQFragment qaqFragment;
    private MyAnswerFragment answerFragment;
    private MyFragmentPagerAdapter adapter;
    private int status = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);
        init();
        event();
    }

    private void init() {
        setText("我的问题");
        tvQAQ = (TextView) findViewById(R.id.tvQAQ);
        tvAns = (TextView) findViewById(R.id.tvAnswer);
        list = new ArrayList<>();
        answerFragment = new MyAnswerFragment();
        qaqFragment = new MyQAQFragment();
        list.add(answerFragment);
        list.add(qaqFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        tvAns.setOnClickListener(this);
        tvQAQ.setOnClickListener(this);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTvBg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
            Log.e("YSF", "我是vp的设置item" + status);
        }
        tvQAQ.setSelected(status == 0 ? true : false);
        tvAns.setSelected(status == 1 ? true : false);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
    }

    @Override
    protected void onFailure(String result, int where) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvQAQ:
                setTvBg(0);
                break;
            case R.id.tvAnswer:
                setTvBg(1);
                break;
        }
    }


}
