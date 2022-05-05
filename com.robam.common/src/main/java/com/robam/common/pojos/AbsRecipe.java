package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;

abstract public class AbsRecipe extends AbsStorePojo<Long> {

    public final static String COLUMN_ID = "id";
    static final public String COLUMN_isToday = "isToday";
    static final public String COLUMN_isFavority = "isFavority";
    static final public String COLUMN_isRecommend = "isRecommend";
    static final public String COLUMN_isAll = "isAll";


    static final public String COLUMN_isStoveRecipe = "isStoveRecipe";
    static final public String COLUMN_isOvenRecipe = "isOvenRecipe";
    static final public String COLUMN_isStemRecipe = "isStemRecipe";
    static final public String COLUMN_isMircoRecipe = "isMircoRecipe";

    /*
    判断菜谱类型
     */
    public boolean isStoveRecipe = false;
    public boolean isOvenRecipe = false;
    public boolean isStemRecipe = false;
    public boolean isMircoRecipe = false;


    @DatabaseField(id = true, columnName = COLUMN_ID)
    @JsonProperty("id")
    public long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;



    /**
     * 供应商ID
     */
    @DatabaseField
    @JsonProperty("type")
    public long type;  // 0 roki 5 下厨房 6 味库 7 豆果

    /**
     * 收藏次数
     */
    @DatabaseField
    @JsonProperty("collectCount")
    public int collectCount;

    /**
     * 浏览量
     */
    @DatabaseField
    @JsonProperty("viewCount")
    public int viewCount;

    public void setId(long id) {
        this.id = id;
    }

    /*
     *是否支持配送   by zhaiyuanyi
     */
    @DatabaseField()
    @JsonProperty("allowDistribution")
    public boolean allowDistribution;


    /**
     * 是否在今日菜单
     */
    @DatabaseField(columnName = COLUMN_isToday)
    public boolean isToday;

    /**
     * 是否是收藏菜谱
     */
    @DatabaseField(columnName = COLUMN_isFavority)
    public boolean isFavority;

    /**
     * 是否推荐菜谱
     */
    @DatabaseField(columnName = COLUMN_isRecommend)
    public boolean isRecommend;
    /**
     * 是否所有菜谱
     */
    @DatabaseField(columnName = COLUMN_isAll)
    public boolean isAll;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


    /**
     * 是否Roki内置菜谱
     */
    public boolean isRoki() {
        return type <= 1;
    }

    public RecipeProvider getProvider() {
        if (type > 1)
            return DaoHelper.getById(RecipeProvider.class, type);
        else
            return null;
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    public void setIsToday(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isToday, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isToday = value;
            save2db();
        }
    }

    public void setIsFavority(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isFavority, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isFavority = value;
            save2db();
        }

    }

    public void setIsRecommend(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isRecommend, value, COLUMN_ID, id);
            // DaoHelper.refresh(this);
            DaoHelper.update(this); //rent 改
        } else {
            this.isRecommend = value;
            save2db();
        }
    }

    /*
    设置属于灶具菜谱
     */
    public void setIsStoveRecipe(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isStoveRecipe, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isStoveRecipe = value;
            save2db();
        }
    }

    /*
   设置属于蒸汽炉菜谱
    */
    public void setIsSteamRecipe(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isStemRecipe, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isStemRecipe = value;
            save2db();
        }
    }

    /*
   设置属于烤箱菜谱
    */
    public void setIsOvenRecipe(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isOvenRecipe, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isOvenRecipe = value;
            save2db();
        }
    }

    /*
    设置属于微波炉菜谱
     */
    public void setIsMicroRecipe(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isMircoRecipe, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isMircoRecipe = value;
            save2db();
        }
    }


    public void setIsAll(boolean value) {
        if (DaoHelper.isExists(getClass(), id)) {
            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isAll, value, COLUMN_ID, id);
            DaoHelper.refresh(this);
        } else {
            this.isAll = value;
            save2db();
        }
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    static public void setIsToday(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsToday(value);
        }
        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsToday(value);
        }
    }

    /**
     * 菜谱详情收藏和取消使用
     */
    static public void setIsFavority(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsFavority(value);
            recipe.collectCount = value ? recipe.collectCount + 1 : recipe.collectCount - 1;
            DaoHelper.update(recipe);
        }

        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsFavority(value);
            recipe3rd.collectCount = value ? recipe3rd.collectCount + 1 : recipe3rd.collectCount - 1;
            DaoHelper.update(recipe3rd);
        }
    }


    static public void setIsRecommend(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsRecommend(value);
        }
        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsRecommend(value);
        }
    }

    /*
     设置是否是灶具菜谱
     */
    static public void setIsStoveRecipe(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsStoveRecipe(value);
        }
        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsStoveRecipe(value);
        }
    }

    /*
 设置是否是烤箱菜谱
 */
    static public void setIsOvenRecipe(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsOvenRecipe(value);
        }
        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsOvenRecipe(value);
        }
    }


    /*
   设置是否是蒸汽炉菜谱
    */
    static public void setIsSteamRecipe(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsSteamRecipe(value);
        }
        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsSteamRecipe(value);
        }
    }

    /*
    设置是否是微波炉菜谱
     */
    static public void setIsMicroRecipe(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsMicroRecipe(value);
        }
        Recipe3rd recipe3rd = DaoHelper.getById(Recipe3rd.class, recipeId);
        if (recipe3rd != null) {
            recipe3rd.setIsMicroRecipe(value);
        }
    }


    public void SetDeviceRecipe(long recipeId, String deviceList) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (deviceList != null && recipe != null) {
            String[] devices = deviceList.split(",");
            for (int i = 0; i < devices.length; i++) {
                if (devices[i].equals(DeviceType.RRQZ)) {
                    recipe.setIsStoveRecipe(true);
                }
                if (devices[i].equals(DeviceType.RDKX)) {
                    recipe.setIsOvenRecipe(true);
                }
                if (devices[i].equals(DeviceType.RWBL)) {
                    recipe.setIsMicroRecipe(true);
                }
                if (devices[i].equals(DeviceType.RZQL)) {
                    recipe.setIsSteamRecipe(true);
                }
            }
        }

    }


    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------


}
