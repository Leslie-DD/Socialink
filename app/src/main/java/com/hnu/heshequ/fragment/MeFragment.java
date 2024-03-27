package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.login.ForgetPwdActivity;
import com.hnu.heshequ.activity.login.LoginActivity;
import com.hnu.heshequ.activity.mine.AttentionActivity;
import com.hnu.heshequ.activity.mine.AuthenticationActivity;
import com.hnu.heshequ.activity.mine.BaseInfoActivity;
import com.hnu.heshequ.activity.mine.FeedBackActivity;
import com.hnu.heshequ.activity.mine.MyCollectActivity;
import com.hnu.heshequ.activity.mine.MyFootprintActivity;
import com.hnu.heshequ.activity.mine.MyPullTheBlackActivity;
import com.hnu.heshequ.activity.mine.MyQuestionActivity1;
import com.hnu.heshequ.activity.mine.MySayActivity;
import com.hnu.heshequ.activity.mine.MyTeamActivity;
import com.hnu.heshequ.activity.mine.SettingActivity;
import com.hnu.heshequ.activity.oldsecond.MygoodActivity;
import com.hnu.heshequ.adapter.listview.ItemAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ItemBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefUserInfo;
import com.hnu.heshequ.launcher.MainActivity;
import com.hnu.heshequ.network.HttpRequestUtil;
import com.hnu.heshequ.network.entity.UserInfoBean;
import com.hnu.heshequ.utils.ImageUtils;
import com.hnu.heshequ.utils.SharedPreferencesHelp;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.ArcImageView;
import com.hnu.heshequ.view.CircleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;


public class MeFragment extends Fragment {
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
    private LinearLayout llSay, llQuestion, llNotice, llSecondhand;
    private final Gson gson = new Gson();
    private UserInfoBean userInfoBean;
    private final int XIANGYU_MONEY = 1001;

    private final int initUserInfo = 1000;
    private int settingClub, settingAsk;

    private final HttpRequestUtil.RequestCallBack requestCallBack = new HttpRequestUtil.RequestCallBack() {

        @Override
        public void onSuccess(JSONObject result, int where, boolean fromCache) {
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
                    if (result.optInt("code") != 0) {
                        Utils.toastShort(getContext(), result.optString("msg"));
                        break;
                    }
                    if (result.optString("data").isEmpty()) {
                        break;
                    }
                    userInfoBean = gson.fromJson(result.optString("data"), UserInfoBean.class);
                    settingAsk = userInfoBean.getSettingAsk();
                    settingClub = userInfoBean.getSettingClub();
                    if (userInfoBean.getHeader() != null && !userInfoBean.getHeader().isEmpty()) {
                        //Glide.with(mContext).load(R.mipmap.head2).asBitmap().into(target);
                        Glide.with(getActivity()).load(Constants.base_url + userInfoBean.getHeader()).asBitmap().into(target);
                        Glide.with(getActivity()).load(Constants.base_url + userInfoBean.getHeader()).asBitmap().into(ivHead);
                        ivBg.setBackgroundColor(Color.WHITE);
                    } else {
                        Glide.with(getActivity()).load(R.mipmap.head2).asBitmap().into(ivHead);
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
                    break;
            }
        }

        @Override
        public void onFailure(String result, int where) {
        }
    };

