package com.hnu.heshequ.activity.team;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.listview.GwPictureAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.entity.BuildingBean;
import com.hnu.heshequ.entity.DelEvent;
import com.hnu.heshequ.entity.RefTjEvent;
import com.hnu.heshequ.utils.PhotoUtils;
import com.hnu.heshequ.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AddTjActivity extends NetWorkActivity  {
    private int type = 0;
    private GridView gw;
    private List<String> strings;
    private GwPictureAdapter gwPictureAdapter;
    private TextView tvCancel;
    private EditText etTitle, etContent;
    private TextView tvSave;
    private BuildingBean buildingBean;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private Uri photoUri;
    private List<File> fileList;
    private int fristfiles;   //初始文件数
    private int[] filesid = new int[9];    //文件id数组
    private String delFileIds = ""; //
    private boolean isCommit = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tj);
        init();
        event();
        initPop();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            setText("添加团建");
        } else if (type == 2) {
            setText("修改团建");
        }
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        fileList = new ArrayList<>();
        gw = (GridView) findViewById(R.id.gv);
        strings = new ArrayList<>();
        strings.add("");
        gwPictureAdapter = new GwPictureAdapter(this);
        gw.setAdapter(gwPictureAdapter);
        gwPictureAdapter.setData(strings);
        EventBus.getDefault().register(this);

        if (type == 2) {
            tvSave.setText("保存修改");
            // 初始化已有数据
            buildingBean = (BuildingBean) getIntent().getSerializableExtra("bean");
            if (buildingBean != null) {
                etTitle.setText(buildingBean.getTitle());
                etTitle.setSelection(etTitle.getText().toString().trim().length());
                etContent.setText(buildingBean.getContent());
                etContent.setSelection(etContent.getText().toString().trim().length());
                if (buildingBean.getPhotos() != null && buildingBean.getPhotos().size() > 0) {
                    fristfiles = buildingBean.getPhotos().size();
                    for (int i = 0; i < buildingBean.getPhotos().size(); i++) {
                        String filepath = Constants.base_url + buildingBean.getPhotos().get(i).getPhotoId();
                        strings.add(strings.size() - 1, filepath);
                        gwPictureAdapter.setData(strings);
                        filesid[i] = buildingBean.getPhotos().get(i).getId();
                    }
                }
                Log.e("DDQ", "fristfiles ==> " + fristfiles + ",strings.size() == >" + strings.size());
            }
        }
    }

    @Subscribe
    public void delImg(DelEvent event) {
        if (type == 1) {
            if (strings.size() != 0) {
                strings.remove(event.getCp());
                gwPictureAdapter.notifyDataSetChanged();
                fileList.remove(event.getCp());
            }
        } else if (type == 2) {
            strings.remove(event.getCp());
            gwPictureAdapter.notifyDataSetChanged();
            if ((strings.size() - 1) > fristfiles) {
                fileList.remove(event.getCp());
            }
            if ((strings.size() - 1) < fristfiles) {
                //Utils.toastShort(this,"删除原有的图片了~");
                if (delFileIds.isEmpty()) {
                    delFileIds = filesid[event.getCp()] + "";
                } else {
                    delFileIds = delFileIds + "," + filesid[event.getCp()];
                }
            }
            Log.e("DDQ", "delFileIds = " + delFileIds);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void event() {
        tvCancel.setOnClickListener(v -> finish());
        tvSave.setOnClickListener(v -> {
            if (isCommit) {
                isCommit = false;
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
                if (type == 1) {
                    tvSave.setClickable(false);
                    OkHttpUtils.post(Constants.base_url + "/api/club/tb/save.do")
                            .tag(this)
                            .headers(Constants.Token_Header, Constants.token)
                            .params("clubId", getIntent().getIntExtra("teamid", 0) + "")
                            .params("title", title + "")
                            .params("content", content + "")
                            .addFileParams("files", fileList)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.e("DDQ", s);
                                    tvSave.setClickable(true);
                                    try {
                                        JSONObject result = new JSONObject(s);
                                        switch (result.optInt("code")) {
                                            case 0:
                                                if (result.optInt("code") == 0) {
                                                    //发送团建刷新event
                                                    EventBus.getDefault().post(new RefTjEvent());
                                                    AddTjActivity.this.finish();
                                                }
                                                break;
                                            case 1:
                                                Utils.toastShort(AddTjActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                break;
                                            case 2:

                                                Utils.toastShort(AddTjActivity.this, result.optString("msg"));
                                                break;
                                            case 3:
                                                Utils.toastShort(AddTjActivity.this, "您没有该功能操作权限");
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
                                    e.printStackTrace();
                                    super.onError(call, response, e);
                                }

                            });
                } else if (type == 2) {
                    if (delFileIds.isEmpty()) { //如果没有图片删除
                        Log.e("DDQ", "没有图片删除");
                        if ((strings.size() - 1) > fristfiles) { //如果有图片添加
                            Log.e("DDQ", "有图片添加,size = " + fileList.size());
                            OkHttpUtils.post(Constants.base_url + "/api/club/tb/update.do")
                                    .tag(this)
                                    .headers(Constants.Token_Header, Constants.token)
                                    .params("id", buildingBean.getId() + "")
                                    .params("title", title + "")
                                    .params("content", content + "")
                                    .addFileParams("files", fileList)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.e("DDQ", s);
                                            try {
                                                JSONObject result = new JSONObject(s);
                                                switch (result.optInt("code")) {
                                                    case 0:
                                                        if (result.optInt("code") == 0) {
                                                            //发送团建刷新event
                                                            EventBus.getDefault().post(new RefTjEvent());
                                                            AddTjActivity.this.finish();
                                                        }
                                                        break;
                                                    case 1:
                                                        Utils.toastShort(AddTjActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                        break;
                                                    case 2:

                                                        Utils.toastShort(AddTjActivity.this, result.optString("msg"));
                                                        break;
                                                    case 3:
                                                        Utils.toastShort(AddTjActivity.this, "您没有该功能操作权限");
                                                        break;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            isCommit = true;
                                            e.printStackTrace();
                                            super.onError(call, response, e);
                                        }

                                    });
                        } else { //如果没有图片添加
                            Log.e("DDQ", "没有图片添加");
                            OkHttpUtils.post(Constants.base_url + "/api/club/tb/update.do")
                                    .tag(this)
                                    .headers(Constants.Token_Header, Constants.token)
                                    .params("id", buildingBean.getId() + "")
                                    .params("title", title + "")
                                    .params("content", content + "")
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.e("DDQ", s);
                                            try {
                                                JSONObject result = new JSONObject(s);
                                                switch (result.optInt("code")) {
                                                    case 0:
                                                        if (result.optInt("code") == 0) {
                                                            //发送团建刷新event
                                                            EventBus.getDefault().post(new RefTjEvent());
                                                            AddTjActivity.this.finish();
                                                        }
                                                        break;
                                                    case 1:
                                                        Utils.toastShort(AddTjActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                        break;
                                                    case 2:

                                                        Utils.toastShort(AddTjActivity.this, result.optString("msg"));
                                                        break;
                                                    case 3:
                                                        Utils.toastShort(AddTjActivity.this, "您没有该功能操作权限");
                                                        break;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            isCommit = true;
                                            e.printStackTrace();
                                            super.onError(call, response, e);
                                        }

                                    });
                        }

                    } else { //有图片删除
                        Log.e("DDQ", "有图片删除,delFileIds = " + delFileIds);
                        OkHttpUtils.post(Constants.base_url + "/api/club/tb/update.do")
                                .tag(this)
                                .headers(Constants.Token_Header, Constants.token)
                                .params("id", buildingBean.getId() + "")
                                .params("title", title + "")
                                .params("content", content + "")
                                .params("delFileIds", delFileIds)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.e("DDQ", s);
                                        try {
                                            JSONObject result = new JSONObject(s);
                                            switch (result.optInt("code")) {
                                                case 0:
                                                    if (result.optInt("code") == 0) {
                                                        //发送团建刷新event
                                                        EventBus.getDefault().post(new RefTjEvent());
                                                        AddTjActivity.this.finish();
                                                    }
                                                    break;
                                                case 1:
                                                    Utils.toastShort(AddTjActivity.this, "您还没有登录或登录已过期，请重新登录");
                                                    break;
                                                case 2:

                                                    Utils.toastShort(AddTjActivity.this, result.optString("msg"));
                                                    break;
                                                case 3:
                                                    Utils.toastShort(AddTjActivity.this, "您没有该功能操作权限");
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        isCommit = true;
                                        e.printStackTrace();
                                        super.onError(call, response, e);
                                    }

                                });
                    }
                }
            }
        });
        gw.setOnItemClickListener((parent, view, position, id) -> {
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
                startActivity(intent);
            }
        });
        gw.setOnItemLongClickListener((parent, view, position, id) -> false);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

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
