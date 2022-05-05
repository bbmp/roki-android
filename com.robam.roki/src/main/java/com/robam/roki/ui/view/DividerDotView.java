package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.robam.roki.R;

/**
 * Created by sylar on 15/6/13.
 */
public class DividerDotView extends View {

    final static float MARGIN = 10f;
    Paint paint;

    public DividerDotView(Context context) {
        super(context);
        init(context, null);
    }

    public DividerDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DividerDotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context cx, AttributeSet attrs) {
        int dotColor = cx.getResources().getColor(R.color.recipe_banner_text);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(dotColor);
    }


    @Override
    public void draw(Canvas c) {
        super.draw(c);

        int width = getWidth();
        float radius = getHeight() / 2f - 1f;
        float cy = getHeight() / 2f;

        for (float cx = 2f; cx < width - radius - 2f; cx += MARGIN + radius) {
            c.drawCircle(cx, cy, radius, paint);
        }
    }


}
