package com.robam.common.pojos;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.TransactionManager;
import com.legent.Callback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.DaoService;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 菜谱
 *
 * @author sylar
 */
public class Recipe extends AbsRecipe implements Serializable, MultiItemEntity {
    public static final String FOREIGN_COLUMNNAME_ID = "PreStep_ID";
    static final public String COLUMN_isCollected = "COLUMN_isCollected";
    public static final int TEXT = 1;
    public static final int IMG = 2;
    private int itemType;
    @DatabaseField
    @JsonProperty("cookbookType")
    String cookbookType;
    private static boolean mTrue;

    public String getCookbookType() {
        return cookbookType;
    }


    public Recipe() {

    }

    public Recipe(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 菜谱描述
     */
    @DatabaseField
    @JsonProperty("introduction")
    public String desc;

    /**
     * 所需时间 （秒）
     */
    @DatabaseField
    @JsonProperty("needTime")
    public int needTime;

    /**
     * 难度系数
     */
    @DatabaseField
    @JsonProperty("difficulty")
    public int difficulty;

    /**
     * 小图
     */
    @DatabaseField
    @JsonProperty("imgSmall")
    public String imgSmall;

    /**
     * 中图
     */
    @DatabaseField
    @JsonProperty("imgMedium")
    public String imgMedium;

    /**
     * 大图
     */
    @DatabaseField
    @JsonProperty("imgLarge")
    public String imgLarge;

    /**
     * 海报图
     */
    @DatabaseField
    @JsonProperty("imgPoster")
    public String imgPoster;

    @DatabaseField
    @JsonProperty("sourceType")
    public int sourceType;

    @DatabaseField
    @JsonProperty("providerImage")
    public String providerImage;

    @DatabaseField
    @JsonProperty("stampLogo")
    public String stampLogo;

    //收藏菜谱
    @DatabaseField()
    @JsonProperty("collected")
    public boolean collected;

    /**
     * 本地存储的库版本号
     */
    @DatabaseField()
    public int version;

    /**
     * 是否有明细数据
     */
    @DatabaseField()
    public boolean hasDetail;

    /**
     * 最近的明细数据更新时间
     */
    @DatabaseField()
    public long lastUpgradeTime;

    @JsonProperty("video")
    public String video;

    @JsonProperty("showType")
    public String showType ;




    @ForeignCollectionField()
    private ForeignCollection<Dc> db_dcs;

    public List<Dc> getJs_dcs() {
        if (db_dcs != null && db_dcs.size() > 0) {
            js_dcs = Lists.newArrayList(db_dcs);
            LogUtils.i("20170914", "js_dcs:" + js_dcs.toString());
        }
        if (js_dcs == null)
            js_dcs = Lists.newArrayList();
        return js_dcs;
    }

    /**
     * 获取菜谱用到的设备品类
     */
    @JsonProperty("dcs")
    protected List<Dc> js_dcs;


    //2020年6月4日 新增
    @JsonProperty("cookbookPlatforms")
    public List<CookbookPlatforms> cookbookPlatformsList;


    public List<CookBookTagGroup> getJs_cookbook() {
        if (js_cookbook == null)
            return Lists.newArrayList();
        return js_cookbook;
    }

    @JsonProperty("cookbookTagGroups")
    protected List<CookBookTagGroup> js_cookbook;


    /**
     * 食材清单
     */
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "materials_id")
    @JsonProperty("materials")
    public Materials materials;

    /**
     * 备菜步骤
     */
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("prepareSteps")
    public PreStep preStep;

