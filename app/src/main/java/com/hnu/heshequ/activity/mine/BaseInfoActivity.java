package com.hnu.heshequ.activity.mine;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.login.LabelSelectionActivity;
import com.hnu.heshequ.adapter.listview.BaseInfoItemAdapter;
import com.hnu.heshequ.base.PhotoBaseActivity;
import com.hnu.heshequ.bean.ItemBean;
import com.hnu.heshequ.bean.UserInfoBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefUserInfo;
import com.hnu.heshequ.utils.PhotoUtils;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 个人基本资料页面
 */

public class BaseInfoActivity extends PhotoBaseActivity  {
    private static final String TAG = "[BaseInfoActivity]";

    private final static int UPLOAD_HEAD = 1000;
    private final static int UPLOAD_NAME = 1001;

    private ArrayList<ItemBean> data;
    private BaseInfoItemAdapter adapter;
    private ListView lv;
    private CircleView ivHead;
    private TextView tvTitle;
    private LinearLayout llHead;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private int status;
    private WindowManager.LayoutParams layoutParams;
    private PopupWindow modifyPop;
    private TextView tvTip;
    private EditText etContent;
    private int popStatus;
    private UserInfoBean userInfoBean;
    private Uri takePhotoUri;
    private String path;
    private String name;
    private final int upSex = 1002;
    private final int upSchool = 1003;
    private String school;
    private int sex;
    private SharedPreferences sp;
    private Gson gson;
    private OptionsPickerView<String> pvOptions;
    private Uri uriTempFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        init();
        event();
        initPop();
    }

    private void init() {
        sp = MeetApplication.getInstance().getSharedPreferences();
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userinfobean");

        ivHead = findViewById(R.id.ivHead);
        llHead = findViewById(R.id.llHead);
        Glide.with(mContext).load(Constants.url5).asBitmap().into(ivHead);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("基本资料");
        getData();
        lv = findViewById(R.id.lv);
        adapter = new BaseInfoItemAdapter(mContext, data);
        lv.setAdapter(adapter);
        if (userInfoBean.getHeader() != null && !userInfoBean.getHeader().isEmpty()) {
            Glide.with(mContext).load(Constants.base_url + userInfoBean.getHeader()).asBitmap().into(ivHead);
        } else {
            Glide.with(mContext).load(R.mipmap.head2).asBitmap().into(ivHead);
        }
        getSchoolData();
    }

    private void initPop() {
        gson = new Gson();
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        tvPic = pv.findViewById(R.id.tvPic);
        tvUp = pv.findViewById(R.id.tvUp);
        tvPic.setOnClickListener(v -> {
            // 拍照
            if (status == 0) {
                takePhoto();
            } else {  // 选择女
                sex = 2;
                setBodyParams(new String[]{"sex"}, new String[]{"" + 2});
                sendPost(Constants.base_url + "/api/user/update.do", upSex, Constants.token);
            }
            pop.dismiss();
        });
        tvUp.setOnClickListener(v -> {
            // 相册选择
            if (status == 0) {
                choosePhoto();
            } else {  // 选择男
                sex = 1;
                setBodyParams(new String[]{"sex"}, new String[]{"" + 1});
                sendPost(Constants.base_url + "/api/user/update.do", upSex, Constants.token);
            }
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
        modifyPop = new PopupWindow(Constants.screenW - Utils.dip2px(mContext, 80), WindowManager.LayoutParams.WRAP_CONTENT);
        View v = LayoutInflater.from(mContext).inflate(R.layout.tklayout, null);
        v.findViewById(R.id.ivHead).setVisibility(View.GONE);
        tvTip = v.findViewById(R.id.tvTip);
        Button btSave = v.findViewById(R.id.btSave);
        etContent = v.findViewById(R.id.etContent);
        v.findViewById(R.id.ivClose).setOnClickListener(view -> modifyPop.dismiss());
        btSave.setOnClickListener(v1 -> {
            String content = etContent.getText().toString();
            if (content.isEmpty()) {
                Utils.toastShort(mContext, "请先输入信息!");
                return;
            }
            // 修改名字
            if (popStatus == 0) {
                name = content;
                setBodyParams(new String[]{"nickname"}, new String[]{content});
                sendPost(Constants.base_url + "/api/user/update.do", UPLOAD_NAME, Constants.token);
            }
            // 修改学校
            if (popStatus == 1) {
                name = content;
                sp.edit().putString(Constants.uid + "school", name).apply();
                data.get(2).setTip(name);
                adapter.notifyDataSetChanged();
            }
            modifyPop.dismiss();
        });
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifyPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        modifyPop.setOutsideTouchable(true);
        // 设置焦点
        modifyPop.setFocusable(true);
        modifyPop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
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
                if (s.length() >= 20) {
                    Utils.toastShort(mContext, "超过最长字符，无法继续输入");
                }
            }
        });
        modifyPop.setContentView(v);
    }

    private void getSchoolData() {
        setBodyParams(new String[]{"type"}, new String[]{"school"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void getData() {
        data = new ArrayList<>();
        ItemBean bean = new ItemBean();
        bean.setName("昵称");
        bean.setTip(userInfoBean.getNickname());
        data.add(bean);
        bean = new ItemBean();
        bean.setName("性别");
        if (userInfoBean.getSex() == 1) {
            bean.setTip("男");
        } else {
            bean.setTip("女");
        }
        data.add(bean);
        bean = new ItemBean();
        bean.setName("学校");
        bean.setTip(userInfoBean.getCollege());
        data.add(bean);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        llHead.setOnClickListener(v -> showPop(0));
        lv.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    showModifyPop(0);
                    break;
                case 1:
                    showPop(1);
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

    private void showPop(int status) {
        this.status = status;
        tvPic.setText(status == 0 ? "拍照" : "女");
        tvUp.setText(status == 0 ? "上传照片" : "男");
        tvPic.setTextColor(Color.parseColor(status == 0 ? "#00bbff" : "#333333"));
        tvUp.setTextColor(Color.parseColor(status == 0 ? "#00bbff" : "#333333"));
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
    }

    private void showModifyPop(int status) {
        this.popStatus = status;
        etContent.setText("");
        if (status == 0) {
            tvTip.setText("修改昵称");
        } else {
            tvTip.setText("修改学校");
        }
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        modifyPop.showAtLocation(tvTitle, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onPicCropSuccess(Uri uri) {
        String filePath = PhotoUtils.getRealPathFromUri(this, uri);
        File uploadFile = new File(filePath);
        setFileBodyParams(new String[]{"file"}, new File[]{uploadFile});
        sendPost(Constants.base_url + "/api/user/update.do", UPLOAD_HEAD, Constants.token);
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

                Log.i(TAG, "UPLOAD_HEAD success " + Constants.base_url + userHead);
                Glide.with(this).load(Constants.base_url + userHead).asBitmap().into(ivHead);
                EventBus.getDefault().post(new RefUserInfo());
                break;
            case UPLOAD_NAME:
                if (result.optInt("code") == 0) {
                    if (popStatus == 0) {
                        data.get(0).setTip(name);
                    }
                    adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case upSex:
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
            case 10086:
                if (result.optInt("code") == 0) {
                    ArrayList<LabelSelectionActivity.LableBean> datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LabelSelectionActivity.LableBean>>() {
                    }.getType());
                    final ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < datas.size(); i++) {
                        data.add(datas.get(i).getValue());
                    }
                    pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
                        school = data.get(options1);
                        setBodyParams(new String[]{"college"}, new String[]{school});
                        sendPost(Constants.base_url + "/api/user/update.do", upSchool, Constants.token);
                    }).setSubmitText("确定")//确定按钮文字
                            .setCancelText("取消")//取消按钮文字
                            .setTitleText("选择学校")//标题
                            .setSubCalSize(18)//确定和取消文字大小
                            .setTitleSize(20)//标题文字大小
                            .setTitleColor(Color.BLACK)//标题文字颜色
                            .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                            .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
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
            case upSchool:
                if (result.optInt("code") == 0) {
                    data.get(2).setTip(school);
                    adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new RefUserInfo());
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
