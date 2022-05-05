package com.robam.roki.ui.view.wave;

/**
 * Created by Ruanwei on 2019/5/20.
 */

public class WaveBean {

    private int level;//水平线
    private int waveHeight;//最高峰--最低谷的大小
    private int speed;//速度
    private float angle;//开始角度
    private int startColor;//渐变色
    private int endColor;//渐变色

    public WaveBean() {

    }

    public WaveBean(int level, int waveHeight, int speed, float angle, int startColor, int endColor) {
        this.level = level;
        this.waveHeight = waveHeight;
        this.speed = speed;
        this.angle = angle;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWaveHeight() {
        return waveHeight;
    }

    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
