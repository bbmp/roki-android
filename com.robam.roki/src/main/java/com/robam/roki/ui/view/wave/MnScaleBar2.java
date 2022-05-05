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

import static com.legent.ContextIniter.context;

/**
 * 防干烧
 */
public class MnScaleBar2 extends View {

    public List<Integer> tempList = new ArrayList<>();
    public Map<String, String> maps = new HashMap<>();


    public MnScaleBar2(Context context) {
        this(context, null);
    }

    public MnScaleBar2(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MnScaleBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


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

    public void setTempList(List<Integer> tempList) {
        this.tempList = tempList;
    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }

    List<String> keyList = new ArrayList<>();

    /**
     * 画文字描述
     */

    int maxTemp = 0;
    private void onDrawText(Canvas canvas) {
        if (canvas == null) return;
        Set<String> strings = maps.keySet();
        for (String key : strings) {
            keyList.add(key);
        }


        for (int i = 0; i < tempList.size(); i++) {

            //遍历出 keyList 集合中的最大值
            if (maxTemp < tempList.get(i)) {
                maxTemp = tempList.get(i);
            }
        }

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#E6ffeae9"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);

        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#E6fa8e89"));
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setStrokeWidth(2);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(40);

        //画居中竖线的笔
        Paint textPaint2 = new Paint();
        textPaint2.setColor(Color.parseColor("#dbdbdb"));
        textPaint2.setTextSize(40);
        textPaint2.setStrokeWidth(3);



        //画一条居中的竖线
        canvas.drawLine(getWidth() / 2, getHeight()-maxTemp*5-100, getWidth() / 2, getHeight(), textPaint2);


        paint.setTextAlign(Paint.Align.CENTER);
        String res = null;
        for (int i = 0; i < keyList.size(); i++) {

            //从 Map集合中根据指定某个key 取value
            for (Map.Entry<String, String> str : maps.entrySet()) {
                if (keyList.get(i).equals(str.getKey())) {
                    res = str.getValue();
                }
            }


            RectF rectF = new RectF(0, getHeight()- Integer.valueOf(keyList.get(i)) * 5, getWidth(), getHeight()-(maxTemp+tempList.get(1)) * 5 );
            canvas.drawRect(rectF, paint);
            canvas.drawText(res,100, getHeight()- Integer.valueOf(keyList.get(i)) * 5-10, textPaint);
            canvas.drawLine(0,getHeight()- Integer.valueOf(keyList.get(i)) * 5, getWidth(),getHeight()-Integer.valueOf(keyList.get(i))*5,paint2);



        }



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
            canvas.drawText(String.valueOf(integer), 50, getHeight() - integer * 5, mPaint);
            canvas.drawText(String.valueOf(integer), getWidth() - 50, getHeight() - integer * 5, mPaint);
        }


    }


}
