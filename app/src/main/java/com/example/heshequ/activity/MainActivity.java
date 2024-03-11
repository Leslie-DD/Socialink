package com.example.heshequ.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.login.LoginActivity;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.UserInfoBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.entity.RefUserInfo;
import com.example.heshequ.entity.RefreshBean;
import com.example.heshequ.fragment.HomeFragment;
import com.example.heshequ.fragment.MeFragment;
import com.example.heshequ.fragment.MsgFragment;
import com.example.heshequ.utils.Utils;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NetWorkActivity implements View.OnClickListener {
    private Fragment currentFragment;

    private HomeFragment homeFragment;
    private MsgFragment msgFragment;
    private MeFragment meFragment;

    private View bottom;
    private PopupWindow pop;
    private TextView tvExit;
    private WindowManager.LayoutParams layoutParams;
    private final int getUserinfo = 1000;
    private Gson gson;
    private SharedPreferences sp;
    private View red;
    private int colum;
    private TextView rb4, rb1, rb5;
    private int oldSystemUiVisibility;
    private View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        decorView = getWindow().getDecorView();
        sp = getSharedPreferences("meet", 0);

        Constants.uid = sp.getInt("uid", 1);
        Constants.token = sp.getString("token", "");
        WenConstans.token = sp.getString("token", "");
        Constants.userName = sp.getString("user", "18274962484");
        init();
        initPop();
    }

    private void init() {
        gson = new Gson();
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        bottom = findViewById(R.id.bottom);
        Constants.screenW = outMetrics.widthPixels;
        Constants.screenH = outMetrics.heightPixels;

        List<String> aar = new ArrayList<>();
        aar.add(Manifest.permission.CAMERA);
        aar.add(Manifest.permission.READ_CONTACTS);
        aar.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        aar.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            aar.add(Manifest.permission.READ_MEDIA_VIDEO);
//            aar.add(Manifest.permission.READ_MEDIA_IMAGES);
//        }
        try {
            requestPermission(aar, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        red = findViewById(R.id.red);
        rb4 = findViewById(R.id.rb4);
        rb1 = findViewById(R.id.rb1);
        rb5 = findViewById(R.id.rb5);
        rb1.setSelected(true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(red.getLayoutParams());
        lp.setMargins((int) ((Constants.screenW * 4 * 1.0 / 5) - (Constants.screenW * 1.0 / 15)), Utils.dip2px(mContext, 5F), 0, 0);
        findViewById(R.id.rls).setOnClickListener(this);
        findViewById(R.id.rb1).setOnClickListener(this);
        findViewById(R.id.rb5).setOnClickListener(this);
        homeFragment = new HomeFragment();
        msgFragment = new MsgFragment();
        meFragment = new MeFragment();
        currentFragment = homeFragment;
        FragmentTransaction f1 = getSupportFragmentManager().beginTransaction();
        f1.add(R.id.fl, currentFragment).commit();

        //请求用户信息
        setBodyParams(new String[]{"uid"}, new String[]{"" + Constants.uid});
        sendPost(Constants.base_url + "/api/user/info.do", getUserinfo, Constants.token);
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        TextView tvCancel = pv.findViewById(R.id.tvPic);
        tvCancel.setText("取消");
        tvCancel.setTextColor(Color.parseColor("#999999"));
        tvExit = pv.findViewById(R.id.tvUp);
        tvExit.setText("退出");
        tvExit.setTextColor(Color.parseColor("#CC5252"));
        tvCancel.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        pop.setOutsideTouchable(true);
        // 设置焦点
        pop.setFocusable(true);
        pop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });
        // 设置所在布局
        pop.setContentView(pv);
        getLabelData();
    }

    private void getLabelData() {
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", 10086, Constants.token);
    }

    public void showPop() {
        if (Constants.uid == 1) {
            tvExit.setText("去登陆");
        }
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(bottom, Gravity.BOTTOM, 0, 0);
    }

    public void switchFragment(Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment == to) {
            return;
        }
        if (!to.isAdded()) {
            transaction.hide(currentFragment).add(R.id.fl, to).commit();
        } else {
            transaction.hide(currentFragment).show(to).commit();
        }
        currentFragment = to;
    }

    private long currentTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - currentTime > 2000) {
                Utils.toastShort(mContext, "再按一次退出高校联盟");
                currentTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refUserinfo(RefUserInfo refUserInfo) {
        //请求用户信息
        setBodyParams(new String[]{"uid"}, new String[]{"" + Constants.uid});
        sendPost(Constants.base_url + "/api/user/info.do", getUserinfo, Constants.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUp:  // 退出登录/登录
                pop.dismiss();
                sp.edit().putString("phone", "").putString("token", "").putInt("uid", 1).putBoolean("isLogin", false).apply();
                sp.edit().putBoolean("isLogin", false).apply();
                Constants.uid = 1;
                MeetApplication.getInstance().finishAll();
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.tvPic: //取消
                pop.dismiss();
                break;
            case R.id.rb1:  // 导航1 首页
                if (colum == 0) {
                    return;
                }
                colum = 0;
                clearAllSelect();
                rb1.setSelected(true);
                decorView.setSystemUiVisibility(oldSystemUiVisibility);
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
                switchFragment(homeFragment);
                break;
            case R.id.rls:  // 导航2 消息
                if (colum == 2) {
                    return;
                }
                colum = 2;
                redStatus(0);
                clearAllSelect();
                rb4.setSelected(true);
                decorView.setSystemUiVisibility(oldSystemUiVisibility);
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
                switchFragment(msgFragment);
                break;
            case R.id.rb5:  // 导航4 我的
                if (colum == 4) {
                    return;
                }
                colum = 4;
                clearAllSelect();
                rb5.setSelected(true);
                oldSystemUiVisibility = decorView.getSystemUiVisibility();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                switchFragment(meFragment);
                break;
        }
    }

    private void clearAllSelect() {
        if (colum == 3) {
            redStatus(0);
        }
        rb1.setSelected(false);
        rb4.setSelected(false);
        rb5.setSelected(false);
    }

    public void redStatus(int type) {
        if (red != null) {
            if (type == 0) {
                red.setVisibility(View.GONE);
            } else if (type == 1) {
                red.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshData(RefreshBean refreshBean) {
        switch (refreshBean.type) {
            case "1":
            case "2":
            case "3":
                if (colum != 3) {
                    redStatus(1);
                }
                break;
        }
        if (NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()) {
            Log.e("DDQ", "Notification ok ");
        } else {
            Log.e("DDQ", "Notification noOpen ");
            Utils.toastLong(mContext, "检测到您没有打开\"高校联盟\"通知权限，请前往应用管理页面打开通知权限，以接收更多新消息");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 102) {
            for (int ret : grantResults) {
                if (ret != PackageManager.PERMISSION_GRANTED) {
                    Utils.toastShort(mContext, "权限申请被拒绝");
                    return;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getUserinfo) {
            switch (result.optInt("code")) {
                case 0:
                    if (!result.optString("data").isEmpty()) {
                        UserInfoBean userInfoBean = gson.fromJson(result.optString("data"), UserInfoBean.class);
                        Constants.userName = userInfoBean.getNickname();
                        //存入用户名
                        MeetApplication.getInstance().getSharedPreferences().edit().putString("user", userInfoBean.getNickname()).apply();
                        //MeetApplication.getInstance().getSharedPreferences().edit().putInt("experience",userInfoBean.getExperience()).apply();
                    }
                    break;
                case 1:
                    Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                    break;
                case 2:
                    Utils.toastShort(this, result.optString("msg"));
                    break;
                case 3:
                    Utils.toastShort(this, "您没有该操作权限");
                    break;
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
