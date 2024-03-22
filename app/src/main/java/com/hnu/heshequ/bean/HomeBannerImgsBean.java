package com.hnu.heshequ.bean;


public class HomeBannerImgsBean {

    /**
     * id : 1
     * gmtCreate : 1528367771000
     * gmtModified : 1528367793000
     * name : 首页广告
     * coverImage : tb/20180601/2_20180601113632_940.png
     * linkUrl : www.hao123.com
     * category : 1
     */

    private int id;
    private long gmtCreate;
    private long gmtModified;
    private String name;
    private String coverImage;
    private String linkUrl;
    private int category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
