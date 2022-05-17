package com.robam.roki.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.robam.common.pojos.Recipe;

public class RecipeCategoryMultiItem implements MultiItemEntity {
    public static final int MAIN_VIEW = 1;
    public static final int OTHER_VIEW = 2;

    private int itemType;
    private Recipe recipe;

    public RecipeCategoryMultiItem(int itemType, Recipe recipe) {
        this.itemType = itemType;
        this.recipe = recipe;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
