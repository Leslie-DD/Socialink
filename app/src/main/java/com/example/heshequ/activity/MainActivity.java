package com.example.heshequ.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.login.LoginActivity;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.UserInfoBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.entity.RefUserInfo;
import com.example.heshequ.entity.RefreshBean;
import com.example.heshequ.entity.VersionBean;
import com.example.heshequ.fragment.HomeFragment;
import com.example.heshequ.fragment.KnowledgeFragment;
import com.example.heshequ.fragment.MeFragment;
import com.example.heshequ.fragment.MsgFragment;
import com.example.heshequ.utils.BarUtils;
import com.example.heshequ.utils.Utils;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends NetWorkActivity implements View.OnClickListener {
    private Fragment currtFragmetn;
    private RadioGroup group;

    private HomeFragment homeFragment;
    private MsgFragment msgFragment;
    private MeFragment meFragment;
    private KnowledgeFragment klFragment;

    private View bottom;
    private PopupWindow pop;
    private TextView tvExit, tvCancel;
    private WindowManager.LayoutParams layoutParams;
    private final int getuserinfo = 1000;
    private UserInfoBean userInfoBean;
    private Gson gson;
    private SharedPreferences sp;
    private View red;
    private int clum;
    private TextView rb4, rb1, rb3, rb5;
    private boolean isThree;
    private int remItem;
    private VersionBean versionBean;
    private Dialog upDialog;
    //版本更新的提示
    private TextView tvDesc, tvSize, tvVersion, tvUp;
    private ImageView ivClose;
    private String apkPath;
    private int progress = 0;
    private int oldSystemUiVisibility;
    private View decorView;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载完成 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载失败 */
    private static final int DOWNLOAD_FAIL = 3;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD:
                    Log.e("DDQ", "下载进度：" + progress);
                    break;
                case DOWNLOAD_FINISH:
                    installApk();
                    break;
                case DOWNLOAD_FAIL:
                    Toast.makeText(mContext, "应用更新失败，请保持网络正常情况下再更新", Toast.LENGTH_SHORT).show();
                    upDialog.dismiss();
                    break;
            }

        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        //ToastUtils.showLong("测试热修复更新的。。。");
        decorView = getWindow().getDecorView();
        sp = getSharedPreferences("meet", 0);
        boolean isLogin = sp.getBoolean("isLogin", false);
