package com.hnu.heshequ.bean;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.List;


public class FriendLikesBean implements Serializable {
    public int uid;
    public String presentorName;
    public Integer id;
    public Integer dynamic_id;
    public ContactsContract.Data gmt_create;
    public Integer presentor;
    public String content;
    public Integer type;
    public Integer critics;
    public Integer comment_id;
    public List<CommentReply> commentReply;
    public String criticsName;
}
