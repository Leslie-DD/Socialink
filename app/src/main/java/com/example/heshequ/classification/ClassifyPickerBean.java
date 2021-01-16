package com.example.heshequ.classification;

/**
 * FileName: ClassifyPickerBean
 * Author: Ding Yifan
 * Data: 2020/9/7
 * Time: 15:20
 * Description:
 */
public class ClassifyPickerBean {
    private String category;
    private int id;

    public ClassifyPickerBean(){ }
    public ClassifyPickerBean(String category, int id){
        this.category = category;;
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString(){
        return "category: "+category+"; id: "+id;
    }

}
