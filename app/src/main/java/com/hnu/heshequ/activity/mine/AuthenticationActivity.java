package com.hnu.heshequ.activity.mine;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.entity.RefUserInfo;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.utils.PhotoUtils;
import com.hnu.heshequ.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 实名认证
 */
public class AuthenticationActivity extends NetWorkActivity {
    private LinearLayout llTip;
    private TextView tvTip;
    private EditText etName, etCard;
    private ImageView ivPositive, ivNegative, ivTip;
    private Button btCheck;
    private int status;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private int type;
    private String path;
    private Uri photoUri;
    private ArrayList<File> files;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uthentication);
        status = getIntent().getIntExtra("certFlag", 0);
        init();
        event();
        setUi();
        initPop();
    }


    private void init() {
        setText("实名认证");
        llTip = (LinearLayout) findViewById(R.id.llTip);
        tvTip = (TextView) findViewById(R.id.tvTip);
        etName = (EditText) findViewById(R.id.etName);
        etCard = (EditText) findViewById(R.id.etCard);
        ivPositive = (ImageView) findViewById(R.id.ivPositive);
        ivNegative = (ImageView) findViewById(R.id.ivNegative);
        ivTip = (ImageView) findViewById(R.id.ivTip);
        btCheck = (Button) findViewById(R.id.btCheck);
        files = new ArrayList<>();
        files.add(null);
        files.add(null);
    }

    private void setUi() {
        llTip.setVisibility(status == 0 ? View.GONE : View.VISIBLE);
        switch (status) {
            case 1:
                ivTip.setImageResource(R.mipmap.wait);
                tvTip.setText("认证已提交,正在审核中...");
                break;
            case 2:
                ivTip.setImageResource(R.mipmap.wait);
                tvTip.setText("认证未通过，请稍后。准备重新认证中~~~");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llTip.setVisibility(View.GONE);
                    }
                }, 3000);
                break;
            case 3:
                ivTip.setImageResource(R.mipmap.review);
                tvTip.setText("审核已通过");
                break;
        }
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        btCheck.setOnClickListener(v -> onBtCheckClick());
        ivPositive.setOnClickListener(v -> showPop(0));
        ivNegative.setOnClickListener(v -> showPop(1));
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        tvPic = (TextView) pv.findViewById(R.id.tvPic);
        tvUp = (TextView) pv.findViewById(R.id.tvUp);
        tvPic.setOnClickListener(v -> {
            path = PhotoUtils.startPhoto(this);
            pop.dismiss();
        });
        tvUp.setOnClickListener(v -> {
            PhotoUtils.choosePhoto(202, this);
            pop.dismiss();
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

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    private void onBtCheckClick() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Utils.toastShort(mContext, "请先输入真实姓名！");
            return;
        }

        String cardId = etCard.getText().toString().trim();
        if (cardId.isEmpty()) {
            Utils.toastShort(mContext, "请先输入证件号码！");
            return;
        }
        if (files.get(0) == null || files.get(1) == null) {
            Utils.toastShort(mContext, "请先选择学生证照片！");
            return;
        }

        OkHttpUtils.post(Constants.base_url + "/api/user/certification.do")
                .tag(this)
                .headers(Constants.Token_Header, Constants.token)
                .params("idcardNumber", "" + cardId)
                .params("name", "" + name)
                .addFileParams("files", files)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject result = new JSONObject(s);
                            switch (result.optInt("code")) {
                                case 0:
                                    AuthenticationActivity.this.finish();
                                    //发送刷新EventBus
                                    EventBus.getDefault().post(new RefUserInfo());
                                    Utils.toastShort(mContext, result.optString("msg"));
                                    finish();
                                    break;
                                case 1:
                                    Utils.toastShort(AuthenticationActivity.this, "您还没有登录或登录已过期，请重新登录");
                                    break;
                                case 2:
                                    Utils.toastShort(AuthenticationActivity.this, result.optString("msg"));
                                    break;
                                case 3:
                                    Utils.toastShort(AuthenticationActivity.this, "您没有该功能操作权限");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                });

        this.finish();
    }

    private void showPop(int type) {
        this.type = type;
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(llTip, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                break;
            case 202:
                if (data == null) {
                    break;
                }
                photoUri = null;
                photoUri = data.getData();
                try {
                    ContentResolver resolver = getContentResolver();
                    Uri originalUri = data.getData(); // 获得图片的uri
                    photoUri = originalUri;
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = resolver.query(originalUri, proj, null, null, null);
                    if (cursor == null) {
                        this.path = photoUri.getPath();
                    } else {
                        if (cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            this.path = cursor.getString(column_index);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        Glide.with(mContext).load(path).centerCrop().into(type == 0 ? ivPositive : ivNegative);
        Luban.with(this).load(new File(path)).
                setCompressListener(new OnCompressListener() {
                    //                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        files.set(type, new File(path));
                    }

                    @Override
                    public void onSuccess(File file) {
                        files.set(type, file);
                    }
                }).launch();
    }


}
