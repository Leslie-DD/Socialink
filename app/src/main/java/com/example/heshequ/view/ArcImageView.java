package com.example.heshequ.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.heshequ.utils.Utils;


/**
 * Created by dev06 on 2018/5/29.
 */
public class ArcImageView extends ImageView {
    /*
     *弧形高度
     */
    private int mArcHeight;
    private static final String TAG = "ArcImageView";
    private Context context;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
//        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        int th=Utils.dip2px(context,270);
        int h150=Utils.dip2px(context,200);
        path.lineTo(0, h150);
        path.quadTo(getWidth() / 2, th , getWidth(), h150);
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}
