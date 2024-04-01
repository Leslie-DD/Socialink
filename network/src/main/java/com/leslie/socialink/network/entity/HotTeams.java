package com.leslie.socialink.network.entity;

import java.util.List;

public class HotTeams {

    private int count;
    private int totalPage;
    private List<TeamBean> list;

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

    public List<TeamBean> getData() {
        return list;
    }

    public void setList(List<TeamBean> list) {
        this.list = list;
    }
}
