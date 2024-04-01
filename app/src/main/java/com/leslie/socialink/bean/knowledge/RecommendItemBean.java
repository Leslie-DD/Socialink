package com.leslie.socialink.bean.knowledge;

import java.io.Serializable;

public class RecommendItemBean implements Serializable {
    public String createTime;
    public String updateTime;
    public Integer id;
    public Integer specialColumnId;
    public String title;
    public String content;
    public String label;
    public Integer authorId;
    public Double price;
    public Double score;
    public Integer likeNum;
    public Integer unlikeNum;
    public Integer viewNum;
    public String status;
    public Author author;
    public Integer commentNum;
}
