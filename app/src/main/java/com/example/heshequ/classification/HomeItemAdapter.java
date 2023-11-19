package com.example.heshequ.classification;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.activity.newsencond.SearchGoodActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * FileName: HomeItemAdapter2
 * Author: Ding Yifan
 * Data: 2020/9/7
 * Time: 9:15
 * Description: 适应服务器数据库分类的商品Adapter
 */
public class HomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<ClassificationBean.DataBean.Category2ListBean> foodDatas;

    public HomeItemAdapter(Context context, List<ClassificationBean.DataBean.Category2ListBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ClassificationBean.DataBean.Category2ListBean subcategory = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.classifation_item_home_category, null);
            viewHold = new ViewHold();
            viewHold.ll_category2 = (LinearLayout) convertView.findViewById(R.id.ll_categroy2);
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (SimpleDraweeView) convertView.findViewById(R.id.item_album);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        /**
         * 二级分类点击能得到二级分类的id
         * 这里得不到一级分类的id
         */
        viewHold.ll_category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int category2Id = subcategory.getCategory2Id();
                String category2Name = subcategory.getCategory2Name();
//                 Utils.toastShort(context, "category2Id:"+category2Id+", category2Name:"+category2Name);
                Intent intent = new Intent(context, SearchGoodActivity.class);
                Bundle bundle = new Bundle();   // getActivity().getIntent().getExtras();
                bundle.putInt("type", 1);
                bundle.putInt("category2_id", category2Id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        viewHold.tv_name.setText(subcategory.getCategory2Name());
        Uri uri = Uri.parse(subcategory.getUrl());
        Log.e("图片url", uri + "");
        viewHold.iv_icon.setImageURI(uri);
        return convertView;

    }

    private static class ViewHold {
        private LinearLayout ll_category2;
        private TextView tv_name;
        private SimpleDraweeView iv_icon;

    }

}
