package com.robam.common.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.RCReponse;
import com.legent.services.AbsService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.CookMomentsRefreshEvent;
import com.robam.common.events.FavorityBookRefreshEvent;
import com.robam.common.events.OrderRefreshEvent;
import com.robam.common.events.RefreshReceipeViewEvent;
import com.robam.common.events.TodayBookCleanEvent;
import com.robam.common.events.TodayBookRefreshEvent;
import com.robam.common.events.WifiChangeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.Reponses.CookbooksResponse;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForPadResponse;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Advert;
import com.robam.common.pojos.Advert.MobAdvert;
import com.robam.common.pojos.AdvertImage;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookStepDetails;
import com.robam.common.pojos.CookingKnowledge;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.History;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.OrderContacter;
import com.robam.common.pojos.OrderInfo;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.pojos.RecipeLiveList;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.pojos.RecipeShow;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.Tag;
import com.robam.common.pojos.liveshow;

import java.util.List;
import java.util.Set;

public class StoreService extends AbsService {

    static private StoreService instance = new StoreService();

    synchronized static public StoreService getInstance() {
        return instance;
    }

    int cloudVersion;
    DaoService daoService = DaoService.getInstance();

    private StoreService() {

    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        daoService.init(cx, params);

    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        daoService.switchUser();

    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        daoService.switchUser();

    }

    @Subscribe
    public void onEvent(WifiChangeEvent event) {
        daoService.switchUser();

    }

    // -------------------------------------------------------------------------------
    // IStoreService
    // -------------------------------------------------------------------------------

