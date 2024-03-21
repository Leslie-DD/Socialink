package com.hnu.heshequ.bean;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2020/4/24.
 */

public class DynamicComment implements Serializable {
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
