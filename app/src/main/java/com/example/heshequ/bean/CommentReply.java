package com.example.heshequ.bean;

import java.io.Serializable;

/**
 * Created by dell on 2020/4/24.
 */

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