//        if (!isLogin) {
//            startActivity(new Intent(mContext, LoginActivity.class));
//            finish();
//            return;
//        }
        Constants.uid = sp.getInt("uid", 1);
        Constants.token = sp.getString("token", "");
        WenConstans.token = sp.getString("token", "");
        Constants.userName = sp.getString("user", "18274962484");
        init();
        //test();
        initPop();
        initDialog();
    }

    private void init() {
        gson = new Gson();
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        bottom = findViewById(R.id.bottom);
        Constants.screenW = outMetrics.widthPixels;
        Constants.screenH = outMetrics.heightPixels;
        String[] arr = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        try {
            requestPermission(arr, 100);
        } catch (Exception e) {
        }
        red = findViewById(R.id.red);
        rb4 = findViewById(R.id.rb4);
        rb1 = findViewById(R.id.rb1);
//        rb3 = findViewById(R.id.rb3);
        rb5 = findViewById(R.id.rb5);
        rb1.setSelected(true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(red.getLayoutParams());
        lp.setMargins((int) ((Constants.screenW * 4 * 1.0 / 5) - (Constants.screenW * 1.0 / 15)), Utils.dip2px(mContext, 5F), 0, 0);
//        group = (RadioGroup) findViewById(R.id.rg);
//        group.setOnCheckedChangeListener(this);
        findViewById(R.id.rls).setOnClickListener(this);
//        findViewById(R.id.knowledge).setOnClickListener(this);
        findViewById(R.id.rb1).setOnClickListener(this);
        findViewById(R.id.rb5).setOnClickListener(this);
        homeFragment = new HomeFragment();
        msgFragment = new MsgFragment();
        meFragment = new MeFragment();
        klFragment = new KnowledgeFragment();
        currtFragmetn = homeFragment;
        FragmentTransaction f1 = getSupportFragmentManager().beginTransaction();
        f1.add(R.id.fl, currtFragmetn).commit();

        //请求用户信息
        setBodyParams(new String[]{"uid"}, new String[]{"" + Constants.uid});
        sendPost(Constants.base_url + "/api/user/info.do", getuserinfo, Constants.token);
    }

    private void initDialog() {
        upDialog = new Dialog(mContext, R.style.custom_dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.updatalayout, null);
        tvVersion = view.findViewById(R.id.tvVersion);
        tvSize = view.findViewById(R.id.tvSize);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvUp = view.findViewById(R.id.tvUp2);
        ivClose = view.findViewById(R.id.ivClose);
        tvUp.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        upDialog.setContentView(view);
        Window window = upDialog.getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        window.getAttributes().dimAmount = 0.5f;
        window.setLayout(Utils.getScreenWidth() - Utils.dip2px(mContext, 100), ViewGroup.LayoutParams.WRAP_CONTENT);
        //请求版本更新的接口
        OkHttpUtils.post(Constants.base_url + "/api/system/lastVersion.do")
                .headers("XIANGYU-ACCESS-TOKEN", Constants.token)
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            versionBean = gson.fromJson(jsonObject.optString("data"), VersionBean.class);
                            if (versionBean != null) {
                                if (Float.parseFloat(versionBean.getVercode()) > Utils.getVerCode(MainActivity.this)) {
                                    if (!versionBean.getApppath().isEmpty()) {
                                        apkPath = Constants.base_url + versionBean.getApppath();
                                        tvVersion.setText("最新版本：V" + versionBean.getVername());
                                        tvDesc.setText(versionBean.getVerdesc() == null || versionBean.getVerdesc().isEmpty() ? "1.修复已知BUG" : versionBean.getVerdesc());
                                        tvSize.setText("新版本大小：" + new DecimalFormat(".00").format(((float) versionBean.getAppsize() / 1024)) + " M");
                                        upDialog.show();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        //upDialog.show();
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        tvCancel = (TextView) pv.findViewById(R.id.tvPic);
        tvCancel.setText("取消");
        tvCancel.setTextColor(Color.parseColor("#999999"));
        tvExit = (TextView) pv.findViewById(R.id.tvUp);
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
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        // 设置所在布局
        pop.setContentView(pv);
        getLableData();
    }

    private void getLableData() {
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
        if (currtFragmetn == to) {
            return;
        }
        if (!to.isAdded()) {
            transaction.hide(currtFragmetn).add(R.id.fl, to).commit();
        } else {
            transaction.hide(currtFragmetn).show(to).commit();
        }
        currtFragmetn = to;
    }

    private long currentTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - currentTime > 2000) {
                Toast.makeText(mContext, "再按一次退出湘遇", Toast.LENGTH_SHORT).show();
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
        sendPost(Constants.base_url + "/api/user/info.do", getuserinfo, Constants.token);
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
                if (clum == 0) {
                    return;
                }
                clum = 0;
                clearAllSelect();
                rb1.setSelected(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    decorView.setSystemUiVisibility(oldSystemUiVisibility);
                } else {
                    BarUtils.setStatusBarVisibility(this, true);
                }
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
                switchFragment(homeFragment);
                break;
            case R.id.rls:  // 导航2 消息
                if (clum == 2) {
                    return;
                }
                clum = 2;
                redStatu(0);
                clearAllSelect();
                rb4.setSelected(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    decorView.setSystemUiVisibility(oldSystemUiVisibility);
                } else {
                    BarUtils.setStatusBarVisibility(this, true);
                }
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
                switchFragment(msgFragment);
                break;
//            case R.id.knowledge:    // 导航3 知识
//                if (clum == 3) {
//                    return;
//                }
//                clum = 3;
//                clearAllSelect();
//                rb3.setSelected(true);
//                //                if (Build.VERSION.SDK_INT >= 21) {
//                    decorView.setSystemUiVisibility(oldSystemUiVisibility);
//                } else {
//                    BarUtils.setStatusBarVisibility(this, true);
//                }
//                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
//                switchFragment(klFragment);
////                startActivity(new Intent().setClass(mContext,KnowledgeActivity.class));
//                break;
            case R.id.rb5:  // 导航4 我的
                if (clum == 4) {
                    return;
                }
                clum = 4;
                clearAllSelect();
                rb5.setSelected(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    oldSystemUiVisibility = decorView.getSystemUiVisibility();
                    int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    // 避免底部虚拟按键和导航栏重复 注释掉下面两行
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    decorView.setSystemUiVisibility(option);
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
//                    getWindow().setNavigationBarColor(Color.TRANSPARENT);
                } else {
                    //设置状态栏不可见
                    BarUtils.setStatusBarVisibility(this, false);
                }

                //StatusBarCompat.setStatusBarColor(this,Color.parseColor("#00bbff"));
                switchFragment(meFragment);
                break;
            case R.id.tvUp2:
                Utils.toastShort(mContext, "下载中");
                // 文件下载
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    downLoadApk();
                } else {
                    requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                }
                upDialog.dismiss();
                break;
            case R.id.ivClose:
                upDialog.dismiss();
                break;
        }
    }

    private void downLoadApk() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String urlStr = apkPath;
                    URL url = new URL(urlStr);
                    HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
                    // 获取文件大小
                    int length = urlcon.getContentLength();
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkPath.substring(apkPath.lastIndexOf("/") + 1));
                    if (!file.exists()) {
                        // 创建文件夹
                        file.mkdirs();
                    } else {
                        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkPath.substring(apkPath.lastIndexOf("/") + 1));
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                    // 获取文件全名
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkPath.substring(apkPath.lastIndexOf("/") + 1));
                    InputStream is = urlcon.getInputStream();
                    // 创建文件
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                        Thread.sleep(50);
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int count = 0;
                    int cp = 0;
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        if (progress != cp) {
                            cp = progress;
                            handler.sendEmptyMessage(DOWNLOAD);
                        }
                        if (numread <= 0) {
                            // 下载完成
                            progress = 0;
                            handler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (true);// 点击取消就停止下载.
                    fos.close();
                    fos = null;
                    is.close();
                    is = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(DOWNLOAD_FAIL);
                }
            }
        }).start();
    }

    private void clearAllSelect() {
//        if (remItem==0){
//            redStatu(0);
//        }else{
//            if (remItem==4){
//                redStatu(0);
//            }else{
//                if (clum==3){
//                    redStatu(0);
//                }
//            }
//        }
        if (clum == 3) {
            redStatu(0);
        }
        rb1.setSelected(false);
        rb4.setSelected(false);
//        rb3.setSelected(false);
        rb5.setSelected(false);
    }

    public void redStatu(int type) {
        if (red != null) {
            if (type == 0) {
                remItem = 0;
                red.setVisibility(View.GONE);
            } else if (type == 1) {
                red.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshData(RefreshBean refreshBean) {
//        if (!refreshBean.type.equals("4")){
//            remItem = clum+1;
//            if (clum!=3){
//                redStatu(1);
//            }
//        }
        if (refreshBean.type.equals("1")) {
            remItem = clum + 1;
            if (clum != 3) {
                redStatu(1);
            }
        } else if (refreshBean.type.equals("2")) {
            remItem = clum + 1;
            if (clum != 3) {
                redStatu(1);
            }
        } else if (refreshBean.type.equals("3")) {
            remItem = clum + 1;
            if (clum != 3) {
                redStatu(1);
            }
        }
        if (NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()) {
            Log.e("DDQ", "Notification ok ");
            //
        } else {
            Log.e("DDQ", "Notification noOpen ");
            Utils.toastLong(mContext, "检测到您没有打开\"湘遇\"通知权限，请前往应用管理页面打开通知权限，以接收更多新消息");
        }
    }

    private void installApk() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkPath.substring(apkPath.lastIndexOf("/") + 1));
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(this, "com.chinamobile.meet.FileProvider"
                    , file);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
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
            downLoadApk();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getuserinfo) {
            switch (result.optInt("code")) {
                case 0:
                    if (!result.optString("data").isEmpty()) {
                        userInfoBean = gson.fromJson(result.optString("data"), UserInfoBean.class);
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
