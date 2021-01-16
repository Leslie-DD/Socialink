package com.example.heshequ.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.R;


/**
 * 自定义进度对话框
 * 
 * @author di.chao
 * 
 */
public class CustomProgressDialog extends Dialog {
	// 等待时间
	// private final static int WAIT_TIME = 40;

	private AnimationDrawable anim;
	private TextView tvMsg;

	// private HoldTimer holderTimer;
	public CustomProgressDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		setContentView(R.layout.customdialoglayout);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		this.setCanceledOnTouchOutside(false);
		ImageView image = (ImageView) this.findViewById(R.id.loadingImageView);
		anim = (AnimationDrawable) image.getBackground();
		tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
		this.setCancelable(true);
	}

	@Override
	public void show() {
		anim.start();
		super.show();
		// holderTimer = new HoldTimer(WAIT_TIME);
		// holderTimer.start();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		anim.stop();
	}

	/**
	 * 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public void setMessage(String strMessage) {
		tvMsg.setText(strMessage);
	}

	public void ShowMsg(boolean show) {
		if (show) {
			tvMsg.setVisibility(View.VISIBLE);
		} else {
			tvMsg.setVisibility(View.GONE);
		}
	}

}
