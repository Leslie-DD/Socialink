package com.leslie.socialink.entity;

import com.leslie.socialink.network.Constants;

import java.io.Serializable;
import java.util.ArrayList;


public class TeamTestBean implements Serializable {

    /**
     * id : 9
     * bzid : 6
     * type : 2
     * obj : {"id":6,"clubId":3,"gmtCreate":"2018-06-04 16:57","presentor":2,"presentorName":"15874893244","title":"计算机1","content":"那等你下课23","applyDeadline":"2018-06-12 16:56","limitMember":10,"addressName":"麓谷林语","likeAmount":0,"commentAmount":0,"photos":[{"id":5,"photoId":"/info/file/pub.do?fileId=activity/20180604/2_20180604165707_993.png","bzId":6},{"id":6,"photoId":"/info/file/pub.do?fileId=activity/20180606/2_20180606182831_767.png","bzId":6},{"id":8,"photoId":"/info/file/pub.do?fileId=activity/20180606/2_20180606184124_64.png","bzId":6}],"likes":[{"uid":2,"bzId":6}],"isLike":0}
     */

    private int id;
    private int bzid;
    private int type;
    private ArrayList<String> imgs;

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public void setImgs() {
        if (obj.getPhotos() != null) {
            imgs = new ArrayList<>();
            for (int i = 0; i < obj.getPhotos().size(); i++) {
                imgs.add(Constants.BASE_URL + obj.getPhotos().get(i).getPhotoId());
            }
        }
    }

    /**
     * id : 6
     * clubId : 3
     * gmtCreate : 2018-06-04 16:57
     * presentor : 2
     * presentorName : 15874893244
     * title : 计算机1
     * content : 那等你下课23
     * applyDeadline : 2018-06-12 16:56
     * limitMember : 10
     * addressName : 麓谷林语
     * likeAmount : 0
     * commentAmount : 0
     * photos : [{"id":5,"photoId":"/info/file/pub.do?fileId=activity/20180604/2_20180604165707_993.png","bzId":6},{"id":6,"photoId":"/info/file/pub.do?fileId=activity/20180606/2_20180606182831_767.png","bzId":6},{"id":8,"photoId":"/info/file/pub.do?fileId=activity/20180606/2_20180606184124_64.png","bzId":6}]
     * likes : [{"uid":2,"bzId":6}]
     * isLike : 0
     */

    private ObjBean obj;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBzid() {
        return bzid;
    }

    public void setBzid(int bzid) {
        this.bzid = bzid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable {
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
        private int likeAmount;
        private int commentAmount;
        private int isLike;
        private String time;
        private int status, isVote, category;
        private String name;
        private String deadline;
        private String header, introduction;
        private ClubInfo clubInfo;

        public ClubInfo getClubInfo() {
            return clubInfo;
        }

        public void setClubInfo(ClubInfo clubInfo) {
            this.clubInfo = clubInfo;
        }

        public static class ClubInfo implements Serializable {
            private String name;
            private int isJoin;
            private boolean admin;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIsJoin() {
                return isJoin;
            }

            public void setIsJoin(int isJoin) {
                this.isJoin = isJoin;
            }

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }
        }

        public int getIsVote() {
            return isVote;
        }

        public void setIsVote(int isVote) {
            this.isVote = isVote;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        /**
         * id : 5
         * photoId : /info/file/pub.do?fileId=activity/20180604/2_20180604165707_993.png
         * bzId : 6
         */


        private ArrayList<PhotosBean> photos;
        /**
         * uid : 2
         * bzId : 6
         */

        private ArrayList<LikesBean> likes;

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
            private int bzId;
            private String header;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getBzId() {
                return bzId;
            }

            public void setBzId(int bzId) {
                this.bzId = bzId;
            }

            public String getHeader() {
                return header;
            }

            public void setHeader(String header) {
                this.header = header;
            }
        }
    }
}
