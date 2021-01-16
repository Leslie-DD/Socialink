package com.example.heshequ.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.utils.HttputilHelp;
import com.example.heshequ.view.CustomDialog;
import com.example.heshequ.view.CustomProgressDialog;
import com.githang.statusbar.StatusBarCompat;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
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
 * 带有网络操作的activity
 *
 * @author wenle
 *
 */
public abstract class NetWorkActivity extends BaseActivity {
	private CustomProgressDialog progressDialog;
	private HttpUtils httpUtils;
	protected NetWorkActivity context;
	private Vector<Integer> vector;
	private List<NameValuePair> bodyParams;

	protected CustomDialog dialog;
	private boolean existsFile;
	private boolean existsBodyParam;
	private String[] fileKey;
	private File[] uploadFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		context = this;
		Constants.uid=MeetApplication.getInstance().getSharedPreferences().getInt("uid",1);
		Log.e("用户id"+Constants.uid,"");
		Constants.token=MeetApplication.getInstance().getSharedPreferences().getString("token","");
		WenConstans.token=MeetApplication.getInstance().getSharedPreferences().getString("token","");
		Constants.userName=MeetApplication.getInstance().getSharedPreferences().getString("user","18274962484");
		try {
			progressDialog = new CustomProgressDialog(this);
			progressDialog.setMessage("载入中...");
		} catch (Exception e) {
			Log.e("YSFC", e.getMessage());
			// TODO: handle exception
		}

		// httpUtils = HttputilHelp.getHttpUtils();
		// httpUtils.configUserAgent(AAA_Application.userAgent);
		if (vector!=null) {
			vector = new Vector<Integer>();
		}
		dialog = new CustomDialog(context);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setupData();
	}

	public void setContentView2 (int layoutResID) {
		Log.e("NetWorkActivity","sss");
		super.setContentView(layoutResID);
		setupData();
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
	 * @param argsKeys
	 * @param argsValues
	 * @param where
	 * @param showDialog
	 *            是否显示进度条
	 */
	public void sendConnection(HttpMethod method, String url, String[] argsKeys, String[] argsValues, int where,
							   boolean showDialog, boolean showNetDialog, String token) {

		if (argsKeys.length != argsValues.length) {
			throw new IllegalArgumentException("check your Params key or value length!");
		}
		if (vector!=null) {
			vector.add(where);
		}
		HttpCallBack httpCallback = new HttpCallBack(where, showDialog);
		RequestParams params = new RequestParams();

		for (int i = 0; i < argsKeys.length; i++) {
			params.addQueryStringParameter(argsKeys[i], argsValues[i]);
		}
		if (token != null&&!token.equals("")) {
			params.addHeader("XIANGYU-ACCESS-TOKEN", token);
		}
//		params.addHeader("enctype", "multipart/form-data");
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
		httpUtils.send(method, url, params, httpCallback);
		// httpUtils.configCurrentHttpCacheExpiry(0);
	}

	/**
	 * 当为post请求是 调用改方法设置body体 参数,此方法在sendConnection 方法前调用
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
		if (bodyParam.lastIndexOf("&") > 0) {
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
		Log.e("DDQ","上传图片"+bodyParam);
		LogUtils.e(bodyParam);
	}
	public void sendPost(String url, int where, String token) {
		Log.e("NetWorkActivity","sendPost:"+url+" "+where+" "+token);
		sendConnection(HttpMethod.POST, url, new String[]{}, new String[]{}, where, false, false, token);
	}

	public void sendGet(String url, String[] argsKeys, String[] argsValues, int where,
						String token) {
		sendConnection(HttpMethod.GET, url, argsKeys, argsValues, where, false, false, token);
	}

	// -------------------------需要重写或实现的方法---------------------
	/**
	 * 联网开始
	 *
	 * @param where
	 */
	protected void onStart(int where) {}

	/**
	 * 取消
	 *
	 * @param where
	 */
	protected void onCancelled(int where) {}

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
	 * 可以在此方法中配置一些基本数据
	 */
	protected void setupData() {
		StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
	}

	// -------------------------------------------------

	private class HttpCallBack extends RequestCallBack<String> {
		private int where;
		private boolean showDialog;

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
			NetWorkActivity.this.onStart(where);
		}

		@Override
		public void onCancelled() {
			NetWorkActivity.this.onCancelled(where);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			NetWorkActivity.this.onLoading(total, current, isUploading, where);
		}

		@Override
		public void onFailure(HttpException ex, String msg) {
			LogUtils.e(msg, ex);
			if (progressDialog!=null) {
				progressDialog.dismiss();
			}
			NetWorkActivity.this.onFailure(msg, where);
		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			String result = responseInfo.result;
			LogUtils.e("返回:" + result + " statusCode:" + responseInfo.statusCode);
			dismissDialog();
			Header[] cookies = responseInfo.getAllHeaders();
			if (cookies != null) {
				for (Header temp : cookies) {
					System.out.println(temp.getName() + "!!!!!" + temp.getValue());
				}
			}
			try {
				JSONObject json = new JSONObject(result);
				NetWorkActivity.this.onSuccess(json, where, false);
				Log.e("YSF","成功返回的数据："+json.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				if (result != null) {
					NetWorkActivity.this.onFailure(e.getMessage(), where);
				}
			}
		}

		/**
		 * 隐藏加载对话框
		 */
		public void dismissDialog() {
			if (vector != null){
				if (vector.size() > 1) {
					vector.remove(0);
				} else {
					if (!NetWorkActivity.this.isFinishing() && progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					vector.clear();
				}
			}
		}

		/**
		 * 显示加载对话框
		 */
		public void showDialog() {
			if (!progressDialog.isShowing() && !isFinishing()) {
				progressDialog.show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
	public TextView setTitleAndBack(String title){
		TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(title);
		findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		return tvTitle;
	}
}
