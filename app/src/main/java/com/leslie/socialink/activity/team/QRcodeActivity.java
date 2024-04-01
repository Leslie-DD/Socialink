package com.leslie.socialink.activity.team;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.utils.ZxingUtils;
import com.leslie.socialink.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

public class QRcodeActivity extends NetWorkActivity {
    private ImageView ivQr;
    private CircleView ivHead;
    private TextView tvName;
    private TeamBean cBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        cBean = (TeamBean) getIntent().getSerializableExtra("bean");
        initQr();
    }

    private void initQr() {
        ivQr = (ImageView) findViewById(R.id.ivQr);
        tvName = (TextView) findViewById(R.id.tvName);
        ivHead = (CircleView) findViewById(R.id.ivHead);
        if (cBean != null) {
            tvName.setText(cBean.getName());
            Utils.setHead(mContext, cBean.getLogoImage(), ivHead);
            if (!TextUtils.isEmpty(cBean.getQrcodeImage())) {
                Log.e("YSF", "我是二维码" + Constants.base_url + cBean.getQrcodeImage());
                Glide.with(mContext).load(Constants.base_url + cBean.getQrcodeImage()).fitCenter().into(ivQr);
            } else {
                ivQr.setImageBitmap(ZxingUtils.createBitmap("XYTeam_" + cBean.getId()));
            }
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

}
