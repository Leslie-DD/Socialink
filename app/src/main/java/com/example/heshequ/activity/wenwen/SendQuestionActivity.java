package com.example.heshequ.activity.wenwen;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.PermissionUtils;
import com.example.heshequ.R;
import com.example.heshequ.activity.login.LabelSelectionActivity;
import com.example.heshequ.adapter.listview.GwPictureAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.entity.TestBean;
import com.example.heshequ.utils.PhotoUtils;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.FlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Hulk_Zhang on 2018/5/31 11:12
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class SendQuestionActivity extends NetWorkActivity implements View.OnClickListener {
    private static final String TAG = "[SendQuestionActivity]";
    private TextView tvCancle;
    private TextView tvTitle;
    private TextView tvSave;
    private TextView tvBelong;
    private EditText etTitle;
    private EditText etContent;
    private FlowLayout flow_layout;
    private ImageView ivNm;
    private GridView gw;
    private List<String> strings, bqList;
    private GwPictureAdapter gwPictureAdapter;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private Uri photoUri;
    private List<File> fileList;
    private File[] fileArray;
    private int niming;
    private LinearLayout llSelect;
    private ArrayList<LabelSelectionActivity.LableBean> datas;
    private Gson gson;
    private ArrayList<TestBean> testData;
    private List<String> stringList;
    private boolean isCommit = true;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 10086) {
            if (result.optInt("code") == 0) {
                datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LabelSelectionActivity.LableBean>>() {
                }.getType());
                if (datas != null && datas.size() > 0) {
                    for (LabelSelectionActivity.LableBean b : datas) {
                        // 循环添加TextView到容器
                        TestBean bean = new TestBean();
                        bean.setName(b.getValue());
                        testData.add(bean);
                        final TextView view = new TextView(this);
                        view.setText(b.getValue());
                        view.setTextColor(Color.parseColor("#999999"));
                        view.setHeight(Utils.dip2px(context, 34));
                        view.setPadding(Utils.dip2px(context, 17), 0, Utils.dip2px(context, 17), 0);
                        view.setGravity(Gravity.CENTER);
                        view.setTextSize(14);
                        view.setBackgroundResource(R.drawable.e6e6e6_17);
                        view.setTag(b.getValue());
                        // 设置点击事件
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String text = view.getText().toString();
                                int index = -1;
                                for (int j = 0; j < testData.size(); j++) {
                                    TestBean bean = testData.get(j);
                                    if (bean.getName().equals(text)) {
                                        bean.setStatus(Math.abs(bean.getStatus() - 1));
                                        index = j;
                                        break;
                                    }
                                }
                                setTvBg(view, testData.get(index).getStatus());
                                if (testData.get(index).getStatus() == 1) {
                                    bqList.add(text);
                                } else {
                                    bqList.remove(text);
                                }

                                if (bqList.size() > 0) {
                                    String str = "";
                                    for (int i = 0; i < bqList.size(); i++) {
                                        str = str + "#" + bqList.get(i) + "#  ";
                                    }
                                    tvBelong.setVisibility(View.VISIBLE);
                                    tvBelong.setText(str);
                                } else {
                                    tvBelong.setVisibility(View.GONE);
                                }

                            }
                        });
                        flow_layout.addView(view);
                    }
                }
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_question);
        init();
        initPop();
        requestCAMERA();
    }

    private void init() {
        fileList = new ArrayList<>();
        strings = new ArrayList<>();
        bqList = new ArrayList<>();
        strings.add("");
        gson = new Gson();
        stringList = new ArrayList<>();
        testData = new ArrayList<TestBean>();
        llSelect = (LinearLayout) findViewById(R.id.llSelect);
        llSelect.setOnClickListener(this);
        tvCancle = (TextView) findViewById(R.id.tvCancel);
        tvCancle.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("发布问题");
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setOnClickListener(this);
        tvBelong = (TextView) findViewById(R.id.tvBelong);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        flow_layout = findViewById(R.id.flow_layout);
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

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("SendQuestionActivity长度<", s.length() + ">");
                if (s.length() >= 100) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("SendQuestionActivity长度<", s.length() + ">");
                if (s.length() >= 2000) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
        getLabel();
    }

    private void getLabel() {
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        tvPic = (TextView) pv.findViewById(R.id.tvPic);
        tvUp = (TextView) pv.findViewById(R.id.tvUp);
        tvPic.setOnClickListener(this);
        tvUp.setOnClickListener(this);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.tvSave:
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Utils.toastShort(mContext, "标题不能为空");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    Utils.toastShort(mContext, "内容不能为空");
                    return;
                }
                if (bqList.size() == 0) {
                    Utils.toastShort(mContext, "标签不能为空");
                    return;
                }
                if (title.length() > 100) {
                    Utils.toastShort(mContext, "标题最多100个字符");
                    return;
                }
                if (content.length() > 5000) {
                    Utils.toastShort(mContext, "内容最多5000个字符");
                    return;
                }
                String biaoqain = "";
                for (int i = 0; i < bqList.size(); i++) {
                    biaoqain = biaoqain + bqList.get(i) + ",";
                }
                biaoqain.substring(0, biaoqain.length() - 1);
                tvSave.setClickable(false);
                OkHttpUtils.post(WenConstans.SendQuestion)
                        .tag(this)
                        .headers(Constants.Token_Header, WenConstans.token)
                        .params("name", title + "")
                        .params("content", content + "")
                        .params("labels", biaoqain + "")
                        .params("anonymity", niming + "")
                        .addFileParams("files", fileList)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.e(TAG, "sresult:" + s);
                                tvSave.setClickable(false);
                                try {
                                    JSONObject result = new JSONObject(s);
                                    Log.e(TAG, "code:" + result.optInt("code"));
                                    if (result.optInt("code") == 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("item", 2);
                                        intent.setAction("fragment.listener");
                                        sendBroadcast(intent);
                                        SendQuestionActivity.this.finish();
                                    } else {
                                        Utils.toastShort(mContext, result.optString("msg"));
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSONException: " + e.getMessage());
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                tvSave.setClickable(false);
                                isCommit = true;
                                super.onError(call, response, e);
                                Log.e(TAG, "onError Exception: " + e.toString());
                            }
                        });
                break;
            case R.id.tvUp:
                PhotoUtils.choosePhoto(202, this);
                pop.dismiss();
                break;
            case R.id.tvPic:
                ActivityCompat.requestPermissions(
                        context,
                        new String[]{Manifest.permission_group.CAMERA},
                        100
                );
                path = PhotoUtils.startPhoto(this);
                pop.dismiss();
                break;
            case R.id.llSelect:
                if (niming == 0) {
                    niming = 1;
                    ivNm.setImageResource(R.mipmap.selected2);
                } else {
                    niming = 0;
                    ivNm.setImageResource(R.mipmap.unselected);
                }
                break;
        }
    }

    /**
     * 请求相机权限
     */
    public void requestCAMERA() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (PermissionUtils.isGranted(Manifest.permission.CAMERA)) {
            return;
        }
        requestPermissions(new String[]{
                Manifest.permission.CAMERA
        }, 104);
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
        view.setBackgroundResource(status == 0 ? R.drawable.e6e6e6_17 : R.drawable.bg_00bbff_17);
        String color = status == 0 ? "#999999" : "#ffffff";
        view.setTextColor(Color.parseColor(color));
    }

    private void showPop() {
        if (PermissionUtils.isGranted(Manifest.permission.CAMERA)) {
            layoutParams.alpha = 0.5f;
            getWindow().setAttributes(layoutParams);
            pop.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
        } else {
            requestCAMERA();
        }
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
                break;
        }
        strings.add(strings.size() - 1, path);
        gwPictureAdapter.setData(strings);
        Luban.with(this).load(new File(path)).
                setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        fileList.add(file);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }


}
