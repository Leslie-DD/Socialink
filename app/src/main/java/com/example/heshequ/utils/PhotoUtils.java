package com.example.heshequ.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.example.heshequ.base.NetWorkActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoUtils {
    private static final String TAG = "[PhotoUtils]";

    public static final String AUTHORITY = "com.example.heshequ.FileProvider";

    private static final String FILE_NAME_FORMAT = "yyyyMMdd_HHmmss";
    private static final String FILE_SUFFIX = ".jpg";
    private static final String FILE_PREFIX = "GALLERY_";

    public static final String IMAGE_TYPE = "image/*";

    public static void choosePhoto(int file_code, NetWorkActivity context) {
        String action;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            action = MediaStore.ACTION_PICK_IMAGES;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent().setAction(action).setType(IMAGE_TYPE);
        try {
            context.startActivityForResult(intent, file_code);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.toastShort(context, "Please install a File Manager.");
        }
    }

    public static Uri takePhoto(FragmentActivity context, int requestCode) {
        File imageFile = PhotoUtils.createTempFile(context);
        if (imageFile == null) {
            Log.w(TAG, "(takePhoto) createTempFile error, null");
            return null;
        }
        Log.d(TAG, String.format("(takePhoto) createTempFile: %s", imageFile));
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, requestCode);
        return uri;
    }

    /**
     * Deprecated and use {@link #takePhoto(FragmentActivity, int)}
     *
     * @see #startPhoto(NetWorkActivity, int)
     */
    @Deprecated
    public static String startPhoto(NetWorkActivity context) {
        return startPhoto(context, 200);
    }

    /**
     * Deprecated and use {@link #takePhoto(FragmentActivity, int)}
     *
     * @see #startPhoto(NetWorkActivity)
     */
    @Deprecated
    public static String startPhoto(NetWorkActivity context, int requestCode) {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission_group.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            return "";
//        }
        File imageFile = PhotoUtils.createTempFile(context);
        if (imageFile == null) {
            Log.w(TAG, "(startPhoto) createTempFile error, null");
            return null;
        }
        Log.d(TAG, String.format("(startPhoto) createTempFile: %s", imageFile));
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, requestCode);
        return imageFile.getAbsolutePath();
    }

    public static Uri generateUri(Context context, Uri sourceUri) {
        File outputFile = createTempFile(context);
        if (outputFile == null) {
            Log.w(TAG, "(generateUri) createPictureFile error");
            return null;
        }
        String originPath = getRealPathFromUri(context, sourceUri);
        Log.i(TAG, String.format("(generateUri) \n\toutputFile: %s\n\toriginPath: %s", outputFile, originPath));

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        Bitmap bitmap = decodeBitmap(originPath, bmOptions);
        if (bitmap == null) {
            Log.w(TAG, "(generateUri) decodeFile failed");
            return null;
        }
        try {
            OutputStream outputStream = Files.newOutputStream(outputFile.toPath());
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, String.format("(generateUri) compress bitmap to outputFile successfully. [%d]x[%d]",
                    bmOptions.outHeight, bmOptions.outWidth));
        } catch (Exception e) {
            Log.e(TAG, String.format("(generateUri) error, %s", e.getMessage()));
            e.printStackTrace();
        }
        Uri outputUri = FileProvider.getUriForFile(context, AUTHORITY, outputFile);
        Log.i(TAG, String.format("(generateUri) outputUri: %s", outputUri));
        return outputUri;
    }

    @Nullable
    public static Bitmap decodeBitmap(String fileName, BitmapFactory.Options options) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (IOException e) {
            Log.e(TAG, String.format("decodeBitmap %s error, %s", fileName, e.getMessage()));
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static File createTempFile(Context context) {
        try {
            String timeStamp = new SimpleDateFormat(FILE_NAME_FORMAT, Locale.CHINA).format(new Date());
            String imageFileName = FILE_PREFIX + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(imageFileName, FILE_SUFFIX, storageDir);
        } catch (Exception e) {
            Log.e(TAG, String.format("(createTempFile) failed, %s", e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    public static String getRealPathFromUri(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(
                    uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null,
                    null,
                    null
            );
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index != -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }

        // 经过上面转换得到真实路径之后,判断一下这个路径,如果还是为空的话,说明有可能文件存在于外置 sd 卡上,不是内置 sd 卡.
        if (!TextUtils.isEmpty(realPath)) {
            return realPath;
        }
        String uriString = uri.toString();
        String imageName = uriString.substring(uriString.lastIndexOf("/"));  // 截取文件名

        // 查看外部储存卡公共照片的文件
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, imageName);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        // 外置 sd 卡的应用缓存 file
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File sdcardFile = new File(storageDir, imageName);
        return sdcardFile.getAbsolutePath();
    }
}
