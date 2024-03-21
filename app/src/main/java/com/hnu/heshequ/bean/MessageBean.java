package com.hnu.heshequ.bean;

import java.io.Serializable;

/**
 * Created by 佳佳 on 2018/11/20.
 */

public class MessageBean implements Serializable {
    private int id = 1;
    private int sender;
    private int receiver;
    private String content;
    private String header;
    private String gmtCreate;
    private String time;
    private int type = 0;
    private int sor = 0;
    private boolean isRead;
    private int indexofpicture;

    public int getIndexofpicture() {
        return indexofpicture;
    }

    public void setIndexofpicture(int indexofpicture) {
        this.indexofpicture = indexofpicture;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getSor() {
        return sor;
    }

    public void setSor(int sor) {
        this.sor = sor;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }
    // public Date Gmtcreate=12-12-12;

    public void setcontent(String str) {
        this.content = str;
    }

    public String getcontent() {
        return content;
    }

    public long getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
