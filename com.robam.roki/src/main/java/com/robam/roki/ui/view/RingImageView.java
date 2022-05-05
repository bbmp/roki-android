package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.robam.roki.R;
import com.robam.roki.listener.onClickRingListener;


public class RingImageView extends View {
    Paint paint;
    Paint ringPaint;

    String content = "跳过";
    //字符宽度
    int width;
    //间距
    int pading = 9;
    //需要的宽度
    int desrie_Width;
    Paint circlePaint;
    //外圈阴影线条
    Paint outerRingPaint;

    int textHeight;

    RectF rect;

    float dre = 0;

    onClickRingListener listener;

    public RingImageView(Context context) {
        super(context);
    }


    public RingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);


        circlePaint = new Paint();
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(getResources().getColor(R.color.White));


        outerRingPaint = new Paint();
        outerRingPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        outerRingPaint.setStyle(Paint.Style.STROKE);
        outerRingPaint.setColor(getResources().getColor(R.color.gray));
        outerRingPaint.setStrokeWidth(1);

        ringPaint = new Paint();
        ringPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setColor(getResources().getColor(R.color.c57));
        ringPaint.setStrokeWidth(4);

        //获取到字符宽度
        width = (int) paint.measureText(content);
        //需要的宽度
        desrie_Width = width + pading * 3;

        textHeight = (int) (paint.descent() - paint.ascent());
//        rect = new RectF(pading - 1, pading - 1, desrie_Width - pading, desrie_Width - pading);
        rect = new RectF(pading - 7, pading - 7, desrie_Width - 2, desrie_Width - 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(desrie_Width, desrie_Width);
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        outerRingPaint
        canvas.drawCircle(desrie_Width / 2, desrie_Width / 2, desrie_Width / 2, outerRingPaint);

        //中心就是控件的中心,半径就是控件的半径
        canvas.drawCircle(desrie_Width / 2, desrie_Width / 2, desrie_Width / 2, circlePaint);

        //居中画text
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText(content, pading + 2, yPos, paint);

        //画等待圆弧,把0--90
        canvas.save();
        canvas.rotate(-90, desrie_Width / 2, desrie_Width / 2);
        //如果为true的话,会出现连接圆心的线
        canvas.drawArc(rect, 0, dre, false, ringPaint);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //增加点击反馈
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.3f);
                break;
            //增加点击事件
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                if (listener != null) {
                    listener.onMyGod();
                }
                break;
        }
        return true;
    }


    public void setListener(onClickRingListener listener) {
        this.listener = listener;
    }

    public void setDre(int total, int now) {
        int space = 360 / total;
        dre = now * space;
        invalidate();
    }
}
