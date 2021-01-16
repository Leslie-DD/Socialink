package com.example.heshequ.entity;

/**
 * Created by Dengdongqi on 2018/7/13.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class ScanResultEvent {

    public ScanResultEvent(String result) {
        this.result = result;
    }

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
