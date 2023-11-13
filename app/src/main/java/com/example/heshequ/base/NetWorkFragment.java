package com.example.heshequ.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.HttpUtilHelp;
import com.example.heshequ.view.CustomDialog;
import com.example.heshequ.view.CustomProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 基类Fragment 要联网操作的Fragment继承该类
 */
public abstract class NetWorkFragment extends Fragment {
    private CustomProgressDialog progressDialog;
    protected Activity mContext;
    private Vector<HttpHandler<String>> vector;
    private List<NameValuePair> bodyParams;
    protected CustomDialog dialog;
    private View mainView;
    // body体中是否包含文件
    private boolean existsFile;
    private boolean existsBodyParam;
    private String[] fileKey;
    private File[] uploadFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("class fragment:", getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        progressDialog = new CustomProgressDialog(mContext);
        dialog = new CustomDialog(mContext);
        vector = new Vector<>();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mainView = createView(inflater);

        ViewUtils.inject(this, mainView);
        requestData();

        Constants.uid = MeetApplication.getInstance().getSharedPreferences().getInt("uid", 1);
        Constants.token = MeetApplication.getInstance().getSharedPreferences().getString("token", "");
        WenConstans.token = MeetApplication.getInstance().getSharedPreferences().getString("token", "");
        Constants.userName = MeetApplication.getInstance().getSharedPreferences().getString("user", "18274962484");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) mainView.getParent();
        if (parent != null) {
            parent.removeView(mainView);
        }
        return mainView;
    }

    public void sendConnection(HttpMethod method, String url, int where, String token) {
        HttpCallBack httpCallback = new HttpCallBack(where, false);
        RequestParams params = new RequestParams();
        if (token != null && !token.equals("")) {
            params.addHeader("XIANGYU-ACCESS-TOKEN", token);
        }
        if (method == HttpMethod.POST) {
            if (existsBodyParam) {
                params.addBodyParameter(bodyParams);
                existsBodyParam = false;
            }
            if (existsFile) {
                for (int i = 0; i < fileKey.length; i++) {
                    params.addBodyParameter(fileKey[i], uploadFile[i]);
                }
                existsFile = false;
            }
        }
        HttpUtils httpUtils = HttpUtilHelp.getHttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.configDefaultHttpCacheExpiry(0);
        HttpHandler<String> httpHandler = httpUtils.send(method, url, params, httpCallback);
        LogUtils.e("请求：" + url);
        if (vector != null) {
            vector.add(httpHandler);
        }
    }

    /**
     * 当为post请求是 调用改方法设置body体 参数,此方法在sendConnection 方法前调用
     */
    public void setBodyParams(String[] bodyKey, String[] bodyValue) {
        if (bodyKey.length != bodyValue.length) {
            throw new IllegalArgumentException("check your BodyParams key or value length!");
        }
        bodyParams = new ArrayList<>();
        StringBuilder bodyParam = new StringBuilder("body参数：");
        for (int i = 0; i < bodyKey.length; i++) {
            NameValuePair param = new BasicNameValuePair(bodyKey[i], bodyValue[i]);
            bodyParam.append(bodyKey[i]).append("=").append(bodyValue[i]).append("&");
            bodyParams.add(param);
        }
        if (bodyParams.lastIndexOf("&") > 0) {
            bodyParam = new StringBuilder(bodyParam.substring(0, bodyParam.length() - 1));
        }
        existsBodyParam = true;
        LogUtils.e(bodyParam.toString());
    }

    public void setBodyParams(String[] bodyKey, String[] bodyValue, int code) {

        StringBuilder sb = new StringBuilder();
        String str = bodyValue[0];
        if (code == 2) {
            if (str.contains("[[") || str.contains("]]"))
                str = str.replace("[[", "[").replace("]]", "]");

        }
        sb.append("{\"result\" :").append(str).append("}");
        Log.e("YSF", sb.toString());
        if (bodyKey.length != bodyValue.length) {
            throw new IllegalArgumentException("check your BodyParams key or value length!");
        }
        bodyParams = new ArrayList<>();
        String bodyParam = "body参数：";
        NameValuePair param = new BasicNameValuePair(bodyKey[0], sb.toString());

        bodyParams.add(param);

        if (bodyParams.lastIndexOf("&") > 0) {
            bodyParam = bodyParam.substring(0, bodyParam.length() - 1);
        }
        existsBodyParam = true;
        LogUtils.e(bodyParam);
    }

    public void setBodyParams(String[] fileKey, File[] uploadFile) {
        if (fileKey.length != uploadFile.length) {
            throw new IllegalArgumentException("check your BodyFile key or value length!");
        }
        this.fileKey = fileKey;
        this.uploadFile = uploadFile;
        StringBuilder bodyParam = new StringBuilder("bodyfile参数：");
        for (int i = 0; i < fileKey.length; i++) {
            bodyParam.append(fileKey[i]).append("=").append(uploadFile[i].getName()).append("&");
        }
        existsFile = true;
        LogUtils.e(bodyParam.toString());
    }

    public void sendPost(String url, int where, String token) {
        sendConnection(HttpMethod.POST, url, where, token);
    }

    /**
     * 成功返回结果
     *
     * @param where 数据是否来自缓存
     */
    protected abstract void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException;

    /**
     * 网络连接错误 或服务器返回错误结果时回调改方法
     */
    protected abstract void onFailure(String result, int where);

    /**
     * 返回fragemtn要创建的View 不必重写 onCreateView 只需从写改方法
     */
    protected abstract View createView(LayoutInflater inflater);

    /**
     * 如果要请求数据，实现改方法
     */
    protected void requestData() {
    }

    private class HttpCallBack extends RequestCallBack<String> {
        private final int where;
        private final boolean showDialog;

        public HttpCallBack(int where, boolean showDialog) {
            this.where = where;
            this.showDialog = showDialog;
        }

        @Override
        public void onStart() {
            if (showDialog) {
                showDialog();
            }
            LogUtils.e("请求：" + this.getRequestUrl());
        }

        @Override
        public void onCancelled() {
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
        }

        @Override
        public void onFailure(HttpException ex, String msg) {
            LogUtils.e(msg, ex);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            NetWorkFragment.this.onFailure("请求连接失败 请重试", where);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String result = responseInfo.result;
            LogUtils.e("changdu： " + result.split(";").length + "，返回:" + result);
            dismissDialog();
            try {
                JSONObject json = new JSONObject(result);
                NetWorkFragment.this.onSuccess(json, where, false);
            } catch (JSONException e) {
                e.printStackTrace();
                NetWorkFragment.this.onFailure(result, where);
            }
        }
    }

    /**
     * find toker form header
     */
    private String getToken(ResponseInfo<String> responseInfo) {
        Header[] cookies = responseInfo.getHeaders("Set-Cookie");
        for (Header header : cookies) {
            if (header.toString().contains("SPRING_SECURITY_REMEMBER_ME_COOKIE")) {
                String[] cookie = header.toString().split("=");
                String token = cookie[1].substring(0, cookie[1].indexOf(";"));
                if (!TextUtils.isEmpty(token)) {
                    return token;
                } else
                    return null;
            }
        }
        return null;
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissDialog() {
        if (vector == null) {
            return;
        }
        if (vector.size() > 1) {
            vector.remove(0);
        } else {
            if (progressDialog == null) {
                return;
            }
            if (!mContext.isFinishing() && progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            vector.clear();
        }
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        if (!progressDialog.isShowing() && !mContext.isFinishing()) {
            progressDialog.show();
        }
    }

    public View findViewById(int resId) {
        if (mainView == null) {
            return null;
        }
        return mainView.findViewById(resId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vector != null) {
            for (HttpHandler<String> handler : vector) {
                handler.cancel();
            }
        }
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
}
