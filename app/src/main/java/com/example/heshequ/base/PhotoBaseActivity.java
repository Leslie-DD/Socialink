package com.example.heshequ.base;


import static com.example.heshequ.utils.PermissionHelper.getPhotoPermissions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.example.heshequ.utils.PhotoUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 实现图片选择、拍照和裁剪功能
 */
public class PhotoBaseActivity extends NetWorkActivity {
    private static final String TAG = "[PhotoBaseActivity]";

    private static final int PHOTO_PERMISSIONS_REQUEST = 200;

    private static final int PHOTO_REQUEST_CUT = 300;
    private static final int PHOTO_REQUEST_CHOOSER = 301;
    private static final int PHOTO_REQUEST_TAKE_PHOTO = 302;

    private Uri takePhotoUri;
    private Uri cropPhotoUri;

    protected void choosePhoto() {
        requestPermission(getPhotoPermissions(), PHOTO_PERMISSIONS_REQUEST, new IPermissionsRequestListener() {
            @Override
            public void onAllow() {
                PhotoUtils.choosePhoto(PHOTO_REQUEST_CHOOSER, PhotoBaseActivity.this);
            }

            @Override
            public void onReject() {
                ToastUtils.showShort("你拒绝了相关权限");
            }
        });
    }

    protected void takePhoto() {
        requestPermission(getPhotoPermissions(), PHOTO_PERMISSIONS_REQUEST, new IPermissionsRequestListener() {
            @Override
            public void onAllow() {
                takePhotoUri = PhotoUtils.takePhoto(PhotoBaseActivity.this, PHOTO_REQUEST_TAKE_PHOTO);
            }

            @Override
            public void onReject() {
                ToastUtils.showShort("你拒绝了相关权限");
            }
        });
    }

    protected void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 读写权限
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");    // 可裁剪
        intent.putExtra("aspectX", 1);      // 宽的比例
        intent.putExtra("aspectY", 1);      // 高的比例
        intent.putExtra("outputX", 400);    // 裁剪图片的宽
        intent.putExtra("outputY", 400);    // 裁剪图片的高
        intent.putExtra("circleCrop", true);
        intent.putExtra("return-data", true);


        intent.setDataAndType(uri, PhotoUtils.IMAGE_TYPE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        cropPhotoUri = uri;
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PHOTO_REQUEST_TAKE_PHOTO:
                // 拍照 path
                if (takePhotoUri != null) {
                    crop(takePhotoUri);
                    takePhotoUri = null;
                }
                break;
            case PHOTO_REQUEST_CHOOSER:
                if (intent != null) {
                    Uri photoUri = intent.getData();
                    crop(PhotoUtils.generateUri(PhotoBaseActivity.this.getApplicationContext(), photoUri));
                } else {
                    Log.w(TAG, "file chooser result is null");
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (intent == null) {
                    Log.w(TAG, "file chooser result intent is null");
                    break;
                }
                if (intent.getData() != null) {
                    Log.i(TAG, "file chooser result intent.getData is not null");
                    onPicCropSuccess(intent.getData());
                } else {
                    Log.w(TAG, "file chooser result intent.getData is null");
                    onPicCropSuccess(cropPhotoUri);
                    cropPhotoUri = null;
                }
                break;
        }
    }

    protected void onPicCropSuccess(Uri uri) {
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
    }

    @Override
    protected void onFailure(String result, int where) {
    }
}
