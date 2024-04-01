package com.leslie.socialink.bean;


public class NoticeBean {

    private String header;
    private String sex;
    private String school;
    private String nickname;
    private int id;
    private boolean notice;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getHeader() {
        return header;
    }

    public boolean isNotice() {
        return notice;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSchool() {
        return school;
    }

    public String getSex() {
        return sex;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
