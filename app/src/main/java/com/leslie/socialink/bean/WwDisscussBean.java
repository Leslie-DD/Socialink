package com.leslie.socialink.bean;

import java.io.Serializable;


public class WwDisscussBean implements Serializable {
    public String id;
    public String askId;
    public String gmtCreate;
    public String content;
    public String nn;
    public String header;
    public String isTop;
    public String time;
    public String title;
    public String uid;
    public int likeAmount;
    public int commentAmount;
}

//        "id": 评论id,
//        "askId": 问问id,
//        "gmtCreate": 评论时间,
//        "gmtModified": 最后更新时间,
//        "presentor": 评论人,
//        "content": "评论内容",
//        "likeAmount": 顶数量,
//        "commentAmount": 回复数量,
//        "status": 状态（0：正常；9：删除的）这里返回的都是状态为0的,
//        "anonymity": 匿名发表的标记。0-不匿名；1-匿名,
//        "header": "/info/file/pub.do?fileId=head/20180611/2_20180611102627_703.png",
//        "nn": "评论人昵称",
//        "uid": 评论人用户id,
//        "time": "16小时前"（评论时间）,
//        "isTop": 是否顶过（1-已顶，null-未顶）

