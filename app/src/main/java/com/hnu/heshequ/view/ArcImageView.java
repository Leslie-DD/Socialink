package com.hnu.heshequ.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.hnu.heshequ.utils.Utils;


/**
 * Created by dev06 on 2018/5/29.
 */
public class ArcImageView extends androidx.appcompat.widget.AppCompatImageView {

    private static final String TAG = "ArcImageView";

    private float radius;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radius = Utils.dip2px(context, 15);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}
