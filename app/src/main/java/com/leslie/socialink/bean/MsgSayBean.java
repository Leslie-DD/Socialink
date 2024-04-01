package com.leslie.socialink.bean;

import java.util.ArrayList;


public class MsgSayBean {

    /**
     * list : [{"id":23,"replyId":5,"receiveId":4,"bizId":46,"content":"哈哈哈","gmtCreate":"2018-07-09 11:47","status":1,"time":"2分钟前","nickName":"18373712587","header":null,"clubName":null},{"id":22,"replyId":5,"receiveId":4,"bizId":61,"content":"哈哈哈","gmtCreate":"2018-07-09 11:46","status":1,"time":"3分钟前","nickName":"18373712587","header":null,"clubName":null}]
     * count : 2
     * totalPage : 1
     */

    private int count;
    private int totalPage;
    private ArrayList<SayBean> list;

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

    public ArrayList<SayBean> getData() {
        return list;
    }

    public void setList(ArrayList<SayBean> list) {
        this.list = list;
    }

    public static class SayBean {
        /**
         * id : 23
         * replyId : 5
         * receiveId : 4
         * bizId : 46
         * content : 哈哈哈
         * gmtCreate : 2018-07-09 11:47
         * status : 1
         * time : 2分钟前
         * nickName : 18373712587
         * header : null
         * clubName : null
         */

        private int id;
        private int replyId;
        private int receiveId;
        private int hisid;
        private int bizId;
        private String content;
        private String gmtCreate;
        private int status;
        private String time;
        private String nickName;
        private String header;
        private String clubName;
        private String senderNickname;
        private int objectId;
        private String objectNickname;

        public void setObjectNickname(String objectNickname) {
            this.objectNickname = objectNickname;
        }

        public String getObjectNickname() {
            return objectNickname;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }

        public int getObjectId() {
            return objectId;
        }

        public String getSenderNickname() {
            return senderNickname;
        }

        public void setSenderNickname(String senderNickname) {
            this.senderNickname = senderNickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getHisid() {
            return hisid;
        }

        public void setHisid(int hisid) {
            this.hisid = hisid;
        }

        public int getReplyId() {
            return replyId;
        }

        public void setReplyId(int replyId) {
            this.replyId = replyId;
        }

        public int getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(int receiveId) {
            this.receiveId = receiveId;
        }

        public int getBizId() {
            return bizId;
        }

        public void setBizId(int bizId) {
            this.bizId = bizId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getClubName() {
            return clubName;
        }

        public void setClubName(String clubName) {
            this.clubName = clubName;
        }
    }
}
