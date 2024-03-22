package com.hnu.heshequ.bean;

import java.util.ArrayList;
import java.util.List;


public class HotAvtivityBean {

    /**
     * list : [{"id":1,"gmtCreate":"2018-06-02 15:04","presentor":1,"presentorName":"18774961742","header":"/info/file/pub.do?fileId=logo/20180528/1_20180528182708_118.jpg","title":"吃西瓜大赛","content":"谁吃的多谁就能得到如花香吻一个","applyDeadline":"2018-06-02 15:07","limitMember":200,"addressName":"湖南大学","like_amount":1,"comment_amount":1,"likes":[{"uid":1,"header":"/info/file/pub.do?fileId=logo/20180528/1_20180528182708_118.jpg","bzId":1}],"isLike":1}]
     * count : 1
     * totalPage : 1
     */

    private int count;
    private int totalPage;
    private ArrayList<HotBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public ArrayList<HotBean> getData() {
        return list;
    }

    public void setList(ArrayList<HotBean> list) {
        this.list = list;
    }

    public static class HotBean {

        /**
         * id : 31
         * clubId : 38
         * gmtCreate : 2018-07-05 15:42
         * presentor : 2
         * presentorName : 贺华
         * header : /info/file/pub.do?fileId=head/20180705/2_20180705112515_300.png
         * title : 2
         * content : 呃
         * applyDeadline : 2018-07-06 15:41
         * limitMember : 1
         * addressName : 尽快
         * likeAmount : 0
         * commentAmount : 0
         * photos : [{"id":18,"photoId":"/info/file/pub.do?fileId=activity/20180705/2_20180705154209_91.png","bzId":31}]
         * isLike : 0
         * time : 4天前
         * clubInfo : {"id":38,"name":"测试1","logoImage":"/info/file/pub.do?fileId=logo/20180705/2_20180705120138_877.png","creator":2,"creatorName":"贺华","memberNumber":1,"introduction":"该团队很懒，什么都没有留下"}
         */

        private int id;
        private int clubId;
        private String gmtCreate;
        private int presentor;
        private String presentorName;
        private String header;
        private String title;
        private String content;
        private String applyDeadline;
        private int limitMember;
        private String addressName;
        private int likeAmount;
        private int commentAmount;
        private int isLike;
        private String time;
        private ClubInfoBean clubInfo;
        private List<PhotosBean> photos;

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

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public ClubInfoBean getClubInfo() {
            return clubInfo;
        }

        public void setClubInfo(ClubInfoBean clubInfo) {
            this.clubInfo = clubInfo;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public static class ClubInfoBean {
            /**
             * id : 38
             * name : 测试1
             * logoImage : /info/file/pub.do?fileId=logo/20180705/2_20180705120138_877.png
             * creator : 2
             * creatorName : 贺华
             * memberNumber : 1
             * introduction : 该团队很懒，什么都没有留下
             * college : 中南大学
             */

            private int id;
            private String name;
            private String logoImage;
            private int creator;
            private String creatorName;
            private int memberNumber;
            private String introduction;
            private String college;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCollege() {
                return college;
            }

            public void setCollege(String college) {
                this.college = college;
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
        }

        public static class PhotosBean {
            /**
             * id : 18
             * photoId : /info/file/pub.do?fileId=activity/20180705/2_20180705154209_91.png
             * bzId : 31
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
}
