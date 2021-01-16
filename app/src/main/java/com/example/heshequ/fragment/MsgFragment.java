package com.example.heshequ.fragment;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.adapter.MyFragmentPagerAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefreshBean;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

public class MsgFragment extends NetWorkFragment implements View.OnClickListener {

    private View view;
    private ArrayList<Fragment> list;
    private ViewPager vp;
    private TextView tvClear, tvTeam, tvSay, tvQuestion,tvMessage,tvFriend;
    private ImageView back;
    private News_TeamFragment teamFragment;
    private News_SayFragment sayFragment;
    private News_QuestionFragment questionFragment;
    private News_MessageFragment messageFragment;
    private News_FriendFragment friendFragment;
    private int cp = -1;
    private MyFragmentPagerAdapter adapter;
    private int status = -1;
    private final int delAll = 1000;
    private AlertDialog deldialog;
    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_msg, null);
        EventBus.getDefault().register(this);
        init();
        event();
        return view;
    }

    private void init() {
        tvClear = (TextView) view.findViewById(R.id.tvClear);
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        tvSay = (TextView) view.findViewById(R.id.tvSay);
        tvTeam = (TextView) view.findViewById(R.id.tvTeam);
        tvMessage=(TextView)view.findViewById(R.id.tvMessage);
        tvFriend = (TextView)view.findViewById(R.id.tvFriend);
        vp = (ViewPager) view.findViewById(R.id.vp);
        list = new ArrayList<>();
        teamFragment = new News_TeamFragment();
        sayFragment = new News_SayFragment();
        questionFragment = new News_QuestionFragment();
        messageFragment=new News_MessageFragment();
        friendFragment = new News_FriendFragment();
        list.add(teamFragment);
        list.add(sayFragment);
        list.add(questionFragment);
        //list.add(questionFragment2);
        list.add(messageFragment);
        list.add(friendFragment);
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), list);
        vp.setAdapter(adapter);
        vp.setId(list.get(0).hashCode());
        vp.setCurrentItem(0);
        setTvBg(0);
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要清空消息吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                if (cp == 0) {
                    setBodyParams(new String[]{"type"}, new String[]{"" + 2});
                    sendPost(Constants.base_url + "/api/user/news/clearBatchNews.do", delAll, Constants.token);
                } else if (cp == 1) {
                    setBodyParams(new String[]{"type"}, new String[]{"" + 1});
                    sendPost(Constants.base_url + "/api/user/news/clearBatchNews.do", delAll, Constants.token);
                } else if (cp == 2) {
                    setBodyParams(new String[]{"type"}, new String[]{"" + 3});
                    sendPost(Constants.base_url + "/api/user/news/clearBatchNews.do", delAll, Constants.token);
                }
                deldialog.dismiss();
            }


        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deldialog.dismiss();
            }
        });
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }

    private void event() {
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

        tvTeam.setOnClickListener(this);
        tvQuestion.setOnClickListener(this);
        tvSay.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvFriend.setOnClickListener(this);
    }


    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        if (vp != null) {
            vp.setCurrentItem(status);
            Log.e("YSF", "我是vp的设置item" + status);
        }
        tvTeam.setSelected(status == 0 ? true : false);
        tvSay.setSelected(status == 1 ? true : false);
        tvQuestion.setSelected(status == 2 ? true : false);
        tvMessage.setSelected(status ==3 ? true:false);
        tvFriend.setSelected(status ==4 ? true:false);
        if (tvMessage.isSelected()) {
            tvClear.setVisibility(View.GONE);
        } else {
            tvClear.setVisibility(View.VISIBLE);
        }
        if (tvFriend.isSelected()) {
            tvClear.setVisibility(View.GONE);
        } else {
            tvClear.setVisibility(View.VISIBLE);
        }
        if (vp != null && vp.getCurrentItem() != status) {
            vp.setCurrentItem(status);
        }
        cp = status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTeam:
                setTvBg(0);
                tvClear.setVisibility(View.VISIBLE);
                break;
            case R.id.tvSay:
                setTvBg(1);
                tvClear.setVisibility(View.VISIBLE);
                break;
            case R.id.tvQuestion:
                setTvBg(2);
                tvClear.setVisibility(View.VISIBLE);
                break;
            case R.id.tvClear:
                deldialog.show();
                break;
            case R.id.tvMessage:
                setTvBg(3);
                tvClear.setVisibility(View.GONE);
                break;
            case R.id.tvFriend:
                setTvBg(4);
                tvClear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where == delAll){
            if (result.optInt("code") == 0){
                switch (cp){
                    case 0:
                        teamFragment.refData();
                        break;
                    case 1:
                        sayFragment.refData();
                        break;
                    case 2:
                        questionFragment.refData();
                        break;
                    case 3:
//                        questionFragment2.refData();
                        messageFragment.refData();
                        break;
                    case 4:
//                        questionFragment2.refData();
                        friendFragment.refData();
                        break;
                }
            }else{
                Utils.toastShort(mContext,""+result.optString("msg"));
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshData(RefreshBean refreshBean){
        News_TeamFragment t1= (News_TeamFragment) adapter.getItem(0);
        News_SayFragment t2= (News_SayFragment) adapter.getItem(1);
        News_QuestionFragment t3= (News_QuestionFragment) adapter.getItem(2);
//        News_QuestionFragment2 t4=(News_QuestionFragment2) adapter.getItem(3);
        News_MessageFragment t4= (News_MessageFragment) adapter.getItem(3);
        News_FriendFragment t5 = (News_FriendFragment) adapter.getItem(4);
        if (refreshBean.type.equals("1")){
            if (t1!=null){
                t1.refData();
            }
        }else if (refreshBean.type.equals("2")){
            if (t2!=null){
                t2.refData();
            }
        }else if (refreshBean.type.equals("3")){
            if (t3!=null){
                t3.refData();
            }
        }else if(refreshBean.type.equals("4")){
            if(t4!=null){
                t4.refData();
            }
        }else if(refreshBean.type.equals("5")){
            if(t5!=null){
                t5.refData();
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
