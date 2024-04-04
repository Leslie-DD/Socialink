package com.leslie.socialink.me.utils

import com.leslie.socialink.R
import com.leslie.socialink.bean.ItemBean
import com.leslie.socialink.network.Constants

object ItemsUtil {

    fun initItems(): List<ItemBean> {
        val list: MutableList<ItemBean> = mutableListOf()
        var bean = ItemBean()
        bean.name = "我的团队"
        bean.resId = R.mipmap.myteam
        list.add(bean)

//        bean = new ItemBean();
//        bean.setName("我的创作");
//        bean.setResId(R.mipmap.myteam);
//        data.add(bean);
        bean = ItemBean()
        bean.name = "我的收藏"
        bean.resId = R.mipmap.saved
        list.add(bean)

        bean = ItemBean()
        bean.name = "我的足迹"
        bean.resId = R.mipmap.zuji
        list.add(bean)

        bean = ItemBean()
        bean.name = "我的拉黑"
        bean.resId = R.mipmap.pulltheblack
        list.add(bean)

        bean.status = 1
        bean = ItemBean()
        bean.name = "实名认证"
        bean.resId = R.mipmap.certified
        bean.tip = "未认证"
        list.add(bean)

        bean = ItemBean()
        bean.name = "意见反馈"
        bean.resId = R.mipmap.fdbk
        list.add(bean)

        bean = ItemBean()
        bean.name = "修改密码"
        bean.resId = R.mipmap.epswd
        list.add(bean)

        bean = ItemBean()
        bean.name = "设置"
        bean.resId = R.mipmap.setting
        list.add(bean)

        if (Constants.uid == 1) {
            bean = ItemBean()
            bean.name = "点击登录"
            bean.resId = R.mipmap.esc
            list.add(bean)
        } else {
            bean = ItemBean()
            bean.name = "退出登录"
            bean.resId = R.mipmap.esc
            list.add(bean)
        }
        return list
    }
}