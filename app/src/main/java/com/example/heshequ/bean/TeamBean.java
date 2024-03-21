package com.example.heshequ.bean;

import com.example.heshequ.constans.Constants;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamBean implements Serializable {


    /**
     * id : 1
     * name : 豆子信息6666
     * logoImage : /info/file/pub.do?fileId=logo/20180528/1_20180528182708_118.jpg
     * creator : 1
     * creatorName : 18774961742
     * memberNumber : 1
     * introduction : 成就客户，为科技创新而生
     * labels : [{"id":1,"clubId":1,"name":"湖南大学"}]
     * speak : {"id":5,"clubId":1,"presentor":1,"presentorName":"18774961742","header":"logo/20180528/1_20180528182708_118.jpg","gmtCreate":"2018-06-04 19:17","content":"老师课讲的很好，充电宝快没电了","itsaidFlag":0,"likeAmount":0,"commentAmount":2,"photos":[{"photoId":"/info/file/pub.do?fileId=speak/20180529/1_20180529191753_292.jpg","bzId":5}],"isLike":0}
     * activity : {"id":5,"clubId":3,"gmtCreate":"2018-06-02 17:42","presentor":2,"presentorName":"15874893244","title":"健康打底裤","content":"决定将洗净","applyDeadline":"2018-06-13 17:41","limitMember":31,"addressName":"长沙岳麓区保利地产","photos":[{"id":3,"photoId":"/info/file/pub.do?fileId=activity/20180602/2_20180602174201_71.png","bzId":5}],"isLike":0}
     */
    private int yesterdayTraffic;
    private int settingVisible;
    private int settingSpeak;
    private int settingActivity;
    private int settingNotice;
    private int settingVote;
    private int settingSpeakComment;

    public int getSettingSpeakComment() {
        return settingSpeakComment;
    }

    public void setSettingSpeakComment(int settingSpeakComment) {
        this.settingSpeakComment = settingSpeakComment;
    }


    private int status;
    private int isFavorite;
    private int isJoin;
    private boolean admin;
    private String college;
    private int collectionNumber;
    private int id;
    private String name;
    private String logoImage;
    private int creator;
    private String creatorName;
    private int memberNumber;
    private String introduction;
    private String gmtCreate;
    private SpeakBean speak;
    private ActivityBean activity;
    private ArrayList<LabelsBean> labels;
    private ArrayList<UsersBean> users;
    private NoticeBean notice;
    private String qrcodeImage;
    private String coverImage;
    private int itemType;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getQrcodeImage() {
        return qrcodeImage;
    }

    public void setQrcodeImage(String qrcodeImage) {
        this.qrcodeImage = qrcodeImage;
    }

    public NoticeBean getNotice() {
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        this.notice = notice;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public ArrayList<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UsersBean> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public int getYesterdayTraffic() {
        return yesterdayTraffic;
    }

    public void setYesterdayTraffic(int yesterdayTraffic) {
        this.yesterdayTraffic = yesterdayTraffic;
    }

    public int getSettingVisible() {
        return settingVisible;
    }

    public void setSettingVisible(int settingVisible) {
        this.settingVisible = settingVisible;
    }

    public int getSettingSpeak() {
        return settingSpeak;
    }

    public void setSettingSpeak(int settingSpeak) {
        this.settingSpeak = settingSpeak;
    }

    public int getSettingActivity() {
        return settingActivity;
    }

    public void setSettingActivity(int settingActivity) {
        this.settingActivity = settingActivity;
    }

    public int getSettingNotice() {
        return settingNotice;
    }

    public void setSettingNotice(int settingNotice) {
        this.settingNotice = settingNotice;
    }

    public int getSettingVote() {
        return settingVote;
    }

    public void setSettingVote(int settingVote) {
        this.settingVote = settingVote;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(int collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public SpeakBean getSpeak() {
        return speak;
    }

    public void setSpeak(SpeakBean speak) {
        this.speak = speak;
    }

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public ArrayList<LabelsBean> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<LabelsBean> labels) {
        this.labels = labels;
    }


    public static class SpeakBean implements Serializable {
        /**
         * id : 5
         * clubId : 1
         * presentor : 1
         * presentorName : 18774961742
         * header : logo/20180528/1_20180528182708_118.jpg
         * gmtCreate : 2018-06-04 19:17
         * content : 老师课讲的很好，充电宝快没电了
         * itsaidFlag : 0
         * likeAmount : 0
         * commentAmount : 2
         * photos : [{"photoId":"/info/file/pub.do?fileId=speak/20180529/1_20180529191753_292.jpg","bzId":5}]
         * isLike : 0
         * time : 7小时前
         */

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
        private String clubName;

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
                    imgs.add(Constants.base_url + photos.get(i).getPhotoId());
                }
            }
        }

        public String getClubName() {
            return clubName;
        }

        public void setClubName(String clubName) {
            this.clubName = clubName;
        }

        public ArrayList<LikesBean> getLikes() {
            return likes;
        }

        public void setLikes(ArrayList<LikesBean> likes) {
            this.likes = likes;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

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

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
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

        public static class PhotosBean implements Serializable {
            /**
             * photoId : /info/file/pub.do?fileId=speak/20180529/1_20180529191753_292.jpg
             * bzId : 5
             */

            private String photoId;
            private int bzId;

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

    public static class ActivityBean implements Serializable {
        /**
         * id : 5
         * clubId : 3
         * gmtCreate : 2018-06-02 17:42
         * presentor : 2
         * presentorName : 15874893244
         * title : 健康打底裤
         * content : 决定将洗净
         * applyDeadline : 2018-06-13 17:41
         * limitMember : 31
         * addressName : 长沙岳麓区保利地产
         * photos : [{"id":3,"photoId":"/info/file/pub.do?fileId=activity/20180602/2_20180602174201_71.png","bzId":5}]
         * isLike : 0
         */

        private int id;
        private int clubId;
        private String gmtCreate;
        private int presentor;
        private String presentorName;
        private String title;
        private String content;
        private String applyDeadline;
        private int limitMember;
        private String addressName;
        private int isLike;
        private ArrayList<PhotosBeanX> photos;

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

        public String getApplyDeadline() {
            return applyDeadline;
        }

        public void setApplyDeadline(String applyDeadline) {
            this.applyDeadline = applyDeadline;
        }

        public int getLimitMember() {
            return limitMember;
        }

        public void setLimitMember(int limitMember) {
            this.limitMember = limitMember;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public ArrayList<PhotosBeanX> getPhotos() {
            return photos;
        }

        public void setPhotos(ArrayList<PhotosBeanX> photos) {
            this.photos = photos;
        }

        public static class PhotosBeanX implements Serializable {
            /**
             * id : 3
             * photoId : /info/file/pub.do?fileId=activity/20180602/2_20180602174201_71.png
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
    }

    public static class LabelsBean implements Serializable {
        /**
         * id : 1
         * clubId : 1
         * name : 湖南大学
         */

        private int id;
        private int clubId;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UsersBean implements Serializable {
        private int memberId;
        private String nickname;
        private int role;
        private String header;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }

    public static class NoticeBean implements Serializable {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
