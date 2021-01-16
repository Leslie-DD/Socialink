package com.example.heshequ.entity;

/**
 * Created by Dengdongqi on 2018/7/9.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class RefHotActivityEvent {
    int type ;   // 1  =刷新  2  = 加载

    public RefHotActivityEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
