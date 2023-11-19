package com.example.heshequ.activity.newsencond;

/**
 * @FileName: Good
 * @Author: Ding Yifan
 * @CreateDate: 2021/1/2
 * @CreateTime: 21:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/2
 * @UpdateTime: 21:35
 * @UpdateRemark: 更新说明
 * @Description:
 */
public class Good {
    private String name;
    private String image;
    private float price;

    public Good(String name, String image, float price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }

    public float getPrice() {
        return this.price;
    }

    public String printGood() {
        return "name: " + this.name + "\nprice: " + this.price;
    }
}
