package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by as on 2016/8/5.
 */

public class RecipeShowItem {
    @JsonProperty("albums")
    public List<RecipeShow> recipeShows;
}
