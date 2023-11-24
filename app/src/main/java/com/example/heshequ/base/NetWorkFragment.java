package com.example.heshequ.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CustomDialog;
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

/**
 * 要联网操作的 Fragment 继承该类
 */
public abstract class NetWorkFragment extends Fragment {
    private static final String TAG = "[NetWorkFragment]";
    protected Activity mContext;
    private Map<String, String> params;
    protected CustomDialog dialog;
    private View mainView;
    // body体中是否包含文件
    private boolean existsFile;
    private boolean existsBodyParam;
    private String[] fileKey;
    private File[] uploadFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "(onCreate) " + getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        dialog = new CustomDialog(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mainView = createView(inflater);

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

    public void sendGetConnection(String url, int where) {
        sendGetConnection(url, new String[]{}, new String[]{}, where, null);
    }

    public void sendGetConnection(String url, int where, String token) {
        sendGetConnection(url, new String[]{}, new String[]{}, where, token);
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

    public void sendPostConnection(String url, int where) {
        sendPostConnection(url, new String[]{}, new String[]{}, where, null);
    }

    public void sendPostConnection(String url, int where, String token) {
        sendPostConnection(url, new String[]{}, new String[]{}, where, token);
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
                    Log.i(TAG, tag + " onSuccess url: " + url + ", json: " + json);
                    NetWorkFragment.this.onSuccess(json, where, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, tag + " onFailure url: " + url + ", exception: " + e);
                    NetWorkFragment.this.onFailure(e.getMessage(), where);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e(TAG, tag + " onFailure url: " + url + ", exception: " + e.toString());
                NetWorkFragment.this.onFailure(e.getMessage(), where);
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
        existsFile = true;
    }

    protected abstract void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException;

    protected abstract void onFailure(String result, int where);

    protected abstract View createView(LayoutInflater inflater);

    /**
     * 如果要请求数据，实现改方法
     */
    protected void requestData() {
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
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.cancel();
            dialog = null;
        }
    }
}
