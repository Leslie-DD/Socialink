package com.example.heshequ.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.knowledge.CreateArticleActivity;
import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.entity.RefreshBean;
import com.example.heshequ.fragment.knowledge.RecommendFragment;
import com.example.heshequ.fragment.knowledge.SubscriptionFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * FileName: KnowledgeFregment
 * Author: Ding Yifan
 * Data: 2020/9/1
 * Time: 14:52
 * Description:知识页面fregment 原是activity，为了统一改为fragment
 */
public class KnowledgeFragment extends NetWorkFragment implements View.OnClickListener {

    private View view;
    private ArrayList<Fragment> list;
    private ViewPager vp;
    private TextView tvRecommend, tvSubscription;
    private RecommendFragment recommendFragment;
    private SubscriptionFragment subscriptionFragment;

    private MyFragmentPagerAdapter adapter;

    private int status = -1;

    private ImageView ivAdd;


    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_knowledge, null);
        EventBus.getDefault().register(this);
        init();
        event();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshData(RefreshBean refreshBean) {
//        RecommendFragment t1= (RecommendFragment) adapter.getItem(0);
//        SubscriptionFragment t2= (SubscriptionFragment) adapter.getItem(1);
//
//        if (refreshBean.type.equals("1")){
//            if (t1!=null){
//
//            }
//        }else if (refreshBean.type.equals("2")) {
//            if (t2 != null) {
//
//            }
//        }
    }

    private void init() {
//        setText("知识");
        tvRecommend = (TextView) view.findViewById(R.id.tvRecommend);
        tvSubscription = (TextView) view.findViewById(R.id.tvSubscription);
        vp = (ViewPager) view.findViewById(R.id.vp);

        ivAdd = view.findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(this);
        list = new ArrayList<>();
        recommendFragment = new RecommendFragment();
        subscriptionFragment = new SubscriptionFragment();

        list.add(recommendFragment);
        list.add(subscriptionFragment);
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setId(list.get(0).hashCode());
        vp.setCurrentItem(0);
        setTvBg(0);
    }

    private void event() {
//        view.findViewById(R.id.ivBack).setOnClickListener(this);

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
        tvRecommend.setOnClickListener(this);
        tvSubscription.setOnClickListener(this);
    }

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
            Log.e("YSF", "我是vp的设置item" + status);
        }
        tvRecommend.setSelected(status == 0);
        tvSubscription.setSelected(status == 1);
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.ivBack:
//                this.finish();
//                break;
            case R.id.tvRecommend:
                setTvBg(0);
                break;
            case R.id.tvSubscription:
                setTvBg(1);
                break;
            case R.id.ivAdd:
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_myTeamAddNewTeam");
                startActivity(new Intent(mContext, CreateArticleActivity.class));
                break;
        }
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(getContext());
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(getContext());
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
//    }


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
