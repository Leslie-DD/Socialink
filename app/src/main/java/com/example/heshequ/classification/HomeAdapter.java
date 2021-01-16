package com.example.heshequ.classification;

/**
 * FileName: HomeAdapter
 * Author: Ding Yifan
 * Data: 2020/9/7
 * Time: 9:13
 * Description: 适应服务器数据库分类的Adapter
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heshequ.R;

import java.util.List;

/**
 * 右侧主界面ListView的适配器
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<ClassificationBean.DataBean> foodDatas;

    public HomeAdapter(Context context, List<ClassificationBean.DataBean> foodDatas) {
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
        ClassificationBean.DataBean dataBean = foodDatas.get(position);
        List<ClassificationBean.DataBean.Category2ListBean> dataList = dataBean.getCategory2List();
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.classifation_item_home, null);
            viewHold = new ViewHold();
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
            viewHold.blank = (TextView) convertView.findViewById(R.id.blank);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        HomeItemAdapter adapter = new HomeItemAdapter(context, dataList);
        viewHold.blank.setText(dataBean.getCategory1Name());
        viewHold.gridView.setAdapter(adapter);

        return convertView;
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;
        private TextView blank;
    }

}