    private final HttpRequestUtil httpRequest = new HttpRequestUtil(requestCallBack, TAG);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "(createView) uid = " + Constants.uid);
        view = inflater.inflate(R.layout.fragment_me, container, false);
        EventBus.getDefault().register(this);

        lv = view.findViewById(R.id.lv);
        getData();
        adapter = new ItemAdapter(getContext(), data);
        lv.setAdapter(adapter);
        headView = LayoutInflater.from(getContext()).inflate(R.layout.mehead, null);
        xiangyuMoney = headView.findViewById(R.id.xiangyuMoney);
        llSay = headView.findViewById(R.id.llSay);
        llQuestion = headView.findViewById(R.id.llQuestion);
        llNotice = headView.findViewById(R.id.llnotice);
        llSecondhand = headView.findViewById(R.id.llSecondhand);
        lv.addHeaderView(headView);
        current = headView.findViewById(R.id.current);
        ivEditor = headView.findViewById(R.id.ivEditor);
        ivBg = headView.findViewById(R.id.ivBg);
        ivHead = headView.findViewById(R.id.ivHead);
        tvName = headView.findViewById(R.id.tvName);
        tvName.setText("点击头像登录");
        tvSchool = headView.findViewById(R.id.tvSchool);
        tvDetail = headView.findViewById(R.id.tvDetail);
        tvLevel = headView.findViewById(R.id.tvLevel);
        initUserinfo();
        event();
        return view;
    }

    private void initUserinfo() {
        httpRequest.setBodyParams(new String[]{"uid"}, new String[]{String.valueOf(Constants.uid)});
        httpRequest.sendPostConnection(Constants.base_url + "/api/user/info.do", initUserInfo, Constants.token);
    }

    private void getXiangyuMoney() {
        httpRequest.setBodyParams(new String[]{"uid"}, new String[]{String.valueOf(Constants.uid)});
        httpRequest.sendPostConnection(Constants.base_url + "/api/user/cornAmount.do", XIANGYU_MONEY, Constants.token);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refUserInfo(RefUserInfo refUserInfo) {
        Log.d(TAG, "onReceive: RefUserInfo");
        initUserinfo();
    }

    private void event() {
        ivEditor.setOnClickListener(v -> {
            if (userInfoBean == null) {
                Utils.toastShort(getActivity(), "数据加载中~");
                return;
            }
            startActivity(new Intent(getActivity(), BaseInfoActivity.class).putExtra("userinfobean", userInfoBean));
        });
        ivHead.setOnClickListener(v -> {
            if (Constants.uid == 1) {
                goLoginPage();
            }
        });
        llSay.setOnClickListener(v -> startActivity(new Intent(getContext(), MySayActivity.class)));
        llQuestion.setOnClickListener(v -> startActivity(new Intent(getContext(), MyQuestionActivity1.class)));
        llNotice.setOnClickListener(v -> startActivity(new Intent(getContext(), AttentionActivity.class)));
        llSecondhand.setOnClickListener(v -> startActivity(new Intent(getContext(), MygoodActivity.class)));
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.e("YSF", "当前的位置：" + i);
            i = i - 1;
            switch (i) {
                case 0://我的团队
                    startActivity(new Intent(getContext(), MyTeamActivity.class));
                    break;
//                case 1://我的创作
//                    startActivity(new Intent(getContext(), MyKnowledgeActivity.class));
//                    break;
                case 1:  //我的收藏
                    startActivity(new Intent(getContext(), MyCollectActivity.class));
                    break;
                case 2: //我的足迹
                    startActivity(new Intent(getContext(), MyFootprintActivity.class));
                    break;
                case 3://我的拉黑
                    startActivity(new Intent(getContext(), MyPullTheBlackActivity.class));
                    break;
                case 4://实名认证
                    if (certFlag == -1) {
                        return;
                    }
                    startActivity(new Intent(getContext(), AuthenticationActivity.class).putExtra("certFlag", certFlag));
                    break;
                case 5://意见反馈
                    startActivity(new Intent(getContext(), FeedBackActivity.class));
                    break;
                case 6://修改密码
                    startActivity(new Intent(getContext(), ForgetPwdActivity.class));
                    break;
                case 7://设置
                    startActivity(new Intent(getContext(), SettingActivity.class)
                                    .putExtra("settingAsk", settingAsk)
                                    .putExtra("settingClub", settingClub)
                            //.putExtra("userLabelsBeans",userInfoBean.getUserLabels())
                            //暂时不知道为什么会报空指针，先注释
                    );
                    break;
                case 8:// 退出登录/登录
                    if (Constants.uid == 1) {
                        goLoginPage();
                        break;
                    }
                    new AlertDialog.Builder(requireContext())
                            .setTitle("退出登录")
                            .setMessage("确定退出当前账号吗")
                            .setNegativeButton("取消", (dialogInterface, i1) -> {
                                dialogInterface.dismiss();//销毁对话框
                            })
                            .setPositiveButton("确定", (dialog1, which) -> {
                                dialog1.dismiss();
                                goLoginPage();
                            })
                            .create()
                            .show();
                    break;
            }
        });
    }

    private void goLoginPage() {
        SharedPreferencesHelp.getEditor()
                .putString("phone", "")
                .putString("token", "")
                .putInt("uid", 1)
                .putBoolean("isLogin", false)
                .apply();
        SharedPreferencesHelp.getEditor()
                .putBoolean("isLogin", false)
                .apply();
        Constants.uid = 1;
        MeetApplication.getInstance().finishAll();
        startActivity(new Intent(getContext(), LoginActivity.class));
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
            int maxLength = Utils.dip2px(requireContext(), 130);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
