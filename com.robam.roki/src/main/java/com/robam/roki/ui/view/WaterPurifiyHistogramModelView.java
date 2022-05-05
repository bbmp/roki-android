package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.DataInfo;

import java.util.ArrayList;

public class WaterPurifiyHistogramModelView extends View {

    private Paint paint;// 柱状图画笔
    private Paint mlpaint;// ml数值画笔
    private Paint tpaint;// 时间文字画笔
    private Paint dpaint;//分割线画笔

    private int[] ml = new int[7];
    private int[] ml1 = new int[7];
    private String[] time = new String[]{" ", " ", " ", " ", " ", " ", " "};
    private String[] time1 = new String[]{" ", " ", " ", " ", " ", " ", " "};
    private int max = 1500;
    private RectF rect;
    private int people;
    int i = 0;
    String type;
    ArrayList<DataInfo> list = new ArrayList<DataInfo>();

    public WaterPurifiyHistogramModelView(Context context) {
        super(context);
        init(context, null);
    }

    public WaterPurifiyHistogramModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }


    public void updateThisData(int[] Dateml, String[] time) {
        for (int j = 0; j < ml.length; j++) {
            ml[j] = 0;
        }
        ml1 = Dateml;
        int i = 0;
        for (int j = 0; j < time.length; j++) {
            LogUtils.i("20170719", "time::" + time[j]);
            if (time[j] != null) {
                ml[i] = ml1[j];
                i++;
            }
        }

        this.postInvalidate();
    }

    public void updateLastData(String[] dateTime) {
        LogUtils.i("20170321", "datetime:" + dateTime.length);
        for (int j = 0; j < time1.length; j++) {
            time1[j] = " ";
        }
        time = dateTime;
        int i = 0;
        for (int j = 0; j < time.length; j++) {
            if (time[j] != null) {
                time1[i] = time[j];
                i++;
            }
        }

        this.postInvalidate();  //可以子线程 更新视图的方法调用。
    }

    public void updateMax(int familyPeople, String timeType) {
        LogUtils.i("20170803", "people::" + familyPeople);
        type = timeType;
        people = familyPeople;
        if (timeType.equals("day")) {
            max = people * 1500;
        } else if (timeType.equals("week")) {
            max = people * 1500 * 7;
        } else if (timeType.equals("month")) {
            max = people * 1500 * 30;
        }
        this.postInvalidate();  //可以子线程 更新视图的方法调用。
    }

    private void init(Context context, AttributeSet attrs) {
        //画的柱状图的颜色
        paint = new Paint();
        paint.setColor(Color.parseColor("#0cc3ff"));
        // paint.setTextAlign(Align.CENTER);
        //设置数值的颜色
        mlpaint = new Paint();
        mlpaint.setTextSize(27);
        mlpaint.setColor(Color.parseColor("#ffffff"));
        //设置时间文字的颜色
        tpaint = new Paint();
        tpaint.setTextSize(32);
        tpaint.setColor(Color.parseColor("#1d1d1d"));
        //设置分割线的颜色
        dpaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() - 100;
        int height = getHeight();
        //绘制单位文字
        if (ml.length <= 0 && ml.length != time1.length)
            return;
        //计算平均每个柱状图宽度
        int average_width = width / (2 * 7 - 1);
        //开始绘制
        for (int i = 0; i < ml.length; i++) {
            float top = 0;
            if (ml[i] == 0) {
//                top = (float) height;
                top = (float) height - 80;
            } else if (ml[i] < max) {
                try {
                    if (max / ml[i] >= 8) {
//                        top = height;
                        top = height - 70;
                    } else {
                        top = height - ((float) ml[i] / max) * (height - 90 - (height + 40) / 3) - 10;
//                        top = height - ((float) ml[i] / max) * (height  - (height + 40) / 3) - 10;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (max != 0) {
                    if (((float) ml[i] / max) >= 2.5) {
                        top = 50;
                    } else {
                        top = 250 - (float) (ml[i] / max) * 100;
                    }
                }
            }
            int left = i * average_width * 2;
            int right = i * average_width * 2 + average_width;
            int bottom = height - 60;
            int t = (int) top - 30;

            //绘制圆角柱状图
            rect = new RectF(10 + left, t, right, bottom - 50);
            canvas.drawRoundRect(rect, 20, 20, paint);
            //绘制日期
            canvas.drawText(time1[i], 10 + left, bottom - 10, tpaint);
        }


        //横线分割
        for (int i = 0; i < height + 20; i = i + 15) {
            if (i > height - 50) break;
            if (i > (height + 20) / 2 && i < (height + 20) / 2 + 15) {
                dpaint.setColor(Color.parseColor("#ff3e18"));
                dpaint.setStyle(Paint.Style.STROKE);
                dpaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
                RectF r = new RectF(0, i - 50, width + 100, i - 49);
                canvas.drawRect(r, dpaint);

            }
        }
    }

}