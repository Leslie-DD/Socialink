package com.hnu.heshequ.entity;


public class RefTDteamEvent {

    private boolean isDel;
    private int[] ref;

    public RefTDteamEvent(int[] ref) {
        this.ref = ref;
    }

    public RefTDteamEvent(int[] ref, boolean isDel) {
        this.ref = ref;
        this.isDel = isDel;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }


    public int[] getRef() {
        return ref;
    }

    public void setRef(int[] ref) {
        this.ref = ref;
    }
}
