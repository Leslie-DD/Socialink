package com.leslie.socialink.activity.mine;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.listview.BaseInfoItemAdapter;
import com.leslie.socialink.base.PhotoBaseActivity;
import com.leslie.socialink.bean.ItemBean;
import com.leslie.socialink.bean.LableBean;
import com.leslie.socialink.entity.RefUserInfo;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.UserInfoBean;
import com.leslie.socialink.utils.PhotoUtils;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 个人基本资料页面
 */
public class BaseInfoActivity extends PhotoBaseActivity {
    private static final String TAG = "[BaseInfoActivity]";

    private final static int UPLOAD_HEAD = 1000;
    private final static int UPLOAD_NAME = 1001;
    private final static int UPDATE_SEX = 1002;
    private final static int UPDATE_SCHOOL = 1003;

    private WindowManager.LayoutParams windowLayoutParams;

    private ArrayList<ItemBean> data;
    private BaseInfoItemAdapter adapter;
    private ListView lv;
    private CircleView ivHead;
    private TextView tvTitle;
    private LinearLayout llHead;

    private PopupWindow modifyHeadPopWindow;
    private PopupWindow modifySexPopWindow;
    private PopupWindow modifyNamePopWindow;
    private PopupWindow modifySchoolPopWindow;

    private EditText etNameContent;
    private EditText etSchoolContent;

    private UserInfoBean userInfoBean;

    private String name;
    private String school;
    private int sex;

    private final Gson gson = new Gson();

    private OptionsPickerView<String> pvOptions;
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        init();
        event();
        windowLayoutParams = getWindow().getAttributes();
        screenWidth = Utils.getScreenWidth(this);

