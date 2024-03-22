package com.hnu.heshequ.entity;


public class RefHotActivityEvent {
    int type;   // 1  =刷新  2  = 加载

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
