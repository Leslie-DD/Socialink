package com.hnu.heshequ.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.utils.Utils;

import java.util.List;

/**
 * 一般项目就实现NineGridLayout类即可，如果没有特殊需求，不要改动NineGridLayout类
 */
public class NineGridTestLayout extends NineGridLayout {

    private Context context;
    private int itemPosition;
    private OnItemPictureClickListener listener;

    public NineGridTestLayout(Context context) {
        this(context, null);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void displayImage(int position, RatioImageView imageView, String url) {
        if (context != null) {
            Glide.with(context).load(url).into(imageView);
            imageView.setTag(Utils.getNameByPosition(itemPosition, position));
            imageView.setTransitionName(Utils.getNameByPosition(itemPosition, position));
        }
    }

    @Override
    protected void onClickImage(int imageIndex, String url, List<String> urlList, ImageView imageView) {
        listener.onItemPictureClick(itemPosition, imageIndex, url, urlList, imageView);
    }


    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setListener(OnItemPictureClickListener listener) {
        this.listener = listener;
    }
}
