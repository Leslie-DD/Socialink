package com.leslie.socialink.entity;

import java.util.List;


public class T {

    /**
     * id : 4
     * gmtCreate : 2018-05-31
     * name : 豆子测试2
     * logoImage : /info/file/pub.do?fileId=logo/20180531/2_20180531180619_768.png
     * creator : 2
     * memberNumber : 1
     * introduction : 该团队很懒，什么都没有留下
     * yesterdayTraffic : 0
     * settingVisible : 0
     * settingSpeak : 0
     * settingActivity : 0
     * settingNotice : 0
     * settingVote : 0
     * status : 0
     * isFavorite : 0
     * collectionNumber : 0
     * labels : [{"id":4,"clubId":4,"name":"湖南大学"},{"id":5,"clubId":4,"name":"相声"},{"id":6,"clubId":4,"name":"中南大学"}]
     * users : [{"memberId":2,"nickname":"15874893244","role":1}]
     */

    private int id;
    private String gmtCreate;
    private String name;
    private String logoImage;
    private int creator;
    private int memberNumber;
    private String introduction;
    private int yesterdayTraffic;
    private int settingVisible;
    private int settingSpeak;
    private int settingActivity;
    private int settingNotice;
    private int settingVote;
    private int status;
    private int isFavorite;
    private int collectionNumber;
    /**
     * id : 4
     * clubId : 4
     * name : 湖南大学
     */

    private List<LabelsBean> labels;
    /**
     * memberId : 2
     * nickname : 15874893244
     * role : 1
     */

    private List<UsersBean> users;

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

    public List<LabelsBean> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelsBean> labels) {
        this.labels = labels;
    }

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class LabelsBean {
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

    public static class UsersBean {
        private int memberId;
        private String nickname;
        private int role;

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
}
