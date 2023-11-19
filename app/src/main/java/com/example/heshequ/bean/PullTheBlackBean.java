package com.example.heshequ.bean;

import java.io.Serializable;

/**
 * Created by 佳佳 on 2018/11/18.
 */

public class PullTheBlackBean implements Serializable {
    private int id;
    private int follower;
    private int concerned;
    private String blackname;
    private String nickname;
    private boolean black;
    private String header;
    private int Cancel;
    private boolean check;
    private String blackNickname;
    private int blackId;

    public int getBlackId() {
        return blackId;
    }

    public void setBlackId(int blackId) {
        this.blackId = blackId;
    }

    public String getBlacknickname() {
        return blackNickname;
    }

    public void setBlacknickname(String blackNickname) {
        this.blackNickname = blackNickname;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    public boolean isBlack() {
        return black;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBlackname() {
        return blackname;
    }

    public void setBlackname(String blackname) {
        this.blackname = blackname;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getCancel() {
        return Cancel;
    }

    public void setCancel(int cancel) {
        Cancel = cancel;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public int getConcerned() {
        return concerned;
    }

    public int getFollower() {
        return follower;
    }

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