    public void getCookbookProviders(final Callback<List<RecipeProvider>> callback) {

        RokiRestHelper.getCookbookProviders(new Callback<List<RecipeProvider>>() {

            @Override
            public void onSuccess(List<RecipeProvider> list) {
//                DaoHelper.deleteAll(RecipeProvider.class);
//                if (list != null) {
//                    for (RecipeProvider cp : list) {
//                        cp.save2db();
//                    }
//                }
                Helper.onSuccess(callback, list);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });

    }

    public void getCookbooksByTag(final long tagId,int pageNo,int pageSize,
                                  final Callback<CookbooksResponse> callback) {

        RokiRestHelper.getCookbooksByTag(tagId,pageNo,pageSize,
                new Callback<CookbooksResponse>() {

                    @Override
                    public void onSuccess(CookbooksResponse result) {
                        Tag tag = DaoHelper.getById(Tag.class, tagId);
                        if (tag != null) {
                            tag.save2db(result.cookbooks, result.cookbooks3rd);
                        }

                        Helper.onSuccess(callback, result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Helper.onFailure(callback, t);
                    }
                });

    }

//    public void getCookbooksByName(final String name, Boolean contain3rd,
//                                   final Callback<CookbooksResponse> callback) {
//        RokiRestHelper.getCookbooksByName(name, contain3rd, new Callback<CookbooksResponse>() {
//            @Override
//            public void onSuccess(CookbooksResponse res) {
////                if (res.cookbooks != null) {
////                    for (Recipe r : res.cookbooks) {
////                        r.tra2Save();
////                    }
////                }
////                if (res.cookbooks3rd != null) {
////                    for (Recipe3rd r : res.cookbooks3rd) {
////                        r.save2db();
////                    }
////                }
////
////                LogUtils.i("20190214","cookbooks:" + res.cookbooks.size());
////
////                LogUtils.i("20190214","cookbooks3rd:" + res.cookbooks3rd.size());
//                Helper.onSuccess(callback, res);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }
//    public void getCookbooksByName(final String name, Boolean contain3rd,boolean notNeedSearchHistory,
//                                   final Callback<CookbooksResponse> callback) {
//        RokiRestHelper.getCookbooksByName(name, contain3rd, notNeedSearchHistory,new Callback<CookbooksResponse>() {
//            @Override
//            public void onSuccess(CookbooksResponse res) {
////                if (res.cookbooks != null) {
////                    for (Recipe r : res.cookbooks) {
////                        r.tra2Save();
////                    }
////                }
////                if (res.cookbooks3rd != null) {
////                    for (Recipe3rd r : res.cookbooks3rd) {
////                        r.save2db();
////                    }
////                }
////
////                LogUtils.i("20190214","cookbooks:" + res.cookbooks.size());
////
////                LogUtils.i("20190214","cookbooks3rd:" + res.cookbooks3rd.size());
//                Helper.onSuccess(callback, res);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }
    /* public void getCookbooksClassify(final String name, Boolean contain3rd,
                                      final Callback<Reponses.CookbooksClassifyResponse> callback){
         RokiRestHelper.getCookbooksClassify(name, contain3rd, new Callback<Reponses.CookbooksClassifyResponse>() {
             @Override
             public void onSuccess(Reponses.CookbooksClassifyResponse res) {
                 if (res.cookbooks != null) {
                     LogUtils.i("20170313","res:"+res.toString());
                     for (Cookbooks r : res.cookbooks) {
                         r.save2db();
                     }
                 }
                 if (res.cookbooks3rd != null) {
                     for (Recipe3rd r : res.cookbooks3rd) {
                         r.save2db();
                     }
                 }
                 Helper.onSuccess(callback, res);
             }

             @Override
             public void onFailure(Throwable t) {
                 Helper.onFailure(callback, t);
             }
         });
     }*/
    //????????????app????????????
    public void getRecommendCookbooks(final Callback<List<Recipe>> callback) {

        if (Utils.isMobApp()) {
            RokiRestHelper.getRecommendCookbooksForMob(new Callback<List<Recipe>>() {

                        @Override
                        public void onSuccess(List<Recipe> result) {
                            DaoHelper.setField(Recipe.class, AbsRecipe.COLUMN_isRecommend, false);
                            DaoHelper.setField(Recipe3rd.class, AbsRecipe.COLUMN_isRecommend, false);

                            if (result != null) {
                                LogUtils.i("fuckbug++", result.toString());
                                for (Recipe book : result) {
                                    book.setIsRecommend(true);
                                }
                                //LogUtils.i("fuckbug_after", result.toString());
                            }
                            Helper.onSuccess(callback, result);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            //LogUtils.out(t.getMessage());
                            //LogUtils.i("fuckbug", t.getMessage());
                            Helper.onFailure(callback, t);
                        }
                    });
        } else {

        }
    }

//    public void getHotKeysForCookbook(final Callback<List<String>> callback) {
//        RokiRestHelper.getHotKeysForCookbook(new Callback<List<String>>() {
//
//            @Override
//            public void onSuccess(List<String> result) {
//                Set<String> keys = Sets.newHashSet(result);
//                PreferenceUtils.setStrings(PrefsKey.HotKeys, keys);
//                Helper.onSuccess(callback, result);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    public void getCookbookByIds(long userId,String[] recipes, final Callback<List<Recipe>> callback) {
        RokiRestHelper.getRecipeOfThmem(userId,recipes, new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                if (recipes != null && recipes.size() > 0) {
                    for (Recipe recipe : recipes) {
                        if (recipe != null) {
                            recipe.hasDetail = true;
                            recipe.tra2Save();
                        }
                    }
                    Helper.onSuccess(callback, recipes);
                } else {
                    Helper.onSuccess(callback, null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void getCookBookSteps(long cookbookId, String categoryCode, String PlatCode, final Callback<List<CookStep>> callback){
        RokiRestHelper.getCookbookSteps(cookbookId, categoryCode, PlatCode, new Callback<List<CookStep>>() {
            @Override
            public void onSuccess(List<CookStep> cookSteps) {
                if (cookSteps!=null){
                    for (int i = 0; i < cookSteps.size(); i++) {
                        cookSteps.get(i).save2db();
                    }
                }
                Helper.onSuccess(callback,cookSteps);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    //???????????????
//    public void getCookbookById(long bookId, final Callback<Recipe> callback) {
//        getCookbookById(bookId, null, callback);
//    }

    //??????????????????????????????????????????needStepsInfo.
//    public void getCookbookById(long bookId, String entranceCode,String needStepsInfo, final Callback<Recipe> callback) {
//        RokiRestHelper.getCookbookById(bookId, entranceCode, needStepsInfo, new Callback<Recipe>() {
//            @Override
//            public void onSuccess(Recipe result) {
//                if (result != null) {
//                    result.hasDetail = true;
////                    result.tra2Save();
//
//                }
//                Helper.onSuccess(callback, result);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    /**
     * ?????????????????????????????????
     * @param bookId
     * @param entranceCode
     * @param needStepsInfo 1
     * @param callback
     */
//    public void getCookbookById2(long bookId, String entranceCode,String needStepsInfo, final Callback<Reponses.CookbookResponse> callback) {
//        RokiRestHelper.getCookbookById2(bookId, entranceCode, needStepsInfo, new Callback<Reponses.CookbookResponse>() {
//            @Override
//            public void onSuccess(Reponses.CookbookResponse result) {
//
//                Helper.onSuccess(callback, result);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }
//    public void getCookbookById(long bookId, String entranceCode, final Callback<Recipe> callback) {
//        RokiRestHelper.getCookbookById(bookId, entranceCode, new Callback<Recipe>() {
//
//            @Override
//            public void onSuccess(Recipe result) {
//                if (result != null) {
//                    result.hasDetail = true;
//                    result.tra2Save();
//                }
//                Helper.onSuccess(callback, result);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//
//    }


    //????????????
    public void getYouzanDetailContent(long userId, String type, String telepone,
                                       final Callback<Reponses.TokenResponses>callback){
        RokiRestHelper.getYouzanDetailContent(userId, type, telepone,
                new Callback<Reponses.TokenResponses>() {
                    @Override
                    public void onSuccess(Reponses.TokenResponses tokenResponses) {
                        Helper.onSuccess(callback,tokenResponses);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    //??????????????????
    public void getYouzanOrders(long userId, String[] list,
                                       final Callback<Reponses.YouzanOrdersReponses>callback){
        RokiRestHelper.getYouzanOrders(userId, list,
                new Callback<Reponses.YouzanOrdersReponses>() {
                    @Override
                    public void onSuccess(Reponses.YouzanOrdersReponses result) {
                        Helper.onSuccess(callback,result);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    public void getMallManagement(final Callback<Reponses.MallManagementResponse> callback){

        RokiRestHelper.getMallManagement(new Callback<Reponses.MallManagementResponse>() {
            @Override
            public void onSuccess(Reponses.MallManagementResponse mallManagementResponse) {
                Helper.onSuccess(callback,mallManagementResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback,t);
            }
        });
    }


    //App??????????????????
    public void getAppStartImages(String appType,
                               final retrofit2.Callback<Reponses.AppStartImgResponses> callback){
        RokiRestHelper.getAppStartImages(appType, callback);
    }
    //App????????????????????????
//    public void getAppAdvertImg(final retrofit2.Callback<Reponses.AppAdvertImgResponses> callback){
//        RokiRestHelper.getAppAdvertImg(callback);
//    }

    //????????????????????????
//    public void getCookingKnowledge(String typeCode,int isActive, String lable,int pageNo, int pageSize, final Callback<List<CookingKnowledge>> callback){
//        RokiRestHelper.getCookingKnowledge(typeCode, isActive,lable,pageNo, pageSize, new Callback<Reponses.CookingKnowledgeResponse>() {
//            @Override
//            public void onSuccess(Reponses.CookingKnowledgeResponse cookingKnowledgeResponse) {
//                List<CookingKnowledge> cookingKnowledges = cookingKnowledgeResponse.cookingKnowledges;
//                if (cookingKnowledges != null && cookingKnowledges.size()>0){
//                    Helper.onSuccess(callback,cookingKnowledges);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//
//    }

    //????????????
    public void getOldCookbookById(long bookId, final Callback<Recipe> callback) {
        RokiRestHelper.getOldCookbookById(bookId, new Callback<Recipe>() {

            @Override
            public void onSuccess(Recipe result) {
                if (result != null) {
                    result.hasDetail = true;
                    result.tra2Save();
                }
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });

    }


    public void getAccessoryFrequencyForMob(final Callback<List<MaterialFrequency>> callback) {
        RokiRestHelper.getAccessoryFrequencyForMob(callback);
    }

    public void getTodayCookbooks(final Callback<CookbooksResponse> callback) {
        RokiRestHelper.getTodayCookbooks(new Callback<CookbooksResponse>() {

            @Override
            public void onSuccess(CookbooksResponse result) {
                DaoHelper.setField(Recipe.class, AbsRecipe.COLUMN_isToday, false);
                DaoHelper.setField(Recipe3rd.class, AbsRecipe.COLUMN_isToday, false);

                if (result != null) {
                    if (result.cookbooks != null) {
                        for (Recipe book : result.cookbooks) {
                            book.setIsToday(true);
                        }
                    }

                    if (result.cookbooks3rd != null) {
                        for (Recipe3rd book : result.cookbooks3rd) {
                            book.setIsToday(true);
                        }
                    }
                }

                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void addTodayCookbook(final long bookId, final VoidCallback callback) {
        RokiRestHelper.addTodayCookbook(bookId, new VoidCallback() {
            @Override
            public void onSuccess() {
                AbsRecipe.setIsToday(bookId, true);
                EventUtils.postEvent(new TodayBookRefreshEvent(bookId, true));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void deleteTodayCookbook(final long bookId, final VoidCallback callback) {
        RokiRestHelper.deleteTodayCookbook(bookId, new VoidCallback() {
            @Override
            public void onSuccess() {
                AbsRecipe.setIsToday(bookId, false);
                EventUtils.postEvent(new TodayBookRefreshEvent(bookId, false));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }


    public void deleteKitComment(final long id, final VoidCallback callback) {
        RokiRestHelper.deleteKitComment(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }




    public void deleteAllTodayCookbook(final VoidCallback callback) {
        RokiRestHelper.deleteAllTodayCookbook(new VoidCallback() {
            @Override
            public void onSuccess() {
                DaoHelper.setField(Recipe.class, Recipe.COLUMN_isToday,
                        false);
                DaoHelper.setField(Recipe3rd.class, Recipe3rd.COLUMN_isToday,
                        false);

                EventUtils.postEvent(new TodayBookCleanEvent());
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void exportMaterialsFromToday(Callback<Materials> callback) {
        RokiRestHelper.exportMaterialsFromToday(callback);
    }

    public void addMaterialsToToday(long materialId, VoidCallback callback) {
        RokiRestHelper.addMaterialsToToday(materialId, callback);
    }

    public void deleteMaterialsFromToday(long materialId, VoidCallback callback) {
        RokiRestHelper.deleteMaterialsFromToday(materialId, callback);
    }

//    public void getFavorityCookbooks(final Callback<CookbooksResponse> callback) {
//        RokiRestHelper.getFavorityCookbooks(new Callback<CookbooksResponse>() {
//
//            @Override
//            public void onSuccess(CookbooksResponse result) {
//                DaoHelper.setField(Recipe.class, AbsRecipe.COLUMN_isFavority, false);
//                DaoHelper.setField(Recipe3rd.class, AbsRecipe.COLUMN_isFavority, false);
//                if (result != null) {
//                    if (result.cookbooks != null) {
//                        for (Recipe book : result.cookbooks) {
//                            book.setIsFavority(true);
//                        }
//                    }
//                    if (result.cookbooks3rd != null) {
//                        for (Recipe3rd book : result.cookbooks3rd) {
//                            book.setIsFavority(true);
//                        }
//                    }
//                    EventUtils.postEvent(new RefreshReceipeViewEvent());
//                }
//
//                Helper.onSuccess(callback, result);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

//    public void addFavorityCookbooks(final long bookId, final VoidCallback callback) {
//        RokiRestHelper.addFavorityCookbooks(bookId, new VoidCallback() {
//            @Override
//            public void onSuccess() {
////                Recipe.setIsFavority(bookId, true);
////                EventUtils.postEvent(new FavorityBookRefreshEvent(bookId, true));
//                Helper.onSuccess(callback);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                LogUtils.i("20181113","t:" + t);
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    public void deleteFavorityCookbooks(final long bookId, final VoidCallback callback) {
        RokiRestHelper.deleteFavorityCookbooks(bookId, new VoidCallback() {
            @Override
            public void onSuccess() {
//                Recipe.setIsFavority(bookId, false);
//                EventUtils
//                        .postEvent(new FavorityBookRefreshEvent(bookId, false));
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void delteAllFavorityCookbooks(final VoidCallback callback) {
        RokiRestHelper.delteAllFavorityCookbooks(new VoidCallback() {
            @Override
            public void onSuccess() {
                DaoHelper.setField(Recipe.class, AbsRecipe.COLUMN_isFavority, false);
                DaoHelper.setField(Recipe3rd.class, AbsRecipe.COLUMN_isFavority, false);

                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void getGroundingRecipes(int start, int limit, String language, final Callback<List<Recipe>> callback) {
        RokiRestHelper.getGroundingRecipes(start, limit, language, callback);
    }

    //rent??????
    public void getGroundingRecipes_new(int start, int limit, String language, final Callback<Reponses.ThumbCookbookResponse> callback) {
        RokiRestHelper.getGroundingRecipes_new(start, limit, language, callback);
    }


    /**
     * ?????????????????????
     *
     * @Auth ??????
     */
    public void getPersonalizedRecipes(long userId, int pageNo, int pageSize, final Callback<List<Recipe>> callback) {
        RokiRestHelper.getPersonalizeRecipes(userId, pageNo, pageSize, callback);
    }

    /**
     * ????????????????????????????????????
     * 20160630?????????
     */
//    public void getGroundingRecipesByDevice(String dc, int start, int limit,String recipeType, String devicePlat,final Callback<List<Recipe>> callback) {
//        //RokiRestHelper.getGroundingRecipesByDevice(dc, recipeType,start, limit, devicePlat,callback);
//        RokiRestHelper.getGroundingRecipesByDevice(dc, recipeType,start, limit, callback);
//    }

    /**
     * ????????????????????????
     */
//    public void getIsCollectBookId(long userId, long cookbookId, final Callback<Reponses.IsCollectBookResponse> callback){
//        RokiRestHelper.getIsCollectBook(userId,cookbookId,callback);
//    }

    public void getKuFRecipeInter(Callback<Reponses.GetKufaRecipeResponse> callback){
        RokiRestHelper.getKuFRecipe(callback);
    }

    public void getKuFRecipeDetailInte(String id, Callback<Reponses.GetKuFRecipeDetailResonse> callback){
        RokiRestHelper.getKeRecipeDetail(id,callback);
    }


    /**
     * ????????????????????????????????????
     * 20160630?????????
     */
    public void getTodayRecipesByDevice(String dc, final Callback<CookbooksResponse> callback) {
        RokiRestHelper.getTodayRecipesByDevice(dc, callback);
    }

    public void getGroundingRecipes(int start, int limit, final Callback<List<Recipe>> callback) {
        RokiRestHelper.getGroundingRecipes(start, limit, callback);
    }

    //rent??????
    public void getGroundingRecipes_new(int start, int limit, final Callback<Reponses.ThumbCookbookResponse> callback) {
        RokiRestHelper.getGroundingRecipes_new(start, limit, callback);
    }


    /**
     * ????????????????????????????????????
     * 20160630?????????
     */
    public void getRecommendRecipesByDevice(String dc, final String language, final Callback<List<Recipe>> callback) {
        if (Utils.isMobApp()) {
            RokiRestHelper
                    .getRecommendRecipesByDeviceForCellphone(dc, new Callback<List<Recipe>>() {

                        @Override
                        public void onSuccess(List<Recipe> result) {
                            DaoHelper.setField(Recipe.class, AbsRecipe.COLUMN_isRecommend, false);
                            DaoHelper.setField(Recipe3rd.class, AbsRecipe.COLUMN_isRecommend, false);

                            if (result != null) {
                                for (Recipe book : result) {
                                    book.setIsRecommend(true);
                                }
                            }
                            Helper.onSuccess(callback, result);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Helper.onFailure(callback, t);
                        }
                    });
        }
    }


    /**
     * ???????????????????????????????????????
     * 20160630?????????
     */
    public void getNotRecommendRecipesByDevice(String dc, int start, int limit,
                                               final Callback<List<Recipe>> callback) {

        RokiRestHelper.getNotRecommendRecipesByDevice(dc, start, limit, callback);
    }


    public void addCookingLog(String deviceId, long cookbookId, long start,
                              long end, boolean isBroken, VoidCallback callback) {
        RokiRestHelper.addCookingLog(deviceId, cookbookId, start, end,
                isBroken, callback);
    }

    //???????????????????????????
    public void addCookingLog_New(long cookbookId, int stepCount, String deviceGuid, String appType, long start, long end,
                                  boolean isBroken, List<CookStepDetails> stepDetails, VoidCallback callback){
        RokiRestHelper.addCookingLog_New(cookbookId,stepCount,deviceGuid,appType,start,end,
                isBroken,stepDetails,callback);

    }
    //??????????????????
//    public void getCookbookSearchHistory(long userId, Callback<Reponses.HistoryResponse> callback){
//        RokiRestHelper.getCookbookSearchHistory(userId,callback);
//
//    }

    //??????????????????
    public void deleteCookbookSearchHistory(String name,long userId, Callback<Reponses.DeleteHistoryResponse> callback){
        RokiRestHelper.deleteCookbookSearchHistory(name,userId,callback);

    }


    public void getMyCookAlbumByCookbook(long cookbookId, Callback<CookAlbum> callback) {
        RokiRestHelper.getMyCookAlbumByCookbook(cookbookId, callback);
    }

    public void getOtherCookAlbumsByCookbook(long bookId, int start, int limit,
                                             Callback<List<CookAlbum>> callback) {
        RokiRestHelper.getOtherCookAlbumsByCookbook(bookId, start, limit, callback);
    }

    public void getOtherCookAlbumsByCookbook_new(long bookId, int start, int limit,
                                                 Callback<List<CookAlbum>> callback) {
        RokiRestHelper.getOtherCookAlbumsByCookbook_new(bookId, start, limit, callback);
    }

    public void submitCookAlbum(long bookId, Bitmap image, String desc,
                                final VoidCallback callback) {
        RokiRestHelper.submitCookAlbum(bookId, image, desc, new VoidCallback() {
            @Override
            public void onSuccess() {
                EventUtils
                        .postEvent(new CookMomentsRefreshEvent());
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void removeCookAlbum(final long albumId, final VoidCallback callback) {
        RokiRestHelper.removeCookAlbum(albumId, new VoidCallback() {
            @Override
            public void onSuccess() {
                DaoHelper.deleteWhereEq(CookAlbum.class, CookAlbum.Col_ID, albumId);
                EventUtils
                        .postEvent(new CookMomentsRefreshEvent());
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void praiseCookAlbum(long albumId, VoidCallback callback) {
        RokiRestHelper.praiseCookAlbum(albumId, callback);
    }

    public void unpraiseCookAlbum(long albumId, VoidCallback callback) {
        RokiRestHelper.unpraiseCookAlbum(albumId, callback);
    }

//    public void getMyCookAlbums(final Callback<List<CookAlbum>> callback) {
//        RokiRestHelper.getMyCookAlbums(new Callback<List<CookAlbum>>() {
//            @Override
//            public void onSuccess(List<CookAlbum> albums) {
//                DaoHelper.deleteAll(CookAlbum.class);
//                if (albums != null) {
//                    for (CookAlbum album : albums) {
//                        album.save2db();
//                    }
//                }
//                Helper.onSuccess(callback, albums);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }


    public void clearMyCookAlbums(final VoidCallback callback) {
        RokiRestHelper.clearMyCookAlbums(new VoidCallback() {
            @Override
            public void onSuccess() {
                DaoHelper.deleteAll(CookAlbum.class);
                EventUtils
                        .postEvent(new CookMomentsRefreshEvent());
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }


    public void getHomeAdvertsForMob(final Callback<List<MobAdvert>> callback) {
        RokiRestHelper.getHomeAdvertsForMob(new Callback<List<MobAdvert>>() {

            @Override
            public void onSuccess(List<MobAdvert> list) {
                DaoHelper.deleteAll(MobAdvert.class);
                if (list != null) {
                    for (MobAdvert advert : list) {
                        advert.save2db();
                    }
                }
                Helper.onSuccess(callback, list);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void getHomeTitleForMob(final Callback<List<MobAdvert>> callback) {
        RokiRestHelper.getHomeTitleForMob(new Callback<List<MobAdvert>>() {
            @Override
            public void onSuccess(List<MobAdvert> mobAdverts) {
                DaoHelper.deleteAll(MobAdvert.class);
                if (mobAdverts != null) {
                    for (MobAdvert advert : mobAdverts) {
                        advert.save2db();
                    }
                }
                Helper.onSuccess(callback, mobAdverts);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    //????????????
    public void getYiGuoUrl(final Callback<Reponses.GetYiGuoUrlResponse> callback) {
        RokiRestHelper.getYiGuoUrl(new Callback<Reponses.GetYiGuoUrlResponse>() {

            @Override
            public void onSuccess(Reponses.GetYiGuoUrlResponse getYiGuoUrlResponse) {

                if (getYiGuoUrlResponse != null) {
                    Helper.onSuccess(callback, getYiGuoUrlResponse);
                } else {
                    ToastUtils.show("??????????????????", Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getHomeAdvertsForPad(
            final Callback<HomeAdvertsForPadResponse> callback) {
        RokiRestHelper
                .getHomeAdvertsForPad(new Callback<HomeAdvertsForPadResponse>() {

                    @Override
                    public void onSuccess(HomeAdvertsForPadResponse result) {
                        DaoHelper.deleteAll(Advert.PadAdvert.class);
                        if (result != null) {
                            if (result.left != null) {
                                for (Advert.PadAdvert advert : result.left) {
                                    advert.localtion = Advert.PadAdvert.LEFT;
                                    advert.save2db();
                                }
                            }
                            if (result.middle != null) {
                                for (Advert.PadAdvert advert : result.middle) {
                                    advert.localtion = Advert.PadAdvert.MIDDLE;
                                    advert.save2db();
                                }
                            }
                        }

                        Helper.onSuccess(callback, result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Helper.onFailure(callback, t);
                    }
                });
    }

    public void getFavorityImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        RokiRestHelper
                .getFavorityImagesForPad(new Callback<List<AdvertImage>>() {

                    @Override
                    public void onSuccess(List<AdvertImage> images) {
                        DaoHelper.setField(AdvertImage.class, AdvertImage.FIELD_isFavority,
                                false);
                        if (images != null) {
                            for (AdvertImage img : images) {
                                img.updateField(AdvertImage.FIELD_isFavority, true);
                            }
                        }
                        Helper.onSuccess(callback, images);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Helper.onFailure(callback, t);
                    }
                });
    }

    public void getRecommendImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        RokiRestHelper
                .getRecommendImagesForPad(new Callback<List<AdvertImage>>() {

                    @Override
                    public void onSuccess(List<AdvertImage> images) {
                        DaoHelper.setField(AdvertImage.class, AdvertImage.FIELD_isRecommend,
                                false);
                        if (images != null) {
                            for (AdvertImage img : images) {
                                img.updateField(AdvertImage.FIELD_isRecommend, true);
                            }
                        }
                        Helper.onSuccess(callback, images);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Helper.onFailure(callback, t);
                    }
                });
    }

    public void getAllBookImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        RokiRestHelper.getAllBookImagesForPad(new Callback<List<AdvertImage>>() {

            @Override
            public void onSuccess(List<AdvertImage> images) {
                DaoHelper.setField(AdvertImage.class, AdvertImage.FIELD_isInAll, false);
                if (images != null) {
                    for (AdvertImage img : images) {
                        img.updateField(AdvertImage.FIELD_isInAll, true);
                    }
                }
                Helper.onSuccess(callback, images);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    /******************** RENTAO??????********************/
    /**
     * ????????????????????????????????????
     */
//    public void getThemeRecipe(final Callback<List<RecipeTheme>> callback) {
//        RokiRestHelper.getThemeRecipeList(new Callback<Reponses.RecipeThemeResponse>() {
//            @Override
//            public void onSuccess(Reponses.RecipeThemeResponse recipeThemeResponse) {
//                Helper.onSuccess(callback, recipeThemeResponse.recipeThemes);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    public void getThemeRecipe_new(final Callback<List<RecipeTheme>> callback) {
        RokiRestHelper.getThemeRecipeList_new(new Callback<Reponses.RecipeThemeResponse>() {
            @Override
            public void onSuccess(Reponses.RecipeThemeResponse recipeThemeResponse) {
                Helper.onSuccess(callback, recipeThemeResponse.items);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }
/*
    public void getThemeRecipeDetail(final long themeId, final Callback<Reponses.ThemeRecipeDetailResponse> callback) {
        RokiRestHelper.getThemeRecipeDetail(themeId, new Callback<Reponses.ThemeRecipeDetailResponse>() {
            @Override
            public void onSuccess(Reponses.ThemeRecipeDetailResponse themeRecipeDetailResponse) {
//                Helper.onSuccess(callback, themeRecipeDetailResponse.themeRecipeDetail);
                Helper.onSuccess(callback, themeRecipeDetailResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }
*/
    /**
     * ???????????????????????????
     */
//    public void getMyFavoriteThemeRecipeList(final Callback<List<RecipeTheme>> callback) {
//        if (true) {
//            getMyFavoriteThemeRecipeList_new(callback);
//            return;
//        }
//        RokiRestHelper.getMyFavoriteThemeRecipeList(new Callback<Reponses.RecipeThemeResponse2>() {
//            @Override
//            public void onSuccess(Reponses.RecipeThemeResponse2 recipeThemeResponse) {
//                Helper.onSuccess(callback, recipeThemeResponse.recipeThemes);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }
//
//    public void getMyFavoriteThemeRecipeList_new(final Callback<List<RecipeTheme>> callback) {
//        RokiRestHelper.getMyFavoriteThemeRecipeList_new(new Callback<Reponses.RecipeThemeResponse3>() {
//            @Override
//            public void onSuccess(Reponses.RecipeThemeResponse3 recipeThemeResponse) {
//                Helper.onSuccess(callback, recipeThemeResponse.recipeThemes);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onFailure(callback, t);
//            }
//        });
//    }

    /**
     * ??????????????????????????????
     */
    public void getDynamicCover(final Callback<Reponses.RecipeDynamicCover> callback) {
        RokiRestHelper.getDynamicCover(new Callback<Reponses.RecipeDynamicCover>() {
            @Override
            public void onSuccess(Reponses.RecipeDynamicCover recipeDynamicCover) {
                Helper.onSuccess(callback, recipeDynamicCover);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onSuccess(callback, null);
            }
        });
    }

    /**
     * ????????????????????????
     */
    public void getRecipeLiveList(final int start, final int num, final Callback<List<RecipeLiveList>> callback) {
        RokiRestHelper.getRecipeLiveList(start, num, new Callback<Reponses.RecipeLiveListResponse>() {
            @Override
            public void onSuccess(Reponses.RecipeLiveListResponse recipeLiveList) {
                Helper.onSuccess(callback, recipeLiveList.lives);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onSuccess(callback, null);
            }
        });
    }

    /**
     * ??????????????????
     */
    public void getRecipeShowList(final int start, final int num, final Callback<List<RecipeShow>> callback) {
        RokiRestHelper.getRecipeShowList(start, num, new Callback<Reponses.RecipeShowListResponse>() {
            @Override
            public void onSuccess(Reponses.RecipeShowListResponse recipeShowListResponse) {
                //LogUtils.out("onSuccess:" + recipeShowListResponse.items);
                Helper.onSuccess(callback, recipeShowListResponse.items);
            }

            @Override
            public void onFailure(Throwable t) {
                //LogUtils.out("onFailure:" + t.getMessage());
                Helper.onSuccess(callback, null);
            }
        });
    }

    /**
     * ????????????????????????
     */
    public void getThemeCollectStatus(final long themeId, final Callback<Boolean> callback) {
        RokiRestHelper.getThemeCollectStatus(themeId, new Callback<Reponses.ThemeFavorite>() {
            @Override
            public void onSuccess(Reponses.ThemeFavorite themeFavorite) {
                if (themeFavorite != null && "1".equals(themeFavorite.isFavorite))
                    Helper.onSuccess(callback, true);
                else
                    Helper.onSuccess(callback, false);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onSuccess(callback, false);
            }
        });
    }

    /**
     * ??????
     */
//    public void setThemeCollect(final long themeID, final Callback<Boolean> callback) {
//        RokiRestHelper.setCollectOfTheme(themeID, new Callback<Reponses.CollectStatusRespone>() {
//            @Override
//            public void onSuccess(Reponses.CollectStatusRespone collectStatusRespone) {
//                // LogUtils.i("20170217","collectStatusRespone"+collectStatusRespone.toString());
//                if (collectStatusRespone != null && "1".equals(collectStatusRespone.status)) {
//                    Helper.onSuccess(callback, true);
//                } else if ("0".equals(collectStatusRespone.status)) {
//                    Helper.onSuccess(callback, true);
//                } else {
//                    Helper.onSuccess(callback, false);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onSuccess(callback, false);
//            }
//        });
//    }

    /**
     * ????????????
     */
    public void setCancelThemeCollect(final long themeID, final Callback<Boolean> callback) {
        RokiRestHelper.cancelCollectOfTheme(themeID, new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse rcReponse) {
                if (rcReponse != null && rcReponse.rc == 0)
                    Helper.onSuccess(callback, true);
                else Helper.onSuccess(callback, false);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onSuccess(callback, false);
            }
        });
    }

    /**
     * ??????????????????
     */
    public void getConsultationList(int page, int size, final Callback<List<RecipeConsultation>> callback) {
        RokiRestHelper.getConsultationList(page, size, new Callback<Reponses.ConsultationListResponse>() {
            @Override
            public void onSuccess(Reponses.ConsultationListResponse consultationListResponse) {
                // LogUtils.i("20170414","consultationListResponse:"+consultationListResponse.toString());
                if (consultationListResponse != null && consultationListResponse.items != null && consultationListResponse.items.size() != 0)
                    Helper.onSuccess(callback, consultationListResponse.items);
                else
                    Helper.onSuccess(callback, null);
            }

            @Override
            public void onFailure(Throwable t) {
                //LogUtils.out(t.getMessage());
                Helper.onSuccess(callback, null);
            }
        });
    }

    /**
     * ???????????????????????????
     */
    public void getConsultationList(final Callback<List<RecipeConsultation>> callback) {
        RokiRestHelper.getConsultationList(new Callback<Reponses.ConsultationListResponse>() {
            @Override
            public void onSuccess(Reponses.ConsultationListResponse consultationListResponse) {
                //LogUtils.out(consultationListResponse.items + "");
                if (consultationListResponse != null && consultationListResponse.items != null && consultationListResponse.items.size() != 0)
                    Helper.onSuccess(callback, consultationListResponse.items);
                else
                    Helper.onSuccess(callback, null);
            }

            @Override
            public void onFailure(Throwable t) {
                //LogUtils.out(t.getMessage());
                Helper.onSuccess(callback, null);
            }
        });
    }

    /**
     * ????????????????????????
     */
//    public void getDeviceRecipeImg(String dc, final Callback<Reponses.CategoryRecipeImgRespone> callback) {
//        RokiRestHelper.getDeviceRecipeImg(dc, new Callback<Reponses.CategoryRecipeImgRespone>() {
//            @Override
//            public void onSuccess(Reponses.CategoryRecipeImgRespone categoryRecipeImgRespone) {
//                if (categoryRecipeImgRespone != null)
//                    Helper.onSuccess(callback, categoryRecipeImgRespone);
//                else
//                    Helper.onSuccess(callback, null);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Helper.onSuccess(callback, null);
//            }
//        });
//    }

    /**
     * ????????????????????????
     */
    public void getCurrentLiveShow(final Callback<liveshow> callback) {
        RokiRestHelper.getCurrentLiveShow(new Callback<Reponses.CurrentLiveResponse>() {
            @Override
            public void onSuccess(Reponses.CurrentLiveResponse currentLiveResponse) {
                if (currentLiveResponse != null)
                    Helper.onSuccess(callback, currentLiveResponse.liveshow);
                else
                    Helper.onSuccess(callback, null);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }


    public void applyAfterSale(String deviceId, VoidCallback callback) {
        RokiRestHelper.applyAfterSale(deviceId, callback);
    }

    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    public void getCustomerInfo(final Callback<OrderContacter> callback) {
        RokiRestHelper.getCustomerInfo(callback);
    }

    public void saveCustomerInfo(String name, String phone, String city, String address, final VoidCallback callback) {
        RokiRestHelper.saveCustomerInfo(name, phone, city, address, callback);
    }

    public void submitOrder(List<Long> ids, final Callback<Long> callback) {
        RokiRestHelper.submitOrder(ids, new Callback<Long>() {
            @Override
            public void onSuccess(Long result) {
                EventUtils.postEvent(new OrderRefreshEvent());
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void getOrder(long orderId, final Callback<OrderInfo> callback) {
        RokiRestHelper.getOrder(orderId, callback);
    }

    public void queryOrder(long time, int limit, final Callback<List<OrderInfo>> callback) {
        RokiRestHelper.queryOrder(time, limit, callback);
    }

    public void cancelOrder(long orderId, final VoidCallback callback) {
        RokiRestHelper.cancelOrder(orderId, new VoidCallback() {
            @Override
            public void onSuccess() {
                EventUtils.postEvent(new OrderRefreshEvent());
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param membersCount
     * @param guid
     * @param callback
     */
    public void setFamilyMember(String membersCount, String guid, final VoidCallback callback) {
        RokiRestHelper.setFamilyMember(membersCount, guid, callback);
    }

    public void getFamilyMember(String guid, final Callback<Reponses.GetFamilyResponse> callback) {
        RokiRestHelper.getFamilyMember(guid, new Callback<Reponses.GetFamilyResponse>() {
            @Override
            public void onSuccess(Reponses.GetFamilyResponse getFamilyResponse) {
                if (getFamilyResponse != null) {
                    Helper.onSuccess(callback, getFamilyResponse);
                    if (Plat.DEBUG)
                        LogUtils.i("ssss", "getfamilyResponse" + getFamilyResponse.toString());
                } else {
                    Helper.onSuccess(callback, null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onSuccess(callback, null);
            }
        });
    }

    //?????????????????????
    public void getTodayDrinking(String guid, String timeType, final Callback<Reponses.TodayDrinkingResponse> callback) {
        RokiRestHelper.getTodayDrinking(guid, timeType, new Callback<Reponses.TodayDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.TodayDrinkingResponse todayDrinkingResponse) {
                if (todayDrinkingResponse != null) {
                    Helper.onSuccess(callback, todayDrinkingResponse);
                } else {
                    Helper.onSuccess(callback, null);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onSuccess(callback, null);
            }
        });
    }

    public void getChuYuanAc(int pageNo,int pageSize,int statusisHistory,final Callback<Reponses.ChuYuanActivityResponse> callback){
        RokiRestHelper.getChuYuanAc(pageNo, pageSize,statusisHistory, new Callback<Reponses.ChuYuanActivityResponse>() {
            @Override
            public void onSuccess(Reponses.ChuYuanActivityResponse chuYuanActivityResponse) {
                LogUtils.i("20180523","gggg:::"+chuYuanActivityResponse.toString());
                if (chuYuanActivityResponse!=null){
                    Helper.onSuccess(callback,chuYuanActivityResponse);
                }else{
                    Helper.onSuccess(callback,null);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    //?????????????????????
    public void getHistoryDrinking(String guid, String timeType, String startDate, String endDate, final Callback<Reponses.HistoryDrinkingResponse> callback) {
        RokiRestHelper.getHistoryDrinking(guid, timeType, startDate, endDate, new Callback<Reponses.HistoryDrinkingResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryDrinkingResponse historyDrinkingResponse) {
                if (historyDrinkingResponse != null) {
                    Helper.onSuccess(callback, historyDrinkingResponse);
                } else {
                    Helper.onSuccess(callback, null);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void updateOrderContacter(long orderId, String name, String phone, String city, String address, final VoidCallback callback) {
        RokiRestHelper.updateOrderContacter(orderId, name, phone, city, address, callback);
    }

    public void orderIfOpen(final Callback<Boolean> callback) {
        RokiRestHelper.orderIfOpen(callback);
    }

    public void getEventStatus(final Callback<Reponses.EventStatusReponse> callback) {
        RokiRestHelper.getEventStatus(callback);
    }

    public void deiverIfAllow(final Callback<Integer> callback) {
        RokiRestHelper.deiverIfAllow(callback);
    }


    // -------------------------------------------------------------------------------
    // ??????????????????
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------

    private int getLocalVersion() {
        int ver = SysCfgManager.getInstance().getLocalVersion();
        return ver;
    }

    private long getUserId() {
        return Plat.accountService.getCurrentUserId();
    }

    private void onThrowable(Throwable t) {
        t.printStackTrace();
    }

//    public void getGroundingRecipesByDc(long userId, String dc, String recipeType, int start, int limit, String devicePlat,Callback<List<Recipe>> callback) {
//        RokiRestHelper.getGroundingRecipesByDc(userId,dc, recipeType,start, limit, devicePlat,callback);
//
//    }
}
