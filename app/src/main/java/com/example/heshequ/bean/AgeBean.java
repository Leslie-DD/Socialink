package com.example.heshequ.bean;

/**
 * FileName: AgeBean
 * Author: Ding Yifan
 * Data: 2020/9/4
 * Time: 16:44
 * Description:
 */
public class AgeBean {
    private String nianling;
    private int age;

    public AgeBean() {
    }

    public AgeBean(String nianling, int age) {
        this.nianling = nianling;
        this.age = age;
    }

    public String getNianling() {
        return nianling;
    }

    public void setNianling(String nianling) {
        this.nianling = nianling;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
