package com.example.heshequ.bean;

/**
 * Created by Dengdongqi on 2018/7/9.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class AppliedMemberBean {
    /**
     * uid : 4
     * header : head/20180706/4_20180706170038_139.jpg
     * nickname : 123
     * role : 1
     * grade : 1
     */
    private int uid;
    private String header;
    private String nickname;
    private int role;
    private int grade;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
