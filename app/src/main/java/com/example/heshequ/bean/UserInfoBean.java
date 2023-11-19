package com.example.heshequ.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户信息类
 * Created by Dengdongqi on 2018/6/13.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class UserInfoBean implements Serializable {
    /**
     * id : 4
     * sex : 1
     * nickname : 哈哈哈
     * college : 中南大学
     * grade : 1
     * experience : 27
     * certFlag : 0
     * header : /info/file/pub.do?fileId=head/20180801/4_20180801105505_756.jpg
     * settingClub : 0
     * settingAsk : 0
     * totalExperience : 1
     * needExperience : 49
     */

    private int id;
    private int sex;
    private String nickname;
    private String college;
    private int grade;
    private int experience;
    private int certFlag;
    private String header;
    private int settingClub;
    private int settingAsk;
    private int totalExperience;
    private int needExperience;
    private String userNickName;
    private ArrayList<UserLabelsBean> userLabels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getCertFlag() {
        return certFlag;
    }

    public void setCertFlag(int certFlag) {
        this.certFlag = certFlag;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getSettingClub() {
        return settingClub;
    }

    public void setSettingClub(int settingClub) {
        this.settingClub = settingClub;
    }

    public int getSettingAsk() {
        return settingAsk;
    }

    public void setSettingAsk(int settingAsk) {
        this.settingAsk = settingAsk;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public int getNeedExperience() {
        return needExperience;
    }

    public void setNeedExperience(int needExperience) {
        this.needExperience = needExperience;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public ArrayList<UserLabelsBean> getUserLabels() {
        return userLabels;
    }

    public void setUserLabels(ArrayList<UserLabelsBean> userLabels) {
        this.userLabels = userLabels;
    }

    public static class UserLabelsBean implements Serializable {
        /**
         * id : 26
         * userId : 28
         * label : 美食
         * frequency : 1
         */

        private int id;
        private int userId;
        private String label;
        private int frequency;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }


}
