package com.example.heshequ.entity;



public class ScanResultEvent {

    public ScanResultEvent(String result) {
        this.result = result;
    }

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
