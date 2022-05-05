package com.robam.roki.ui.view.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.legent.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 健康热油
 */
public class MnScaleBar extends View {

    public List<Integer> tempList = new ArrayList<>();
    public Map<String, String> maps = new HashMap<>();


    public MnScaleBar(Context context) {
        this(context, null);
    }


    public MnScaleBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MnScaleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }

    public void setMax(List<Integer> max) {
        this.tempList = max;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        onDrawText(canvas);
        onDrawScale(canvas);
        super.onDraw(canvas);
    }


    List<String> keyList = new ArrayList<>();

    /**
     * 画文字描述
     */
    private void onDrawText(Canvas canvas) {
        if (canvas == null) return;

        Set<String> strings = maps.keySet();
        for (String key : strings) {
            keyList.add(key);
        }


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#E6FFFFFF"));
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#E6f9ef8d"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);


        //画居中竖线的笔
        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor("#dbdbdb"));
        textPaint.setTextSize(40);
        textPaint.setStrokeWidth(3);


        Paint textPaint2 = new Paint();
        textPaint2.setTextAlign(Paint.Align.CENTER);
        textPaint2.setColor(Color.GRAY);
        textPaint2.setTextSize(40);


        int maxTemp = 0;

        for (int i = 0; i < tempList.size(); i++) {
            if (maxTemp < tempList.get(i)) {
                maxTemp = tempList.get(i);
            }

        }


        canvas.drawLine(getWidth() / 2, getHeight() - maxTemp * 8, getWidth() / 2, getHeight(), textPaint);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.setTextAlign(Paint.Align.CENTER);
        String res = null;


//        int max = Integer.valueOf(keyList.get(0));
        int max = 0;
        for (int i = 0; i < keyList.size(); i++) {

            //遍历出 keyList 集合中的最大值
            if (max < Integer.valueOf(keyList.get(i))) {
                max = Integer.valueOf(keyList.get(i));
            }


//            //从 keyList 集合中剔除某个值
//            if (String.valueOf(max).equals(keyList.get(i))) {
//                keyList.remove(i);
//            }

            //从 Map集合中根据指定某个key 取value
            for (Map.Entry<String, String> str : maps.entrySet()) {
                if (keyList.get(i).equals(str.getKey())) {
                    res = str.getValue();
                }
            }


            RectF rectF = new RectF(0, getHeight() - Integer.valueOf(keyList.get(i)) * 7 - 100, getWidth(), getHeight() - Integer.valueOf(keyList.get(i)) * 7);
            float baseline = rectF.centerY() - (fontMetrics.top / 2) - (fontMetrics.top / 2);
            canvas.drawRect(rectF, paint);
            canvas.drawText(res, rectF.centerX(), baseline, textPaint);

        }
        String resMax = null;
        for (Map.Entry<String, String> str : maps.entrySet()) {
            if (String.valueOf(max).equals(str.getKey())) {
                resMax = str.getValue();


            }
        }


        RectF rectF = new RectF(0, getHeight() - max * 7 - 100, getWidth(), getHeight() - max * 7);
        float baseline = rectF.centerY() - (fontMetrics.top / 2) - (fontMetrics.top / 2);
        canvas.drawRect(rectF, paint2);
        canvas.drawText(resMax, rectF.centerX(), baseline, textPaint2);


    }


    /**
     * 画刻度
     */
    private void onDrawScale(Canvas canvas) {
        if (canvas == null) return;
        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.CENTER); //文字居中
        mPaint.setTextSize(40);


        for (int i = 0; i < tempList.size(); i++) {
            Integer integer = tempList.get(i);
            canvas.drawText(String.valueOf(integer), 50, getHeight() - integer * 7, mPaint);
            canvas.drawText(String.valueOf(integer), getWidth() - 50, getHeight() - integer * 7, mPaint);


        }


    }


}
