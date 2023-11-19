package com.example.heshequ.entity;

import java.util.ArrayList;

/**
 * Created by Dengdongqi on 2018/7/7.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class VoteJsonBean {
    private int category;
    private String content;
    private ArrayList<Option> options;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public static class Option {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
