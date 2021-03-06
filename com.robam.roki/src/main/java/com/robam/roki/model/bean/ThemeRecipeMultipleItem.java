package com.robam.roki.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.ThemeRecipeDetail;
import com.robam.common.pojos.ThemeRecipeList;

import java.util.List;

public class ThemeRecipeMultipleItem implements MultiItemEntity {

    public static final int IMG_RECIPE_MSG_TEXT = 5;//菜谱列表
    public static final int IMG_THEME_RECIPE_MSG_TEXT = 6;//精选专题菜谱信息
    private int itemType;
    private Recipe recipe;
    private ThemeRecipeList themeRecipeList;
    private RecipeTheme recipeTheme;

    public ThemeRecipeMultipleItem(int itemType, Recipe recipe) {
        this.itemType = itemType;
        this.recipe = recipe;
    }


    public ThemeRecipeMultipleItem(int itemType, ThemeRecipeList themeRecipeList) {
        this.itemType = itemType;
        this.themeRecipeList = themeRecipeList;
    }
    public ThemeRecipeMultipleItem(int itemType, RecipeTheme recipeTheme) {
        this.itemType = itemType;
        this.recipeTheme = recipeTheme;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public ThemeRecipeList getThemeRecipeList() {
        return themeRecipeList;
    }

    public RecipeTheme getRecipeTheme() {
        return recipeTheme;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
