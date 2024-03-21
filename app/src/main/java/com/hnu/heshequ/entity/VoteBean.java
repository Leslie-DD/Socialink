package com.hnu.heshequ.entity;

import java.util.ArrayList;

/**
 * Created by dev06 on 2018/6/5.
 */
public class VoteBean {
    private int id;
    private String name;
    private String content;
    private String endTime;
    private ArrayList<VoteItem> list;


    public int getId() {
        return id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ArrayList<VoteItem> getList() {
        return list;
    }

    public void setList(ArrayList<VoteItem> list) {
        this.list = list;
    }

    public class VoteItem {
        private int voteId;
        private String theme;
        private int type;  // 0 单  1 双
        private ArrayList<Item> data = new ArrayList<>();

        public int getVoteId() {
            return voteId;
        }

        public void setVoteId(int id) {
            this.voteId = id;
        }

        public VoteItem() {
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ArrayList<Item> getData() {
            return data;
        }

        public void setData(ArrayList<Item> data) {
            this.data = data;
        }
    }
}
