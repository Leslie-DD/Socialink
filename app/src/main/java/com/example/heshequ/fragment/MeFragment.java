package com.example.heshequ.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.heshequ.R;
import com.example.heshequ.activity.MainActivity;
import com.example.heshequ.activity.login.ForgetPwdActivity;
import com.example.heshequ.activity.mine.AttentionActivity;
import com.example.heshequ.activity.mine.AuthenticationActivity;
import com.example.heshequ.activity.mine.BaseInfoActivity;
import com.example.heshequ.activity.mine.FeedBackActivity;
import com.example.heshequ.activity.mine.MyCollectActivity;
import com.example.heshequ.activity.mine.MyFootprintActivity;
import com.example.heshequ.activity.mine.MyPullTheBlackActivity;
import com.example.heshequ.activity.mine.MyQuestionActivity1;
import com.example.heshequ.activity.mine.MySayActivity;
import com.example.heshequ.activity.mine.MyTeamActivity;
import com.example.heshequ.activity.mine.SettingActivity;
import com.example.heshequ.activity.oldsecond.MygoodActivity;
import com.example.heshequ.adapter.listview.ItemAdapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.ItemBean;
import com.example.heshequ.bean.UserInfoBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefUserInfo;
import com.example.heshequ.utils.ImageUtils;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.ArcImageView;
import com.example.heshequ.view.CircleView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;


public class MeFragment extends NetWorkFragment implements View.OnClickListener {

    private static final String TAG = "[MeFragment]";

    private View view;
    private ArcImageView ivBg;
    private CircleView ivHead;
    private View headView;
    private TextView tvName, tvDetail, tvLevel, tvSchool, xiangyuMoney;
    private int certFlag = -1;  //认证状态
    private ListView lv;
    private ArrayList<ItemBean> data;
    private ItemAdapter adapter;
    private ImageView ivEditor;
    private TextView current;
    private MainActivity mainActivity;
    private LinearLayout llSay, llQuestion, llNotice, llSecondhand;
    private Gson gson;
    private UserInfoBean userInfoBean;
    private final int XIANGYU_MONEY = 1001;

    private final int initUserInfo = 1000;
    private int settingClub, settingAsk;

