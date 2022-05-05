package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.legent.utils.api.DisplayUtils;
import com.robam.roki.service.MobileStoveCookTaskService;

/**
 * Created by sylar on 15/6/13.
 */
public class RecipeCookingProgressView extends View implements View.OnClickListener {

    public interface OnCookingStepFinishedCallback {
        void onFinished();
    }

    final static String TXT_NEXT = "下一步";
    final static String TXT_FINISHED = "煮好了";
    final static float DIP_CircleStrokeWidth = 1f;
    final static float DIP_DarkProgressStrokeWidth = 3f;
    final static float DIP_ScaleStrokeWidth = 2f;
    final static float DIP_RadiusDiff = 20f;
    final static float DIP_ScaleLen = 8f;
    final static float SP_TxtNextSize = 30f;
    Paint paint;


    int colorDark1, colorDark2, colorRed, colorWhite60, colorTxtNext;
    float circleStrokeWidth, darkProgressStrokeWidth, scaleStrokeWidth, radiusDiff, scaleLen;
    float txtNextSize = 30;
    float txtNextHeight;

    float minute = 25;
    float progress = 1f;
    boolean isFinished;
    OnCookingStepFinishedCallback callback;

    public RecipeCookingProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeCookingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeCookingProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context cx, AttributeSet attrs) {
        colorDark1 = Color.parseColor("#888888");
        colorDark2 = Color.parseColor("#888888");
        colorWhite60 = Color.parseColor("#9affffff");
        colorTxtNext = Color.parseColor("#db4545");
        colorRed = Color.RED;

        circleStrokeWidth = DisplayUtils.dip2px(cx, DIP_CircleStrokeWidth);
        darkProgressStrokeWidth = DisplayUtils.dip2px(cx, DIP_DarkProgressStrokeWidth);
        scaleStrokeWidth = DisplayUtils.dip2px(cx, DIP_ScaleStrokeWidth);
        radiusDiff = DisplayUtils.dip2px(cx, DIP_RadiusDiff);
        scaleLen = DisplayUtils.dip2px(cx, DIP_ScaleLen);
        txtNextSize = DisplayUtils.sp2px(cx, SP_TxtNextSize);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(txtNextSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        txtNextHeight = (fontMetrics.descent - fontMetrics.ascent) / 2f;

        this.setOnClickListener(this);
    }

    public void setOnCookingStepFinishedCallback(OnCookingStepFinishedCallback callback) {
        this.callback = callback;
    }

    public void setValueBySeconds(float seconds, float progress) {
       // LogUtils.i("20171026","se::"+seconds+"  pro::"+progress);
        float minute = seconds / 60f;
        setValue(minute, progress);
    }

    public void setValue(float minute, float progress) {
        this.progress = progress;
        this.minute = minute;
        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }


    @Override
    public void draw(Canvas c) {
        super.draw(c);
        int width = getWidth();
        int height = getHeight();

        float radiusOutside = Math.min(width, height) / 2f - 1f;
        float radiusInside = radiusOutside - radiusDiff;

        float cx = width / 2f;
        float cy = height / 2f;
        float angle;
        RectF oval;

        //外圆
        paint.setColor(colorDark1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleStrokeWidth);
        c.drawCircle(cx, cy, radiusOutside, paint);

        //内圆
        paint.setColor(colorDark1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleStrokeWidth);
        c.drawCircle(cx, cy, radiusInside, paint);

        //刻度
        for (angle = 0f; angle < 360; angle += 45f) {
            c.save();
            c.translate(cx, cy);
            c.rotate(angle);
            c.translate(-radiusInside, 0);
            paint.setColor(colorDark1);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(scaleStrokeWidth);
            c.drawLine(0, 0, scaleLen, 0, paint);
            c.restore();
        }


        //进度
        angle = progress * 360;
        oval = new RectF(-radiusOutside, -radiusOutside, radiusOutside,
                radiusOutside);

        paint.setColor(colorRed);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleStrokeWidth);
        c.save();
        c.translate(cx, cy);
        c.rotate(-90f);
        c.drawArc(oval, 0, angle, false, paint);
        c.restore();

        //当前时间刻度
        angle = minute * 6;
        float r = radiusInside - darkProgressStrokeWidth / 2f;
        oval = new RectF(-r, -r, r, r);

        paint.setColor(colorDark2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(darkProgressStrokeWidth);
        c.save();
        c.translate(cx, cy);
        c.rotate(-90f);
        c.drawArc(oval, 0, angle, false, paint);
        c.restore();

        //
        isFinished = progress >= 1;
        if (isFinished) {
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(colorTxtNext);
            paint.setTextSize(txtNextSize);
            paint.setStyle(Paint.Style.FILL);

            if (!isInEditMode()) {
                boolean isLastStep = MobileStoveCookTaskService.getInstance().getStepIndex() + 1 == MobileStoveCookTaskService.getInstance().getStepCount();
                c.drawText(isLastStep ? TXT_FINISHED : TXT_NEXT, cx, cy + txtNextHeight / 2, paint);
            }
        } else {
            c.drawColor(colorWhite60);
        }
    }

    @Override
    public void onClick(View v) {
        if (callback != null && isFinished) {
            callback.onFinished();
        }
    }
}
