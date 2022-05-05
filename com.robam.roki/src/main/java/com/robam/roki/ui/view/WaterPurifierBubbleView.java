package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/27.
 */

public class WaterPurifierBubbleView extends View {
    private List<Bubble> bubbles = new ArrayList<Bubble>();
    private Random random = new Random();
    private int width, height;
    private boolean starting = false;
    private boolean isPause = false;
    private int total;


    public WaterPurifierBubbleView(Context context) {
        super(context);
    }

    public WaterPurifierBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterPurifierBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        isPause = false;
        width = getWidth();
        height = getMeasuredHeight();
        if (!starting) {
            starting = true;
            new Thread() {
                public void run() {

                    while (total<6) {
                        if(isPause){
                            continue;
                        }
                        try {
                            Thread.sleep(random.nextInt(3) * 1000);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                        Bubble bubble = new Bubble();
                        int radius = random.nextInt(30);
                        while (radius < 10) {
                            radius = random.nextInt(30)+20;
                        }
                        float speedY = random.nextFloat()*5;
                        while (speedY < 0) {
                            speedY = random.nextFloat()*5;
                        }
                        int temp=random.nextInt(width);
                        bubble.setRadius(radius);
                        bubble.setSpeedY(speedY);
                       // bubble.setX(width / 2+temp);
                        bubble.setX(temp);
                        bubble.setY(height);
                        float speedX = random.nextFloat()-0.5f;
                        while (speedX == 0) {
                            speedX = random.nextFloat()-0.5f;
                        }
                        bubble.setSpeedX(speedX*2);
                            bubbles.add(bubble);
                        total++;
                        if (total>=6){
                            total=0;
                        }
                    }
                }
            }.start();
        }
        Paint paint = new Paint();
        paint.reset();
        paint.setColor(Color.parseColor("#E6ffffff"));
        paint.setAlpha(400);
        List<Bubble> list = new ArrayList<Bubble>(bubbles);
        for (Bubble bubble : list) {
            if (bubble.getY() - bubble.getSpeedY() <= 10) {
                bubbles.remove(bubble);
            } else {
                int i = bubbles.indexOf(bubble);
                bubble.setX(bubble.getX() + bubble.getSpeedX());
                bubble.setY(bubble.getY() - bubble.getSpeedY());
                bubbles.set(i, bubble);
                canvas.drawCircle(bubble.getX(), bubble.getY(),
                        bubble.getRadius(), paint);
            }
        }
        invalidate();
    }
    @Override
    public void invalidate() {
        // TODO Auto-generated method stub
        super.invalidate();
        isPause = true;
    }

    private class Bubble {
        /** 气泡半径 */
        private int radius;
        /** 上升速度 */
        private float speedY;
        /** 平移速度 */
        private float speedX;
        /** 气泡x坐标 */
        private float x;
        /** 气泡y坐标 */
        private float y;

        /**
         * @return the radius
         */
        public int getRadius() {
            return radius;
        }

        /**
         * @param radius
         *            the radius to set
         */
        public void setRadius(int radius) {
            this.radius = radius;
        }

        /**
         * @return the x
         */
        public float getX() {
            return x;
        }

        /**
         * @param x
         *            the x to set
         */
        public void setX(float x) {
            this.x = x;
        }

        /**
         * @return the y
         */
        public float getY() {
            return y;
        }

        /**
         * @param y
         *            the y to set
         */
        public void setY(float y) {
            this.y = y;
        }

        /**
         * @return the speedY
         */
        public float getSpeedY() {
            return speedY;
        }

        /**
         * @param speedY
         *            the speedY to set
         */
        public void setSpeedY(float speedY) {
            this.speedY = speedY;
        }

        /**
         * @return the speedX
         */
        public float getSpeedX() {
            return speedX;
        }

        /**
         * @param speedX
         *            the speedX to set
         */
        public void setSpeedX(float speedX) {
            this.speedX = speedX;
        }

    }
}
