package com.leslie.socialink.bean;

import java.io.Serializable;
import java.util.List;


public class FriendNewBean implements Serializable {
    public int user_id;
    public int dtid;
    public String headImg;
    public String date;
    public String name;
    public int type = 0;
    public String content;
    public String place;
    public List<String> photos;
    public List<FriendLikesBean> likes;
    public String college;
    public int likeamount;
    public boolean islike = false;
    public List<DynamicComment> dynamicCommentlist;

    public String getCollege() {
        return college;
    }

    public int getType() {
        return type;
    }
}
