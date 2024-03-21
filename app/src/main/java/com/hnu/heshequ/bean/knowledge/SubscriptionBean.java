package com.hnu.heshequ.bean.knowledge;

import java.io.Serializable;

public class SubscriptionBean implements Serializable {
    public String createTime;
    public String updateTime;
    public int id;
    public String name;
    public String summary;
    public int authorId;
    public double price;
    public double score;
    public int subscriptionNum;
    public String status;
    public Author author;
}
