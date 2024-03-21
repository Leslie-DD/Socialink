package com.hnu.heshequ.bean;

import android.provider.ContactsContract;

import java.io.Serializable;

/**
 * Created by dev06 on 2018/5/30.
 */
public class FriendBean implements Serializable {
    private String academy; //学院
    private String nickname;    //昵称
    private ContactsContract.Data birthday;    //生日
    private String college;     //大学
    private String descroption; //个人描述
    private int emotion;     //情感状态
    private String header;  //头像
    private String hometown;  //家乡
    private String profession; //专业
    private String schoolgrate;  //年级
    private int sex;    //性别

    public String getName() {
        return nickname;
    }

    public int getSex() {
        return sex;
    }

    public String getSchoolgrate() {
        return schoolgrate;
    }

    public String getAcademy() {
        return academy;
    }

    public String getNickname() {
        return nickname;
    }

    public ContactsContract.Data getBirthday() {
        return birthday;
    }

    public String getCollege() {
        return college;
    }

    public int getEmotion() {
        return emotion;
    }

    public String getHometown() {
        return hometown;
    }

    public String getDescroption() {
        return descroption;
    }

    public String getHeader() {
        return header;
    }

    public void SetHeader(String heander) {
        this.header = header;
    }
}
