package com.robam.roki.db.model;

import com.robam.common.pojos.Recipe;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author r210190
 * 菜谱步骤
 */
public class RecipeStepBean extends LitePalSupport {
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
     * 制作模式
     */
    @Column
    private int work_mode;
    /**
     * 制作模式tag
     */
    @Column
    private String function_code;
    /**
     * 制作模式name
     */
    @Column
    private String function_name;

    @Column
    private String function_params;
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
     * 温度
     */
    @Column
    private int temperature2;

    /**
     * 蒸汽量
     */
    @Column
    private int steam_flow;
    /**
     * 入表时间
     */
    @Column
    private long inset_time;
    /**
     * 更新时间
     */
    @Column
    private long update_time;
    /**
     * 本地
     */
    @Column
    private boolean isLocal;


    private RecipeBean recipebean ;


    public int getId() {
        return id;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
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

    public int getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(int work_mode) {
        this.work_mode = work_mode;
    }

    public String getFunction_code() {
        return function_code;
    }

    public void setFunction_code(String function_code) {
        this.function_code = function_code;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getFunction_params() {
        return function_params;
    }

    public void setFunction_params(String function_params) {
        this.function_params = function_params;
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

    public int getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(int temperature2) {
        this.temperature2 = temperature2;
    }

    public int getSteam_flow() {
        return steam_flow;
    }

    public void setSteam_flow(int steam_flow) {
        this.steam_flow = steam_flow;
    }

    public long getInset_time() {
        return inset_time;
    }

    public void setInset_time(long inset_time) {
        this.inset_time = inset_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public RecipeBean getRecipebean() {
        return recipebean;
    }

    public void setRecipebean(RecipeBean recipebean) {
        this.recipebean = recipebean;
    }
}
