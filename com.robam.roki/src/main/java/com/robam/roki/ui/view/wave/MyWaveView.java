package com.robam.roki.ui.view.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MyWaveView extends View {


    /**
     * 当前的进度
     */
    private int mProgress;
    private WaveBean waveBean;


    /**
     * 获取进度
     *
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置进度
     */
    private void setProgress(int progress) {
        mProgress = progress;
    }


    public void startProgress(int progress) {
        waveBean.setLevel(progress);
        postInvalidate();
    }


    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    private int i = 0;
    private List<WaveBean> waveBeanList = new ArrayList<>();


    public MyWaveView(Context context) {
        super(context);
    }

    public MyWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        waveBean = new WaveBean();
        waveBean.setLevel(0);
        waveBean.setWaveHeight(10);
        waveBean.setSpeed(30);
        waveBean.setAngle(0);

        waveBean.setStartColor(Color.parseColor("#ff0000"));//红色
        waveBean.setEndColor(Color.parseColor("#f0ff00"));//黄色
        waveBeanList.add(waveBean);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Path> pathList = new ArrayList<>();
        for (WaveBean waveBean : waveBeanList) {
            pathList.add(getWavePath(waveBean));
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                i = 0;
                for (WaveBean waveBean : waveBeanList) {
                    waveBean.setAngle(waveBean.getAngle() + (float) Math.PI / waveBean.getSpeed());
                }
                postInvalidate();
            }
        }, 17);

        for (int i = 0; i < pathList.size(); i++) {
            drawPath(canvas, waveBeanList.get(i), pathList.get(i));
        }
    }

    private Path getWavePath(WaveBean waveBean) {
        //计算波纹绘制区间的开始Y坐标：level:使波纹具备一定深度（level - waveHeight/2）
        Path path = new Path();
        //开始的X坐标是最左边
        //结束的X,Y坐标在右下角
        float startY = mHeight - waveBean.getLevel() - waveBean.getWaveHeight() / 2;
        for (i = 0; i < mWidth; i += 5) {
            float y = (float) (startY - waveBean.getWaveHeight() * Math.sin(i * (2 * Math.PI / mWidth) + waveBean.getAngle()));
            //设置path的起点
            if (i == 0) {
                path.moveTo(0, y);
            } else { //连线
                path.lineTo(i, y);
            }
        } //封闭 路径
        path.lineTo(mWidth, mHeight);
        path.lineTo(0, mHeight);
        path.close();
        return path;
    }

    /**
     * 计算渐变色区间的开始Y坐标,即波纹的开始Y坐标
     */
    private void drawPath(Canvas canvas, WaveBean waveBean, Path path) {
        //开始的X坐标是最左边
        //结束的X,Y坐标在右下角
        float startY = mHeight - waveBean.getLevel() - waveBean.getWaveHeight() / 2;
        mPaint.setShader(new LinearGradient(
                0,//起点x坐标
//                startY,//起点y坐标
                0,//起点y坐标
                mWidth,//终点x坐标
                mHeight,//终点y坐标
                new int[]{waveBean.getStartColor(), waveBean.getEndColor()},//表示渐变的颜色数组
                null,//用来指定颜色数组的相对位置,可以为null，为null是表示颜色均匀分布
                Shader.TileMode.CLAMP)//表示平铺模式
        );



        //设置alpha不透明度，范围为0~255
        mPaint.setAlpha(160);
        canvas.drawPath(path, mPaint);
    }


}
