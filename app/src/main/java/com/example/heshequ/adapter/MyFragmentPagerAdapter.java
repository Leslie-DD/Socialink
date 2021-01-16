package com.example.heshequ.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2017/7/18 09:51
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> list = new ArrayList<>();
    FragmentManager fm;
    String string;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.list = list;
    }

    public void setList(List<Fragment> list, String string) {
        this.string = string;
        //Log.e("zhang","刷新？？？？？");
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

}
