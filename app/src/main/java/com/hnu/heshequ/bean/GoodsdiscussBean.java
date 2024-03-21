package com.hnu.heshequ.bean;

import java.io.Serializable;

/**
 * Created by dell on 2019/8/25.
 */

public class GoodsdiscussBean implements Serializable {
    public String id;
    public String goodsId;
    public String content;
    public String gmtCreate;
    public String nn;
    public String header;
    public String isTop;
    public String time;
    public String title;
    public String uid;
    public int comment;
    public int likeAmount;
    public int commentAmount;


//    id：评论的唯一标识
//    uid：发布评论的用户ID
//    nn：发布评论的用户昵称
//    header：评论发布者的头像
//    time：评论发表的时间
//
//    content：评论的内容
//    comment：评论的评论数

}
