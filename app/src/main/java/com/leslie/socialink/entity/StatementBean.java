package com.leslie.socialink.entity;

import com.leslie.socialink.network.Constants;

import java.io.Serializable;
import java.util.ArrayList;


public class StatementBean implements Serializable {

    /**
     * id : 7
     * clubId : 3
     * presentor : 2
     * presentorName : 15874893244
     * gmtCreate : 2018-06-04 15:41
     * content : 你先看看动漫都到门口想你想你才看到年底能洗掉你的快递呢
     * itsaidFlag : 1
     * likeAmount : 1
     * commentAmount : 2
     * photos : [{"photoId":"/info/file/pub.do?fileId=speak/20180604/2_20180604154136_305.png","bzId":7},{"photoId":"/info/file/pub.do?fileId=speak/20180604/2_20180604154136_466.png","bzId":7},{"photoId":"/info/file/pub.do?fileId=speak/20180604/2_20180604154136_747.png","bzId":7},{"photoId":"/info/file/pub.do?fileId=speak/20180604/2_20180604154136_902.png","bzId":7}]
     * likes : [{"uid":2,"presentorName":"15874893244","bzId":7}]
     * isLike : 0
     */

    /*private int id;
    private int clubId;
    private String header;
    private int presentor;
    private String presentorName;
    private String gmtCreate;
    private String content;
    private int itsaidFlag;
    private int likeAmount;
    private int commentAmount;
    private int isLike;*/

    private int id;
    private int clubId;
    private int presentor;
    private String presentorName;
    private String header;
    private String gmtCreate;
    private String content;
    private int itsaidFlag;
    private int likeAmount;
    private int commentAmount;
    private int isLike;
    private String time;
    private ArrayList<PhotosBean> photos;
    private ArrayList<LikesBean> likes;
    private ArrayList<String> imgs = new ArrayList<>();


    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public void setImgs() {
        if (photos != null) {
            imgs = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                imgs.add(Constants.BASE_URL + photos.get(i).getPhotoId());
            }
        }
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * photoId : /info/file/pub.do?fileId=speak/20180604/2_20180604154136_305.png
     * bzId : 7
     */

    //private ArrayList<PhotosBean> photos;

    /**
     * uid : 2
     * presentorName : 15874893244
     * bzId : 7
     */

    //private ArrayList<LikesBean> likes;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getItsaidFlag() {
        return itsaidFlag;
    }

    public void setItsaidFlag(int itsaidFlag) {
        this.itsaidFlag = itsaidFlag;
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
