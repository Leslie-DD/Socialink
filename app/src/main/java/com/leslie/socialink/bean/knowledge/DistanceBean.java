package com.leslie.socialink.bean.knowledge;

/**
 * FileName: DistanceBean
 * Author: Ding Yifan
 * Data: 2020/9/4
 * Time: 16:09
 * Description:
 */
public class DistanceBean {
    private String juli;
    private int distance;

    public DistanceBean() {
    }

    public DistanceBean(String juli, int distance) {
        this.juli = juli;
        this.distance = distance;
    }

    public String getJuli() {
        return juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String toString() {
        return "juli: " + juli + "; distance: " + distance;
    }
}
