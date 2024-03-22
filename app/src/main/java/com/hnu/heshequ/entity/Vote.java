package com.hnu.heshequ.entity;


public class Vote {

    /**
     * id : 9
     * gmtCreate : 2018-06-07 09:27:30
     * clubId : 3
     * presentor : 2
     * presentorName : 15874893244
     * name : 贺华是傻逼吗
     * introduction : 随意
     * deadline : 2018-06-08 09:27
     * status : 0
     * isVote : 0
     */

    private int id;
    private String gmtCreate;
    private int clubId;
    private int presentor;
    private String presentorName;
    private String name;
    private String introduction;
    private String deadline;
    private int status;
    private int isVote;

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

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }
}
