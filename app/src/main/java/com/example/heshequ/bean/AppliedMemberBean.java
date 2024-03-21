package com.example.heshequ.bean;



public class AppliedMemberBean {
    /**
     * uid : 4
     * header : head/20180706/4_20180706170038_139.jpg
     * nickname : 123
     * role : 1
     * grade : 1
     */
    private int uid;
    private String header;
    private String nickname;
    private int role;
    private int grade;
    private String telephone;
    private String college;
    private String name;
    private int sex;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[" +
                "uid=" + uid +
                ", header='" + header + '\'' +
                ", nickname='" + nickname + '\'' +
                ", role=" + role +
                ", grade=" + grade +
                ", telephone='" + telephone + '\'' +
                ", college='" + college + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ']';
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
