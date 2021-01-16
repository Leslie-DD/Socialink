package com.example.heshequ.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.login.LabelSelectionActivity;
import com.example.heshequ.adapter.listview.ItemAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ItemBean;
import com.example.heshequ.bean.UserInfoBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefUserInfo;
import com.example.heshequ.utils.FileUtilcll;
import com.example.heshequ.utils.PhotoUtils;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 个人基本资料页面
 */

public class BaseInfoActivity extends NetWorkActivity implements View.OnClickListener {
    private final int PHOTO_REQUEST_CUT = 300;
    private ArrayList<ItemBean> data;
    private ItemAdapter adapter;
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
    private Button btSave;
    private EditText etContent;
    private int popStatus;
    private ImageView ivClose;
    private UserInfoBean userInfoBean;
    private String path;
    private Uri photoUri;
    private final int upHead = 1000;
    private final int upName = 1001;
    private String name;
    private final int upSex = 1002;
    private final int upSchool = 1003;
    private String school;
    private int sex;
    private SharedPreferences sp;
    private Gson gson;
    private OptionsPickerView pvOptions;
    private ArrayList<LabelSelectionActivity.LableBean> datas;
    private Uri uritempFile;

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

        ivHead = (CircleView) findViewById(R.id.ivHead);
        llHead = (LinearLayout) findViewById(R.id.llHead);
        Glide.with(mContext).load(Constants.url5).asBitmap().into(ivHead);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("基本资料");
        getData();
        lv = (ListView) findViewById(R.id.lv);
        adapter = new ItemAdapter(mContext, data);
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
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        // 设置所在布局
        pop.setContentView(pv);
        modifyPop = new PopupWindow(Constants.screenW - Utils.dip2px(mContext, 80), WindowManager.LayoutParams.WRAP_CONTENT);
        View v = LayoutInflater.from(mContext).inflate(R.layout.tklayout, null);
        v.findViewById(R.id.ivHead).setVisibility(View.GONE);
        tvTip = (TextView) v.findViewById(R.id.tvTip);
        btSave = (Button) v.findViewById(R.id.btSave);
        etContent = (EditText) v.findViewById(R.id.etContent);
        ivClose = (ImageView) v.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
        btSave.setOnClickListener(this);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifyPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        modifyPop.setOutsideTouchable(true);
        // 设置焦点
        modifyPop.setFocusable(true);
        modifyPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
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
                if (s.length() >= 20) {
                    Utils.toastShort(mContext, "超过最长字符，无法继续输入");
                }
            }
        });
        // 设置所在布局
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
        findViewById(R.id.ivBack).setOnClickListener(this);
        llHead.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        showModifyPop(0);
                        break;
                    case 1:
                        showPop(1);
                        break;
                    case 2:
                        if (!TextUtils.isEmpty(userInfoBean.getCollege())) {
                            return;
                        }
                        if (pvOptions == null) {
                            Utils.toastShort(mContext, "数据获取中~~~");
                            return;
                        }
                        pvOptions.show();
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ivClose:
                modifyPop.dismiss();
                break;
            case R.id.llHead:
                showPop(0);
                break;
            case R.id.tvUp:
                if (status == 0)//相册选择
                {
                    PhotoUtils.showFileChooser(202, this);
                } else {  //选择男
                    sex = 1;
                    setBodyParams(new String[]{"sex"}, new String[]{"" + 1});
                    sendPost(Constants.base_url + "/api/user/update.do", upSex, Constants.token);
                }
                pop.dismiss();
                break;
            case R.id.tvPic:
                if (status == 0)//拍照
                {
                    path = PhotoUtils.startPhoto(this);
                } else {  //选择女
                    sex = 2;
                    setBodyParams(new String[]{"sex"}, new String[]{"" + 2});
                    sendPost(Constants.base_url + "/api/user/update.do", upSex, Constants.token);
                }
                pop.dismiss();
                break;
            case R.id.btSave:
                String content = etContent.getText().toString();
                if (content.length() == 0) {
                    Utils.toastShort(mContext, "请先输入信息!");
                    return;
                }
                //修改名字
                if (popStatus == 0) {
                    name = content;
                    setBodyParams(new String[]{"nickname"}, new String[]{content});
                    sendPost(Constants.base_url + "/api/user/update.do", upName, Constants.token);
                }
                //修改学校
                if (popStatus == 1) {
                    name = content;
                    sp.edit().putString(Constants.uid + "school", name).apply();
                    data.get(2).setTip(name);
                    adapter.notifyDataSetChanged();
                }
                modifyPop.dismiss();
                break;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                //拍照  path
                File temp = new File(path);
                crop(Uri.fromFile(temp));
                break;
            case 202:
                if (data != null) {
                    photoUri = null;
                    photoUri = data.getData();
                    crop(photoUri);
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }


        if (requestCode == PHOTO_REQUEST_CUT) {
            Luban.with(this).load(new File(path)).
                    setCompressListener(new OnCompressListener() {
                        //                    @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onSuccess(File file) {
                            setBodyParams(new String[]{"file"}, new File[]{file});
                            sendPost(Constants.base_url + "/api/user/update.do", upHead, Constants.token);
                        }

                    }).launch();
        }
    }

    /*
     * 剪切图片
	 */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        if (Build.MANUFACTURER.equals("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", Utils.dip2px(this, 75));
        intent.putExtra("outputY", Utils.dip2px(this, 75));
        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "XiangYuIcon.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        try {
            Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
            path = FileUtilcll.saveFile(BaseInfoActivity.this, "temphead.jpg", photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case upHead:
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_changeIcon");
                if (result.optInt("code") == 0) {
                    Glide.with(this).load(path).asBitmap().into(ivHead);
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case upName:
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
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_changeSex");
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
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_changeSchool");
                if (result.optInt("code") == 0) {
                    datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LabelSelectionActivity.LableBean>>() {
                    }.getType());
                    final ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < datas.size(); i++) {
                        data.add(datas.get(i).getValue());
                    }
                    pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
                            school = data.get(options1);
                            setBodyParams(new String[]{"college"}, new String[]{school});
                            sendPost(Constants.base_url + "/api/user/update.do", upSchool, Constants.token);
                        }
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