    @Override
    protected View createView(LayoutInflater inflater) {
        Log.i(TAG, "(createView) uid = " + Constants.uid);
        view = inflater.inflate(R.layout.fragment_me, null);
        EventBus.getDefault().register(this);
        gson = new Gson();
        lv = (ListView) view.findViewById(R.id.lv);
        getData();
        adapter = new ItemAdapter(mContext, data);
        lv.setAdapter(adapter);
        headView = LayoutInflater.from(mContext).inflate(R.layout.mehead, null);
        xiangyuMoney = (TextView) headView.findViewById(R.id.xiangyuMoney);
        llSay = (LinearLayout) headView.findViewById(R.id.llSay);
        llQuestion = (LinearLayout) headView.findViewById(R.id.llQuestion);
        llNotice = (LinearLayout) headView.findViewById(R.id.llnotice);
        llSecondhand = (LinearLayout) headView.findViewById(R.id.llSecondhand);
        lv.addHeaderView(headView);
        current = (TextView) headView.findViewById(R.id.current);
        ivEditor = (ImageView) headView.findViewById(R.id.ivEditor);
        ivBg = (ArcImageView) headView.findViewById(R.id.ivBg);
        ivHead = (CircleView) headView.findViewById(R.id.ivHead);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvName.setText("点击头像登录");
        tvSchool = headView.findViewById(R.id.tvSchool);
        tvDetail = (TextView) headView.findViewById(R.id.tvDetail);
        tvLevel = (TextView) headView.findViewById(R.id.tvLevel);
        initUserinfo();
        event();
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    private void initUserinfo() {
        setBodyParams(new String[]{"uid"}, new String[]{String.valueOf(Constants.uid)});
        sendPostConnection(Constants.base_url + "/api/user/info.do", initUserInfo, Constants.token);
    }

    private void getXiangyuMoney() {
        setBodyParams(new String[]{"uid"}, new String[]{String.valueOf(Constants.uid)});
        sendPostConnection(Constants.base_url + "/api/user/cornAmount.do", XIANGYU_MONEY, Constants.token);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refUserInfo(RefUserInfo refUserInfo) {
        Log.d(TAG, "onReceive: RefUserInfo");
        initUserinfo();
    }

    private void event() {
        ivEditor.setOnClickListener(this);
        ivHead.setOnClickListener(this);
        llSay.setOnClickListener(this);
        llQuestion.setOnClickListener(this);
        llNotice.setOnClickListener(this);
        llSecondhand.setOnClickListener(this);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.e("YSF", "当前的位置：" + i);
            i = i - 1;
            switch (i) {
                case 0://我的团队
                    startActivity(new Intent(mContext, MyTeamActivity.class));
                    break;
//                case 1://我的创作
//                    startActivity(new Intent(mContext, MyKnowledgeActivity.class));
//                    break;
                case 1:  //我的收藏
                    startActivity(new Intent(mContext, MyCollectActivity.class));
                    break;
                case 2: //我的足迹
                    startActivity(new Intent(mContext, MyFootprintActivity.class));
                    break;
                case 3://我的拉黑
                    startActivity(new Intent(mContext, MyPullTheBlackActivity.class));
                    break;
                case 4://实名认证
                    if (certFlag == -1) {
                        return;
                    }
                    startActivity(new Intent(mContext, AuthenticationActivity.class).putExtra("certFlag", certFlag));
                    break;
                case 5://意见反馈
                    startActivity(new Intent(mContext, FeedBackActivity.class));
                    break;
                case 6://修改密码
                    startActivity(new Intent(mContext, ForgetPwdActivity.class));
                    break;
                case 7://设置
                    startActivity(new Intent(mContext, SettingActivity.class)
                                    .putExtra("settingAsk", settingAsk)
                                    .putExtra("settingClub", settingClub)
                            //.putExtra("userLabelsBeans",userInfoBean.getUserLabels())
                            //暂时不知道为什么会报空指针，先注释
                    );
                    break;
                case 8:// 退出登录/登录
                    if (mainActivity != null) {
                        mainActivity.showPop();
                    }
                    break;

            }
        });
    }

    private void getData() {
        data = new ArrayList<>();
        ItemBean bean = new ItemBean();
        bean.setName("我的团队");
        bean.setResId(R.mipmap.myteam);
        data.add(bean);

//        bean = new ItemBean();
//        bean.setName("我的创作");
//        bean.setResId(R.mipmap.myteam);
//        data.add(bean);

        bean = new ItemBean();
        bean.setName("我的收藏");
        bean.setResId(R.mipmap.saved);
        data.add(bean);

        bean = new ItemBean();
        bean.setName("我的足迹");
        bean.setResId(R.mipmap.zuji);
        data.add(bean);

        bean = new ItemBean();
        bean.setName("我的拉黑");
        bean.setResId(R.mipmap.pulltheblack);
        data.add(bean);

//        bean=new ItemBean();
//        bean.setName("我的消息");
//        bean.setResId(R.mipmap.message2);
//        /*bean.setType(1);//有新消息为1*/
//        data.add(bean);

        bean.setStatus(1);

        bean = new ItemBean();
        bean.setName("实名认证");
        bean.setResId(R.mipmap.certified);
        bean.setTip("未认证");
        data.add(bean);

        bean = new ItemBean();
        bean.setName("意见反馈");
        bean.setResId(R.mipmap.fdbk);
        data.add(bean);

        bean = new ItemBean();
        bean.setName("修改密码");
        bean.setResId(R.mipmap.epswd);
        data.add(bean);

        bean = new ItemBean();
        bean.setName("设置");
        bean.setResId(R.mipmap.setting);
        data.add(bean);

        if (Constants.uid == 1) {
            bean = new ItemBean();
            bean.setName("点击登录");
            bean.setResId(R.mipmap.esc);
            data.add(bean);
        } else {
            bean = new ItemBean();
            bean.setName("退出登录");
            bean.setResId(R.mipmap.esc);
            data.add(bean);
        }

    }

