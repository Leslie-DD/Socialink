package com.hnu.heshequ.activity.team;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.Label;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefTeamChild1;
import com.hnu.heshequ.utils.FileUtilcll;
import com.hnu.heshequ.utils.PhotoUtils;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AddTeamActivity extends NetWorkActivity {
    private final int PHOTO_REQUEST_CUT = 300;
    private Button btSave;
    private EditText etName;
    private CircleView ivHead;
    private FlowLayout flowLayout;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private int status;
    private WindowManager.LayoutParams layoutParams;
    private ArrayList<Label> labels;
    private final int getLabelCode = 1000;
    private String path;
    private Uri photoUri;
    private List<File> fileList;
    private final int addCode = 1001;
    private String name;
    private Uri uritempFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        init();
        event();
        initPop();
    }

    private void init() {
        setText("新建团队");
        ivHead = (CircleView) findViewById(R.id.ivHead);
        etName = (EditText) findViewById(R.id.etName);
        btSave = (Button) findViewById(R.id.btSave);
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        //获取标签
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", getLabelCode, Constants.token);

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

    private void showPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(btSave, Gravity.BOTTOM, 0, 0);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        btSave.setOnClickListener(v -> {
            if (labels == null) {
                return;
            }
            name = etName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Utils.toastShort(mContext, "请先输入团队名称");
                return;
            }
            if (TextUtils.isEmpty(path)) {
                Utils.toastShort(mContext, "请先设置团队Logo");
                return;
            }
            String label = "";
            for (int i = 0; i < labels.size(); i++) {
                Label bean = labels.get(i);
                if (bean.getStatus() == 1) {
                    if (label.length() == 0) {
                        label = bean.getValue();
                    } else {
                        label = label + "," + bean.getValue();
                    }
                }
            }
            if (label.length() == 0) {
                Utils.toastShort(mContext, "请先选择团队的标签");
                return;
            }
            btSave.setClickable(false);
            setFileBodyParams(new String[]{"file"}, new File[]{fileList.get(0)});
            setBodyParams(new String[]{"name", "labels"}, new String[]{name, label});
            sendPost(Constants.base_url + "/api/club/base/save.do", addCode, Constants.token);
        });
        ivHead.setOnClickListener(v -> showPop());

    }

    private void setTvBg(TextView view, int status) {
        view.setBackgroundResource(status == 0 ? R.drawable.e6e6e6_17 : R.drawable.bg_00bbff_17);
        String color = status == 0 ? "#999999" : "#ffffff";
        view.setTextColor(Color.parseColor(color));
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int code = result.optInt("code");
        Gson gs = new Gson();
        switch (where) {
            case getLabelCode:
                if (code == 0) {
                    labels = gs.fromJson(result.optString("data"), new TypeToken<List<Label>>() {
                    }.getType());
                    if (labels != null) {
                        for (int i = 0; i < labels.size(); i++) {
                            final TextView view = new TextView(this);
                            view.setText(labels.get(i).getValue());
                            view.setTextColor(Color.parseColor("#999999"));
                            view.setHeight(Utils.dip2px(context, 34));
                            view.setPadding(Utils.dip2px(context, 17), 0, Utils.dip2px(context, 17), 0);
                            view.setGravity(Gravity.CENTER);
                            view.setTextSize(14);
                            view.setBackgroundResource(R.drawable.e6e6e6_17);
                            // 设置点击事件
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text = view.getText().toString();
                                    int index = -1;
                                    for (int j = 0; j < labels.size(); j++) {
                                        Label bean = labels.get(j);
                                        if (bean.getValue().equals(text)) {
                                            bean.setStatus(Math.abs(bean.getStatus() - 1));
                                            index = j;
                                            break;
                                        }
                                    }
                                    setTvBg(view, labels.get(index).getStatus());
                                }
                            });
                            flowLayout.addView(view);
                        }
                    }
                }
                break;
            case addCode:
                btSave.setClickable(true);
                code = result.optInt("code");
                if (code == 0) {
                                 /*int id=result.optJSONObject("data").optInt("id");
                                 Intent intent=new Intent(mContext,TeamDetailActivity.class);
                                 intent.putExtra("id",id);
                                 startActivity(intent);*/
                    EventBus.getDefault().post(new RefTeamChild1());
                    finish();
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (requestCode == 200) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                path = "";
            }
            return;
        }
        switch (requestCode) {
            case 200:
                //拍照  path
                File temp = new File(path);
                Uri u = FileProvider.getUriForFile(context, PhotoUtils.AUTHORITY, temp);
                crop(u);
                break;
            case 202:
                if (data != null) {
                    photoUri = null;
                    photoUri = data.getData();
                    crop(PhotoUtils.generateUri(AddTeamActivity.this.getApplicationContext(), photoUri));
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }


        if (requestCode == PHOTO_REQUEST_CUT) {
            Glide.with(mContext).load(path).asBitmap().into(ivHead);
            Luban.with(this).load(new File(path)).
                    setCompressListener(new OnCompressListener() {
                        //                    @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            fileList = new ArrayList<File>();
                            fileList.add(file);
                        }

                        @Override
                        public void onError(Throwable e) {
                            fileList = new ArrayList<File>();
                            fileList.add(new File(path));
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
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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
//        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "XiangYuIcon.jpg");
        File file = new File(FileUtilcll.getPublicDir() + "/XiangYuIcon.jpg");
        uritempFile = FileProvider.getUriForFile(context, PhotoUtils.AUTHORITY, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT

        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uritempFile, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }

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
            path = FileUtilcll.saveFile(AddTeamActivity.this, "temphead.jpg", photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        if (where == addCode) {
            btSave.setClickable(true);
        }
    }

}
