package com.hnu.heshequ.entity;



public class VersionBean {

    /**
     * id : 5
     * vercode : 2.8
     * vername : 测试4
     * verdesc :
     * appsize : 10
     * apppath : /info/file/pub.do?fileId=/info/file/pub.do?fileId=qrcode/20180921/10_20180921163033_214.apk
     * uptime : 1537518638000
     * apptype : 1
     */

    private int id;
    private String vercode;
    private String vername;
    private String verdesc;
    private int appsize;
    private String apppath;
    private long uptime;
    private int apptype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }

    public String getVername() {
        return vername;
    }

    public void setVername(String vername) {
        this.vername = vername;
    }

    public String getVerdesc() {
        return verdesc;
    }

    public void setVerdesc(String verdesc) {
        this.verdesc = verdesc;
    }

    public int getAppsize() {
        return appsize;
    }

    public void setAppsize(int appsize) {
        this.appsize = appsize;
    }

    public String getApppath() {
        return apppath;
    }

    public void setApppath(String apppath) {
        this.apppath = apppath;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public int getApptype() {
        return apptype;
    }

    public void setApptype(int apptype) {
        this.apptype = apptype;
    }

    @Override
    public String toString() {
        return id + "\n" +
                vercode + "\n" +
                vername + "\n" +
                verdesc + "\n" +
                appsize + "\n" +
                apppath + "\n" +
                uptime + "\n" +
                apptype;
    }
}
