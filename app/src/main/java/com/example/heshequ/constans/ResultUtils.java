package com.example.heshequ.constans;

import android.content.Context;

import com.example.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Hulk_Zhang on 2018/6/12 17:04
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ResultUtils {
    public static boolean isFail(JSONObject result, Context context){
        if (result.has("code")){
            int resultType = result.optInt("code");
            if (resultType==0){
                return false;
            }else{
                try {
                    Utils.toastShort(context,result.getString("msg")+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
