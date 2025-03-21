package com.leslie.socialink.bean;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.utils.FileUtilcll;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConsTants {
    public static int screenH;
    public static int screenW;
    public static String uid;
    public static String token;
    public static String phone;
    public static int homeItem = 1;

    public static boolean fail(Context context, JSONObject result) {
        if (result.has("ret")) {
            try {
                int ret = result.getInt("ret");
                if (ret != 0) {
                    if (result.has("msg")) {
                        String msg = result.getString("msg");
                        if (!TextUtils.isEmpty(msg)) {
                            if (!msg.equals("未找到记录")) {
                                if (msg.equals("Token验证错误")) {
                                    Utils.toastShort(context, "用户信息过期，请重新登录");
                                } else {
                                    Utils.toastShort(context, msg);
                                }
                            }
                        }
                    } else {
                        Utils.toastShort(context, "后台错误");
                    }
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void initXRecycleView(Context context, boolean loadmore, boolean refresh, XRecyclerView xRecyclerView) {
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (refresh) {
            xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
            xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
            xRecyclerView.getDefaultRefreshHeaderView().mMeasuredHeight = Utils.dip2px(context, 68);
        } else {
            xRecyclerView.setPullRefreshEnabled(false);
        }
        if (loadmore) {
            xRecyclerView.setLoadingMoreEnabled(true);
            xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
            xRecyclerView.getDefaultFootView().setLoadingHint("加载中");
            xRecyclerView.getDefaultFootView().setNoMoreHint("加载完成");
        } else {
            xRecyclerView.setLoadingMoreEnabled(false);
        }
        xRecyclerView.setLimitNumberToCallLoadMore(0);
    }

    public static String getPicName() {
        SimpleDateFormat fat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        return fat.format(new Date()) + ".jpg";
    }

    public static void showFileChooser(int file_code, NetWorkActivity context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        try {
            context.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), file_code);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.toastShort(context, "Please install a File Manager.");
        }
    }

    public static String startPhoto(NetWorkActivity context) {
        String path = getPath();
        if (path == null) {
            Utils.toastShort(context, "创建文件失败，请打开相关权限");
            return "";
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(path));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, 200);
        return path;
    }

    public static String getPath() {
//        String path = Environment.getExternalStorageDirectory() + "/" + "douzisocket/";
        String path = FileUtilcll.getPublicDir() + "/" + "douzisocket/";

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        path = path + getPicName();
        f = new File(path);
//        Log.e("YSF", "内存卡路径：" + Environment.getExternalStorageDirectory() + "当前路径：" + path);
        try {
            f.createNewFile();
            Log.e("YSF", "创建好了文件" + f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("YSF", "创建文件出错" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return path;
    }
}
