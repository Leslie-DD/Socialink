package com.example.heshequ.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.HttputilHelp;
import com.example.heshequ.view.CustomDialog;
import com.example.heshequ.view.CustomProgressDialog;
import com.example.heshequ.MeetApplication;
import com.google.gson.Gson;
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
	private HttpUtils httpUtils;
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
	private Gson gs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("class fragment:", getClass().getSimpleName());
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
		gs = new Gson();
		progressDialog = new CustomProgressDialog(mContext);
		dialog = new CustomDialog(mContext);
		// httpUtils = HttputilHelp.getHttpUtils();
		// httpUtils.configUserAgent(AAA_Application.userAgent);
		vector = new Vector<HttpHandler<String>>();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mainView = createView(inflater);

		ViewUtils.inject(this, mainView);
		requestData();

		Constants.uid= MeetApplication.getInstance().getSharedPreferences().getInt("uid",1);
		Constants.token=MeetApplication.getInstance().getSharedPreferences().getString("token","");
		WenConstans.token=MeetApplication.getInstance().getSharedPreferences().getString("token","");
		Constants.userName=MeetApplication.getInstance().getSharedPreferences().getString("user","18274962484");
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 设置加载对话框是否可以被取消
	 * 
	 * @param canable
	 */
	public void setProgressCancelable(boolean canable) {
		progressDialog.setCancelable(canable);
	}

	/**
	 * 设置加载对话框提示的文字
	 * 
	 * @param message
	 */
	public void setProgressDialogMessage(String message) {
		progressDialog.setMessage(message);
	}

	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            是否显示进度条
	 */
	public void sendConnection(HttpMethod method, String url, int where, String token) {
		HttpCallBack httpCallback = new HttpCallBack(where, false, false);
		RequestParams params = new RequestParams();
		List<String> tempParams = new ArrayList<String>();
		if (token != null&&!token.equals("")) {
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
		httpUtils = HttputilHelp.getHttpUtils();
		//httpUtils.configUserAgent(MeetApplication.userAgent);
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		// httpUtils.configUserAgent(AAA_Application.userAgent);
		HttpHandler<String> httpHandler = httpUtils.send(method, url, params, httpCallback);
		LogUtils.e("请求：" + url);
		if (vector != null) {
			vector.add(httpHandler);
		}
	}

	public void sendConnection(HttpMethod method, String url, String[] argsKeys, String[] argsValues, int where,
							   boolean showDialog, boolean cacheOpen) {
		HttpCallBack httpCallback = new HttpCallBack(where, showDialog, cacheOpen);
		RequestParams params = new RequestParams();
		List<String> tempParams = new ArrayList<String>();
		params.addHeader("Content-Type", "application/x-www-form-urlencoded");
		params.addHeader("Request-Type", "api-request");
		params.addHeader("Authentication", "magictoken");
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
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		//httpUtils.configUserAgent(MeetApplication.userAgent);
		HttpHandler<String> httpHandler = httpUtils.send(method, url, params, httpCallback);
		if (vector!=null) {
			vector.add(httpHandler);
		}
		LogUtils.e("请求：" + url);
	}

	/**
	 * 当为post请求是 调用改方法设置body体 参数,此方法在sendConnection 方法前调用
	 * 
	 * @param bodyKey
	 * @param bodyValue
	 */
	public void setBodyParams(String[] bodyKey, String[] bodyValue) {
		if (bodyKey.length != bodyValue.length) {
			throw new IllegalArgumentException("check your BodyParams key or value length!");
		}
		bodyParams = new ArrayList<NameValuePair>();
		String bodyParam = "body参数：";
		for (int i = 0; i < bodyKey.length; i++) {
			NameValuePair param = new BasicNameValuePair(bodyKey[i], bodyValue[i]);
			bodyParam += bodyKey[i] + "=" + bodyValue[i] + "&";
			bodyParams.add(param);
		}
		if (bodyParams.lastIndexOf("&") > 0) {
			bodyParam = bodyParam.substring(0, bodyParam.length() - 1);
		}
		existsBodyParam = true;
		LogUtils.e(bodyParam);
	}

	public void setBodyParams(String[] bodyKey, String[] bodyValue, int code) {

		String con;
		StringBuffer sb = new StringBuffer();
		String str = bodyValue[0];
		if (code == 2) {
			if (str.contains("[[") || str.contains("]]"))
				str = str.replace("[[", "[").replace("]]", "]");

		}
		// {"result":["
		sb.append("{\"result\" :");
		sb.append(str);
		sb.append("}");
		Log.e("YSF", sb.toString());
		if (bodyKey.length != bodyValue.length) {
			throw new IllegalArgumentException("check your BodyParams key or value length!");
		}
		bodyParams = new ArrayList<NameValuePair>();
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
		String bodyParam = "bodyfile参数：";
		for (int i = 0; i < fileKey.length; i++) {
			bodyParam += fileKey[i] + "=" + uploadFile[i].getName() + "&";
		}
		existsFile = true;
		LogUtils.e(bodyParam);
	}

	public void sendConnection(String url, String[] argsKeys, String[] argsValues, int where, boolean showDialog) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, where, showDialog, false);
	}

	public void sendConnection(String url, String[] argsKeys, String[] argsValues, int where, boolean showDialog,
							   boolean cacheOpen) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, where, showDialog, cacheOpen);
	}

	public void sendConnection(String url, String[] argsKeys, String[] argsValues, int where) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, where, false, false);
	}

	public void sendConnection(HttpMethod method, String url, String[] argsKeys, String[] argsValues, int where,
							   String token) {
		sendConnection(method, url, where, token);
		// sendConnection(method,url, argsKeys, argsValues, where, false,false);
	}

	public void sendPost( String url, int where, String token) {
		sendConnection(HttpMethod.POST, url, where, token);
		// sendConnection(method,url, argsKeys, argsValues, where, false,false);
	}

	public void sendConnection(String url, String[] argsKeys, String[] argsValues) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, 0, false, false);
	}

	public void sendConnection(String url, String[] argsKeys, String[] argsValues, boolean isShow) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, 0, isShow, false);
	}

	public void sendConnection(String url, String[] argsKeys, String[] argsValues, boolean isShow, boolean openCache) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, 0, isShow, openCache);
	}

	public void sendConnection(String url, int where, boolean showDialog) {
		sendConnection(HttpMethod.GET, url, new String[] {}, new String[] {}, where, showDialog, false);
	}

	public void sendConnection(String url, int where) {
		sendConnection(HttpMethod.GET, url, new String[] {}, new String[] {}, where, false, false);
	}

	// -------------------------需要重写或实现的方法---------------------
	/**
	 * 联网开始
	 * 
	 * @param where
	 */
	protected void onStart(int where) {
	}

	/**
	 * 取消
	 * 
	 * @param where
	 */
	protected void onCancelled(int where) {
	}

	/**
	 * 加载中 回调改方法
	 * 
	 * @param total
	 * @param current
	 * @param isUploading
	 * @param where
	 */
	protected void onLoading(long total, long current, boolean isUploading, int where) {
	}

	/**
	 * 成功返回结果
	 * 
	 * @param result
	 * @param where
	 *            数据是否来自缓存
	 */
	protected abstract void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException;

	/**
	 * 网络连接错误 或服务器返回错误结果时回调改方法
	 * 
	 * @param result
	 * @param where
	 */
	protected abstract void onFailure(String result, int where);

	/**
	 * 返回fragemtn要创建的View 不必重写 onCreateView 只需从写改方法
	 */
	protected abstract View createView(LayoutInflater inflater);

	/** 如果要请求数据，实现改方法 */
	protected void requestData() {
	}



	// -------------------------------------------------

	private class HttpCallBack extends RequestCallBack<String> {
		private int where;
		private boolean showDialog;

		public HttpCallBack(int where, boolean showDialog, boolean cacheOpen) {
			this.where = where;
			this.showDialog = showDialog;
		}

		@Override
		public void onStart() {
			if (showDialog) {
				showDialog();
			}
			LogUtils.e("请求：" + this.getRequestUrl());
			NetWorkFragment.this.onStart(where);
		}

		@Override
		public void onCancelled() {
			NetWorkFragment.this.onCancelled(where);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			NetWorkFragment.this.onLoading(total, current, isUploading, where);
		}

		@Override
		public void onFailure(HttpException ex, String msg) {
			LogUtils.e(msg, ex);
			//String result = mContext.getResources().getString(R.string.result_failure);
			if (progressDialog!=null) {
				progressDialog.dismiss();
			}
			// NetWorkFragment.this.onFailure("后台连接报错："+msg, where);
			NetWorkFragment.this.onFailure("请求连接失败 请重试", where);
		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			String result = responseInfo.result;
			String[] res = result.split(";");
			LogUtils.e("changdu " + res.length);
			LogUtils.e("返回:" + result);
			dismissDialog();
			try {
				JSONObject json = new JSONObject(result);
				NetWorkFragment.this.onSuccess(json, where, false);
			} catch (JSONException e) {
				e.printStackTrace();
				if (result != null)
					NetWorkFragment.this.onFailure(result, where);
			}
		}
	}

	/** find toker form header */
	private String getToken(ResponseInfo<String> responseInfo) {
		Header[] cookies = responseInfo.getHeaders("Set-Cookie");
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].toString().contains("SPRING_SECURITY_REMEMBER_ME_COOKIE")) {
				String[] cookie = cookies[i].toString().split("=");
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
		if (vector!=null){
			if (vector.size() > 1) {
				vector.remove(0);
			} else {
				if (progressDialog!=null){
					if (!mContext.isFinishing() && progressDialog!=null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					vector.clear();
				}

			}
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
		if (vector!=null) {
			for (HttpHandler<String> handler : vector) {
				handler.cancel();
			}
		}
		if (progressDialog!=null){
			if (progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			progressDialog.cancel();
			progressDialog=null;
		}
		if (dialog!=null){
			if (dialog.isShowing()){
				dialog.dismiss();
			}
			dialog.cancel();
			dialog=null;
		}
	}
}
