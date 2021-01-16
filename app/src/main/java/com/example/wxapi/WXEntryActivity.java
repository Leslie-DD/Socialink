package com.example.wxapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.utils.Utils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import org.json.JSONObject;


/**
 * 微信客户端回调activity
 */
public class WXEntryActivity extends NetWorkActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        Constants.api = WXAPIFactory.createWXAPI(this, Constants.APP_AD_WX, false);

        // 将该app注册到微信
        Constants.api.registerApp(Constants.APP_AD_WX);

        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            if (!Constants.api.handleIntent(getIntent(), this)) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sp = getSharedPreferences("hanwenxueyuan", 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Constants.api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType(); //类型：分享还是登录
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                //Utils.toastShort(this, "拒绝授权微信登录");
                this.finish();
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                Utils.toastShort(mContext, message);
                this.finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {

                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    //EventBus发送微信分享消息
                    Utils.toastShort(mContext,"微信分享消息成功");
                    this.finish();
                }
                break;
        }
    }


    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e("DDQ", result + "");

    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "访问网络失败~");
        finish();
    }


}


