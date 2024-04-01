package com.leslie.socialink.classification;

/**
 * FileName: ClassifySecondaryBean
 * Author: Ding Yifan
 * Data: 2020/9/8
 * Time: 9:32
 * Description:
 */
public class ClassifySecondaryBean {
    private String category1Name;
    private String category2Name;
    private int category1Id;
    private int category2Id;

    public ClassifySecondaryBean() {
    }

    public ClassifySecondaryBean(String category1Name, String category2Name, int category1Id, int category2Id) {
        this.category1Name = category1Name;
        this.category2Name = category2Name;
        this.category1Id = category1Id;
        this.category2Id = category2Id;

    }

    public String getCategory2Name() {
        return category2Name;
    }

    public void setCategory2Name(String categroy2Name) {
        this.category2Name = categroy2Name;
    }

    public String getCategory1Name() {
        return category1Name;
    }

    public void setCategory1Name(String category1Name) {
        this.category1Name = category1Name;
    }

    public int getCategory1Id() {
        return category1Id;
    }

    public void setCategory1Id(int category1Id) {
        this.category1Id = category1Id;
    }

    public int getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(int category2Id) {
        this.category2Id = category2Id;
    }

    public String toString() {
        return "category1Name: " + getCategory1Name()
                + "; category1Id: " + getCategory1Id()
                + "; category2Name: " + getCategory2Name()
                + "; category2Id: " + getCategory2Id();
    }
}
