package com.hnu.heshequ.utils;

import static android.content.Context.APP_OPS_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.constans.Name;
import com.hnu.heshequ.entity.TeamMemberBean;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.view.CircleView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    // 获取控件的高度
    public static int getHeight(View view) // 获得某组件的高度
    {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float radiu) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, android.renderscript.Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur: 0 < radius <= 25
        blurScript.setRadius(radiu);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;

    }

    public static String getNameByPosition(int itemPosition, int i) {
        String name = itemPosition + "_" + Name.IMAGE_1;
        switch (i) {
            case 0:
                name = itemPosition + "_" + Name.IMAGE_1;
                break;
            case 1:
                name = itemPosition + "_" + Name.IMAGE_2;
                break;
            case 2:
                name = itemPosition + "_" + Name.IMAGE_3;
                break;
            case 3:
                name = itemPosition + "_" + Name.IMAGE_4;
                break;
            case 4:
                name = itemPosition + "_" + Name.IMAGE_5;
                break;
            case 5:
                name = itemPosition + "_" + Name.IMAGE_6;
                break;
            case 6:
                name = itemPosition + "_" + Name.IMAGE_7;
                break;
            case 7:
                name = itemPosition + "_" + Name.IMAGE_8;
                break;
            case 8:
                name = itemPosition + "_" + Name.IMAGE_9;
                break;

        }
        return name;
    }

    private static Pair<String, Long> lastToast = null;

    private static boolean forbidShow(String info, long duration) {
        return lastToast != null
                && !TextUtils.isEmpty(lastToast.first)
                && lastToast.second != null
                && TextUtils.equals(lastToast.first, info)
                && System.currentTimeMillis() - lastToast.second < duration;
    }

    public static void toastLong(Context context, String info) {
        if (forbidShow(info, 5000)) {
            return;
        }
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
        lastToast = new Pair<>(info, System.currentTimeMillis());
    }

    public synchronized static void toastShort(Context context, String info) {
        if (forbidShow(info, 3000)) {
            return;
        }
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        lastToast = new Pair<>(info, System.currentTimeMillis());
    }

    public static ArrayList<TeamMemberBean> getSortData(ArrayList<TeamMemberBean> pData) {
        ArrayList<TeamMemberBean> tempData = new ArrayList<>();
        //提取队长副队 并设置首字母
        for (int i = 0; i < pData.size(); i++) {
            TeamMemberBean bean = pData.get(i);
            bean.setInitialLetter(Utils.setUserInitialLetter(bean.getNickname()));
            if (bean.getRole() == 1 || bean.getRole() == 2) {
                tempData.add(bean);
                pData.remove(i);
                i--;
            }
        }
        //对pdata排序
        Collections.sort(pData);
        //统计个数
        String letter = "";
        for (int i = 0; i < pData.size(); i++) {
            TeamMemberBean bean = pData.get(i);

            letter = bean.getInitialLetter();

            int count = 0;
            if (i == 0 || !letter.equals(pData.get(i - 1).getInitialLetter())) {
                for (int j = 0; j < pData.size(); j++) {
                    if (pData.get(j).getInitialLetter().equals(letter)) {
                        count++;
                    }
                }
                bean.setCount(count);
            }
        }
        //设置队长 副队
        for (int i = 0; i < tempData.size(); i++) {
            TeamMemberBean bean = tempData.get(i);
            bean.setInitialLetter("团长、副队长");
            bean.setCount(tempData.size());
            if (bean.getRole() == 1) {
                pData.add(0, bean);
                tempData.remove(i);
                i--;
            }
        }
        Collections.sort(tempData);
        for (int i = tempData.size() - 1; i >= 0; i--) {
            pData.add(1, tempData.get(i));
        }

        return pData;
    }

    /**
     * 设置user昵称(没有昵称取username)的首字母属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     */
    public static String setUserInitialLetter(String name) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if (!TextUtils.isEmpty(name)) {
            letter = new GetInitialLetter().getLetter(name);
            return letter;
        }
        return letter;
    }

    /**
     * 复制图片
     *
     * @param oldPath
     * @param newPath
     */
    public static boolean saveBitmap(String oldPath, String newPath) {

        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(newPath);
            // if (!oldfile.exists()) { //文件不存在时
            InputStream inStream = new FileInputStream(oldPath); // 读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
            return true;
            // }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return false;

        }

    }

    // 通过网络url加载图片
    public static Bitmap getBitmap(String imageUrl) {
        Bitmap mBitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mBitmap;
    }

    /**
     * 计算n天之前的日期   style=0返回yyyy-MM-dd
     * style=1返回yyyy年MM月dd日
     */
    public static String getDateBefore(int n, int style) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - n);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        if (style == 1) {
            return year + "年" + (month < 10 ? "0" + month : month) + "月" + (date < 10 ? "0" + date : date) + "日";
        }
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date);
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private static Random random = new Random();

    /**
     * 根据手机分辨率从dp转成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f) - 15;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getDeviceId();
    }

    /**
     * 返回电话的IMEI:手机的唯一标识码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String imei = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            imei = tm.getDeviceId();
            if (isEmpty(imei)) {
                imei = createIMSI();
            }
        }
        return imei;
    }

    /**
     * 获取手机IMSI
     *
     * @return
     */
    public static String createIMSI() {
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < 15; i++)
            strbuf.append(random.nextInt(10));
        return strbuf.toString();
    }

    /**
     * 手机制造商
     *
     * @return
     */
    public static String getProduct() {

        return Build.PRODUCT;
    }

    /**
     * 手机系统版本
     *
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前的版本号
     *
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) {
        String version = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 通过Wifi获取MAC ADDRESS作为DEVICE ID 缺点：如果Wifi关闭的时候，硬件设备可能无法返回MAC ADDRESS.。
     *
     * @return
     */
    public static String getMacAddress() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * 判断是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return (s == null || "".equals(s) || "null".equals(s.toLowerCase()));
    }

    /**
     * 得到手机屏幕的宽
     *
     * @param mActivity
     * @return
     */
    public static int getScreenWidth(Activity mActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 得到手机屏幕的高
     *
     * @return
     */
    public static int getScreenHeight(Activity mActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取文件夹大小
     *
     * @param path
     * @return
     */
    public static String getSizeFile(String path) {
        if (isEmpty(path))
            return null;
        String fileSize = null;
        try {
            File ff = new File(path);
            // 如果路径是文件夹的时候
            if (ff.isDirectory()) {
                long l = getFileFolderSize(ff);
                fileSize = formetFileSize(l);
            } else {
                long lo = getFileSizes(ff);
                fileSize = formetFileSize(lo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSize;
    }

    public static long getFileDirSize(String path) {
        if (Utils.isEmpty(path))
            return 0;
        File sizeFile = new File(path);
        try {
            if (sizeFile.isDirectory()) {
                return getFileFolderSize(sizeFile);
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    /**
     * 递归 //取得文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileFolderSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileFolderSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.##");
        String fileSizeString = "";
        if (fileS == 0 || fileS < 350)
            return "0M";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 根据key获取metadata中的值
     *
     * @param context
     * @param channelKey
     * @return
     */
    public static int getMetaDataIntValue(Context context, String channelKey) {
        int msg = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getInt(channelKey);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 根据key获取metadata中的值
     *
     * @param context
     * @param channelKey
     * @return
     */
    public static String getMetaDataStringValue(Context context, String channelKey) {
        String msg = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString(channelKey);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否符合6-18位的字母数字密码组合
     *
     * @param str
     * @return
     */
    public static boolean isPwd(String str) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,18}$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 得到画布
        Canvas canvas = new Canvas(output);

        // 将画布的四角圆化
        final int color = Color.RED;
        final Paint paint = new Paint();
        // 得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        // 值越大角度越明显
        final float roundPx = 40;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedCornerBitmap2(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 得到画布
        Canvas canvas = new Canvas(output);

        // 将画布的四角圆化
        final int color = Color.RED;
        final Paint paint = new Paint();
        // 得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        // 值越大角度越明显
        final float roundPx = 90;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * is has sd
     *
     * @return
     */
    public static boolean isSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 返回保存图片名字
     *
     * @return
     */
    public static String getPicName() {
        SimpleDateFormat fat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        return fat.format(new Date()) + ".jpg";
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static boolean isHttpUrl(String url) {
        if (url != null) {
            return url.contains("http");
        }
        return false;
    }

    /**
     * 格式化日期为"yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化字符串格式"yyyy-MM-dd HH:mm:ss"为日期
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parseString(String str, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    public static boolean isSlideToBottom(XRecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange()) {
            return true;
        }
        return false;
    }

    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Options newOpts = new Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    // 质量压缩
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options == 0) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static String formatDecimal(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if (d < 0.00001 && d > -0.00001) {
            return "0.0";
        }
        return decimalFormat.format(d);
    }

    public static void setHead(Context mContext, String logoImage, CircleView ivHead) {
        Glide.with(mContext).load(Constants.base_url + logoImage).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(ivHead);
    }

    public static void setImg(Context mContext, String url, ImageView ivPic, int resId) {
        Glide.with(mContext).load(Constants.base_url + url).error(resId).placeholder(resId).centerCrop().into(ivPic);
    }

    /**
     * 动态显示键盘.
     *
     * @param activity
     */
    public static void showSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态显示键盘.
     *
     * @param view
     */
    public static void showSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) MeetApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘.
     *
     * @param activity The activity.
     */
    public static void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏键盘.
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) MeetApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static boolean isKeyBoarVisiableForLast = false;
    public static ViewTreeObserver.OnGlobalLayoutListener KeyBoaronGlobalLayoutListener = null;

    /**
     * 监听软键盘高度 px.
     *
     * @param activity
     */
    public static void addOnSoftKeyBoardVisibleListener(Activity activity) {
        if (Constants.keyboardHeight > 0) {
            return;
        }
        final View decorView = activity.getWindow().getDecorView();
        KeyBoaronGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕整体的高度
                int hight = decorView.getHeight();
                boolean visible = (double) displayHight / hight < 0.8;
                int statusBarHeight = 0;
                try {
                    @SuppressLint("PrivateApi") Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = MeetApplication.getInstance().getApplicationContext().getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (visible && visible != isKeyBoarVisiableForLast) {
                    //获得键盘高度
                    Constants.keyboardHeight = hight - displayHight - statusBarHeight;
                    SharedPreferencesHelp.getEditor().putInt("keyboardHeight", Constants.keyboardHeight).apply();
                    Log.e("DDQ", "软键盘高度：keyboardHeight == " + Constants.keyboardHeight);
                }
                isKeyBoarVisiableForLast = visible;
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(KeyBoaronGlobalLayoutListener);
    }

    /**
     * 将包含系统表情的字符串转化为HEX字符串
     *
     * @param s
     * @return
     */
    public static String getStringByEmoji(String s) {
        int length = s.length();
        String context = "";
        //循环遍历字符串，将字符串拆分为一个一个字符
        for (int i = 0; i < length; i++) {
            char codePoint = s.charAt(i);
            //判断字符是否是emoji表情的字符
            if (isEmojiCharacter(codePoint)) {
                //如果是将以大括号括起来
                String emoji = "{" + Integer.toHexString(codePoint) + "}";
                context = context + emoji;
                continue;
            }
            context = context + codePoint;
        }
        return context;
    }

    /**
     * 是否包含表情
     *
     * @param codePoint
     * @return 如果不包含 返回false,包含 则返回true
     */

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF)));
    }

    /**
     * 将表情描述转换成表情
     *
     * @param str
     * @return
     */
    public static String getEmoji(Context context, String str) {
        String string = str;
        String rep = "\\{(.*?)\\}";
        Pattern p = Pattern.compile(rep);
        Matcher m = p.matcher(string);
        while (m.find()) {
            String s1 = m.group().toString();
            String s2 = s1.substring(1, s1.length() - 1);
            String s3;
            try {
                s3 = String.valueOf((char) Integer.parseInt(s2, 16));
                string = string.replace(s1, s3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return string;
    }

    /**
     * url图片下载到文件
     *
     * @param file
     * @param urlstr
     */
    public static boolean DownloadPicture(File file, String urlstr) {
        URL url = null;
        try {
            //生成图片链接的url类
            url = new URL(urlstr);
            //将url链接下的图片以字节流的形式存储到 DataInputStream类中
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            //为file生成对应的文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //定义字节数组大小
            byte[] buffer = new byte[1024];
            //从所包含的输入流中读取[buffer.length()]的字节，并将它们存储到缓冲区数组buffer 中。
            //dataInputStream.read()会返回写入到buffer的实际长度,若已经读完 则返回-1
            while (dataInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);//将buffer中的字节写入文件中区
            }
            dataInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断时间是否未过期
     */
    public static boolean isPastDue(String time, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        Date dt1 = df.parse(time);//将字符串转换为date类型
        Date dt2 = new Date();
        return dt1.getTime() > dt2.getTime();
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }

            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }

    public static int getVerCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) MeetApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the paths of sdcard.
     *
     * @return the paths of sdcard
     */
    public static List<String> getSDCardPaths() {
        StorageManager storageManager = (StorageManager) MeetApplication.getInstance().getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            //noinspection JavaReflectionMemberAccess
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
