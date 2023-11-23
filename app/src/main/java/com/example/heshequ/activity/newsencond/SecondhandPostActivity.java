package com.example.heshequ.activity.newsencond;

import android.annotation.SuppressLint;
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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.heshequ.R;
import com.example.heshequ.adapter.listview.GwPictureAdapter;
import com.example.heshequ.base.PhotoBaseActivity;
import com.example.heshequ.classification.ClassificationBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.PhotoUtils;
import com.example.heshequ.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SecondhandPostActivity extends PhotoBaseActivity {
    private static final String TAG = "[SecondhandPostActivity]";
    private TextView tvTitle;

    // 商品类型选择
    int categoryId1;
    int categoryId2;
    private TextView tvCategory1;
    private TextView tvCategory2;
    ClassificationBean classificationBean;
    OptionsPickerView pvOptions;    // 分类选择器
    private final ArrayList<String> options1ItemsString = new ArrayList<>();
    private final ArrayList<ArrayList<String>> options2ItemsString = new ArrayList<>();

    //商品的描述
    private EditText goodDescription;
    private EditText goodPrice;
    private List<String> strings;
    private GwPictureAdapter gwPictureAdapter;
    private PopupWindow popView;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private List<File> fileList;
    //展示图片
    private ImageView ivNm;

    //last
    private int niming = 0;

    private PromptDialog promptDialog;


    @SuppressLint("LongLogTag")
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 10086) {
            String json2 = result.toString();
            classificationBean = com.alibaba.fastjson.JSONObject.parseObject(json2, ClassificationBean.class);
            for (int i = 0; i < classificationBean.getData().size(); i++) {
                String category1Name = classificationBean.getData().get(i).getCategory1Name();
                options1ItemsString.add(category1Name);
                ArrayList<String> category2ListString = new ArrayList<>();
                for (int j = 0; j < classificationBean.getData().get(i).getCategory2List().size(); j++) {
                    String category2Name = classificationBean.getData().get(i).getCategory2List().get(j).getCategory2Name();
                    category2ListString.add(category2Name);
                }
                options2ItemsString.add(category2ListString);
            }
            categoryId1 = classificationBean.getData().get(0).getCategory1Id();
            categoryId2 = classificationBean.getData().get(0).getCategory2List().get(0).getCategory2Id();
            tvCategory1.setText(options1ItemsString.get(0));
            tvCategory2.setText(options2ItemsString.get(0).get(0));
            initPickerView();
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondhandpost);
        init();
        initPop();
    }

    private void init() {
        findViewById(R.id.llClassify).setOnClickListener(v -> pvOptions.show());
        findViewById(R.id.tvCancel).setOnClickListener(v -> finish());
        findViewById(R.id.tvSave).setOnClickListener(v -> onSave());
        findViewById(R.id.llSelect).setOnClickListener(v -> {
            if (niming == 0) {
                niming = 1;
                ivNm.setImageResource(R.mipmap.selected2);
            } else {
                niming = 0;
                ivNm.setImageResource(R.mipmap.unselected);
            }
        });

        fileList = new ArrayList<>();
        strings = new ArrayList<>();
        strings.add("");

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("发布商品");
        tvCategory1 = findViewById(R.id.tv_category1);
        tvCategory2 = findViewById(R.id.tv_category2);
        goodDescription = findViewById(R.id.gooddescribe);
        goodPrice = findViewById(R.id.goodprice);
        ivNm = findViewById(R.id.ivNm);
        GridView gridView = findViewById(R.id.gw);
        gwPictureAdapter = new GwPictureAdapter(this);
        gridView.setAdapter(gwPictureAdapter);
        gwPictureAdapter.setData(strings);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == strings.size() - 1) {
                if (strings.size() == 10) {
                    Utils.toastShort(mContext, "最多添加9张图片");
                    return;
                }
                showPop();
            }
        });
        gridView.setOnItemLongClickListener((parent, view, position, id) -> false);

        goodDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 32) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
        getCategory();
    }

    private void getCategory() {
        sendPost(WenConstans.SecondhandClassify, 10086, Constants.token);
    }

    private void showPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        popView.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
    }

    private void initPop() {
        popView = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        pv.findViewById(R.id.tvPic).setOnClickListener(v -> {
            path = PhotoUtils.startPhoto(this);
            popView.dismiss();
        });
        // 上传照片时选项中的文字
        pv.findViewById(R.id.tvUp).setOnClickListener(v -> {
            PhotoUtils.choosePhoto(202, this);
            popView.dismiss();
        });
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        popView.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        popView.setOutsideTouchable(true);
        // 设置焦点
        popView.setFocusable(true);
        popView.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });
        // 设置所在布局
        popView.setContentView(pv);
    }


    private void initPickerView() {
        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            categoryId1 = classificationBean.getData().get(options1).getCategory1Id();
            categoryId2 = classificationBean.getData().get(options1).getCategory2List().get(options2).getCategory2Id();
            String categoryName1 = classificationBean.getData().get(options1).getCategory1Name();
            String categoryName2 = classificationBean.getData().get(options1).getCategory2List().get(options2).getCategory2Name();
            tvCategory1.setText(categoryName1);
            tvCategory2.setText(categoryName2);
        }).setSubmitText("确定")  // 确定按钮文字
                .setCancelText("取消")    // 取消按钮文字
                .setTitleText("选择类别") // 标题
                .setSubCalSize(18)  // 确定和取消文字大小
                .setTitleSize(20)   // 标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#00BBFF"))    // 确定按钮文字颜色
                .setCancelColor(Color.parseColor("#00BBFF"))    // 取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)    // 标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF) // 滚轮背景颜色
                .setContentTextSize(18) // 滚轮文字大小
                .isCenterLabel(false)   // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(true) // 点击外部dismiss default true
                .isDialog(false)    // 是否显示为对话框样式
                .isRestoreItem(true)// 切换时是否还原，设置默认选中第一项。
                .build();

        pvOptions.setPicker(options1ItemsString, options2ItemsString);

    }

    private void onSave() {
        String content = goodDescription.getText().toString();
        String temp = goodPrice.getText().toString();

        if (TextUtils.isEmpty(content)) {
            Utils.toastShort(mContext, "内容不能为空");
            return;
        }
        if (fileList.size() == 0) {
            Utils.toastShort(mContext, "图片不能为空");
            return;
        }
        if (temp.equals("")) {
            Utils.toastShort(mContext, "请填写价格");
            return;
        }
        if (content.length() > 2000) {
            Utils.toastShort(mContext, "内容最多2000个字符");
            return;
        }
        promptDialog = new PromptDialog(this);
        promptDialog.showLoading("正在发布");
        OkHttpUtils.post(WenConstans.Sendgoods).tag(this)
                .headers(Constants.Token_Header, WenConstans.token)
                .params("price", temp + "")
                .params("content", content + "")
                .params("anonymity", niming + "")
                .params("category1Id", categoryId1)
                .params("category2Id", categoryId2)
                .addFileParams("files", fileList)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject result = new JSONObject(s);
                            if (result.optInt("code") == 0) {
                                Intent intent = new Intent();
                                intent.putExtra("item", 2);
                                intent.setAction("fragment.listener");
                                sendBroadcast(intent);
                                SecondhandPostActivity.this.finish();
                                Utils.toastShort(mContext, "发布成功");
                                promptDialog.dismiss();
                            } else {
                                Utils.toastShort(mContext, result.optString("msg"));
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Uri originalUri = data.getData();
                if (originalUri != null) {
                    path = PhotoUtils.getRealPathFromUri(SecondhandPostActivity.this, originalUri);
                    Log.i(TAG, "onActivityResult 202 path: " + path);
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
                        if (file.exists()) {
                            fileList.add(file);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }


}

