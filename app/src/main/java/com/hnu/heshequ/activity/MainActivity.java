package com.hnu.heshequ.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.login.LoginActivity;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.UserInfoBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.entity.RefUserInfo;
import com.hnu.heshequ.entity.RefreshBean;
import com.hnu.heshequ.fragment.HomeFragment;
import com.hnu.heshequ.fragment.MeFragment;
import com.hnu.heshequ.fragment.MsgFragment;
import com.hnu.heshequ.fragment.TeamsFragment;
import com.hnu.heshequ.fragment.WenwenFragment;
import com.hnu.heshequ.utils.Utils;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NetWorkActivity {
    private static final String TAG = "[MainActivity]";
    private Fragment currentFragment;

    private HomeFragment homeFragment;
    private TeamsFragment teamFragment;
    private WenwenFragment wenwenFragment;
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
    private TextView rb1, rb2, rb3, rb4, rb5;
    private int oldSystemUiVisibility;
    private View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        decorView = getWindow().getDecorView();
        sp = getSharedPreferences("meet", 0);

        Log.i(TAG, "(onCreate) uid = " + sp.getInt("uid", 1));
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

        try {
            requestPermission(aar, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        red = findViewById(R.id.red);

        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);

        rb1.setOnClickListener(v -> {
            if (colum == 0) {
                return;
            }
            colum = 0;
            clearAllSelect();
            rb1.setSelected(true);
            decorView.setSystemUiVisibility(oldSystemUiVisibility);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
            switchFragment(homeFragment);
        });
        rb2.setOnClickListener(v -> {
            if (colum == 1) {
                return;
            }
            colum = 1;
            clearAllSelect();
            rb2.setSelected(true);
            decorView.setSystemUiVisibility(oldSystemUiVisibility);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
            switchFragment(teamFragment);
        });
        rb3.setOnClickListener(v -> {
            if (colum == 2) {
                return;
            }
            colum = 2;
            clearAllSelect();
            rb3.setSelected(true);
            decorView.setSystemUiVisibility(oldSystemUiVisibility);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
            switchFragment(wenwenFragment);
        });
        findViewById(R.id.rls).setOnClickListener(v -> {
            if (colum == 3) {
                return;
            }
            colum = 3;
            redStatus(0);
            clearAllSelect();
            rb4.setSelected(true);
            decorView.setSystemUiVisibility(oldSystemUiVisibility);
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
            switchFragment(msgFragment);
        });
        rb5.setOnClickListener(v -> {
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
        });

        rb1.setSelected(true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(red.getLayoutParams());
        lp.setMargins((int) ((Constants.screenW * 4 * 1.0 / 5) - (Constants.screenW * 1.0 / 15)), Utils.dip2px(mContext, 5F), 0, 0);
        homeFragment = new HomeFragment();
        teamFragment = new TeamsFragment();
        wenwenFragment = new WenwenFragment();
        msgFragment = new MsgFragment();
        meFragment = new MeFragment();
        currentFragment = homeFragment;
        FragmentTransaction f1 = getSupportFragmentManager().beginTransaction();
        f1.add(R.id.fl, currentFragment).commit();

        //请求用户信息
        setBodyParams(new String[]{"uid"}, new String[]{String.valueOf(sp.getInt("uid", 1))});
        sendPost(Constants.base_url + "/api/user/info.do", getUserinfo, Constants.token);
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        TextView tvCancel = pv.findViewById(R.id.tvPic);
        tvCancel.setText("取消");
        tvCancel.setTextColor(Color.parseColor("#999999"));
        tvCancel.setOnClickListener(v -> pop.dismiss());
        tvExit = pv.findViewById(R.id.tvUp);
        tvExit.setText("退出");
        tvExit.setTextColor(Color.parseColor("#CC5252"));
        tvExit.setOnClickListener(v -> {
            pop.dismiss();
            sp.edit().putString("phone", "").putString("token", "").putInt("uid", 1).putBoolean("isLogin", false).apply();
            sp.edit().putBoolean("isLogin", false).apply();
            Constants.uid = 1;
            MeetApplication.getInstance().finishAll();
            startActivity(new Intent(mContext, LoginActivity.class));
        });
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

    private void clearAllSelect() {
        if (colum == 3) {
            redStatus(0);
        }
        rb1.setSelected(false);
        rb2.setSelected(false);
        rb3.setSelected(false);
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
                        Constants.uid = userInfoBean.getId();
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
            Log.i(TAG, "(onSuccess) Constants.uid = " + Constants.uid);
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Log.w(TAG, "(onFailure) Constants.uid = " + Constants.uid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
