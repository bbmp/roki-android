package com.robam.common.services;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.j256.ormlite.stmt.QueryBuilder;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.pojos.RCReponse;
import com.legent.services.AbsService;
import com.legent.services.ConnectivtyService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.Reponses.CookbooksResponse;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookStepDetails;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.History;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.pojos.RecipeLiveList;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.pojos.RecipeShow;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.Tag;
import com.robam.common.util.RecipeDeviceHelper;
import com.robam.common.util.SaveArrayListUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CookbookManager extends AbsService {

    //6小时的更新间隔
    public static final long UpdatePeriod = 1000 * 60 * 60 * 3;
    private static CookbookManager instance = new CookbookManager();

    synchronized static public CookbookManager getInstance() {
        return instance;
    }

    StoreService ss = StoreService.getInstance();

    long lastUpdateTime_recommend;
    long lastUpdateTime_favority;
    ConnectivityManager connectivityManager;

    private CookbookManager() {
        connectivityManager = ConnectivtyService.getInstance().getCm();
    }

    public Recipe getRecipeById(long id) {
        return DaoHelper.getById(Recipe.class, id);
    }

    public Recipe3rd getRecipe3rdById(long id) {
        return DaoHelper.getById(Recipe3rd.class, id);
    }

    // ------------------------------------------------------------------------------------------------------------------

    public void saveHistoryKeysForCookbook(String word) {
        ArrayList<String> list = getHistoryKeysForCookbook();
        if (list != null && list.size() != 0) {
            list.remove(word);
        } else {
            list = new ArrayList<>();
        }
        list.add(0, word);
        if (list.size() >= 5) {
            List subList = list.subList(0, 5);
            ArrayList list2ArrayList = SaveArrayListUtil.getList2ArrayList(subList);
            String key = SaveArrayListUtil.SearchList2String(list2ArrayList);
            PreferenceUtils.setString(PrefsKey.HistoryKeys, key);
        } else {
            String key = SaveArrayListUtil.SearchList2String(list);
            PreferenceUtils.setString(PrefsKey.HistoryKeys, key);
        }
    }

    public ArrayList<String> getHistoryKeysForCookbook() {

        String key = PreferenceUtils.getString(PrefsKey.HistoryKeys, null);
        if (key != null) {
            LogUtils.i("20180327", "key:" + key);
            ArrayList<String> list = SaveArrayListUtil.String2SearchList(key);
            return list;
        }

        return null;
    }

    /**
     * 未登录状态 清楚搜索历史
     */
    public void clearHistoryKeyForCookbook() {
        PreferenceUtils.setString(PrefsKey.HistoryKeys, null);
    }

    public void saveCookingHistory(Recipe recipe) {
        List<String> list = getCookingHistory();
        list.remove(recipe.name);

        list.add(0, recipe.name);
        LogUtils.i("20180327", "name_list" + list);
        list = list.subList(0, Math.min(5, list.size()));
        TreeSet<String> keys = new TreeSet(list);
        PreferenceUtils.setStrings(PrefsKey.HistoryCooking + Plat.accountService.getCurrentUserId(), keys);
    }

    public List<String> getCookingHistory() {

        Set<String> keys = PreferenceUtils.getStrings(PrefsKey.HistoryCooking + Plat.accountService.getCurrentUserId(),
                null);

        List<String> result = Lists.newArrayList();
        if (keys != null) {
            result.addAll(keys);
        }
        for (int i = 0; i < result.size(); i++) {
            LogUtils.i("20180327", "dd" + result.get(i));
        }
        return result;
    }


    // ------------------------------------------------------------------------------------------------------------------
//   未调用
//    public void getProviders(final Callback<List<RecipeProvider>> callback) {
//
//        boolean isNewest = SysCfgManager.getInstance().isNewest();
//        if (isNewest) {
//            List<RecipeProvider> list = DaoHelper.getAll(RecipeProvider.class);
//            if (list != null)
//                Helper.onSuccess(callback, list);
//            else
//                ss.getCookbookProviders(callback);
//        } else {
//            ss.getCookbookProviders(callback);
//        }
//
//    }
//
//    public void getGroups(final Callback<List<Group>> callback) {
//        boolean isNewest = SysCfgManager.getInstance().isNewest();
//        if (isNewest) {
//            List<Group> list = DaoHelper.getAll(Group.class);
//            if (list != null)
//                Helper.onSuccess(callback, list);
//            else
//                ss.getStoreCategory(callback);
//        } else {
//            ss.getStoreCategory(callback);
//        }
//
//    }

//    未调用
//    public void getGroupsWithoutHome(final Callback<List<Group>> callback) {
//
//        getGroups(new Callback<List<Group>>() {
//
//            @Override
//            public void onSuccess(List<Group> result) {
//                Helper.onSuccess(callback, Group.getGroupsWithoutHome());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//
//    }
//
//    public void getHomeTags(final int count, final Callback<List<Tag>> callback) {
//
//        getGroups(new Callback<List<Group>>() {
//
//            @Override
//            public void onSuccess(List<Group> result) {
//                Group homeGroup = Group.getHomeGroup();
//                List<Tag> res = Lists.newArrayList();
//
//                if (homeGroup != null) {
//                    List<Tag> tags = homeGroup.getTags();
//                    if (tags != null && tags.size() > 0) {
//                        res = tags.subList(0, Math.min(count, tags.size()));
//                        Helper.onSuccess(callback, res);
//                    } else {
//                        Helper.onSuccess(callback, res);
//                    }
//                } else {
//                    Helper.onSuccess(callback, res);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//
//    }
//
//    public void getCookbooksByTag(Tag tag,int pageNo,int pageSize,
//                                  final Callback<CookbooksResponse> callback) {
//
//        if (tag.isNewest()) {
//            // TinyBook
//            List<Recipe> books = StoreHelper.getCookooksByTag(tag);
//
//            // Recipe3rd
//            List<Recipe3rd> books2 = StoreHelper.getThirdBooksByTag(tag);
//
//            // CookbooksResponse
//            CookbooksResponse res = new CookbooksResponse();
//            res.cookbooks = books;
//            res.cookbooks3rd = books2;
//
//            Helper.onSuccess(callback, res);
//        } else {
//            ss.getCookbooksByTag(tag.id,pageNo,pageSize, callback);
//        }
//    }
//
//
//    public void getCookbooksByName(final String name, boolean contain3rd,
//                                   final Callback<CookbooksResponse> callback) {
//        ss.getCookbooksByName(name, contain3rd, new Callback<CookbooksResponse>() {
//
//            @Override
//            public void onSuccess(CookbooksResponse result) {
//                Helper.onSuccess(callback, result);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                CookbooksResponse result = StoreHelper.searchByName(name);
//                Helper.onSuccess(callback, result);
//            }
//        });
//    }
//
//    public void getChuYuanAcM(int pageNo, int pageSize,  int statusisHistory, final Callback<Reponses.ChuYuanActivityResponse> callback) {
//        ss.getChuYuanAc(pageNo, pageSize, statusisHistory, new Callback<Reponses.ChuYuanActivityResponse>() {
//            @Override
//            public void onSuccess(Reponses.ChuYuanActivityResponse chuYuanActivityResponse) {
//                Helper.onSuccess(callback, chuYuanActivityResponse);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//
//    }
//
//
//    public void getYouzanDetailContent(long userId, String type, String telephone,
//                                       final Callback<Reponses.TokenResponses> callback) {
//        ss.getYouzanDetailContent(userId, type, telephone, new Callback<Reponses.TokenResponses>() {
//            @Override
//            public void onSuccess(Reponses.TokenResponses tokenReponses) {
//                Helper.onSuccess(callback, tokenReponses);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                LogUtils.i("20170705","ttt:"+t);
//            }
//        });
//
//    }

    /**
     * 获取有赞订单数量
     *
     * @param userId
     * @param list
     * @param callback
     */
    public void getYouzanOrders(long userId, String[] list,
                                final Callback<Reponses.YouzanOrdersReponses> callback) {
        ss.getYouzanOrders(userId, list, new Callback<Reponses.YouzanOrdersReponses>() {

            @Override
            public void onSuccess(Reponses.YouzanOrdersReponses result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20170705", "tt:" + t);

            }
        });

    }

    public void getMallManagement(final Callback<Reponses.MallManagementResponse> callback) {
        ss.getMallManagement(new Callback<Reponses.MallManagementResponse>() {
            @Override
            public void onSuccess(Reponses.MallManagementResponse mallManagementResponse) {
                Helper.onSuccess(callback, mallManagementResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void getCookbooksByName(final String name,
                                   final Callback<CookbooksResponse> callback) {
        ss.getCookbooksByName(name, true, new Callback<CookbooksResponse>() {

            @Override
            public void onSuccess(CookbooksResponse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void getCookbooksBy(final String name, boolean notNeedSearchHistory,
                               final Callback<CookbooksResponse> callback) {
        ss.getCookbooksByName(name, true, notNeedSearchHistory, new Callback<CookbooksResponse>() {

            @Override
            public void onSuccess(CookbooksResponse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                CookbooksResponse result = StoreHelper.searchByName(name);
                Helper.onSuccess(callback, result);
            }
        });
    }

    public void getHotKeysForCookbook(final Callback<List<String>> callback) {
        ss.getHotKeysForCookbook(new Callback<List<String>>() {

            @Override
            public void onSuccess(List<String> result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                Set<String> keys = PreferenceUtils.getStrings(
                        PrefsKey.HotKeys, null);
                List<String> result = Lists.newArrayList();
                if (keys != null) {
                    result.addAll(keys);
                }
                Helper.onSuccess(callback, result);
            }
        });
    }

    /**
     * 灶具
     *
     * @param bookId
     * @param callback 未调用
     */
//    public void getOldCookbookById(final long bookId, final Callback<Recipe> callback) {
//        final Recipe recipe = DaoHelper.getById(Recipe.class, bookId);
//        if (recipe != null && recipe.isNewest() && recipe.hasDetail) {
//            Helper.onSuccess(callback, recipe);
//        } else {
//            ss.getOldCookbookById(bookId, new Callback<Recipe>() {
//                @Override
//                public void onSuccess(Recipe re) {
//                    if (re != null)
//                        Helper.onSuccess(callback, re);
//                    else
//                        Helper.onSuccess(callback, recipe);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    Helper.onFailure(callback, t);
//                    Helper.onSuccess(callback, recipe);
//                }
//            });
//        }
//    }


    /**
     * 烤蒸微
     *
     * @param bookId
     * @param callback
     */

    public void getCookbookById(final long bookId, final Callback<Recipe> callback) {
        final Recipe recipe = DaoHelper.getById(Recipe.class, bookId);
        if (recipe != null && recipe.isNewest() && recipe.hasDetail) {
            Helper.onSuccess(callback, recipe);
        } else {
            ss.getCookbookById(bookId, new Callback<Recipe>() {
                @Override
                public void onSuccess(Recipe re) {
                    if (re != null) {
                        Helper.onSuccess(callback, re);
                        return;
                    }
                    if (recipe != null && recipe.hasDetail)
                        Helper.onSuccess(callback, recipe);
                    else {
                        if (callback != null)
                            callback.onFailure(new Throwable());
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    if (recipe != null && recipe.hasDetail)
                        Helper.onSuccess(callback, recipe);
                    else {
                        if (callback != null)
                            callback.onFailure(t);
                    }
                }
            });
        }
    }

    public void getTodayCookbooks(final Callback<CookbooksResponse> callback) {
        ss.getTodayCookbooks(new Callback<CookbooksResponse>() {

            @Override
            public void onSuccess(CookbooksResponse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                CookbooksResponse result = StoreHelper.getTodayList();
                Helper.onSuccess(callback, result);
            }
        });
    }

    public void getCookbooks(final Callback<List<Recipe>> callback) {
        List<Recipe> list = DaoHelper.getAll(Recipe.class);
        if (list != null && list.size() > 0) {
            Helper.onSuccess(callback, list);
        } else {
            ProgressDialogHelper.hide();
        }
    }

    public void getFavorityCookbooks(final Callback<CookbooksResponse> callback) {
        ss.getFavorityCookbooks(new Callback<CookbooksResponse>() {
            @Override
            public void onSuccess(CookbooksResponse result) {
                lastUpdateTime_favority = Calendar.getInstance().getTimeInMillis();
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                CookbooksResponse result = StoreHelper.getFavorityList();
                Helper.onSuccess(callback, result);
            }
        });
    }

    public void getRecommendCookbooks(final Callback<List<Recipe>> callback) {
        ss.getRecommendCookbooks(new Callback<List<Recipe>>() {

            @Override
            public void onSuccess(List<Recipe> result) {
                lastUpdateTime_recommend = Calendar.getInstance().getTimeInMillis();
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                CookbooksResponse result = StoreHelper.getRecommendList();
                Helper.onSuccess(callback, result.cookbooks);
            }
        });
//        }
    }

//    未调用
//    public void getGroundingRecipes(int start, int limit, String language, final Callback<List<Recipe>> callback) {
//        ss.getGroundingRecipes(start, limit, language, callback);
//    }
//
//
//    public void getPersonalizedRecipeBooks(long userId, int pageNo, int pageSize, final Callback<List<Recipe>> callback) {
//        ss.getPersonalizedRecipes(userId, pageNo, pageSize, callback);
//    }


    /**
     * 根据设备种类获取所有菜谱手机端
     * 20160630周定钧
     */
    public void getGroundingRecipesByDevice(final String dc, final String recipeType, final int start, final int limit, String devicePlat, final Callback<List<Recipe>> callback) {

        getGroundingRecipesByDevice(dc, start, limit, recipeType, null, devicePlat, callback);
    }


    private static final String LastGroundRecipes = "LastUpdateKey_Time";

    //获取所有的商家菜谱包括第三方 但第三方菜谱暂时不考虑存储
    public void getGroundingRecipes_new(final int start, final int limit, final String language, final Callback<Reponses.ThumbCookbookResponse> callback) {
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isAvailable()) {
            Reponses.ThumbCookbookResponse response = new Reponses.ThumbCookbookResponse();
            response.cookbooks = DaoHelper.getPage(Recipe.class, start, limit);
            response.cookbook_3rds = DaoHelper.getPage(Recipe3rd.class, start, limit);
            Helper.onSuccess(callback, response);

        } else {
            ss.getGroundingRecipes_new(start, limit, language, new Callback<Reponses.ThumbCookbookResponse>() {
                @Override
                public void onSuccess(Reponses.ThumbCookbookResponse thumbCookbookResponse) {
                    Reponses.ThumbCookbookResponse response = new Reponses.ThumbCookbookResponse();
                    if (thumbCookbookResponse.cookbooks != null && thumbCookbookResponse.cookbooks.size() > 0) {
                        response.cookbooks = thumbCookbookResponse.cookbooks;
                        for (Recipe recipe : thumbCookbookResponse.cookbooks) {
                            Recipe recipe_dao = DaoHelper.getById(Recipe.class, recipe.id);
                            if (recipe_dao == null || !recipe_dao.isNewest()) {
                                recipe.hasDetail = false;
                                recipe.tra2Save();
                            }
                        }
                    } else {
                        List<Recipe> recipes = DaoHelper.getPage(Recipe.class, start, limit);
                        response.cookbooks = recipes;
                    }
                    if (IAppType.RKPAD.equals(Plat.appType)) {
                        Helper.onSuccess(callback, response);
                        return;
                    }
                    if (thumbCookbookResponse.cookbook_3rds != null && thumbCookbookResponse.cookbook_3rds.size() > 0) {
                        response.cookbook_3rds = thumbCookbookResponse.cookbook_3rds;
                        for (Recipe3rd recipe3rd : thumbCookbookResponse.cookbook_3rds) {
                            recipe3rd.save2db();
                        }
                    } else {
                        List<Recipe3rd> recipes3rd = DaoHelper.getPage(Recipe3rd.class, start, limit);
                        if (recipes3rd != null && recipes3rd.size() > 0) {
                            response.cookbook_3rds = recipes3rd;
                        }
                    }
                    Helper.onSuccess(callback, response);
                }

                @Override
                public void onFailure(Throwable t) {
                    Reponses.ThumbCookbookResponse response = new Reponses.ThumbCookbookResponse();
                    List<Recipe> recipes = DaoHelper.getPage(Recipe.class, start, limit);
                    response.cookbooks = recipes;
                    if (IAppType.RKPAD.equals(Plat.appType)) {
                        Helper.onSuccess(callback, response);
                        return;
                    }
                    List<Recipe3rd> recipes3rd = DaoHelper.getPage(Recipe3rd.class, start, limit);
                    if (recipes3rd != null && recipes3rd.size() > 0) {
                        response.cookbook_3rds = recipes3rd;
                    }
                    Helper.onSuccess(callback, response);
                }
            });
        }
    }

    /**
     * 根据设备种类获取所有菜谱
     * 20160630周定钧
     */
    public void getGroundingRecipesByDevice(final String dc, final int start, final int limit, final String recipeType, String language, String devicePlat, final Callback<List<Recipe>> callback) {

//        LogUtils.i("20171114","connectivityManager:"+connectivityManager+ " isAvailable"+!connectivityManager.getActiveNetworkInfo().isAvailable() );
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isAvailable()) {

            if (false) {
                List<Recipe> list = DaoHelper.getAll(Recipe.class);
                ArrayList<Recipe> recipe_dcs = Lists.newArrayList();
                ArrayList<Recipe> recipe_dcs_out = Lists.newArrayList();
                if (list != null)
                    for (Recipe recipe : list)
                        if (RecipeDeviceHelper.queryIfContainDC(recipe.getJs_dcs(), dc))
                            recipe_dcs.add(recipe);
                if (recipe_dcs.size() - 1 >= start) {
                    for (int i = start; i < start + limit; i++) {
                        if (i >= recipe_dcs.size())
                            break;
                        if (recipe_dcs.get(i) != null) {
                            recipe_dcs_out.add(recipe_dcs.get(i));
                        }
                    }
                }
                Helper.onSuccess(callback, recipe_dcs_out);
            } else {
                try {
                    QueryBuilder queryRecipeBuilder = DaoHelper.getDao(Recipe.class).queryBuilder();
                    QueryBuilder queryDcBuilder = DaoHelper.getDao(Dc.class).queryBuilder();
                    queryDcBuilder.where().eq("dc", dc);
                    ArrayList<Recipe> recipe_dcs_out = (ArrayList<Recipe>) queryRecipeBuilder.leftJoin(queryDcBuilder).offset((long) start).limit((long) limit).distinct().query();
                    Helper.onSuccess(callback, recipe_dcs_out);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Helper.onSuccess(callback, new ArrayList<Recipe>());
                }
            }
        } else {
            ss.getGroundingRecipesByDevice(dc, start, limit, recipeType, devicePlat, new Callback<List<Recipe>>() {
                @Override
                public void onSuccess(List<Recipe> recipes) {
                    if (recipes != null && recipes.size() > 0) {
                        for (Recipe recipe : recipes) {
                            LogUtils.i("20171114", "recipe:" + recipe);
                            Recipe recipe_dao = DaoHelper.getById(Recipe.class, recipe.id);
                            LogUtils.i("20171114", "recipe_dao:" + recipe_dao);
                            if (recipe_dao == null || !recipe_dao.isNewest()) {
                                recipe.hasDetail = false;
                                recipe.tra2Save();
                            }
                        }
                        Helper.onSuccess(callback, recipes);
                    } else {
                        List<Recipe> list = DaoHelper.getAll(Recipe.class);
                        ArrayList<Recipe> recipe_dcs = Lists.newArrayList();
                        ArrayList<Recipe> recipe_dcs_out = Lists.newArrayList();
                        if (list != null)
                            for (Recipe recipe : list)
                                if (RecipeDeviceHelper.queryIfContainDC(recipe.getJs_dcs(), dc))
                                    recipe_dcs.add(recipe);
                        if (recipe_dcs.size() - 1 >= start) {
                            for (int i = start; i < start + limit; i++) {
                                if (i >= recipe_dcs.size())
                                    break;
                                if (recipe_dcs.get(i) != null) {
                                    recipe_dcs_out.add(recipe_dcs.get(i));
                                }
                            }
                        }
                        Helper.onSuccess(callback, recipe_dcs_out);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    List<Recipe> list = DaoHelper.getAll(Recipe.class);
                    ArrayList<Recipe> recipe_dcs = Lists.newArrayList();
                    ArrayList<Recipe> recipe_dcs_out = Lists.newArrayList();
                    if (list != null)
                        for (Recipe recipe : list)
                            if (RecipeDeviceHelper.queryIfContainDC(recipe.getJs_dcs(), dc))
                                recipe_dcs.add(recipe);
                    if (recipe_dcs.size() - 1 >= start) {
                        for (int i = start; i < start + limit; i++) {
                            if (i >= recipe_dcs.size())
                                break;
                            if (recipe_dcs.get(i) != null) {
                                recipe_dcs_out.add(recipe_dcs.get(i));
                            }
                        }
                    }
                    Helper.onSuccess(callback, recipe_dcs_out);
                }
            });
        }
    }


    /**
     * 根据设备种类获取今日菜谱
     * 20160630周定钧 未调用
     */
//    public void getTodayRecipesByDevice(String dc, final Callback<CookbooksResponse> callback) {
//        ss.getTodayRecipesByDevice(dc, callback);
//    }


    // ------------------------------------------------------------------------------------------------------------------

    public void addTodayCookbook(final long bookId, final VoidCallback callback) {
        ss.addTodayCookbook(bookId, callback);
    }

    public void deleteTodayCookbook(final long bookId,
                                    final VoidCallback callback) {
        ss.deleteTodayCookbook(bookId, callback);
    }

    public void deleteKitComment(final long id,
                                 final VoidCallback callback) {
        ss.deleteKitComment(id, callback);
    }

    public void deleteAllTodayCookbook(final VoidCallback callback) {
        ss.deleteAllTodayCookbook(callback);
    }

    public void exportMaterialsFromToday(Callback<Materials> callback) {
        // TODO
        ss.exportMaterialsFromToday(callback);
    }

    public void addMaterialsToToday(long materialId, VoidCallback callback) {
        // TODO
        ss.addMaterialsToToday(materialId, callback);
    }

    public void deleteMaterialsFromToday(long materialId, VoidCallback callback) {
        // TODO
        ss.deleteMaterialsFromToday(materialId, callback);
    }

    // ------------------------------------------------------------------------------------------------------------------

    public void addFavorityCookbooks(final long bookId,
                                     final VoidCallback callback) {
        ss.addFavorityCookbooks(bookId, callback);
    }

    public void deleteFavorityCookbooks(final long bookId,
                                        final VoidCallback callback) {
        ss.deleteFavorityCookbooks(bookId, callback);
    }

    public void delteAllFavorityCookbooks(final VoidCallback callback) {
        ss.delteAllFavorityCookbooks(callback);
    }

    // ------------------------------------------------------------------------------------------------------------------


    public void addCookingLog(String deviceGuid, Recipe recipe, long startTime, long endTime, boolean isBroken, VoidCallback callback) {
        if (recipe == null) return;
        saveCookingHistory(recipe);
        ss.addCookingLog(deviceGuid, recipe.id, startTime, endTime, isBroken, callback);
    }

    //烧菜记录新接口
    public void addCookingLog_New(String deviceGuid, Recipe recipe, int stepCount, String appType, long start,
                                  long end, boolean isBroken, List<CookStepDetails> stepDetails, VoidCallback callback) {
        if (recipe == null) return;

        saveCookingHistory(recipe);
        ss.addCookingLog_New(recipe.id, stepCount, deviceGuid, appType, start, end, isBroken, stepDetails, callback);

    }

    public void getCookbookSearchHistory(long userId, Callback<Reponses.HistoryResponse> callback) {
        ss.getCookbookSearchHistory(userId, callback);
    }

    public void deleteCookbookSearchHistory(String name, long userId, Callback<Reponses.DeleteHistoryResponse> callback) {
        ss.deleteCookbookSearchHistory(name, userId, callback);
    }

    public void getMyCookAlbumByCookbook(long bookId, Callback<CookAlbum> callback) {
        // TODO
        ss.getMyCookAlbumByCookbook(bookId, callback);
    }

    public void getOtherCookAlbumsByCookbook(long bookId, int start, int limit,
                                             Callback<List<CookAlbum>> callback) {
        // TODO
        ss.getOtherCookAlbumsByCookbook(bookId, start, limit, callback);
    }

    public void getOtherCookAlbumsByCookbook_new(long bookId, int start, int limit,
                                                 Callback<List<CookAlbum>> callback) {
        // TODO
        ss.getOtherCookAlbumsByCookbook_new(bookId, start, limit, callback);
    }

    public void submitCookAlbum(long bookId, Bitmap image, String desc,
                                final VoidCallback callback) {
        ss.submitCookAlbum(bookId, image, desc, callback);
    }

    public void removeCookAlbum(final long albumId, final VoidCallback callback) {
        ss.removeCookAlbum(albumId, callback);
    }

    public void praiseCookAlbum(long albumId, final VoidCallback callback) {
        ss.praiseCookAlbum(albumId, callback);
    }

    public void unpraiseCookAlbum(long albumId, final VoidCallback callback) {
        ss.unpraiseCookAlbum(albumId, callback);
    }

    public void getAccessoryFrequencyForMob(final Callback<List<MaterialFrequency>> callback) {
        ss.getAccessoryFrequencyForMob(callback);
    }

    public void getMyCookAlbums(final Callback<List<CookAlbum>> callback) {
        ss.getMyCookAlbums(new Callback<List<CookAlbum>>() {
            @Override
            public void onSuccess(List<CookAlbum> cookAlbums) {
                Helper.onSuccess(callback, cookAlbums);
            }

            @Override
            public void onFailure(Throwable t) {
                List<CookAlbum> cookAlbums = DaoHelper.getAll(CookAlbum.class);
                Helper.onSuccess(callback, cookAlbums);
            }
        });
    }

    public void clearMyCookAlbums(final VoidCallback callback) {
        ss.clearMyCookAlbums(callback);
    }

    /**
     * 获主题菜谱列表精选专题
     */
    //RENT ADD
    public void getThemeRecipes(final Callback<List<RecipeTheme>> callback) {
        //先从网络获取最新的，若失败从底层数据库寻找
        ss.getThemeRecipe(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
//                if (recipeThemes != null && recipeThemes.size() > 0) {
                Helper.onSuccess(callback, recipeThemes);
//                    for (RecipeTheme recipeTheme : recipeThemes) {
//                        recipeTheme.save2db();
//                    }
//                } else {
//                    List<RecipeTheme> themes = DaoHelper.getAll(RecipeTheme.class);
//                    if (themes != null && themes.size() != 0) {
//                        Helper.onSuccess(callback, themes);
//                    } else {
//                        Helper.onSuccess(callback, null);
//                    }
//                }
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
//                List<RecipeTheme> themes = DaoHelper.getAll(RecipeTheme.class);
//                if (themes != null && themes.size() != 0) {
//                    Helper.onSuccess(callback, themes);
//                } else {
//                    Helper.onSuccess(callback, null);
//                }
            }
        });
    }

    public void getThemeRecipes_new(final Callback<List<RecipeTheme>> callback) {
        //先从网络获取最新的，若失败从底层数据库寻找
        ss.getThemeRecipe_new(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
                if (recipeThemes != null && recipeThemes.size() > 0) {
                    Helper.onSuccess(callback, recipeThemes);
                    for (RecipeTheme recipeTheme : recipeThemes) {
                        recipeTheme.save2db();
                    }
                } else {
                    List<RecipeTheme> themes = DaoHelper.getAll(RecipeTheme.class);
                    if (themes != null && themes.size() != 0)
                        Helper.onSuccess(callback, themes);
                    else
                        Helper.onSuccess(callback, null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                List<RecipeTheme> themes = DaoHelper.getAll(RecipeTheme.class);
                if (themes != null && themes.size() != 0)
                    Helper.onSuccess(callback, themes);
                else
                    Helper.onSuccess(callback, null);
            }
        });
    }


    public void getThemeRecipeDetail(final long themeId, final Callback<Reponses.ThemeRecipeDetailResponse> callback) {
        //先从网络获取最新的，若失败从底层数据库寻找
        ss.getThemeRecipeDetail(themeId, new Callback<Reponses.ThemeRecipeDetailResponse>() {
            @Override
            public void onSuccess(Reponses.ThemeRecipeDetailResponse themeRecipeDetailResponse) {
                callback.onSuccess(themeRecipeDetailResponse);

            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);

            }
        });
    }

    /**
     * 获取首页菜谱视频列表
     */
    public void getRecipeLiveList(final int start, final int num, final Callback<List<RecipeLiveList>> callback) {
        ss.getRecipeLiveList(start, num, new Callback<List<RecipeLiveList>>() {
            @Override
            public void onSuccess(List<RecipeLiveList> recipeLiveLists) {
                if (recipeLiveLists != null && recipeLiveLists.size() > 0) {
                    Helper.onSuccess(callback, recipeLiveLists);
                    for (RecipeLiveList liveslist : recipeLiveLists) {
                        liveslist.save2db();
                    }
                } else {
                    List<RecipeLiveList> themes = DaoHelper.getPage(RecipeLiveList.class, start, num);
                    if (themes != null && themes.size() != 0)
                        Helper.onSuccess(callback, themes);
                    else {
                        ToastUtils.show("没有更多视频", Toast.LENGTH_SHORT);
                        Helper.onSuccess(callback, null);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                List<RecipeLiveList> themes = DaoHelper.getPage(RecipeLiveList.class, start, num);
                if (themes != null && themes.size() != 0)
                    Helper.onSuccess(callback, themes);
                else {
                    ToastUtils.show("没有更多视频", Toast.LENGTH_SHORT);
                    Helper.onSuccess(callback, null);
                }
            }
        });
    }

    /**
     * 获取朋友圈厨艺
     */
    public void getRecipeDynamicShow(final int start, final int num, final Callback<List<RecipeShow>> callback) {
        ss.getRecipeShowList(start, num, new Callback<List<RecipeShow>>() {
            @Override
            public void onSuccess(List<RecipeShow> recipeShows) {
                if (recipeShows != null && recipeShows.size() > 0) {
                    Helper.onSuccess(callback, recipeShows);
                    for (RecipeShow liveslist : recipeShows) {
                        liveslist.save2db();
                    }
                } else {
                    List<RecipeShow> recipes = DaoHelper.getPage(RecipeShow.class, start, num);
                    if (recipes != null && recipes.size() != 0)
                        Helper.onSuccess(callback, recipes);
                    else {
                        ToastUtils.show("没有更多厨艺", Toast.LENGTH_SHORT);
                        Helper.onSuccess(callback, null);
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                List<RecipeShow> recipes = DaoHelper.getPage(RecipeShow.class, start, num);
                if (recipes != null && recipes.size() != 0)
                    Helper.onSuccess(callback, recipes);
                else {
                    ToastUtils.show("没有更多厨艺", Toast.LENGTH_SHORT);
                    Helper.onSuccess(callback, null);
                }

            }
        });
    }

    /**
     * 获取咨询列表
     */
    public void getConsultationList(int page, int size, final Callback<List<RecipeConsultation>> callback) {
        ss.getConsultationList(page, size, new Callback<List<RecipeConsultation>>() {
            @Override
            public void onSuccess(List<RecipeConsultation> recipeConsultations) {
                if (recipeConsultations != null && recipeConsultations.size() > 0) {
                    Helper.onSuccess(callback, recipeConsultations);
                    for (RecipeConsultation liveslist : recipeConsultations) {
                        liveslist.save2db();
                    }
                } else {
                    List<RecipeConsultation> recipesConsulations = DaoHelper.getAll(RecipeConsultation.class);
                    if (recipesConsulations != null && recipesConsulations.size() != 0)
                        Helper.onSuccess(callback, recipesConsulations);
                    else {
                        Helper.onSuccess(callback, null);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                List<RecipeConsultation> recipesConsulations = DaoHelper.getAll(RecipeConsultation.class);
                if (recipesConsulations != null && recipesConsulations.size() != 0)
                    Helper.onSuccess(callback, recipesConsulations);
                else {
                    Helper.onSuccess(callback, null);
                }
            }
        });
    }

    /**
     * 获取咨询列表
     */
    public void getConsultationList(final Callback<List<RecipeConsultation>> callback) {
        ss.getConsultationList(new Callback<List<RecipeConsultation>>() {
            @Override
            public void onSuccess(List<RecipeConsultation> recipeConsultations) {
                if (recipeConsultations != null && recipeConsultations.size() > 0) {
                    Helper.onSuccess(callback, recipeConsultations);
                    for (RecipeConsultation liveslist : recipeConsultations) {
                        liveslist.save2db();
                    }
                } else {
                    List<RecipeConsultation> recipesConsulations = DaoHelper.getAll(RecipeConsultation.class);
                    if (recipesConsulations != null && recipesConsulations.size() != 0)
                        Helper.onSuccess(callback, recipesConsulations);
                    else {
                        Helper.onSuccess(callback, null);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                List<RecipeConsultation> recipesConsulations = DaoHelper.getAll(RecipeConsultation.class);
                if (recipesConsulations != null && recipesConsulations.size() != 0)
                    Helper.onSuccess(callback, recipesConsulations);
                else {
                    Helper.onSuccess(callback, null);
                }
            }
        });
    }

    public void getGroundingRecipesByDc(long userId, final String dc, String recipeType, final int start, final int limit, String devicePlat, final Callback<List<Recipe>> callback) {
        ss.getGroundingRecipesByDc(userId, dc, recipeType, start, limit, devicePlat, new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                if (recipes != null && recipes.size() > 0) {
                    for (Recipe recipe : recipes) {
                        LogUtils.i("20171114", "recipe:" + recipe);
                        //这里不需要查询
//                        Recipe recipe_dao = DaoHelper.getById(Recipe.class, recipe.id);
//                        LogUtils.i("20171114", "recipe_dao:" + recipe_dao);
//                        if (recipe_dao == null || !recipe_dao.isNewest()) {
                            recipe.hasDetail = false;
//                            recipe.tra2Save();
//                        }
                    }
                    Helper.onSuccess(callback, recipes);
                } else {
                    List<Recipe> list = DaoHelper.getAll(Recipe.class);
                    ArrayList<Recipe> recipe_dcs = Lists.newArrayList();
                    ArrayList<Recipe> recipe_dcs_out = Lists.newArrayList();
                    if (list != null)
                        for (Recipe recipe : list)
                            if (RecipeDeviceHelper.queryIfContainDC(recipe.getJs_dcs(), dc))
                                recipe_dcs.add(recipe);
                    if (recipe_dcs.size() - 1 >= start) {
                        for (int i = start; i < start + limit; i++) {
                            if (i >= recipe_dcs.size())
                                break;
                            if (recipe_dcs.get(i) != null) {
                                recipe_dcs_out.add(recipe_dcs.get(i));
                            }
                        }
                    }
                    Helper.onSuccess(callback, recipe_dcs_out);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                List<Recipe> list = DaoHelper.getAll(Recipe.class);
                ArrayList<Recipe> recipe_dcs = Lists.newArrayList();
                ArrayList<Recipe> recipe_dcs_out = Lists.newArrayList();
                if (list != null)
                    for (Recipe recipe : list)
                        if (RecipeDeviceHelper.queryIfContainDC(recipe.getJs_dcs(), dc))
                            recipe_dcs.add(recipe);
                if (recipe_dcs.size() - 1 >= start) {
                    for (int i = start; i < start + limit; i++) {
                        if (i >= recipe_dcs.size())
                            break;
                        if (recipe_dcs.get(i) != null) {
                            recipe_dcs_out.add(recipe_dcs.get(i));
                        }
                    }
                }
                Helper.onSuccess(callback, recipe_dcs_out);
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------------


}
