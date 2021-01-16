package com.example.heshequ.bean;

import java.io.Serializable;

/**
 * Created by dell on 2020/5/11.
 */

public class FriendAddNewsBean implements Serializable {
    private int id;
    private int replyId;
    private int receiveId;
    private int bizId;
    private String content;
    private String gmtCreate;
    private  int status;
    private String time;
    private String nickName;
    private String header;
    private String clubName;
    public void setnickName(String nickName) {
        this.nickName = nickName;
    }

    public String getnickName() {
        return nickName;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getid() {
        return id;
    }


    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public int getBizId() {
        return bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
