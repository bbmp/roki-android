package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sylar on 15/6/13.
 */
public class DividerDashedView extends View {

    final static float MARGIN = 5f;
    final static float LINE_LENGTH = MARGIN * 2;
    Paint paint;

    public DividerDashedView(Context context) {
        super(context);
        init(context, null);
    }

    public DividerDashedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DividerDashedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context cx, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

    }


    @Override
    public void draw(Canvas c) {
        super.draw(c);

        int width = getWidth();
        int height = getHeight();

        paint.setStrokeWidth(height);

        for (float cx = 0; cx < width; cx += MARGIN + LINE_LENGTH) {
            c.drawLine(cx, 0, cx + LINE_LENGTH, 0, paint);
        }
    }


}
