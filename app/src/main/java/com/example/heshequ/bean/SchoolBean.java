package com.example.heshequ.bean;

import java.io.Serializable;

/**
 * Created by 佳佳 on 2019/8/27.
 */

public class SchoolBean implements Serializable {
    private String school;//学校名称
    private int type;//学校编码

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
