package com.robam.roki.ui.bean3;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.net.request.bean.AlumListBean;

import java.util.List;

/**
 * @author r210190
 * des 菜谱详情列表item
 */
public class RecipeDetailItem implements MultiItemEntity {





    public  boolean isPlaying =false;
    /**
     * 图片
     */
    public static final int IMAGE = 1;
    /**
     * 视频
     */
    public static final int VIDEO = 2;
    /**
     * 食材
     */
    public static final int MATERIALS = 3;
    /**
     * 步骤
     */
    public static final int STEPS = 4;
    /**
     * 步骤
     */
    public static final int STEPS_VIDEO = 6;

    /**
     * 大家都在做菜谱
     */
    public static final int RECIPE = 5;


    /**
     * 晒厨艺
     */
    public static final int SHOW = 9;

    /**
     * 视频横板
     */
    public static final int VIDEO_H = 7;
    /**
     * 备菜步骤
     */
    public static final int PRE_STEPS = 8;
    private int itemType;

    /**
     * 步骤列表
     */
    public List<CookStep> cookSteps;
    /**
     * 做菜步骤
     */
    public CookStep cookStep;
    /**
     * 做菜步骤
     */
    public PreSubStep preSubStep;
    /**
     * 视频源 /图片
     */
    public String video;
    /**
     * 视频：2 图片：1
     */
    public String showType;
    /**
     * 食材
     */
    public Materials materials;
    /**
     * 大家都在做菜谱
     */
    public List<Recipe> recipes;

    public AlumListBean mAlumListBean;



    @Override
    public int getItemType() {
        return itemType;
    }

    public RecipeDetailItem(String showType, String video, String imgPoster) {
//        this.showType = showType;
        if (video != null && !video.isEmpty()) {
            this.itemType = VIDEO_H;
            this.showType = "2";
            this.video = video;
        } else {
            this.itemType = IMAGE;
            this.showType = "1";
            this.video = imgPoster;
        }
//        if ("1".equals(showType)){
//            this.itemType = IMAGE;
//            this.video = imgPoster;
//        }else if ("2".equals(showType)){
//            this.itemType = VIDEO_H;
//            this.video = video;
//        }else {
//            this.itemType = IMAGE;
//            this.video = imgPoster;
//        }
    }

    public RecipeDetailItem(Materials materials) {
        this.itemType = MATERIALS;
        this.materials = materials;
    }


    public RecipeDetailItem(AlumListBean mAlumListBean) {
        this.itemType = SHOW;
        this.mAlumListBean = mAlumListBean;
    }

//    public RecipeDetailItem(List<CookStep> cookSteps) {
//        this.itemType = STEPS;
//        this.cookSteps = cookSteps;
//    }

    public RecipeDetailItem(CookStep cookStep) {
//        if ("2".equals(cookStep.showType)){
//            this.itemType = STEPS_VIDEO;
//            this.cookStep = cookStep;
//        }else if ("1".equals(cookStep.showType)){
//            this.itemType = STEPS;
//            this.cookStep = cookStep;
//        }else {
//            this.itemType = STEPS;
//            this.cookStep = cookStep;
//        }
        if (cookStep.stepVideo != null && !cookStep.stepVideo.isEmpty()) {
            this.itemType = STEPS_VIDEO;
        } else {
            this.itemType = STEPS;
        }
        this.cookStep = cookStep;
    }

    public RecipeDetailItem(PreSubStep preSubStep) {
        if (preSubStep.stepVideo != null) {
            this.itemType = STEPS_VIDEO;
            this.preSubStep = preSubStep;
        } else {
            this.itemType = PRE_STEPS;
            this.preSubStep = preSubStep;
        }
    }

    public RecipeDetailItem(List<Recipe> recipes) {
        this.itemType = RECIPE;
        this.recipes = recipes;
    }
}
