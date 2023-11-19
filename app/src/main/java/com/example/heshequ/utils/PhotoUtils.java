package com.example.heshequ.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.heshequ.base.NetWorkActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hulk_Zhang on 2018/6/1 10:07
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class PhotoUtils {
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
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String startPhoto(NetWorkActivity context) {
        String path = getPath();
        if (path == null) {
            Utils.toastShort(context, "创建文件失败，请打开相关权限");
            return "";
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        Uri uri = Uri.fromFile(new File(path));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, 200);
        return path;
    }

    public static String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/" + "douzisocket/";
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        path = path + getPicName();
        f = new File(path);
        Log.e("YSF", "内存卡路径：" + Environment.getExternalStorageDirectory() + "当前路径：" + path);
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
