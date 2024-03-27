package com.hnu.heshequ.network;

import android.util.Log;

import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.lzy.okhttputils.request.GetRequest;
import com.lzy.okhttputils.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HttpRequestUtil {
    private static final String TAG = "[HttpRequestUtil]";

    private String tag = TAG;

    // body体中是否包含文件
    private boolean existsFile;
    private boolean existsBodyParam;
    private String[] fileKey;
    private File[] uploadFile;
    private Map<String, String> params;

    private RequestCallBack requestCallBack;

    public HttpRequestUtil() {
    }

    public HttpRequestUtil(RequestCallBack callBack) {
        requestCallBack = callBack;
    }

    public HttpRequestUtil(RequestCallBack callBack, String tag) {
        requestCallBack = callBack;
        this.tag = tag;
    }

    public void sendGetConnection(String url, int where) {
        sendGetConnection(url, new String[]{}, new String[]{}, where, null);
    }

    public void sendGetConnection(String url, int where, String token) {
        sendGetConnection(url, new String[]{}, new String[]{}, where, token);
    }

    public void sendGetConnection(String url, String[] argsKeys, String[] argsValues, int where, String token) {
        GetRequest getRequest = OkHttpUtils.get(url).tag(this);
        if (token != null && !token.isEmpty()) {
            getRequest = getRequest.headers(Constants.Token_Header, WenConstans.token);
        }
        for (int i = 0; i < argsKeys.length; i++) {
            getRequest = getRequest.params(argsKeys[i], argsValues[i]);
        }
        executeRequest("(sendGetConnection)", url, where, getRequest);
    }

    public void sendPostConnection(String url, int where) {
        sendPostConnection(url, new String[]{}, new String[]{}, where, null);
    }

    public void sendPostConnection(String url, int where, String token) {
        sendPostConnection(url, new String[]{}, new String[]{}, where, token);
    }


    public void sendPostConnection(String url, String[] argsKeys, String[] argsValues, int where, String token) {
        PostRequest postRequest = OkHttpUtils.post(url).tag(this);
        if (token != null && !token.isEmpty()) {
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

    private <REQUEST extends BaseRequest<REQUEST>> void executeRequest(String funcTag, String url, int where, BaseRequest<REQUEST> request) {
        Log.d(tag, funcTag + " executeRequest, url: " + url);
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject json = new JSONObject(s);
                    Log.i(tag, funcTag + " " + Thread.currentThread().getName() + " onSuccess url: " + url + ", json: " + json);
                    if (requestCallBack != null) {
                        requestCallBack.onSuccess(json, where, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(tag, funcTag + " onFailure url: " + url + ", exception: " + e);
                    if (requestCallBack != null) {
                        requestCallBack.onFailure(e.getMessage(), where);
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e(tag, funcTag + " onFailure url: " + url + ", exception: " + e.toString());
                if (requestCallBack != null) {
                    requestCallBack.onFailure(e.getMessage(), where);
                }
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

    @SuppressWarnings("unused")
    public void setFileBodyParams(String[] fileKey, File[] uploadFile) {
        if (fileKey.length != uploadFile.length) {
            throw new IllegalArgumentException("check your BodyFile key or value length!");
        }
        this.fileKey = fileKey;
        this.uploadFile = uploadFile;
        this.existsFile = true;
    }

    public interface RequestCallBack {
        void onSuccess(JSONObject result, int where, boolean fromCache);

        void onFailure(String result, int where);
    }
}
