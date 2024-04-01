package com.leslie.socialink.activity.mine;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.leslie.socialink.R;
import com.leslie.socialink.adapter.MyFragmentPagerAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.fragment.MyAnswerFragment;
import com.leslie.socialink.fragment.MyQAQFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的问题页面
 */
public class MyQuestionActivity extends NetWorkActivity {

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
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        tvQAQ.setOnClickListener(v -> setTvBg(0));
        tvAns.setOnClickListener(v -> setTvBg(1));
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
        tvQAQ.setSelected(status == 0);
        tvAns.setSelected(status == 1);
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

}
