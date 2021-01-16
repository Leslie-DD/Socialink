package com.example.heshequ.entity;

/**
 * Created by Dengdongqi on 2018/7/6.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class RefTDteamEvent {

    private boolean isDel;
    private int[] ref;

    public RefTDteamEvent(int[] ref) {
        this.ref = ref;
    }

    public RefTDteamEvent(int[] ref,boolean isDel) {
        this.ref = ref;
        this.isDel = isDel;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }


    public int[] getRef() {
        return ref;
    }

    public void setRef(int[] ref) {
        this.ref = ref;
    }
}
