package com.leslie.socialink.bean.knowledge;

import java.io.Serializable;

public class SubscriptionItemBean implements Serializable {
    public String createTime;
    public String updateTime;
    public Integer id;
    public String name;
    public Integer authorId;
    public String authorSummary;
    public String summary;
    public String readerSummary;
    public Double price;
    public Double score;
    public Integer subscriptionNum;
    public Integer passageNum;
    public String status;
    public Author author;
}
