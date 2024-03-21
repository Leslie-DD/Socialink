package com.hnu.heshequ.bean.knowledge;

import java.io.Serializable;

public class ArticleBean implements Serializable {
    public String createTime;
    public String updateTime;
    public int id;
    public int specialColumnId;
    public String title;
    public String content;
    public String label;
    public int authorId;
    public double price;
    public double score;
    public int likeNum;
    public int unlikeNum;
    public int viewNum;
    public String status;
    public Author author;
    public int commentNum;
}
