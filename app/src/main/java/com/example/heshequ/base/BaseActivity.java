package com.example.heshequ.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.bean.ConsTants;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类activity 每一个acitivty都要继承该类
 *
 * @date 2013-12-12
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected Context mContext;
    private int REQUEST_CODE_PERMISSION = 99;

    protected interface IPermissionsRequestListener {

        @SuppressLint("MissingPermission")
        void onAllow();

        void onReject();
    }

    private IPermissionsRequestListener permissionsRequestListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("[ActivityCreate]", "(onCreate) " + TAG);
        super.onCreate(savedInstanceState);
        MeetApplication.getInstance().addActivity(this);
        WindowManager wm = this.getWindowManager();
        ConsTants.screenW = wm.getDefaultDisplay().getWidth();
        ConsTants.screenH = wm.getDefaultDisplay().getHeight();
        mContext = this;
        setWindowStatusBarColor(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
    }

    public void requestPermission(List<String> permissions, int requestCode) {
        requestPermission(permissions, requestCode, null);
    }

    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public void requestPermission(List<String> permissions, int requestCode, IPermissionsRequestListener listener) {
        REQUEST_CODE_PERMISSION = requestCode;
        permissionsRequestListener = listener;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
            if (listener != null) {
                listener.onAllow();
            }
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(
                    this,
                    needPermissions.toArray(new String[needPermissions.size()]),
                    REQUEST_CODE_PERMISSION
            );
        }
    }

    public void setText(String title) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(List<String> permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(List<String> permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    public void setWindowStatusBarColor(Activity activity) {
        try {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } catch (Exception e) {
            Log.w(TAG, "(setWindowStatusBarColor) failed, " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
                if (permissionsRequestListener != null) {
                    permissionsRequestListener.onAllow();
                }
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
                if (permissionsRequestListener != null) {
                    permissionsRequestListener.onReject();
                }
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new android.app.AlertDialog.Builder(this).setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", (dialog, which) -> {
                }).setPositiveButton("确定", (dialog, which) -> startAppSettings()).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        Log.d(TAG, "获取权限成功=" + requestCode);

    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    public void permissionFail(int requestCode) {
        Log.d(TAG, "获取权限失败=" + requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeetApplication.getInstance().removeActivity(this);
    }
}
