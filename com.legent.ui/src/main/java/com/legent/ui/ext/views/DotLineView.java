package com.legent.ui.ext.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.legent.ui.R;


/**
 * Created by sylar on 15/6/13.
 */
public class DotLineView extends View {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    final static int DOT_COLOR = Color.GRAY;
    final static int DOT_RADIUS = 2;
    final static int DASH_GAP = 4;

    Paint paint;
    int dotColor = DOT_COLOR;
    int dotRadius = DOT_RADIUS;
    int dashGap = DASH_GAP;
    int orientation = HORIZONTAL;

    public DotLineView(Context context) {
        super(context);
        init(context, null);
    }

    public DotLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context cx, AttributeSet attrs) {

        if (attrs != null) {

            TypedArray a = cx.obtainStyledAttributes(attrs, R.styleable.DotLineView);
            dotColor = a.getColor(R.styleable.DotLineView_dot_line_color, DOT_COLOR);
            dotRadius = a.getDimensionPixelSize(R.styleable.DotLineView_dot_line_dot_radius, DOT_RADIUS);
            dashGap = a.getDimensionPixelSize(R.styleable.DotLineView_android_dashGap, DASH_GAP);
            orientation = a.getInt(R.styleable.DotLineView_android_orientation, HORIZONTAL);
            a.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(dotColor);
    }


    @Override
    public void draw(Canvas c) {
        super.draw(c);

        if (orientation == HORIZONTAL) {
            int cy = getPaddingTop() + (int) ((getHeight() - getPaddingTop() - getPaddingBottom()) / 2f);
            for (int cx = getPaddingLeft() + dotRadius; cx < getWidth() - getPaddingRight(); cx += dashGap + dotRadius) {
                c.drawCircle(cx, cy, dotRadius, paint);
            }
        } else {
            int cx = getPaddingLeft() + (int) ((getWidth() - getPaddingLeft() - getPaddingRight()) / 2f);
            for (int cy = getPaddingTop() + dotRadius; cy < getHeight() - getPaddingBottom(); cy += dashGap + dotRadius) {
                c.drawCircle(cx, cy, dotRadius, paint);
            }
        }
    }
}
