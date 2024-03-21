package com.hnu.heshequ.activity.knowledge;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.fragment.knowledge.MyArticleFragment;
import com.hnu.heshequ.fragment.knowledge.MyColumnFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyKnowledgeActivity extends NetWorkActivity  {

    private ViewPager vp;
    private TextView tvArticle, tvColumn;
    private ArrayList<Fragment> list;
    private MyArticleFragment myArticleFragment;
    private MyColumnFragment myColoumnFragment;

    private MyFragmentPagerAdapter adapter;

    private int status = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_knowledge);
        init();
        event();
    }


    private void init() {
        setText("知识");
        list = new ArrayList<>();
        tvArticle = findViewById(R.id.tvRecommend);
        tvColumn = findViewById(R.id.tvSubscription);
        myArticleFragment = new MyArticleFragment();
        myColoumnFragment = new MyColumnFragment();

        list.add(myArticleFragment);
        list.add(myColoumnFragment);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        tvArticle.setOnClickListener(v -> setTvBg(0));
        tvColumn.setOnClickListener(v -> setTvBg(1));
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
    }

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
            Log.e("YSF", "我是vp的设置item" + status);
        }
        tvArticle.setSelected(status == 0);
        tvColumn.setSelected(status == 1);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }

}
