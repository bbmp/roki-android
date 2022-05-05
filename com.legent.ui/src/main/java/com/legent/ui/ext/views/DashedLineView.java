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
public class DashedLineView extends View {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    final static int Line_COLOR = Color.GRAY;
    final static int DASH_WIDTH = 2;
    final static int DASH_GAP = 5;


    Paint paint;
    int lineColor = Line_COLOR;
    int dashWidth = DASH_WIDTH;
    int dashGap = DASH_GAP;
    int orientation = HORIZONTAL;


    public DashedLineView(Context context) {
        super(context);
        init(context, null);
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context cx, AttributeSet attrs) {


        if (attrs != null) {

            TypedArray a = cx.obtainStyledAttributes(attrs, R.styleable.DashedLineView);
            lineColor = a.getColor(R.styleable.DashedLineView_dashed_line_color, Line_COLOR);
            dashWidth = a.getDimensionPixelSize(R.styleable.DashedLineView_android_dashWidth, DASH_WIDTH);
            dashGap = a.getDimensionPixelSize(R.styleable.DashedLineView_android_dashGap, DASH_GAP);
            orientation = a.getInt(R.styleable.DashedLineView_android_orientation, HORIZONTAL);
            a.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(lineColor);

    }


    @Override
    public void draw(Canvas c) {
        super.draw(c);

        if (orientation == HORIZONTAL) {
            int cy = getPaddingTop();
            int h = getHeight() - getPaddingTop() - getPaddingBottom();
            paint.setStrokeWidth(h);

            for (int cx = getPaddingLeft(); cx < getWidth() - getPaddingRight(); cx += dashGap + dashWidth) {
                c.drawLine(cx, cy, cx + dashWidth, cy, paint);
            }
        } else {
            int cx = getPaddingLeft();
            int h = getWidth() - getPaddingLeft() - getPaddingRight();

            paint.setStrokeWidth(h);

            for (int cy = getPaddingTop(); cy < getHeight() - getPaddingBottom(); cy += dashGap + dashWidth) {
                c.drawLine(cx, cy, cx, cy + dashWidth, paint);
            }
        }
    }


}
