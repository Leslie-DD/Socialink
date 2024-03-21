package com.hnu.heshequ.activity.statement;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.ImagePreviewActivity;
import com.hnu.heshequ.adapter.listview.GwPictureAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.entity.DelEvent;
import com.hnu.heshequ.entity.RefTDteamEvent;
import com.hnu.heshequ.entity.TeamTestBean;
import com.hnu.heshequ.utils.PhotoUtils;
import com.hnu.heshequ.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ReleaseActivitiesActivity extends NetWorkActivity implements View.OnClickListener {
    private int type;
    private GridView gw;
    private List<String> strings;
    private GwPictureAdapter gwPictureAdapter;
    private TextView tvCancel;
    private EditText etTitle, etContent;
    private TextView tvSave;
    private TextView tvGetTime;
    private TextView tvCount, tvGetCount;
    private EditText etAddress;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private Uri photoUri;
    private List<File> fileList;
    //时间选择器
    private TimePickerView pvTime;
    private String time;
    private OptionsPickerView pvOptions;
    private List<Integer> options;
    private TeamTestBean.ObjBean bean;
    private int fristfiles;   //初始文件数
    private int[] filesid = new int[9];    //文件id数组
    private String delFileIds = ""; //
    private boolean isCommit = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_activities);
        EventBus.getDefault().register(this);
        init();
        event();
        initPop();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        tvGetTime = (TextView) findViewById(R.id.tvGetTime);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvGetCount = (TextView) findViewById(R.id.tvGetCount);
        etAddress = (EditText) findViewById(R.id.etAddress);
        gw = (GridView) findViewById(R.id.gw);
        strings = new ArrayList<>();
        strings.add("");
        gwPictureAdapter = new GwPictureAdapter(this);
        gw.setAdapter(gwPictureAdapter);
        gwPictureAdapter.setData(strings);
        fileList = new ArrayList<>();
        if (type == 1) {
            setText("发布活动");
            tvSave.setText("发表");
        } else if (type == 2) {
            setText("编辑活动");
            tvSave.setText("保存");
            bean = (TeamTestBean.ObjBean) getIntent().getSerializableExtra("bean");
            if (bean != null) {
                etTitle.setText(bean.getTitle());
                etTitle.setSelection(etTitle.getText().toString().trim().length());
                etContent.setText(bean.getContent());
                etAddress.setText(bean.getAddressName());
                tvGetTime.setText(bean.getApplyDeadline());
                time = bean.getApplyDeadline();
                tvCount.setText(bean.getLimitMember() + "");
                if (bean.getPhotos() != null && bean.getPhotos().size() > 0) {
                    fristfiles = bean.getPhotos().size();
                    for (int i = 0; i < bean.getPhotos().size(); i++) {
                        strings.add(strings.size() - 1, Constants.base_url + bean.getPhotos().get(i).getPhotoId());
                        gwPictureAdapter.setData(strings);
                        filesid[i] = bean.getPhotos().get(i).getId();

                    }
                }
            }
        }
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.set(startDate.get(Calendar.YEAR) + 2, startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DAY_OF_MONTH));
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                try {
                    if (Utils.isPastDue(Utils.formatDate(date, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm")) {
                        tvGetTime.setText(Utils.formatDate(date, "yyyy-MM-dd HH:mm"));
                        time = Utils.formatDate(date, "yyyy-MM-dd HH:mm");
                    } else {
                        Utils.toastShort(mContext, "时间选择有误，请重新选择");
                        tvGetTime.setText("请选择时间");
                        time = "";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)
                .setLabel("-", "-", " ", " :", "", "")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                tvCount.setText(options.get(options1) + "");
            }
        }).setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择限制人数")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .build();
        options = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            options.add(i);
        }
        pvOptions.setPicker(options);
    }

    @Subscribe
    public void delImg(DelEvent event) {
        if (strings.size() != 0) {
            strings.remove(event.getCp());
            gwPictureAdapter.notifyDataSetChanged();
            if ((strings.size() - 1) > fristfiles) {
                fileList.remove(event.getCp());
            }

            if ((strings.size() - 1) > fristfiles) {
                fileList.remove(event.getCp());
            }
            if ((strings.size() - 1) < fristfiles) {
                Utils.toastShort(this, "删除原有的图片了~");
                if (delFileIds.isEmpty()) {
                    delFileIds = filesid[event.getCp()] + "";
                } else {
                    delFileIds = delFileIds + "," + filesid[event.getCp()];
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (pop.isShowing()) {
            pop.dismiss();
        }
    }

    private void event() {
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvGetTime.setOnClickListener(this);
        tvGetCount.setOnClickListener(this);
        etTitle.addTextChangedListener(new TextWatcher() {
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
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 128) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == strings.size() - 1) {
                    if (strings.size() == 10) {
                        Utils.toastShort(mContext, "最多添加9张图片");
                        return;
                    }
                    showPop();
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < strings.size() - 1; i++) {
                        list.add(strings.get(i));
                    }
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", list);
                    intent.putExtra(P.START_ITEM_POSITION, position);
                    intent.putExtra("del", true);
                    intent.putExtra(P.START_IAMGE_POSITION, position);
                    intent.putExtra("isdel2", true);
                    startActivity(intent);
                }
            }
        });
        gw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.tvSave:
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                String addressName = etAddress.getText().toString().trim();
                String limitMember = tvCount.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    Utils.toastShort(mContext, "标题不能为空");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    Utils.toastShort(mContext, "内容不能为空");
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    Utils.toastShort(mContext, "请选择截止时间");
                    return;
                }
                if (TextUtils.isEmpty(addressName)) {
                    Utils.toastShort(mContext, "活动地址不能为空");
                    return;
                }
                if (type == 1) {
                    tvSave.setClickable(false);
                    OkHttpUtils.post(Constants.base_url + "/api/club/activity/save.do")
                            .tag(this)
                            .headers(Constants.Token_Header, Constants.token)
                            .params("clubId", "" + Constants.clubId)
                            .params("title", "" + title)
                            .params("content", "" + content)
                            .addFileParams("files", fileList)
                            .params("applyDeadline", "" + time)
                            .params("addressName", "" + addressName)
                            .params("limitMember", "" + limitMember)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    tvSave.setClickable(true);
                                    try {
                                        JSONObject result = new JSONObject(s);
                                        switch (result.optInt("code")) {
                                            case 0:
                                                ReleaseActivitiesActivity.this.finish();
                                                //发送刷新EventBus
                                                EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 2}));
                                                Utils.toastShort(mContext, result.optString("msg"));
                                                break;
                                            case 1:
                                                Utils.toastShort(ReleaseActivitiesActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                break;
                                            case 2:
                                                Utils.toastShort(ReleaseActivitiesActivity.this, result.optString("msg"));
                                                break;
                                            case 3:
                                                Utils.toastShort(ReleaseActivitiesActivity.this, "您没有该功能操作权限");
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    tvSave.setClickable(true);
                                    isCommit = true;
                                    super.onError(call, response, e);
                                }

                            });
                } else if (type == 2) {
                    if (delFileIds.isEmpty()) { //如果没有图片删除
                        tvSave.setClickable(false);
                        Log.e("DDQ", "没有图片删除");
                        //修改保存
                        OkHttpUtils.post(Constants.base_url + "/api/club/activity/update.do")
                                .tag(this)
                                .headers(Constants.Token_Header, Constants.token)
                                .params("clubId", "" + Constants.clubId)
                                .params("title", "" + title)
                                .params("content", "" + content)
                                .addFileParams("files", fileList)
                                .params("applyDeadline", "" + time)
                                .params("addressName", "" + addressName)
                                .params("limit", "" + limitMember)
                                .params("id", "" + bean.getId())
                                //.params("delFileIds",delFileIds.get(0))

                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        tvSave.setClickable(true);
                                        try {
                                            JSONObject result = new JSONObject(s);
                                            switch (result.optInt("code")) {
                                                case 0:
                                                    ReleaseActivitiesActivity.this.finish();
                                                    //发送刷新EventBus
                                                    EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 2}));
                                                    Utils.toastShort(mContext, result.optString("msg"));
                                                    break;
                                                case 1:
                                                    Utils.toastShort(ReleaseActivitiesActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                    break;
                                                case 2:
                                                    Utils.toastShort(ReleaseActivitiesActivity.this, result.optString("msg"));
                                                    break;
                                                case 3:
                                                    Utils.toastShort(ReleaseActivitiesActivity.this, "您没有该功能操作权限");
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        isCommit = true;
                                        tvSave.setClickable(true);
                                        super.onError(call, response, e);
                                    }

                                });
                    } else {
                        //修改保存
                        tvSave.setClickable(false);
                        OkHttpUtils.post(Constants.base_url + "/api/club/activity/update.do")
                                .tag(this)
                                .headers(Constants.Token_Header, Constants.token)
                                .params("clubId", "" + Constants.clubId)
                                .params("title", "" + title)
                                .params("content", "" + content)
                                .addFileParams("files", fileList)
                                .params("applyDeadline", "" + time)
                                .params("addressName", "" + addressName)
                                .params("limit", "" + limitMember)
                                .params("id", "" + bean.getId())
                                .params("delFileIds", delFileIds)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        tvSave.setClickable(true);
                                        try {
                                            JSONObject result = new JSONObject(s);
                                            switch (result.optInt("code")) {
                                                case 0:
                                                    ReleaseActivitiesActivity.this.finish();
                                                    //发送刷新EventBus
                                                    EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 2}));
                                                    Utils.toastShort(mContext, result.optString("msg"));
                                                    break;
                                                case 1:
                                                    Utils.toastShort(ReleaseActivitiesActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                    break;
                                                case 2:
                                                    Utils.toastShort(ReleaseActivitiesActivity.this, result.optString("msg"));
                                                    break;
                                                case 3:
                                                    Utils.toastShort(ReleaseActivitiesActivity.this, "您没有该功能操作权限");
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        tvSave.setClickable(true);
                                        isCommit = true;
                                        super.onError(call, response, e);
                                    }

                                });
                    }
                }
                break;
            case R.id.tvUp:
                PhotoUtils.choosePhoto(202, this);
                pop.dismiss();
                break;
            case R.id.tvPic:
                path = PhotoUtils.startPhoto(this);
                pop.dismiss();
                break;
            case R.id.tvGetTime:
                pvTime.show();
                break;
            case R.id.tvGetCount:
                pvOptions.show();
                break;
        }
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
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        // 设置所在布局
        pop.setContentView(pv);
    }

    private void showPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(tvSave, Gravity.BOTTOM, 0, 0);
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
                if (resultCode == Activity.RESULT_OK && data != null) {
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
                        fileList.add(file);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }


}