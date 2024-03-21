package com.example.heshequ.bean;

import java.io.Serializable;
import java.util.List;


public class WenwenBean implements Serializable {
    public int type = 0;
    public String userLike;
    public String specialId;
    public String yesterdayVisitAmount;
    public String visitAmount;
    public String commentAmount;
    public int likeAmount;
    public String title;
    public String time;
    public String header;
    public String nn;
    public String uid;
    public String id;
    public String status;
    public int anonymity;
    public String gmtModified;
    public String content;
    public String isCollect;
    public List<WwPhotoBean> photos;
    public List<WwLablesBean> labels;

    public String name;
    public String logoImage;
    public String creatorName;
    public String introduction;
    public String creator;
    public String memberNumber;
    public int category;
    public String college;

//    id：问题的唯一标识
//    uid：发布问问的用户ID
//    nn：发表问问的用户昵称
//    header：头像的url地址
//    time：问问发表的时间
//    title：问问的标题
//    likeAmount：问问的点赞数
//    commentAmount：问问的评论数
//    visitAmount：问问总评论数
//    yesterdayVisitAmount:昨天的浏览量
//    specialId：所属专场的ID，0为不属于任何专场
//    status：状态，0：正常；9：删除的
//    lables：问问的标签数组
//    anonymity：是否匿名（0-不匿名，1-匿名）
//    userLike：是否点赞（1-点赞，null-未点赞）
//    gmtModified:记录修改时间
//    Photos：图片数组
//    commentVos：问问评论数组

}