    //动态设置Textview长度
    private void setStarLine(final TextView tv, final int n, final int t, final int e) {
        tv.post(() -> {
            int maxLength = Utils.dip2px(mContext, 130);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            int width = (int) (maxLength * (((e - t) * 1.0F) / n));
            Log.d("[MeFragment]", "width == " + width);
            if (width > 0 && width < 10) {
                lp.width = width + 10;
            } else {
                lp.width = width;
            }
            tv.setLayoutParams(lp);
        });
    }


    private final SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            //这里我们拿到回掉回来的bitmap，可以加载到我们想使用到的地方
//            Bitmap finalBitmap = Fuzzy_Background.with(mContext)
//                    .bitmap(bitmap) //要模糊的图片
//                    .radius(0)//模糊半径
//                    .blur();


            Bitmap newBitmap = ImageUtils.stackBlur(bitmap, 25);

            //Bitmap newBitmap = StackBlur.blur(bitmap, (int) 50, false);
//            Bitmap newBitmap=Utils.blurBitmap(mContext,bitmap,5);
            ivBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivBg.setImageBitmap(newBitmap);
        }
    };


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivEditor:
                if (userInfoBean == null) {
                    Utils.toastShort(getActivity(), "数据加载中~");
                    return;
                }
                startActivity(new Intent(mContext, BaseInfoActivity.class).putExtra("userinfobean", userInfoBean));
                break;
            case R.id.ivHead:
                if (Constants.uid == 1 && mainActivity != null) {
                    mainActivity.showPop();
                }
                break;
            case R.id.llSay:
                startActivity(new Intent(mContext, MySayActivity.class));
                break;
            case R.id.llQuestion:
                startActivity(new Intent(mContext, MyQuestionActivity1.class));
                break;
            case R.id.llnotice:
                startActivity(new Intent(mContext, AttentionActivity.class));
                break;

            case R.id.llSecondhand:
                startActivity(new Intent(mContext, MygoodActivity.class));
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e(TAG, result.toString());
        switch (where) {
            case XIANGYU_MONEY:
                if (result.optInt("code") == 0) {
                    int money = result.optInt("data");
                    xiangyuMoney.setText(String.valueOf(money));
                } else {
                    String msg = result.optString("msg");
                    Utils.toastShort(getContext(), msg);
                }
                break;

            case initUserInfo:
                if (result.optInt("code") == 0) {
                    if (!result.optString("data").isEmpty()) {
                        userInfoBean = gson.fromJson(result.optString("data"), UserInfoBean.class);
                        settingAsk = userInfoBean.getSettingAsk();
                        settingClub = userInfoBean.getSettingClub();
                        if (userInfoBean.getHeader() != null && !userInfoBean.getHeader().isEmpty()) {
                            //Glide.with(mContext).load(R.mipmap.head2).asBitmap().into(target);
                            Glide.with(mContext).load(Constants.base_url + userInfoBean.getHeader()).asBitmap().into(target);
                            Glide.with(mContext).load(Constants.base_url + userInfoBean.getHeader()).asBitmap().into(ivHead);
                            ivBg.setBackgroundColor(Color.WHITE);
                        } else {
                            Glide.with(mContext).load(R.mipmap.head2).asBitmap().into(ivHead);
                        }
                        tvName.setText(userInfoBean.getNickname());
                        tvSchool.setText(userInfoBean.getCollege());
                        tvDetail.setText("经验值：" + (userInfoBean.getExperience() - userInfoBean.getTotalExperience()) + "/" + userInfoBean.getNeedExperience());
                        tvLevel.setText("LV" + userInfoBean.getGrade());
                        certFlag = userInfoBean.getCertFlag();
                        setStarLine(current, userInfoBean.getNeedExperience(), userInfoBean.getTotalExperience(), userInfoBean.getExperience());
                        switch (certFlag) {
                            case 0:
                                data.get(4).setTip("未认证");
                                break;
                            case 1:
                                data.get(4).setTip("审核中");
                                break;
                            case 2:
                                data.get(4).setTip("验证未通过");
                                break;
                            case 3:
                                data.get(4).setTip("验证通过");
                                break;
                        }
                        adapter.notifyDataSetChanged();

                        getXiangyuMoney();
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        //Utils.toastShort(getContext(), "网络状态错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
