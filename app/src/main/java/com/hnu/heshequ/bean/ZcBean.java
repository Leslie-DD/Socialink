package com.hnu.heshequ.bean;

import java.io.Serializable;


public class ZcBean implements Serializable {
    public String title;
    public String time;
    public String presentorName;
    public String presentorJob;
    public String presentor;
    public String id;
    public String beginTime;
    public String endTime;
    public String coverImage;
    public String introduction;
    public String isLike;
    public int likeAmount;
}

//        id：专场ID
//        title：专场标题
//        presentor：专场发布者UID
//        name：专场发布者姓名
//        job：专场发布者头衔
//        img：专场封面图片
//        time：专场发布时间
//        endday：专场截止时间

