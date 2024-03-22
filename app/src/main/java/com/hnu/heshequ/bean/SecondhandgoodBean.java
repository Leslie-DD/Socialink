package com.hnu.heshequ.bean;

import java.io.Serializable;
import java.util.List;


public class SecondhandgoodBean implements Serializable {
    public String userLike;
    public int type = 0;//查询类型
    public int id;
    public String price;
    public String gmtCreate;
    public int presentor;
    public String content;
    public String header;
    public int likeAmount;
    public String commentAmount;
    public int visitAmount;
    public int anonymity = 0;
    public int status;
    public String nn;
    public String uid;
    public int sex;
    public String college;
    public String time;
    public String isCollect;
    public List<SecondhandphotoBean> photos;

    public List<SecondhandlabelsBean> labels;

    public String getCollege() {
        return college;
    }

    public int getType() {
        return type;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }


    public int getSettingVisible() {
        int a = 1;
        return a;
    }

}