    public String getGroupIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (js_cookbook != null && js_cookbook.size() > 0) {
            for (CookBookTagGroup group : js_cookbook) {
                stringBuilder.append(group.getID() + ":");
            }
            if (stringBuilder != null && !"".equals(stringBuilder))
                return stringBuilder.subSequence(0, stringBuilder.length() - 1).toString();
        }
        //数据库
        if (groupIds == null)
            return "";
        return groupIds;
    }

    /**
     * 菜谱对应GroupId
     */
    @DatabaseField()
    protected String groupIds;
    // ------------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------------------

    @ForeignCollectionField(eager = true)
    private ForeignCollection<CookStep> db_cookSteps;

    public List<CookStep> getJs_cookSteps() {
        if (db_cookSteps != null && db_cookSteps.size() > 0) {
            js_cookSteps = Lists.newArrayList(db_cookSteps);
        }
        if (js_cookSteps == null)
            js_cookSteps = Lists.newArrayList();
        return js_cookSteps;
    }

    @JsonProperty("steps")
    public List<CookStep> js_cookSteps;

    @JsonProperty("categories")
    public List<Categories> categories;

    @Override
    public String toString() {
        try {
            return JsonUtils.pojo2Json(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "抛异常了" + e.toString();
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * 菜谱分享查看 的url链接
     *
     * @return
     */
    public String getViewUrl() {


        //https://h5.myroki.com/dist/index.html#/recipeDetail?cookbookId=30&entranceCode=code1&isFromWx=true&userId=978640479
        String url = String.format("https://h5.myroki.com/dist/index.html#/recipeDetail?cookbookId=" +
                "%d&entranceCode=code1&isFromWx=true&userId=%d", id,Plat.accountService.getCurrentUserId());
        return url;
    }

    public boolean isNewest() {
        return Calendar.getInstance().getTimeInMillis() - lastUpgradeTime <= CookbookManager.UpdatePeriod;
    }

    public void getDetail(Callback<Recipe> callback) {
        CookbookManager.getInstance().getCookbookById(id, callback);
    }


    public void setIsCollected(boolean value) {
//        if (DaoHelper.isExists(getClass(), id)) {
//            DaoHelper.setFieldWhereEq(getClass(), COLUMN_isCollected, value, COLUMN_ID, id);
//            DaoHelper.refresh(this);
//        } else {
            this.collected = value;
//            save2db();
//        }

    }

    public boolean getCollected() {

        return mTrue;
    }


    /**
     * 菜谱详情收藏和取消使用
     */
    static public void setIsFavority(long recipeId, boolean value) {
        Recipe recipe = DaoHelper.getById(Recipe.class, recipeId);
        if (recipe != null) {
            recipe.setIsCollected(value);
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

    @Override
    public void save2db() {
        delete(this.id);
        //super.save2db();

        if (preStep != null) {
            preStep.save2db();
        }


        if (materials != null) {
            materials.save2db();
        }
        if (js_cookbook != null && js_cookbook.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CookBookTagGroup group : js_cookbook) {
                stringBuilder.append(group.getID() + ":");
            }
            if (stringBuilder != null && !"".equals(stringBuilder))
                groupIds = stringBuilder.subSequence(0, stringBuilder.length() - 1).toString();
        }

        lastUpgradeTime = Calendar.getInstance().getTimeInMillis();
        super.save2db();//此位置不能变

        if (js_cookSteps != null) {
//            DaoHelper.deleteWhereEq(CookStep.class, CookStep.COLUMN_BOOK_ID, id);
            for (CookStep cs : js_cookSteps) {
                cs.cookbook = this;
                cs.save2db();
            }
        }

        if (js_dcs != null) {
            for (Dc dc : js_dcs) {
                dc.cookbook = this;
                dc.save2db();
            }
        }

       /* if (js_cookbook != null) {
            for (CookBookTagGroup group : js_cookbook) {
                group.cookbook = this;
                group.save2db();
            }
        }*/


        lastUpgradeTime = hasDetail ? Calendar.getInstance().getTimeInMillis() : 0;
        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            Recipe recipe_db = null;
            if (id != 0)
                recipe_db = DaoHelper.getById(Recipe.class, id);
            else {
                if (this.id != 0)
                    recipe_db = DaoHelper.getById(Recipe.class, this.id);
            }
            if (recipe_db == null) return;
            try {
                recipe_db.preStep.delete(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                recipe_db.materials.delete(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (recipe_db.db_cookSteps != null) {
                for (CookStep step : recipe_db.db_cookSteps) {
                    try {
                        step.delete(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (recipe_db.db_dcs != null) {
                for (Dc dc : recipe_db.db_dcs) {
                    try {
                        dc.delete(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
           /* if (recipe_db.db_cookbook != null) {
                for (CookBookTagGroup group : recipe_db.db_cookbook) {
                    try {
                    }
                        group.delete(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }*/
            DaoHelper.delete(recipe_db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 事物提交
     */
    public void tra2Save() {
//        try {
//            TransactionManager.callInTransaction(DaoService.getInstance().getCurrentDbHelper().getConnectionSource(),
//                    new Callable<Boolean>() {
//                        public Boolean call() throws Exception {
//                            save2db();
//                            return true;
//                        }
//                    });
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 事物删除
     * 如若删除自己 id 为0
     */
    public void tra2Del(final long id) {
        try {
            TransactionManager.callInTransaction(DaoService.getInstance().getCurrentDbHelper().getConnectionSource(),
                    new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            delete(id);
                            return true;
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setTrue(boolean aTrue) {
        mTrue = aTrue;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
