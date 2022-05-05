package com.robam.common.pojos;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

public class ThemeRecipeList implements Serializable {

    private RecipeTheme recipeTheme;
    private List<Recipe> recipeList;

    public ThemeRecipeList(RecipeTheme recipeTheme, List<Recipe> recipeList) {
        this.recipeTheme = recipeTheme;
        this.recipeList = recipeList;
    }

    public RecipeTheme getRecipeTheme() {
        return recipeTheme;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }
}
