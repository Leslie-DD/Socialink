package com.example.heshequ.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dev06 on 2018/5/25.
 */
public class BuildingBean implements Serializable {


    /**
     * id : 8
     * clubId : 3
     * gmtCreate : 2018-06-04 15:15
     * presentor : 2
     * presentorName : 15874893244
     * title : 测试静态添加1
     * content : 看得见你都没电脑打开门学科心里都没打开
     * header :  logo/20180528/1_20180528182708_118.jpg
     * recommend : 1
     * likeAmount : 1
     * commentAmount : 5
     * photos : [{"id":23,"photoId":"/info/file/pub.do?fileId=tb/20180604/2_20180604151540_749.png","bzId":8},{"id":25,"photoId":"/info/file/pub.do?fileId=tb/20180604/2_20180604152731_636.png","bzId":8}]
     * likes : [{"uid":2,"presentorName":"15874893244","bzId":8}]
     * isLike : 0
     * time : 22小时前
     */

    /*private int id;
    private int clubId;
    private String gmtCreate;
    private int presentor;
    private String presentorName;
    private String header;
    private String title;
    private String content;
    private int recommend;
    private int likeAmount;
    private int commentAmount;
    private int isLike;
    private String time;
    */
    private int id;
    private int clubId;
    private int presentor;
    private String presentorName;
    private String header;
    private String gmtCreate;
    private String content;
    private String title;
    private int recommend;
    private int likeAmount;
    private int commentAmount;
    private int isLike;
    private String time;


    /**
     * id : 23
     * photoId : /info/file/pub.do?fileId=tb/20180604/2_20180604151540_749.png
     * bzId : 8
     */
    private ArrayList<PhotosBean> photos;

    /**
     * uid : 2
     * presentorName : 15874893244
     * bzId : 8
     */

    private ArrayList<LikesBean> likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(int likeAmount) {
        this.likeAmount = likeAmount;
    }

    public int getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(int commentAmount) {
        this.commentAmount = commentAmount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public ArrayList<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotosBean> photos) {
        this.photos = photos;
    }

    public ArrayList<LikesBean> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<LikesBean> likes) {
        this.likes = likes;
    }

    public static class PhotosBean implements Serializable {
        /**
         * photoId : /info/file/pub.do?fileId=speak/20180529/1_20180529191753_292.jpg
         * bzId : 5
         */
        private int id;
        private String photoId;
        private int bzId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhotoId() {
            return photoId;
        }

        public void setPhotoId(String photoId) {
            this.photoId = photoId;
        }

        public int getBzId() {
            return bzId;
        }

        public void setBzId(int bzId) {
            this.bzId = bzId;
        }
    }


    public static class LikesBean implements Serializable {
        private int uid;
        private String presentorName;
        private int bzId;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPresentorName() {
            return presentorName;
        }

        public void setPresentorName(String presentorName) {
            this.presentorName = presentorName;
        }

        public int getBzId() {
            return bzId;
        }

        public void setBzId(int bzId) {
            this.bzId = bzId;
        }
    }
}
