package com.leslie.socialink.activity.team;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.adapter.listview.GwPictureAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.P;
import com.leslie.socialink.entity.DelEvent;
import com.leslie.socialink.entity.RefStatementEvent;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.PhotoUtils;
import com.leslie.socialink.utils.Utils;
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

public class StatementActivity extends NetWorkActivity {
    private GridView gw;
    private List<String> strings;
    private GwPictureAdapter gwPictureAdapter;
    private TextView tvCancel;
    private EditText etContent;
    private TextView tvSave;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private Uri photoUri;
    private List<File> fileList;
    private int teamId = 0;
    private final int POSTSTATEMENT = 1000;
    private boolean isCommit = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        init();
        event();
    }

    private void init() {
        teamId = getIntent().getIntExtra("teamId", 0);
        setText("发布团言");
        gw = (GridView) findViewById(R.id.gw);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        etContent = (EditText) findViewById(R.id.etContent);
        fileList = new ArrayList<>();
        EventBus.getDefault().register(this);
        initPop();
    }

    private void event() {
        tvCancel.setOnClickListener(v -> finish());
        tvSave.setOnClickListener(v -> {
            String content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Utils.toastShort(mContext, "内容不能为空");
                return;
            }
            if (fileList.size() < 0) {
                Utils.toastShort(this, "请至少添加一张图片");
                return;
            }
            tvSave.setClickable(false);
            OkHttpUtils.post(Constants.BASE_URL + "/api/club/speak/save.do")
                    .tag(this)
                    .headers(Constants.TOKEN_HEADER, Constants.token)
                    .params("clubId", teamId + "")
                    .params("content", content + "")
                    .addFileParams("files", fileList)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            isCommit = true;
                            tvSave.setClickable(true);
                            try {
                                JSONObject result = new JSONObject(s);
                                switch (result.optInt("code")) {
                                    case 0:
                                        if (result.optString("msg").contains("发表成功")) {
                                            Utils.toastShort(mContext, "发表成功");
                                            StatementActivity.this.finish();
                                            //发送团言刷新EventBus
                                            EventBus.getDefault().post(new RefStatementEvent());
                                        }
                                        break;
                                    case 1:
                                        Utils.toastShort(StatementActivity.this, "您还没有登录或登录已过期，请重新登录");
                                        break;
                                    case 2:
                                        Utils.toastShort(StatementActivity.this, result.optString("msg"));
                                        break;
                                    case 3:
                                        Utils.toastShort(StatementActivity.this, "您没有该功能操作权限");
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
        });
        strings = new ArrayList<>();
        strings.add("");
        gwPictureAdapter = new GwPictureAdapter(this);
        gw.setAdapter(gwPictureAdapter);
        gwPictureAdapter.setData(strings);
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
                    startActivity(intent);
                }
            }
        });
        gw.setOnItemLongClickListener((parent, view, position, id) -> false);
    }

    @Subscribe
    public void delImg(DelEvent event) {
        if (strings.size() != 0) {
            strings.remove(event.getCp());
            gwPictureAdapter.notifyDataSetChanged();
            fileList.remove(event.getCp());
        }
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
        pop.showAtLocation(tvSave, Gravity.BOTTOM, 0, 0);
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


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
