package com.example.heshequ.utils;

import android.text.TextUtils;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.taobao.sophix.SophixManager;

/**
 * Created by Dengdongqi on 2018/10/25.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class QueryPatchUtils {
    public static void freeQuery(){
        String SPNAME = "sopHix";
        String oldTime = SPUtils.getInstance(SPNAME).getString("oldTime",""); //上一次查询补丁的时间
        if (TextUtils.isEmpty(oldTime)){
            SophixManager.getInstance().queryAndLoadNewPatch();
            SPUtils.getInstance(SPNAME).put("oldTime",TimeUtils.getNowString());
            return;
        }
        long interval = (long)( 60 * 60 * 1000 * 1.4);  // 时间间隔  1.4H 查询一次
        long oldDate = TimeUtils.string2Millis(oldTime);
        long gap = TimeUtils.getMillisByNow(oldDate,TimeConstants.MSEC);
        if (gap >= interval){
            SophixManager.getInstance().queryAndLoadNewPatch();
            SPUtils.getInstance(SPNAME).put("oldTime",TimeUtils.getNowString());
        }
    }
}
