package com.robam.common.util;

import com.legent.Callback;
import com.legent.VoidCallback3;
import com.legent.dao.DaoHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.services.StoreService;

import java.util.List;

import static com.legent.ContextIniter.context;

/**
 * Created by as on 2017-02-20.
 */

public class RecipeUtils {
    /**
     * p判断是否温控锅菜谱
     * oneKey---5 一键烹饪
     * pot---4 温控锅
     * origin---1
     * 这个all可能是空也可能不是，反正all返回原先所有菜谱
     *
     * @return
     */
    public static boolean isPlotRecipe(Recipe recipe) {
        if (recipe == null)
            return false;
        return "4".equals(recipe.getCookbookType());
    }

    public static boolean isFastRecipe(Recipe recipe) {
        if (recipe == null)
            return false;
        return "5".equals(recipe.getCookbookType());
    }

    /**
     * 获取菜谱展示图片
     *
     * @param recipe
     * @return
     */
    public static String getRecipeImgUrl(AbsRecipe recipe) {
        if (recipe instanceof Recipe) {
            Recipe rokiRecipe = (Recipe) recipe;
            if (rokiRecipe.imgLarge != null && !rokiRecipe.imgLarge.isEmpty()) {
                return rokiRecipe.imgLarge;
            }

            if (rokiRecipe.imgMedium != null && !rokiRecipe.imgMedium.isEmpty()) {
                return rokiRecipe.imgMedium;
            }

            if (rokiRecipe.imgSmall != null && !rokiRecipe.imgSmall.isEmpty()) {
                return rokiRecipe.imgSmall;
            }

            if (rokiRecipe.imgPoster != null) {
                return rokiRecipe.imgPoster;
            }
        }

        if (recipe instanceof Recipe3rd) {

            Recipe3rd recipe3rd = (Recipe3rd) recipe;

            if (recipe3rd.imgMedium != null) {
                return recipe3rd.imgMedium;
            }

            if (recipe3rd.imgSmall != null) {
                return recipe3rd.imgSmall;
            }

        }

        return "";

    }

    /**
     * 获取菜谱展示图片（中图）
     *
     * @param recipe
     * @return
     */
    public static String getMediumRecipeImgUrl(AbsRecipe recipe) {
        if (recipe instanceof Recipe) {
            Recipe rokiRecipe = (Recipe) recipe;
            if (rokiRecipe.imgLarge != null && !rokiRecipe.imgLarge.isEmpty()) {
                return rokiRecipe.imgLarge;
            }

            if (rokiRecipe.imgMedium != null && !rokiRecipe.imgMedium.isEmpty()) {
                return rokiRecipe.imgMedium;
            }

            if (rokiRecipe.imgSmall != null && !rokiRecipe.imgSmall.isEmpty()) {
                return rokiRecipe.imgSmall;
            }

            if (rokiRecipe.imgPoster != null) {
                return rokiRecipe.imgPoster;
            }
        }

        return "";

    }

    /**
     * 通过Raw索引index反射tclass 对象
     *
     * @return
     * @throws Exception
     */
    public static <T> T getFromeJson(Class<T> tClass, int index) throws Exception {
        String dicContent = ResourcesUtils.raw2String(index);
        T clazz = null;
        try {
            clazz = JsonUtils.json2Pojo(dicContent, tClass);
        } finally {

        }
        return clazz;
    }

    /**
     * 判断菜谱是否包含详情数据
     *
     * @return
     */
    public static boolean ifRecipeContainStep(Recipe recipe) {
        if (recipe == null)
            return false;
        List<CookStep> list = recipe.getJs_cookSteps();
        return list != null && list.size() > 0;
    }

    public static void getRecipeDetailFromDBOrNET(long id, final VoidCallback3 callback3) throws Exception {
        if (id == 0)
            throw new NullPointerException();
        Recipe recipe = null;
        try {
            recipe = DaoHelper.getById(Recipe.class, id);
        } catch (Exception e) {
        } finally {
            if (recipe != null && recipe.hasDetail && ifRecipeContainStep(recipe)) {
                callback3.onCompleted(recipe);
            } else {
                ProgressDialogHelper.setRunning(context, true);
                StoreService.getInstance().getCookbookById(id, new Callback<Recipe>() {
                    @Override
                    public void onSuccess(Recipe recipe) {
                        callback3.onCompleted(recipe);
                        ProgressDialogHelper.setRunning(context, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        callback3.onCompleted(null);
                        ProgressDialogHelper.setRunning(context, false);
                    }
                });
            }
        }

    }


}
