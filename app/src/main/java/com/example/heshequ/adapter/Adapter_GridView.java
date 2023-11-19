package com.example.heshequ.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.heshequ.R;
import com.example.heshequ.utils.Utils;

import java.util.ArrayList;

public class Adapter_GridView extends BaseAdapter {

    // 上下文对象

    private Context context;

    // 图片数组

    private ArrayList<String> url;

    private LayoutParams layoutParams;

    private int mImageWidth2, width, height;
    private int mImageWidth;

    private int mImageWidth3;
    private Dialog dialog;
    private ArrayList<Bitmap> data;
    // private ImageView iv;

    float mx;

    private float my;

    public Adapter_GridView(Context context, ArrayList<String> url) {

        this.context = context;

        this.url = url;

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;

        int io = (int) context.getResources().getDimension(R.dimen.gv_width);
        mImageWidth = (outMetrics.widthPixels - io - Utils.dip2px(context, 57)) / 3;
        mImageWidth3 = (int) ((outMetrics.widthPixels - 12 * outMetrics.density - io - Utils.dip2px(context, 20)) / 3);
        mImageWidth2 = (int) ((outMetrics.widthPixels - 12 * outMetrics.density - io - Utils.dip2px(context, 10)) / 2);
    }

    public int getCount() {

        return url == null ? 0 : url.size();

    }

    public Object getItem(int item) {

        return item;

    }

    public long getItemId(int id) {

        return id;

    }

    // 创建View方法

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ImageView imageView;

        int x = 0;
        boolean onlyOne = false;
        if (convertView == null) {

            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.mipmap.mrtp);
            // imageView.setMaxHeight(mImageWidth2 * 2 + 20);
            imageView.setMaxHeight(500);
            imageView.setMaxWidth(width - 50);

            switch (url.size()) {

                case 1:

                    imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,
                            GridView.LayoutParams.WRAP_CONTENT));// 设置ImageView对象布局
//				imageView.setLayoutParams(new GridView.LayoutParams(Utils.dip2px(context,150),
//						Utils.dip2px(context,250)));// 设置ImageView对象布局
                    // imageView.setLayoutParams(new
                    // GridView.LayoutParams(mImageWidth2 + 30, mImageWidth2 * 2 +
                    // 20));
                    onlyOne = true;
//				imageView.setPadding(50, 4, 4, 50);// 设置间距
                    break;

                case 2:

                    // imageView.setLayoutParams(new
                    // GridView.LayoutParams(mImageWidth2, mImageWidth2));
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth2, mImageWidth2));
                    x = mImageWidth2;
                    onlyOne = false;

                    break;

                case 4:

                    // imageView.setLayoutParams(new
                    // GridView.LayoutParams(mImageWidth2, mImageWidth2));
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth2, mImageWidth2));
                    x = mImageWidth2;
                    onlyOne = false;

                    break;

                default:

                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth3, mImageWidth3));// 设置ImageView对象布局
                    x = mImageWidth3;
                    onlyOne = false;

                    break;

            }

            imageView.setAdjustViewBounds(false);// 设置边界对齐
            convertView = imageView;
            // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//
            // 设置刻度的类型


        } else {

            imageView = (ImageView) convertView;

        }

        // if (x != 0) {
        // Glide.with(context).load(url.get(position)).placeholder(R.drawable.zlogo1).override(x,
        // x).into(imageView);
        // } else {
        if (url.size() == 1) {

            Glide.with(context).load(url.get(position)).priority(Priority.HIGH).placeholder(R.mipmap.mrtp)
                    .fitCenter().override(width - 50, mImageWidth2 * 2 + 20).into(imageView);

        } else {

            Glide.with(context).load(url.get(position)).placeholder(R.mipmap.mrtp).fitCenter().into(imageView);

        }
        return imageView;

    }

}
