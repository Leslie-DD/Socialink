package com.leslie.socialink.activity.friend;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.login.LoginActivity;
import com.leslie.socialink.adapter.listview.GwPictureAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.FriendUserId;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.PhotoUtils;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class SendNew extends NetWorkActivity {
    private TextView tvCancle;
    private TextView tvTitle;
    private TextView tvSave;
    //用户id
    public static Integer id;
    //商品的描述
    private EditText NewDescribe;
    private List<String> strings;
    private GwPictureAdapter gwPictureAdapter;
    private GridView gw;
    private PopupWindow pop;
    //上传照片时选项中的文字
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private Uri photoUri;
    private List<File> fileList;
    //展示图片
    private ImageView ivNm;
    //last
    private int niming = 0;
    //用户id
    private FriendUserId bean;
    // 地点
    private String location = "";
    //时间
    private Date date;
    //图片字符串
    private String photoList;
    private Gson gson;
    private List<String> stringList;
    private boolean isCommit = true;


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 10086) {
            if (result.optInt("code") == 0) {


            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        }
    }

    private void setTvBg(TextView view, int status) {
        if (status == 0) {
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).equals(view.getText().toString())) {
                    stringList.remove(i);
                }
            }
        } else {
            stringList.add(view.getText().toString());
        }
        view.setBackgroundResource(status == 0 ? R.drawable.e6e6e6_17 : R.drawable.bg_2cd22b_17);
        String color = status == 0 ? "#999999" : "#ffffff";
        view.setTextColor(Color.parseColor(color));
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnew);
        init();
        initPop();
    }

    private void init() {


//        id = WenConstans.id;
        id = Constants.uid;
        String ids = id + "";
        Log.e("ids", ids + "");
        if (ids.equals("0")) {
            Intent intents = new Intent(SendNew.this, LoginActivity.class);

            startActivity(intents);
            Utils.toastShort(this, "我们需要验证您的身份");
        }
        bean = new FriendUserId();
        fileList = new ArrayList<>();
        strings = new ArrayList<>();
        strings.add("");
        gson = new Gson();
        stringList = new ArrayList<>();

        tvCancle = (TextView) findViewById(R.id.tvCancel);
        tvCancle.setOnClickListener(v -> finish());
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("发布动态");
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setOnClickListener(v -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date(System.currentTimeMillis());
            Log.e("thisisdate", "" + simpleDateFormat.format(date));
            String time = simpleDateFormat.format(date);
            String content = NewDescribe.getText().toString();

            if (TextUtils.isEmpty(content)) {
                Utils.toastShort(mContext, "内容不能为空");
                return;
            }
//                if (fileList.size()<=0){
//                    Utils.toastShort(mContext, "图片不能为空");
//                    return;
//                }
            if (content.length() > 2000) {
                Utils.toastShort(mContext, "内容最多2000个字符");
                return;
            }

            Log.e("showid", id + "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
            setBodyParams(new String[]{"uid", "content", "location", "photoList"}, new String[]{id + "", content + "", location + "", photoList});
            sendPost(WenConstans.SendNew, 100, WenConstans.token);

            SendNew.this.finish();
        });

        NewDescribe = (EditText) findViewById(R.id.NewDescribe);

        ivNm = (ImageView) findViewById(R.id.ivNm);
        gw = (GridView) findViewById(R.id.gw);
        gwPictureAdapter = new GwPictureAdapter(this);
        gw.setAdapter(gwPictureAdapter);
        gwPictureAdapter.setData(strings);
        gw.setOnItemClickListener((parent, view, position, id) -> {
            if (position == strings.size() - 1) {
                if (strings.size() == 10) {
                    Utils.toastShort(mContext, "最多添加9张图片");
                    return;
                }
                showPop();
            }
        });
        gw.setOnItemLongClickListener((parent, view, position, id) -> false);

        NewDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("动态发布长度<", s.length() + ">");
                if (s.length() >= 1024) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
    }

    private void showPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                break;
            case 202:
                if (data != null) {
                    photoUri = null;
                    photoUri = data.getData();
                    try {
                        ContentResolver resolver = this.getContentResolver();
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
                }
                break;
        }
        strings.add(strings.size() - 1, path);
        gwPictureAdapter.setData(strings);
        Luban.with(this).load(new File(path)).
                setCompressListener(new OnCompressListener() {
                    //                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (file.exists()) {
                            fileList.add(file);
                        } else {
                            Log.e("ss", "andoahdpajpdjaspodas[ojagpoagingp[nap[inaphnpahpajhpjash[");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }


}
