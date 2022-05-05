package com.robam.roki.db.model;

import com.legent.plat.Plat;
import com.robam.roki.db.AppDataBase;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author r210190
 * 菜谱步骤
 */
public class CookingStepsModel  extends LitePalSupport {
    /**
     * 主键id
     */
    @Column
    private int id;
    /**
     * 菜谱id
     */
    @Column
    private String recipeId;
    /**
     * 菜谱名
     */
    @Column
    private String name;
    /**
     * 用户id
     */
    @Column
    private long userId;
    /**
     * 设备类型
     */
    @Column
    private String device;
    /**
     * 制作模式
     */
    @Column
    private int workMode;
    /**
     * 烹饪时间
     */
    @Column
    private int time;
    /**
     * 温度
     */
    @Column
    private int temperature;
    /**
     * 入表时间
     */
    @Column
    private long insetTime;

    public CookingStepsModel(String name, long userId, String device, int workMode, int time, int temperature, long insetTime) {
        this.name = name;
        this.userId = userId;
        this.device = device;
        this.workMode = workMode;
        this.time = time;
        this.temperature = temperature;
        this.insetTime = insetTime;
    }

    public CookingStepsModel(String recipeId, String name, long userId, String device, int workMode, int time, int temperature, long insetTime) {
        this.recipeId = recipeId;
        this.name = name;
        this.userId = userId;
        this.device = device;
        this.workMode = workMode;
        this.time = time;
        this.temperature = temperature;
        this.insetTime = insetTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public long getInsetTime() {
        return insetTime;
    }

    public void setInsetTime(long insetTime) {
        this.insetTime = insetTime;
    }
}
