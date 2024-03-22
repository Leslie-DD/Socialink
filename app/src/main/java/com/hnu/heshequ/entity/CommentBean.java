package com.hnu.heshequ.entity;


public class CommentBean {
    /**
     * id : 1
     * gmtCreate : 2018-05-30 14:49
     * presentor : 1
     * presentorName : 18774961742
     * header : /info/file/pub.do?fileId=logo/20180528/1_20180528182708_118.jpg
     * content : 你这坑货，玩个李白0-20
     * time : 31 分钟前
     * itsaidFlag : 是否为他们说
     */

    private int id;
    private String gmtCreate;
    private int presentor;
    private String presentorName;
    private String header;
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private String itsaidFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getPresentor() {
        return presentor;
    }

    public void setPresentor(int presentor) {
        this.presentor = presentor;
    }

    public String getPresentorName() {
        return presentorName;
    }

    public void setPresentorName(String presentorName) {
        this.presentorName = presentorName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getItsaidFlag() {
        return itsaidFlag;
    }

    public void setItsaidFlag(String itsaidFlag) {
        this.itsaidFlag = itsaidFlag;
    }

    /*
    Test Data-->
    private String comment;
    private String name,time,url;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }*/

}
