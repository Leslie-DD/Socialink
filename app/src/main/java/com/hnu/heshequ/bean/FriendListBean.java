package com.hnu.heshequ.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2020/3/28.
 */

public class FriendListBean implements Serializable {
    public int user_id;
    public int type = 0;
    public List<String> label;
    public double longitude;
    public double latitude;
    public String college;
    public String header;
    public int score;
    public String nickname;
    public int sex;
    public int age;
    public int distance;
    public String url;
    public String descroption;

    public String getCollege() {
        return college;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public int getSex() {
        return sex;
    }

    public int getDistance() {
        return distance;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        return "user_id:" + user_id + ", " +
                "age:" + age + ", " +
                "sex:" + sex + ", " +
                "nick_name:" + nickname;
    }

}