        initHeadModifyPopWindow();
        initSexModifyPopWindow();
        initNameModifyPopWindow();
        initSchoolModifyPopWindow();
    }

    private void init() {
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userinfobean");

        ivHead = findViewById(R.id.ivHead);
        llHead = findViewById(R.id.llHead);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("基本资料");
        initItemsView();
        lv = findViewById(R.id.lv);
        adapter = new BaseInfoItemAdapter(mContext, data);
        lv.setAdapter(adapter);
        if (userInfoBean.getHeader() != null && !userInfoBean.getHeader().isEmpty()) {
            Glide.with(mContext).load(Constants.BASE_URL + userInfoBean.getHeader()).asBitmap().into(ivHead);
        } else {
            Glide.with(mContext).load(R.mipmap.head2).asBitmap().into(ivHead);
        }
        getSchoolData();
    }

    private void initHeadModifyPopWindow() {
        modifyHeadPopWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        View content = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);

        TextView tvPic = content.findViewById(R.id.tvPic);
        tvPic.setText("拍照");
        tvPic.setTextColor(context.getColor(R.color.colorPrimary));
        tvPic.setOnClickListener(v -> {
            takePhoto();
            modifyHeadPopWindow.dismiss();
        });

        TextView tvUp = content.findViewById(R.id.tvUp);
        tvUp.setText("上传照片");
        tvUp.setTextColor(context.getColor(R.color.colorPrimary));
        tvUp.setOnClickListener(v -> {
            choosePhoto();
            modifyHeadPopWindow.dismiss();
        });
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifyHeadPopWindow.setBackgroundDrawable(new BitmapDrawable());
        modifyHeadPopWindow.setOutsideTouchable(true);
        modifyHeadPopWindow.setFocusable(true);
        modifyHeadPopWindow.setOnDismissListener(() -> {
            windowLayoutParams.alpha = 1f;
            getWindow().setAttributes(windowLayoutParams);
        });
        modifyHeadPopWindow.setContentView(content);
    }

    private void initSexModifyPopWindow() {
        modifySexPopWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        View content = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);

        TextView sexFemale = content.findViewById(R.id.tvPic);
        sexFemale.setText("女");
        sexFemale.setTextColor(context.getColor(R.color.light_gray));
        sexFemale.setOnClickListener(v -> {
            sex = 2;
            setBodyParams(new String[]{"sex"}, new String[]{"" + 2});
            sendPost(Constants.BASE_URL + "/api/user/update.do", UPDATE_SEX, Constants.token);
            modifySexPopWindow.dismiss();
        });

        TextView sexMale = content.findViewById(R.id.tvUp);
        sexMale.setText("男");
        sexMale.setTextColor(context.getColor(R.color.light_gray));
        sexMale.setOnClickListener(v -> {
            sex = 1;
            setBodyParams(new String[]{"sex"}, new String[]{"" + 1});
            sendPost(Constants.BASE_URL + "/api/user/update.do", UPDATE_SEX, Constants.token);
            modifySexPopWindow.dismiss();
        });

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifySexPopWindow.setBackgroundDrawable(new BitmapDrawable());
        modifySexPopWindow.setOutsideTouchable(true);
        modifySexPopWindow.setFocusable(true);
        modifySexPopWindow.setOnDismissListener(() -> {
            windowLayoutParams.alpha = 1f;
            getWindow().setAttributes(windowLayoutParams);
        });
        modifySexPopWindow.setContentView(content);
    }

    private void initNameModifyPopWindow() {
        modifyNamePopWindow = new PopupWindow(
                screenWidth - Utils.dip2px(context, 20),
                WindowManager.LayoutParams.WRAP_CONTENT
        );

        View content = LayoutInflater.from(mContext).inflate(R.layout.tklayout, null);
        content.findViewById(R.id.ivHead).setVisibility(View.GONE);
        content.findViewById(R.id.ivClose).setOnClickListener(view -> modifyNamePopWindow.dismiss());
        content.findViewById(R.id.btSave).setOnClickListener(v1 -> {
            String modifiedValue = etNameContent.getText().toString();
            if (modifiedValue.isEmpty()) {
                Utils.toastShort(mContext, "请先输入信息!");
                return;
            }
            name = modifiedValue;
            setBodyParams(new String[]{"nickname"}, new String[]{modifiedValue});
            sendPost(Constants.BASE_URL + "/api/user/update.do", UPLOAD_NAME, Constants.token);
            modifyNamePopWindow.dismiss();
        });
        TextView tvTip = content.findViewById(R.id.tvTip);
        tvTip.setText("修改昵称");
        etNameContent = content.findViewById(R.id.etContent);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifyNamePopWindow.setBackgroundDrawable(new BitmapDrawable());
        modifyNamePopWindow.setOutsideTouchable(true);
        modifyNamePopWindow.setFocusable(true);
        modifyNamePopWindow.setOnDismissListener(() -> {
            windowLayoutParams.alpha = 1f;
            getWindow().setAttributes(windowLayoutParams);
        });
        etNameContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 20) {
                    Utils.toastShort(mContext, "超过最长字符，无法继续输入");
                }
            }
        });
        modifyNamePopWindow.setContentView(content);
    }

    private void initSchoolModifyPopWindow() {
        modifySchoolPopWindow = new PopupWindow(
                screenWidth - Utils.dip2px(context, 20),
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        View content = LayoutInflater.from(mContext).inflate(R.layout.tklayout, null);
        content.findViewById(R.id.ivHead).setVisibility(View.GONE);
        content.findViewById(R.id.ivClose).setOnClickListener(view -> modifySchoolPopWindow.dismiss());
        content.findViewById(R.id.btSave).setOnClickListener(v1 -> {
            String modifiedValue = etNameContent.getText().toString();
            if (modifiedValue.isEmpty()) {
                Utils.toastShort(mContext, "请先输入信息!");
                return;
            }
            name = modifiedValue;
            data.get(2).setTip(name);
            adapter.notifyDataSetChanged();
            modifySchoolPopWindow.dismiss();
        });
        TextView tvTip = content.findViewById(R.id.tvTip);
        tvTip.setText("修改学校");
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifySchoolPopWindow.setBackgroundDrawable(new BitmapDrawable());
        modifySchoolPopWindow.setOutsideTouchable(true);
        modifySchoolPopWindow.setFocusable(true);
        modifySchoolPopWindow.setOnDismissListener(() -> {
            windowLayoutParams.alpha = 1f;
            getWindow().setAttributes(windowLayoutParams);
        });
        etSchoolContent = content.findViewById(R.id.etContent);
        etSchoolContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 20) {
                    Utils.toastShort(mContext, "超过最长字符，无法继续输入");
                }
            }
        });
        modifySchoolPopWindow.setContentView(content);
    }

    private void getSchoolData() {
        setBodyParams(new String[]{"type"}, new String[]{"school"});
        sendPost(Constants.BASE_URL + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void initItemsView() {
        data = new ArrayList<>();

        ItemBean bean = new ItemBean();
        bean.setName("昵称");
        bean.setTip(userInfoBean.getNickname());
        data.add(bean);

        bean = new ItemBean();
        bean.setName("性别");
        bean.setTip(userInfoBean.getSex() == 1 ? "男" : "女");
        data.add(bean);

        bean = new ItemBean();
        bean.setName("学校");
        bean.setTip(userInfoBean.getCollege());
        data.add(bean);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        llHead.setOnClickListener(v -> showModifyHeadPopWindow());
        lv.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    showModifyNamePop();
                    break;
                case 1:
                    showModifySexPopWindow();
                    break;
                case 2:
                    if (!TextUtils.isEmpty(userInfoBean.getCollege()) && !TextUtils.equals(userInfoBean.getCollege(), "null")) {
                        Utils.toastShort(mContext, "请联系管理员修改");
                        return;
                    }
                    if (pvOptions == null) {
                        Utils.toastShort(mContext, "数据获取中~~~");
                        return;
                    }
                    pvOptions.show();
                    break;
            }
        });
    }

    private void showModifyHeadPopWindow() {
        windowLayoutParams.alpha = 0.5f;
        getWindow().setAttributes(windowLayoutParams);
        modifyHeadPopWindow.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
    }

    private void showModifySexPopWindow() {
        windowLayoutParams.alpha = 0.5f;
        getWindow().setAttributes(windowLayoutParams);
        modifySexPopWindow.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
    }

    private void showModifyNamePop() {
        etNameContent.setText("");
        windowLayoutParams.alpha = 0.5f;
        getWindow().setAttributes(windowLayoutParams);
        modifyNamePopWindow.showAtLocation(tvTitle, Gravity.CENTER, 0, 0);
//
//        new AlertDialog.Builder(context)
//                .setTitle("退出登录")
//                .setMessage("确定退出当前账号吗")
//                .setNegativeButton("取消", (dialogInterface, i1) -> {
//                    dialogInterface.dismiss();//销毁对话框
//                })
//                .setPositiveButton("确定", (dialog1, which) -> {
//                    dialog1.dismiss();
//                    goLoginPage();
//                })
//                .create()
//                .show();
    }

    private void showModifySchoolPop() {
        etSchoolContent.setText("");
        windowLayoutParams.alpha = 0.5f;
        getWindow().setAttributes(windowLayoutParams);
        modifySchoolPopWindow.showAtLocation(tvTitle, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onPicCropSuccess(Uri uri) {
        String filePath = PhotoUtils.getRealPathFromUri(this, uri);
        File uploadFile = new File(filePath);
        setFileBodyParams(new String[]{"file"}, new File[]{uploadFile});
        sendPost(Constants.BASE_URL + "/api/user/update.do", UPLOAD_HEAD, Constants.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.d(TAG, String.format("http request %d onSuccess %s", where, result));
        switch (where) {
            case UPLOAD_HEAD:
                if (result.optInt("code") != 0) {
                    Utils.toastShort(this, result.optString("msg"));
                    break;
                }
                String userHead;
                try {
                    JSONObject data = result.getJSONObject("data");
                    userHead = data.getString("head");
                } catch (JSONException e) {
                    Log.e(TAG, "UPLOAD_HEAD success get data failed, " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
                if (TextUtils.isEmpty(userHead)) {
                    Utils.toastShort(this, "更新头像失败");
                    break;
                }

                Log.i(TAG, "UPLOAD_HEAD success " + Constants.BASE_URL + userHead);
                Glide.with(this).load(Constants.BASE_URL + userHead).asBitmap().into(ivHead);
                EventBus.getDefault().post(new RefUserInfo());
                break;
            case UPLOAD_NAME:
                if (result.optInt("code") == 0) {
                    data.get(0).setTip(name);
                    adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case UPDATE_SEX:
                if (result.optInt("code") == 0) {
                    if (sex == 1) {
                        data.get(1).setTip("男");
                    } else if (sex == 2) {
                        data.get(1).setTip("女");
                    }
                    adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case UPDATE_SCHOOL:
                if (result.optInt("code") == 0) {
                    data.get(2).setTip(school);
                    adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case 10086:
                if (result.optInt("code") == 0) {
                    ArrayList<LableBean> datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LableBean>>() {
                    }.getType());
                    final ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < datas.size(); i++) {
                        data.add(datas.get(i).getValue());
                    }
                    pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
                        school = data.get(options1);
                        setBodyParams(new String[]{"college"}, new String[]{school});
                        sendPost(Constants.BASE_URL + "/api/user/update.do", UPDATE_SCHOOL, Constants.token);
                    }).setSubmitText("确定")//确定按钮文字
                            .setCancelText("取消")//取消按钮文字
                            .setTitleText("选择学校")//标题
                            .setSubCalSize(18)//确定和取消文字大小
                            .setTitleSize(20)//标题文字大小
                            .setTitleColor(Color.BLACK)//标题文字颜色
                            .setSubmitColor(getResources().getColor(R.color.colorPrimary, null))//确定按钮文字颜色
                            .setCancelColor(getResources().getColor(R.color.colorPrimary, null))//取消按钮文字颜色
                            .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                            .setBgColor(0xFFFFFFFF)//滚轮背景颜色
                            .setContentTextSize(18)//滚轮文字大小
                            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                            .setOutSideCancelable(true)//点击外部dismiss default true
                            .isDialog(false)//是否显示为对话框样式
                            .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                            .build();
                    pvOptions.setPicker(data);//添加数据源
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
        }

    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络错误");
    }


}
