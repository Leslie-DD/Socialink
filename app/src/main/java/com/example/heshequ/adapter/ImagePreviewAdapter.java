package com.example.heshequ.adapter;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.heshequ.activity.team.ImagePreviewActivity;
import com.example.heshequ.utils.Utils;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * @Description:
 * @Author: Liangchaojie
 * @Create On 2018/3/30 10:33
 */
public class ImagePreviewAdapter extends PagerAdapter {
    private ImagePreviewActivity context;
    private List<String> imageList;
    private int itemPosition;
    private PhotoView photoView;
    public ImagePreviewAdapter(ImagePreviewActivity context, List<String> imageList, int itemPosition) {
        this.context = context;
        this.imageList = imageList;
        this.itemPosition = itemPosition;
    }

    @Override
    public int getCount() {
        return imageList==null?0:imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView image = new PhotoView(context);
        image.setEnabled(true);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setMaximumScale(2.0F);
        image.setMinimumScale(0.8F);
        Glide.with(context).load(imageList.get(position)).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.rlDel.getVisibility() == View.VISIBLE) {
                    context.rlDel.setVisibility(View.GONE);
                } else {
                    context.rlDel.setVisibility(View.VISIBLE);
                }
            }
        });
        container.addView(image);
        return image;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        photoView = (PhotoView) object;
        photoView.setTag(Utils.getNameByPosition(itemPosition,position));
        photoView.setTransitionName(Utils.getNameByPosition(itemPosition,position));
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public PhotoView getPhotoView() {
        return photoView;
    }

}
