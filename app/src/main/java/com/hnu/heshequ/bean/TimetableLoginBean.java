package com.hnu.heshequ.bean;

import java.io.Serializable;

/**
 * Created by dell on 2020/5/1.
 */

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
