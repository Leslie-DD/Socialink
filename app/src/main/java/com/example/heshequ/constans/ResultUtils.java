package com.example.heshequ.constans;

import android.content.Context;

import org.json.JSONObject;


public class ResultUtils {
    public static boolean isFail(JSONObject result, Context context) {
        if (result.has("code")) {
            return result.optInt("code") != 0;
//            int resultType = result.optInt("code");
//            if (resultType == 0) {
//                return false;
//            }
//            else {
//                try {
//                    Utils.toastShort(context, result.getString("msg") + "");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return true;
    }
}
