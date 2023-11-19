package com.example.heshequ.entity;

import java.io.Serializable;

/**
 * Created by dev06 on 2018/5/17.
 */
public class TeamMemberBean implements Comparable<TeamMemberBean>, Serializable {
    private String header, initialLetter;
    private String nickname;
    private String userName;
    private int role;
    private int grade;
    private int status;
    private int memberId;
    private int id;
    private int count;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInitialLetter() {
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

   /* public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }*/

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(TeamMemberBean rhs) {
        if (getInitialLetter().equals(rhs.getInitialLetter())) {
            return getNickname().compareTo(rhs.getNickname());
        } else {
            if ("#".equals(getInitialLetter())) {
                return 1;
            } else if ("#".equals(rhs.getInitialLetter())) {
                return -1;
            }
            return getInitialLetter().compareTo(rhs.getInitialLetter());
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
