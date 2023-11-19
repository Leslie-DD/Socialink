package com.example.heshequ.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CustomDialog;
import com.example.heshequ.view.CustomProgressDialog;
import com.githang.statusbar.StatusBarCompat;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.lzy.okhttputils.request.GetRequest;
import com.lzy.okhttputils.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 带有网络操作的 Activity
 */
public abstract class NetWorkActivity extends BaseActivity {
    private static final String TAG = "[NetWorkActivity]";

    private CustomProgressDialog progressDialog;
    protected NetWorkActivity context;
    private Vector<Integer> vector;

    protected CustomDialog dialog;
    private boolean existsFile;
    private boolean existsBodyParam;
    private String[] fileKey;
    private File[] uploadFile;

    private Map<String, String> params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        context = this;
        Constants.uid = MeetApplication.getInstance().getSharedPreferences().getInt("uid", 1);
        Log.i(TAG, "(onCreate) 用户id: " + Constants.uid);
        Constants.token = MeetApplication.getInstance().getSharedPreferences().getString("token", "");
        WenConstans.token = MeetApplication.getInstance().getSharedPreferences().getString("token", "");
        Constants.userName = MeetApplication.getInstance().getSharedPreferences().getString("user", "18274962484");
        try {
            progressDialog = new CustomProgressDialog(this);
            progressDialog.setMessage("载入中...");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        if (vector != null) {
            vector = new Vector<>();
        }
        dialog = new CustomDialog(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupData();
    }

    public void sendGetConnection(String url, String[] argsKeys, String[] argsValues, int where, String token) {
        GetRequest getRequest = OkHttpUtils.get(url).tag(this);
        if (token != null && !token.equals("")) {
            getRequest = getRequest.headers(Constants.Token_Header, WenConstans.token);
        }
        for (int i = 0; i < argsKeys.length; i++) {
            getRequest = getRequest.params(argsKeys[i], argsValues[i]);
        }
        executeRequest("(sendGetConnection)", url, where, getRequest);
    }


    public void sendPostConnection(String url, String[] argsKeys, String[] argsValues, int where, String token) {
        PostRequest postRequest = OkHttpUtils.post(url).tag(this);
        if (token != null && !token.equals("")) {
            postRequest = postRequest.headers(Constants.Token_Header, WenConstans.token);
        }
        for (int i = 0; i < argsKeys.length; i++) {
            postRequest = postRequest.params(argsKeys[i], argsValues[i]);
        }
        if (existsBodyParam) {
            postRequest = postRequest.params(params);
            existsBodyParam = false;
        }
        if (existsFile) {
            for (int i = 0; i < fileKey.length; i++) {
                postRequest = postRequest.params(fileKey[i], uploadFile[i]);
            }
            existsFile = false;
        }
        executeRequest("(sendPostConnection)", url, where, postRequest);
    }

    private <REQUEST extends BaseRequest<REQUEST>> void executeRequest(String tag, String url, int where, BaseRequest<REQUEST> request) {
        Log.d(TAG, tag + " executeRequest, url: " + url);
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject json = new JSONObject(s);
                    Log.i(TAG, tag + "onSuccess url: " + url + ", json: " + json);
                    NetWorkActivity.this.onSuccess(json, where, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, tag + "onFailure url: " + url + ", exception: " + e);
                    NetWorkActivity.this.onFailure(e.getMessage(), where);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e(TAG, tag + "onFailure url: " + url + ", exception: " + e.toString());
                NetWorkActivity.this.onFailure(e.getMessage(), where);
            }
        });
    }

    public void setBodyParams(String[] bodyKey, String[] bodyValue) {
        params = new HashMap<>();
        for (int i = 0; i < bodyKey.length; i++) {
            params.put(bodyKey[i], bodyValue[i]);
        }
        existsBodyParam = true;
    }

    public void setFileBodyParams(String[] fileKey, File[] uploadFile) {
        if (fileKey.length != uploadFile.length) {
            throw new IllegalArgumentException("check your BodyFile key or value length!");
        }
        this.fileKey = fileKey;
        this.uploadFile = uploadFile;
        existsFile = true;
        Log.i(TAG, "(setFileBodyParams) files: " + Arrays.toString(uploadFile));
    }

    public void sendPost(String url, int where, String token) {
        sendPostConnection(url, new String[]{}, new String[]{}, where, token);
    }

    protected abstract void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException;

    protected abstract void onFailure(String result, int where);

    /**
     * 可以在此方法中配置一些基本数据
     */
    protected void setupData() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.cancel();
            progressDialog = null;
        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.cancel();
            dialog = null;
        }
    }

    public TextView setTitleAndBack(String title) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        return tvTitle;
    }
}
