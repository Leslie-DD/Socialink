package com.hnu.heshequ.bean;

import java.io.Serializable;


public class CommentReply implements Serializable {
    public Integer id;
    public Integer presentor;
    public String content;
    public Integer type;
    public Integer critics;
    public Integer comment_id;
    public String presentorName;
    public String criticsName;
}
