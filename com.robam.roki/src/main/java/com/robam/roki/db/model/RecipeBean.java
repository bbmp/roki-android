package com.robam.roki.db.model;

import com.legent.plat.Plat;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author r210190
 * 菜谱
 */
public class RecipeBean extends LitePalSupport {
    /**
     * 主键
     */
    @Column
    private int id ;
    /**
     * 菜谱id
     */
    @Column
    private String recipe_id;
    /**
     * 菜谱名
     */
    @Column
    private String recipe_names;
    /**
     * 用户id
     */
    @Column
    private long user_id;
    /**
     * 设备类型
     */
    @Column
    private String device;
    /**
     * 入表时间
     */
    @Column
    private long insetTime;

    /**
     * 步骤
     */
    private List<RecipeStepBean> RecipeStepList = new ArrayList<RecipeStepBean>();

    public RecipeBean(String recipe_id , String recipe_names, long user_id, String device, long insetTime) {
        this.recipe_id = recipe_id ;
        this.recipe_names = recipe_names;
        this.user_id = user_id;
        this.device = device;
        this.insetTime = insetTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_names() {
        return recipe_names;
    }

    public void setRecipe_names(String recipe_names) {
        this.recipe_names = recipe_names;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getInsetTime() {
        return insetTime;
    }

    public void setInsetTime(long insetTime) {
        this.insetTime = insetTime;
    }

    public List<RecipeStepBean> getRecipeStepList() {
        List<RecipeStepBean> RecipeStepBeans = LitePal.where("recipeBean_id = ?" , id+"" ).find(RecipeStepBean.class);
        return RecipeStepBeans;
    }

    public void setRecipeStepList(List<RecipeStepBean> recipeStepList) {
        RecipeStepList = recipeStepList;
    }

    public int getRecipeStepTimes() {
        int times = LitePal.where("recipeBean_id = ?" , id+"" ).sum(RecipeStepBean.class , "time" ,int.class);
        return times;
    }
}
