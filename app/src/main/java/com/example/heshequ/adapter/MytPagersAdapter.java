package com.example.heshequ.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2017/7/18 09:51
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class MytPagersAdapter extends FragmentStatePagerAdapter {
    List<Fragment> list = new ArrayList<>();
    FragmentManager fm;
    String string;

    public MytPagersAdapter(FragmentManager fm, List<Fragment> list) {
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
//    @Override
//    public void destroyItem(View arg0, int arg1, Object arg2) {
//        ((ViewPager) arg0).removeView((View) arg2);
//    }
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//    public int getItemPosition(Object object){
//        Log.e("zhang","--getItemPosition--");
//        return POSITION_NONE;
//    }


//    @Override
//    public int getItemPosition(Object object) {
//        Log.e("zhang","--object--");
//        if (string.equals("del")){
//            Log.e("zhang","--getItemPosition--");
//            return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
//    }
}
