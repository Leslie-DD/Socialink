package com.leslie.socialink.network.entity;

import java.util.List;

public class HotQuestions {

    private int count;
    private int totalPage;
    private List<QuestionBean> list;

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

    public List<QuestionBean> getData() {
        return list;
    }

    public void setList(List<QuestionBean> list) {
        this.list = list;
    }
}
