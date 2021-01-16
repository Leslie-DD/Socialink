package com.example.heshequ.bean;

/**
 * Created by dev06 on 2018/5/30.
 */
public class ItemBean {
    private String name;
    private String tip="";
    private int resId;
    private int status;
    private int type = 0;//我的消息中红点使用

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
