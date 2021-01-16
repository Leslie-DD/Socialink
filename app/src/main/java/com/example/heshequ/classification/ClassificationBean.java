package com.example.heshequ.classification;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: ClassificationBean
 * Author: Ding Yifan
 * Data: 2020/9/6
 * Time: 23:10
 * Description:
 */
public class ClassificationBean implements Serializable {

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {


        private int category1Id;
        private String category1Name;
        private String category1Record;
        private String gmtCreate;
        private String gmtModified;
        private int showFlag;
        private int delFlag;
        private List<Category2ListBean> category2List;

        public int getCategory1Id() {
            return category1Id;
        }

        public void setCategory1Id(int category1Id) {
            this.category1Id = category1Id;
        }

        public String getCategory1Name() {
            return category1Name;
        }

        public void setCategory1Name(String category1Name) {
            this.category1Name = category1Name;
        }

        public String getCategory1Record() {
            return category1Record;
        }

        public void setCategory1Record(String category1Record) {
            this.category1Record = category1Record;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public int getShowFlag() {
            return showFlag;
        }

        public void setShowFlag(int showFlag) {
            this.showFlag = showFlag;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public List<Category2ListBean> getCategory2List() {
            return category2List;
        }

        public void setCategory2List(List<Category2ListBean> category2List) {
            this.category2List = category2List;
        }


        public static class Category2ListBean {

            private int category2Id;
            private String category2Name;
            private String category2Record;
            private String category1Id;
            private String url;
            private String gmtCreate;
            private String gmtModified;
            private int showFlag;
            private int delFlag;

            public int getCategory2Id() {
                return category2Id;
            }

            public void setCategory2Id(int category2Id) {
                this.category2Id = category2Id;
            }

            public String getCategory2Name() {
                return category2Name;
            }

            public void setCategory2Name(String category2Name) {
                this.category2Name = category2Name;
            }

            public String getCategory2Record() {
                return category2Record;
            }

            public void setCategory2Record(String category2Record) {
                this.category2Record = category2Record;
            }

            public String getCategory1Id() {
                return category1Id;
            }

            public void setCategory1Id(String category1Id) {
                this.category1Id = category1Id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getGmtCreate() {
                return gmtCreate;
            }

            public void setGmtCreate(String gmtCreate) {
                this.gmtCreate = gmtCreate;
            }

            public String getGmtModified() {
                return gmtModified;
            }

            public void setGmtModified(String gmtModified) {
                this.gmtModified = gmtModified;
            }

            public int getShowFlag() {
                return showFlag;
            }

            public void setShowFlag(int showFlag) {
                this.showFlag = showFlag;
            }

            public int getDelFlag() {
                return delFlag;
            }

            public void setDelFlag(int delFlag) {
                this.delFlag = delFlag;
            }
        }
    }
}

