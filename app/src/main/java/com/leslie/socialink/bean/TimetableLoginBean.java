package com.leslie.socialink.bean;

import java.io.Serializable;


public class TimetableLoginBean implements Serializable {
    public String studentId;
    public String pwd;
    public int type;

    public String getStudentId() {
        return studentId;
    }

    public String getPwd() {
        return pwd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
