package com.robam.common.io.cloud;

import android.graphics.Bitmap;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.io.RCRetrofitCallback;
import com.legent.plat.io.RCRetrofitCallbackWithVoid;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.RCReponse;
import com.legent.utils.LogUtils;
import com.legent.utils.graphic.BitmapUtils;
import com.robam.common.io.cloud.Reponses.AlbumResponse;
import com.robam.common.io.cloud.Reponses.AlbumsResponse;
import com.robam.common.io.cloud.Reponses.CookbookImageReponse;
import com.robam.common.io.cloud.Reponses.CookbookProviderResponse;
import com.robam.common.io.cloud.Reponses.CookbookResponse;
import com.robam.common.io.cloud.Reponses.CookbooksResponse;
import com.robam.common.io.cloud.Reponses.DeiverIfAllowReponse;
import com.robam.common.io.cloud.Reponses.EventStatusReponse;
import com.robam.common.io.cloud.Reponses.GetCrmCustomerReponse;
import com.robam.common.io.cloud.Reponses.GetCustomerInfoReponse;
import com.robam.common.io.cloud.Reponses.GetOrderReponse;
import com.robam.common.io.cloud.Reponses.GetSmartParams360Reponse;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForMobResponse;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForPadResponse;
import com.robam.common.io.cloud.Reponses.HotKeysForCookbookResponse;
import com.robam.common.io.cloud.Reponses.MaterialFrequencyResponse;
import com.robam.common.io.cloud.Reponses.MaterialsResponse;
import com.robam.common.io.cloud.Reponses.OrderIfOpenReponse;
import com.robam.common.io.cloud.Reponses.QueryMaintainReponse;
import com.robam.common.io.cloud.Reponses.QueryOrderReponse;
import com.robam.common.io.cloud.Reponses.SmartParamsReponse;
import com.robam.common.io.cloud.Reponses.StoreCategoryResponse;
import com.robam.common.io.cloud.Reponses.StoreVersionResponse;
import com.robam.common.io.cloud.Reponses.SubmitOrderReponse;
import com.robam.common.io.cloud.Reponses.ThumbCookbookResponse;
import com.robam.common.io.cloud.Requests.ApplyAfterSaleRequest;
import com.robam.common.io.cloud.Requests.CookAlbumRequest;
import com.robam.common.io.cloud.Requests.CookingLogRequest;
import com.robam.common.io.cloud.Requests.GetCookAlbumsRequest;
import com.robam.common.io.cloud.Requests.GetCookbooksByNameRequest;
import com.robam.common.io.cloud.Requests.GetCookbooksByTagRequest;
import com.robam.common.io.cloud.Requests.GetCrmCustomerRequest;
import com.robam.common.io.cloud.Requests.GetGroudingRecipesRequest;
import com.robam.common.io.cloud.Requests.GetOrderRequest;
import com.robam.common.io.cloud.Requests.GetSmartParams360Request;
import com.robam.common.io.cloud.Requests.GetSmartParamsRequest;
import com.robam.common.io.cloud.Requests.QueryMaintainRequest;
import com.robam.common.io.cloud.Requests.QueryOrderRequest;
import com.robam.common.io.cloud.Requests.SaveCustomerInfoRequest;
import com.robam.common.io.cloud.Requests.SetSmartParams360Request;
import com.robam.common.io.cloud.Requests.SetSmartParamsByDailyRequest;
import com.robam.common.io.cloud.Requests.SetSmartParamsByWeeklyRequest;
import com.robam.common.io.cloud.Requests.StoreRequest;
import com.robam.common.io.cloud.Requests.SubmitCookAlbumRequest;
import com.robam.common.io.cloud.Requests.SubmitMaintainRequest;
import com.robam.common.io.cloud.Requests.SubmitOrderRequest;
import com.robam.common.io.cloud.Requests.UserBookRequest;
import com.robam.common.io.cloud.Requests.UserMaterialRequest;
import com.robam.common.io.cloud.Requests.UserOrderRequest;
import com.robam.common.io.cloud.Requests.UserRequest;
import com.robam.common.pojos.Advert.MobAdvert;
import com.robam.common.pojos.AdvertImage;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookStepDetails;
import com.robam.common.pojos.CrmCustomer;
import com.robam.common.pojos.CrmProduct;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DicGroupDto;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.MaintainInfo;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.NetWorkingSteps;
import com.robam.common.pojos.OrderContacter;
import com.robam.common.pojos.OrderInfo;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.pojos.RecipeTheme;
import com.squareup.okhttp.RequestBody;

import org.eclipse.jetty.util.MultiPartInputStream;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.PUT;
import retrofit2.Call;

public class RokiRestHelper {

    public static final String
            APPLICATION_JSON_ACCEPT_APPLICATION_JSON = "application/json; Accept: application/json";
    private static Gson gson = new Gson();
    private static final String TAG = "RokiRestHelper";
    static IRokiRestService svr = CloudHelper.getRestfulApi(IRokiRestService.class);//Plat.getCustomApi(IRokiRestService.class);


    //用户登陆时会触发
    public static <T extends RCReponse> void getStoreCategory(Class<T> entity, final RetrofitCallback<T> callback) {
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), "{}}");
        Call<ResponseBody> call = svr.getStoreCategory(requestBody);
        enqueue(entity, call, callback);

    }

    //用户登陆时会触发
    static public void getCookbookProviders(
            final Callback<List<RecipeProvider>> callback) {
        svr.getCookbookProviders(new RCRetrofitCallback<CookbookProviderResponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookProviderResponse result) {
                if (result == null) return;
                for (int i = 0; i < result.providers.size(); i++) {
                    result.providers.get(i).save2db();
                }
                callback.onSuccess(result.providers);
            }
        });
    }

    static public void getCookbooksByTag(long tagId, int pageNo, int pageSize,
                                         final Callback<CookbooksResponse> callback) {
        svr.getCookbooksByTag(new GetCookbooksByTagRequest(tagId, pageNo, pageSize),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void getCookbooksByName(String name, Boolean contain3rd,
                                          final Callback<CookbooksResponse> callback) {
        svr.getCookbooksByName(new GetCookbooksByNameRequest(name, contain3rd ? "true" : null, Plat.accountService.isLogon() ? Plat.accountService.getCurrentUserId() : 0),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void getCookbooksByName(String name, Boolean contain3rd, boolean notNeedSearchHistory,
                                          final Callback<CookbooksResponse> callback) {
        svr.getCookbooksByName(new GetCookbooksByNameRequest(name, contain3rd ? "true" : null, Plat.accountService.isLogon() ? Plat.accountService.getCurrentUserId() : 0, notNeedSearchHistory,true),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 根据设备种类获取推荐菜谱 for Pad
     * 20160630周定钧
     */
    static public void getRecommendRecipesByDeviceForPad(long userId, String dc,
                                                         final Callback<List<Recipe>> callback) {
        svr.getRecommendRecipesByDeviceForPad(new Requests.GetRecommendRecipesByDeviceForPadRequest(userId, dc),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        callback.onSuccess(result.cookbooks);
                    }
                });
    }

    /**
     * 根据设备种类获取推荐菜谱ForCellphone
     * 20160630周定钧
     */
    static public void getRecommendRecipesByDeviceForCellphone(String dc,
                                                               final Callback<List<Recipe>> callback) {
        svr.getRecommendRecipesByDeviceForCellphone(new Requests.getRecommendRecipesByDeviceForCellphoneRequest(getUserId(), dc),

                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }
                });
    }

    /**
     * 根据设备种类获取非推荐菜谱
     * 20160630周定钧
     */
    static public void getNotRecommendRecipesByDevice(String dc, int start, int limit,
                                                      final Callback<List<Recipe>> callback) {
        svr.getNotRecommendRecipesByDevice(new Requests.getNotRecommendRecipesByDeviceRequest(dc, start, limit),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }
                });
    }

    //wusi 获取个性话菜谱
    static public void getPersonalizeRecipes(long userId, int pageNo, int pageSize, final Callback<List<Recipe>> callback) {
        svr.getPersonalizedRecipeBooks(new Requests.PersonalizedRecipeRequest(userId, pageNo, pageSize), new RCRetrofitCallback<Reponses.PersonalizedRecipeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.PersonalizedRecipeResponse result) {
                Helper.onSuccess(callback, result.cookbooks);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                LogUtils.i("Personalize", "failure: " + e.toString());
            }
        });
    }


    /**
     * 获取所有上架菜谱的随机某个菜
     *
     * @param randomNum
     * @param callback
     */
    public static <T extends RCReponse> void getRamdomCookBook(int randomNum, Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.RamdomCookBookRequest(randomNum).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getRamdomCookBook(requestBody);
        enqueue(entity, call, callback);

    }

    /**
     * 菜谱周排名
     */
    public static <T extends RCReponse> void getWeekTops(String weekTime, int pageNo, int pageSize, Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.WeekTopsRequest(weekTime, pageNo, pageSize).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getWeekTops(requestBody);
        enqueue(entity, call, callback);

    }

    /**
     * 告诉Roki
     */
    static public void submitSuggestApply(String phone, String pic1, String pic2, String pic3, String suggest, long userId, final Callback<Reponses.SuggestApplyReponse> callback) {
        svr.submitSuggestApply(new Requests.SuggestApplyRequest(phone, pic1, pic2, pic3, suggest, userId), new RCRetrofitCallback<Reponses.SuggestApplyReponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.SuggestApplyReponse result) {
                        super.afterSuccess(result);
                        Helper.onSuccess(callback, result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                    }
                }
        );
    }

    public static void upLoad(String type, Map<String, RequestBody> params, final Callback<Reponses.UploadRepones> callback) {
        svr.upLoad(type, params, new RCRetrofitCallback<Reponses.UploadRepones>(callback) {
            @Override
            public void failure(RetrofitError e) {
                super.failure(e);

            }

            @Override
            protected void afterSuccess(Reponses.UploadRepones result) {
                super.afterSuccess(result);
                Helper.onSuccess(callback, result);
            }
        });
    }

    public static void upLoad(String type, File params, final Callback<Reponses.UploadRepones> callback) {
        svr.upLoad(type, params, new RCRetrofitCallback<Reponses.UploadRepones>(callback) {
            @Override
            public void failure(RetrofitError e) {
                super.failure(e);

            }

            @Override
            protected void afterSuccess(Reponses.UploadRepones result) {
                super.afterSuccess(result);
                Helper.onSuccess(callback, result);
            }
        });
    }

    public static void upLoad(String type, MultiPartInputStream.MultiPart params, final Callback<Reponses.UploadRepones> callback) {
        svr.upLoad(type, params, new RCRetrofitCallback<Reponses.UploadRepones>(callback) {
            @Override
            public void failure(RetrofitError e) {
                super.failure(e);

            }

            @Override
            protected void afterSuccess(Reponses.UploadRepones result) {
                super.afterSuccess(result);
                Helper.onSuccess(callback, result);
            }
        });
    }

    /**
     * 获取某个标签或推荐或周上新的分页菜谱
     */
//    static public void getbyTagOtherCooks(Long cookbookTagId, boolean needStatisticCookbook, int pageNo, int pageSize, int type, final Callback<List<Recipe>> callback) {
//        svr.getbyTagOtherCooks(new Requests.TagOtherCooksRequest(cookbookTagId, needStatisticCookbook, pageNo, pageSize, type), new RCRetrofitCallback<Reponses.PersonalizedRecipeResponse>(callback) {
//            @Override
//            protected void afterSuccess(Reponses.PersonalizedRecipeResponse result) {
//                Helper.onSuccess(callback, result.cookbooks);
//            }
//
//            @Override
//            public void failure(RetrofitError e) {
//                super.failure(e);
//                LogUtils.i("Personalize", "failure: " + e.toString());
//            }
//        });
//    }

    /**
     * 获取某个标签或推荐或周上新的分页菜谱(随机并排除)
     */
    public static <T extends RCReponse> void getbyTagOtherCooks(Long cookbookTagId, boolean needStatisticCookbook, int pageNo, int pageSize, int type, List<Long> excludeCookIds, Class<T> entity,
                                                                final RetrofitCallback<T> callback) {
        String json = new Requests.TagOtherCooksRequest(cookbookTagId, needStatisticCookbook, pageNo, pageSize, type, excludeCookIds).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getbyTagOtherCooks(requestBody);
        enqueue(entity, call, callback);

    }

    /**
     * 获取某个标签或推荐或周上新的分页菜谱
     */
    public static <T extends RCReponse> void getCookbookByTag(Long cookbookTagId, int pageNo, int pageSize, Class<T> entity,
                                                              final RetrofitCallback<T> callback) {
        String json = new Requests.TagCooksRequest(cookbookTagId, pageNo, pageSize).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getCookbookByTag(requestBody);
        enqueue(entity, call, callback);

    }

    /**
     * 查询用户关联多段菜谱列表
     */
    static public void getMultiRecipe(String userId, int pageNo, int pageSize, final Callback<Reponses.MultiRecipeResponse> callback) {
        svr.getMultiRecipe(userId, pageNo, pageSize, new RCRetrofitCallback<Reponses.MultiRecipeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.MultiRecipeResponse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                LogUtils.i("Personalize", "failure: " + e.toString());
            }
        });
    }

    /**
     * 新增/更新 多段菜谱
     */
    static public void saveMultiRecipe(Requests.saveMultiRecipeRequest reqBody,
                                       final Callback<RCReponse> callback) {
        svr.saveMultiRecipe(reqBody, new RCRetrofitCallback<RCReponse>(callback) {
            @Override
            protected void afterSuccess(RCReponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                LogUtils.i("Personalize", "failure: " + e.toString());
            }
        });
    }

    /**
     * 删除 多段菜谱
     */
    static public void deleteMultiRecipe(int id,
                                         final Callback<RCReponse> callback) {
        svr.deleteMultiRecipe(id, new RCRetrofitCallback<RCReponse>(callback) {
            @Override
            protected void afterSuccess(RCReponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                LogUtils.i("Personalize", "failure: " + e.toString());
            }
        });
    }

    /**
     * 删除 多段菜谱
     */
    static public void deleteMultiItem(int id, int no,
                                       final Callback<RCReponse> callback) {
        svr.deleteMultiItem(id, no, new RCRetrofitCallback<RCReponse>(callback) {
            @Override
            protected void afterSuccess(RCReponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError e) {
                Helper.onFailure(callback, ExceptionHelper.newRestfulException(e.getMessage()));
            }
        });
    }

    /**
     * 获取某个标签或推荐或周上新的主题
     */
    public static <T extends RCReponse> void getByTagOtherThemes(Long cookbookTagId, boolean needStatisticCookbook, int pageNo, int pageSize, int type,
                                                                 Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.TagOtherThemesRequest(cookbookTagId, needStatisticCookbook, pageNo, pageSize, type).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getByTagOtherThemes(requestBody);
        enqueue(entity, call, callback);
    }

    /**
     * 根据主题id查询下属所有菜单
     *
     * @param lang
     * @param limit
     * @param start
     * @param themeId
     * @param callback
     */
    public static <T extends RCReponse> void getCookBookBythemeId(String lang, long limit, int start, int themeId,
                                                                  Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.CookbookbythemeIdRequest(lang, limit, start, themeId).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getCookBookBythemeId(requestBody);

        enqueue(entity, call, callback);

    }

    /**
     * 获取全量字典
     *
     * @param callback
     */
    static public void getGatewayDic(final Callback<HashMap<String, Object>> callback) {
        svr.getGatewayDic(new RCRetrofitCallback<Reponses.GatewayDicResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GatewayDicResponse result) {
//                super.afterSuccess(result);
                LogUtils.i(TAG, "rc:" + result.rc);
                callback.onSuccess(result.dicGroupDtoList);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                LogUtils.i(TAG, e.toString());
                callback.onFailure(e);
            }
        });
    }


    public static <T extends RCReponse> void getIsCollectBook(long userId, long cookbookId, Class<T>entity, final RetrofitCallback<T> callback) {
        String json = new Requests.IsCollectRequest(userId, cookbookId).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getIsCollectBook(requestBody);
        enqueue(entity, call, callback);

    }


    static public void getKuFRecipe(final Callback<Reponses.GetKufaRecipeResponse> callback) {
        svr.getKuFRecipe(new RCRetrofitCallback<Reponses.GetKufaRecipeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetKufaRecipeResponse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
            }
        });
    }

    static public void getKeRecipeDetail(String id, final Callback<Reponses.GetKuFRecipeDetailResonse> callback) {
        svr.setGetKuFRecipeDetail(id, new RCRetrofitCallback<Reponses.GetKuFRecipeDetailResonse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetKuFRecipeDetailResonse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void failure(RetrofitError e) {
                LogUtils.i("20180612", "e::" + e);
                super.failure(e);
            }
        });
    }


    /**
     * 根据设备种类获取今日菜谱
     * 20160630周定钧
     */
    static public void getTodayRecipesByDevice(String dc,
                                               final Callback<CookbooksResponse> callback) {
        svr.getTodayRecipesByDevice(new Requests.GetTodayRecipesByDeviceRequest(getUserId(), dc),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }
//RENT ADD//

    /**
     * 获取主题菜谱列表精选专题
     */
    public static <T extends RCReponse> void getThemeRecipeList(Class<T> entity, final RetrofitCallback<T> callback) {
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), "{}");
        Call<ResponseBody> call = svr.setGetThemeRecipeList(requestBody);
        enqueue(entity, call, callback);
    }


    static public void getThemeRecipeList_new(final Callback<Reponses.RecipeThemeResponse> callback) {
        svr.setGetThemeRecipeList_new(new RCRetrofitCallback<Reponses.RecipeThemeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    public static <T extends RCReponse> void getThemeRecipeDetail(final long themeId, Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.ThemeRecipeDetailRequest(themeId).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getThemeRecipeDetail(requestBody);
        enqueue(entity, call, callback);

    }

    /**
     * 获取已收藏主题菜谱列表
     */
    static public void getMyFavoriteThemeRecipeList(final Callback<Reponses.RecipeThemeResponse2> callback) {
        svr.getMyFavoriteThemeRecipeList(getUserId() + "", new RCRetrofitCallback<Reponses.RecipeThemeResponse2>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse2 result) {
                callback.onSuccess(result);
            }
        });
    }

    static public void getMyFavoriteThemeRecipeList_new(final Callback<Reponses.RecipeThemeResponse3> callback) {
        LogUtils.i("20161021", getUserId() + "");
        svr.getMyFavoriteThemeRecipeList_new(getUserId() + "", new RCRetrofitCallback<Reponses.RecipeThemeResponse3>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse3 result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取动态封面
     */
    static public void getDynamicCover(final Callback<Reponses.RecipeDynamicCover> callback) {
        svr.getDynamicCover(new RCRetrofitCallback<Reponses.RecipeDynamicCover>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeDynamicCover result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取直播视频列表
     */
    static public void getRecipeLiveList(final int start, final int num, final Callback<Reponses.RecipeLiveListResponse> callback) {
        svr.getRecipeLiveList(new Requests.GetPageRequest(start, num), new RCRetrofitCallback<Reponses.RecipeLiveListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeLiveListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取动态厨艺
     */
    static public void getRecipeShowList(final int start, final int num, final Callback<Reponses.RecipeShowListResponse> callback) {
        svr.getRecipeShowList(new Requests.GetPageUserRequest(start, num), new RCRetrofitCallback<Reponses.RecipeShowListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeShowListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取这道菜是否收藏过
     */
    static public void getThemeCollectStatus(final long themeId, final Callback<Reponses.ThemeFavorite> callback) {
        svr.getThemeCollectStatus(new Requests.ThemeCollectRequest(getUserId(), themeId), new RCRetrofitCallback<Reponses.ThemeFavorite>(callback) {
            @Override
            protected void afterSuccess(Reponses.ThemeFavorite result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 主题收藏
     */
    static public void setCollectOfTheme(final long themeId, final Callback<Reponses.CollectStatusRespone> callback) {
        svr.setSetCollectOfTheme(new Requests.ThemeCollectRequest(getUserId(), themeId), new RCRetrofitCallback<Reponses.CollectStatusRespone>(callback) {
            @Override
            protected void afterSuccess(Reponses.CollectStatusRespone result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 主题取消收藏
     */
    static public void cancelCollectOfTheme(final long themeId, final Callback<RCReponse> callback) {
        svr.setCancelCollectOfTheme(new Requests.ThemeCollectRequest(getUserId(), themeId), new RCRetrofitCallback<RCReponse>(callback) {
            @Override
            protected void afterSuccess(RCReponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取咨询列表
     */
    static public void getConsultationList(int page, int size, final Callback<Reponses.ConsultationListResponse> callback) {
        svr.getConsultationList(new Requests.ConsultationListRequest(page, size), new RCRetrofitCallback<Reponses.ConsultationListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.ConsultationListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取咨询列表
     */
    static public void getConsultationList(final Callback<Reponses.ConsultationListResponse> callback) {
        svr.getConsultationList(new RCRetrofitCallback<Reponses.ConsultationListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.ConsultationListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取设备菜谱封面
     */
    public static <T extends RCReponse> void getDeviceRecipeImg(String dc, Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.CategoryRecipeImgRequest(dc).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getDeviceRecipeImg(requestBody);
        enqueue(entity, call, callback);

    }

    /**
     * 获取直播视频信息
     */
    static public void getCurrentLiveShow(final Callback<Reponses.CurrentLiveResponse> callback) {
        svr.getCurrentLiveShow(new RCRetrofitCallback<Reponses.CurrentLiveResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.CurrentLiveResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    static public void getSeasonCookbooks(
            final Callback<CookbooksResponse> callback) {
        svr.getSeasonCookbooks(new RCRetrofitCallback<CookbooksResponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbooksResponse result) {
                callback.onSuccess(result);
            }
        });
    }


    static public void getRecommendCookbooksForMob(
            final Callback<List<Recipe>> callback) {
        svr.getRecommendCookbooksForMob(new UserRequest(getUserId()),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        callback.onSuccess(result.cookbooks);
                    }
                });
    }


    static public void getRecommendCookbooksForPad(
            final Callback<List<Recipe>> callback) {
        svr.getRecommendCookbooksForPad(new UserRequest(getUserId()),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        callback.onSuccess(result.cookbooks);
                    }
                });
    }

    static public void getHotKeysForCookbook(
            final Callback<List<String>> callback) {
        svr.getHotKeysForCookbook(new RCRetrofitCallback<HotKeysForCookbookResponse>(
                callback) {
            @Override
            protected void afterSuccess(HotKeysForCookbookResponse result) {
                LogUtils.i("RecipeBook", "result:" + result.toString());
                callback.onSuccess(result.hotKeys);
            }
        });
    }


    public static <T extends RCReponse> void getCookbookById(long cookbookId,
                                                             final Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new UserBookRequest(getUserId(), cookbookId).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getCookbookById(requestBody);
        enqueue(entity, call, callback);
    }

    static public void getCookbookSteps(long cookbookId, String categoryCode, String platCode, final Callback<List<CookStep>> callback) {
        svr.getCookbookSteps(new Requests.UserCookBookSteps(cookbookId, categoryCode, platCode),
                new RCRetrofitCallback<Reponses.CookbookStepResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.CookbookStepResponse result) {
                        callback.onSuccess(result.cookSteps);
                    }
                });
    }


    static public void getYouzanDetailContent(long userId, String type, String telephone,
                                              final Callback<Reponses.TokenResponses> callback) {
        svr.GetYouzanDetailContent(new Requests.GetYouzanRequst(userId, type, telephone),
                new RCRetrofitCallback<Reponses.TokenResponses>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.TokenResponses result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 获取有赞订单数量
     */
    static public void getYouzanOrders(long userId, String[] list, final Callback<Reponses.YouzanOrdersReponses> callback) {
        svr.getYouzanOrders(new Requests.GetYouzanOrdersRequst(userId, list),
                new RCRetrofitCallback<Reponses.YouzanOrdersReponses>(callback) {

                    @Override
                    protected void afterSuccess(Reponses.YouzanOrdersReponses result) {
                        callback.onSuccess(result);
                    }

                });
    }


    static public void getMallManagement(final Callback<Reponses.MallManagementResponse> callback) {

        svr.getMallManagement(new Requests.MallManagementRequest(), new RCRetrofitCallback<Reponses.MallManagementResponse>(callback) {

            @Override
            protected void afterSuccess(Reponses.MallManagementResponse result) {
                callback.onSuccess(result);
            }
        });

    }

    /**
     * App启动时图片
     */
    static public void getAppStartImages(String appType, final retrofit2.Callback<Reponses.AppStartImgResponses> callback) {
        String json = new Requests.GetAppStartImg(appType).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse("application/json; Accept: application/json"), json);
        Call<Reponses.AppStartImgResponses> call = svr.getAppStartImages(requestBody);
        call.enqueue(callback);
    }

    /**
     * App启动时广告图片
     */
    public static <T extends RCReponse> void getAppAdvertImg(Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Reponses.AppAdvertImgResponses().toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse("application/json; Accept: application/json"), json);
        Call<ResponseBody> call = svr.getAppAdvertImg(requestBody);

        enqueue(entity, call, callback);
    }

    //获取厨房知识列表
    public static <T extends RCReponse> void getCookingKnowledge(String typeCode, int isActive, String lable, int pageNo, int pageSize,
                                           final Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.CookingKnowledgeRequest(typeCode, isActive, lable, pageNo, pageSize).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse("application/json; Accept: application/json"), json);
        Call<ResponseBody> call = svr.getCookingKnowledge(requestBody);
        enqueue(entity, call, callback);

    }


    public static <T extends RCReponse> void getCookbookById(long cookbookId, String entranceCode, String needStepsInfo,
                                       Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new UserBookRequest(getUserId(), cookbookId, entranceCode, needStepsInfo).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getNewCookbookById(requestBody);
        enqueue(entity, call, callback);
    }

    /**
     * 获取菜谱详情及烹饪步骤
     *
     * @param cookbookId
     * @param entranceCode
     * @param needStepsInfo
     * @param callback
     */
//    public static void getCookbookById2(long cookbookId, String entranceCode, String needStepsInfo,
//                                        final Callback<CookbookResponse> callback) {
//        svr.getNewCookbookById(new UserBookRequest(getUserId(), cookbookId, entranceCode, needStepsInfo),
//                new RCRetrofitCallback<CookbookResponse>(callback) {
//                    @Override
//                    protected void afterSuccess(CookbookResponse result) {
////                        LogUtils.i("20170415", "result15:" + result.toString());
//                        callback.onSuccess(result);
//                    }
//                });
//    }

    static public void getRecipeOfThmem(long userId, String[] strings, final Callback<List<Recipe>> callback) {
        svr.getRecipeOfThemeList(new Requests.GetReicpeOfTheme(userId, strings),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result.cookbooks);
                    }
                });
    }


    static public void getOldCookbookById(long cookbookId,
                                          final Callback<Recipe> callback) {
        svr.getOldCookbookById(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallback<CookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbookResponse result) {
                        LogUtils.i("20170415", "result15:" + result.toString());
                        callback.onSuccess(result.cookbook);
                    }
                });
    }

    static public void getAccessoryFrequencyForMob(final Callback<List<MaterialFrequency>> callback) {
        svr.getAccessoryFrequencyForMob(new UserRequest(0), new RCRetrofitCallback<MaterialFrequencyResponse>(callback) {
            @Override
            protected void afterSuccess(MaterialFrequencyResponse result) {
                Helper.onSuccess(callback, result.list);
            }
        });
    }

    // -------------------------------------------------------------------------------

    static public void getTodayCookbooks(
            final Callback<CookbooksResponse> callback) {
        svr.getTodayCookbooks(new UserRequest(getUserId()),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 设置家庭人数
     */
    static public void setFamilyMember(String memberCount, String guid, final VoidCallback callback) {
        svr.setFamilyMember(new Requests.FamilyMember(getUserId(), memberCount, guid),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getFamilyMember(String guid, final Callback<Reponses.GetFamilyResponse> callback) {
        svr.getFamilyMember(new Requests.getFamilytotal(getUserId(), guid),
                new RCRetrofitCallback<Reponses.GetFamilyResponse>(callback) {
                    protected void afterSuccess(Reponses.GetFamilyResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 获取今日饮水量
     *
     * @param guid
     * @param timeType
     * @param callback
     */
    static public void getTodayDrinking(String guid, String timeType, final Callback<Reponses.TodayDrinkingResponse> callback) {
        svr.getTodayDrinking(new Requests.TodayDrinkingRequest(getUserId(), guid, timeType),
                new RCRetrofitCallback<Reponses.TodayDrinkingResponse>(callback) {
                    protected void afterSuccess(Reponses.TodayDrinkingResponse result) {
                        callback.onSuccess(result);
                    }
                });

    }

    /**
     * 获取厨源活动
     */
    static public void getChuYuanAc(int pageNo, int pageSize, int statusisHistory, final Callback<Reponses.ChuYuanActivityResponse> callback) {
        svr.getGetChuYuanActivityMethod(new Requests.getChuYuan(pageNo, pageSize, statusisHistory), new RCRetrofitCallback<Reponses.ChuYuanActivityResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.ChuYuanActivityResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取历史饮水量
     *
     * @param guid
     * @param timeType
     * @param callback
     */

    static public void getHistoryDrinking(String guid, String timeType, String startDate, String endDate,
                                          final Callback<Reponses.HistoryDrinkingResponse> callback) {
        svr.getHistoryDrinking(new Requests.HistoryDrinkingRequest(getUserId(), guid, timeType, startDate, endDate), new RCRetrofitCallback<Reponses.HistoryDrinkingResponse>(callback) {
            protected void afterSuccess(Reponses.HistoryDrinkingResponse result) {
                LogUtils.i("20180326", "result::" + result.toString());
                callback.onSuccess(result);
                if (Plat.DEBUG)
                    LogUtils.i("rc", "rc:" + result);
            }
        });

    }


    static public void addTodayCookbook(long cookbookId,
                                        final VoidCallback callback) {
        svr.addTodayCookbook(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteTodayCookbook(long cookbookId,
                                           VoidCallback callback) {
        svr.deleteTodayCookbook(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    //删除厨源论
    static public void deleteKitComment(long id, VoidCallback callback) {
        svr.deleteKitComment(new Requests.UserCommentRequest(getUserId(), id),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteAllTodayCookbook(VoidCallback callback) {
        svr.deleteAllTodayCookbook(new UserRequest(getUserId()),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void exportMaterialsFromToday(
            final Callback<Materials> callback) {
        svr.exportMaterialsFromToday(new UserRequest(getUserId()),
                new RCRetrofitCallback<MaterialsResponse>(callback) {
                    @Override
                    protected void afterSuccess(MaterialsResponse result) {
                        callback.onSuccess(result.materials);
                    }
                });
    }

    static public void addMaterialsToToday(long materialId,
                                           VoidCallback callback) {
        svr.addMaterialsToToday(
                new UserMaterialRequest(getUserId(), materialId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteMaterialsFromToday(long materialId,
                                                VoidCallback callback) {
        svr.deleteMaterialsFromToday(new UserMaterialRequest(getUserId(),
                        materialId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }


    /**
     * 烹饪曲线列表
     */
    static public void getCurveCookbooks(String userId,
                                         final Callback<Reponses.GetCurveCookbooksResonse> callback) {
        svr.queryCurveCookbooks(userId,
                new RCRetrofitCallback<Reponses.GetCurveCookbooksResonse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.GetCurveCookbooksResonse result) {
                        super.afterSuccess(result);
                        Helper.onSuccess(callback, result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
                        LogUtils.i("Personalize", "failure: " + e.toString());
                    }
                });
    }

    // -------------------------------------------------------------------------------

    static public void getFavorityCookbooks(
            final Callback<CookbooksResponse> callback) {
        svr.getFavorityCookbooks(new UserRequest(getUserId()),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void addFavorityCookbooks(long cookbookId,
                                            VoidCallback callback) {
        svr.addFavorityCookbooks(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteFavorityCookbooks(long cookbookId,
                                               VoidCallback callback) {
        svr.deleteFavorityCookbooks(
                new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void delteAllFavorityCookbooks(VoidCallback callback) {
        svr.delteAllFavorityCookbooks(new UserRequest(getUserId()),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getGroundingRecipes(int start, int limit, String lang,
                                           final Callback<List<Recipe>> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit, lang), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result.cookbooks);
            }
        });
    }

    //rent新增
    static public void getGroundingRecipes_new(int start, int limit, String lang,
                                               final Callback<ThumbCookbookResponse> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit, lang), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void getGroundingRecipes(int start, int limit,
                                           final Callback<List<Recipe>> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result.cookbooks);
            }
        });
    }

    //rent新增
    static public void getGroundingRecipes_new(int start, int limit,
                                               final Callback<ThumbCookbookResponse> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    /**
     * 根据设备种类获取所有菜谱
     * 20160630周定钧
     */
    public static <T extends RCReponse> void getGroundingRecipesByDevice(String dc, String recipeType, int start, int limit,
                                                   Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.getGroundingRecipesByDeviceRequest(dc, recipeType, start, limit).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getGroundingRecipesByDevice(requestBody);
        enqueue(entity, call, callback);

    }

    // -------------------------------------------------------------------------------

    static public void addCookingLog(String deviceId, long cookbookId,
                                     long start, long end, boolean isBroken, final VoidCallback callback) {
        svr.addCookingLog(new CookingLogRequest(getUserId(), cookbookId,
                        deviceId, start, end, isBroken),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    //烧菜记录提交
    static public void addCookingLog_New(long cookbookId, int stepCount, String deviceGuid, String appType,
                                         long start, long end, boolean isBroken, List<CookStepDetails> stepDetails,
                                         final VoidCallback callback) {
        svr.addCookingLog_New(new CookingLogRequest(getUserId(), cookbookId, stepCount, deviceGuid, appType,
                start, end, isBroken, stepDetails), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    //获取搜索记录
    static public void getCookbookSearchHistory(long userId, final Callback<Reponses.HistoryResponse> callback) {
        svr.getCookbookSearchHistory(new Requests.CookbookSearchHistory(userId), new RCRetrofitCallback<Reponses.HistoryResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.HistoryResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    //删除搜索记录
    static public void deleteCookbookSearchHistory(String name, long userId, final Callback<Reponses.DeleteHistoryResponse> callback) {
        svr.deleteCookbookSearchHistory(new Requests.DeleteCookbookSearchHistory(name, userId), new RCRetrofitCallback<Reponses.DeleteHistoryResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.DeleteHistoryResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }


    static public void getMyCookAlbumByCookbook(long cookbookId,
                                                final Callback<CookAlbum> callback) {

        svr.getMyCookAlbumByCookbook(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallback<AlbumResponse>(callback) {
                    @Override
                    protected void afterSuccess(AlbumResponse result) {
                        callback.onSuccess(result.album);
                    }
                });

    }

    static public void getOtherCookAlbumsByCookbook(long cookbookId, int start, int limit,
                                                    final Callback<List<CookAlbum>> callback) {
        svr.getOtherCookAlbumsByCookbook(new GetCookAlbumsRequest(getUserId(), cookbookId,
                        start, limit),
                new RCRetrofitCallback<AlbumsResponse>(callback) {
                    @Override
                    protected void afterSuccess(AlbumsResponse result) {
                        callback.onSuccess(result.cookAlbums);
                    }
                });
    }

    static public void getOtherCookAlbumsByCookbook_new(long cookbookId, int start, int limit,
                                                        final Callback<List<CookAlbum>> callback) {
        svr.getOtherCookAlbumsByCookbook_new(new GetCookAlbumsRequest(getUserId(), cookbookId,
                        start, limit),
                new RCRetrofitCallback<AlbumsResponse>(callback) {
                    @Override
                    protected void afterSuccess(AlbumsResponse result) {
                        callback.onSuccess(result.cookAlbums);
                    }
                });
    }

    static public void submitCookAlbum(long cookbookId, Bitmap image,
                                       String desc, VoidCallback callback) {
        String strImg = BitmapUtils.toBase64(image);
        svr.submitCookAlbum(new SubmitCookAlbumRequest(getUserId(), cookbookId,
                strImg, desc), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));
    }

    static public void removeCookAlbum(long albumId, VoidCallback callback) {
        svr.removeCookAlbum(new CookAlbumRequest(getUserId(), albumId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void praiseCookAlbum(long albumId, VoidCallback callback) {
        svr.praiseCookAlbum(new CookAlbumRequest(getUserId(), albumId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unpraiseCookAlbum(long albumId, VoidCallback callback) {
        svr.unpraiseCookAlbum(new CookAlbumRequest(getUserId(), albumId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getMyCookAlbums(final Callback<List<CookAlbum>> callback) {
        svr.getMyCookAlbums(new UserRequest(getUserId()), new RCRetrofitCallback<AlbumsResponse>(callback) {
            @Override
            protected void afterSuccess(AlbumsResponse result) {
                Helper.onSuccess(callback, result.cookAlbums);
            }
        });
    }

    static public void clearMyCookAlbums(final VoidCallback callback) {
        svr.clearMyCookAlbums(new UserRequest(getUserId()), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // -------------------------------------------------------------------------------

    static public void getHomeAdvertsForMob(
            final Callback<List<MobAdvert>> callback) {
        svr.getHomeAdvertsForMob(new RCRetrofitCallback<HomeAdvertsForMobResponse>(
                callback) {
            @Override
            protected void afterSuccess(HomeAdvertsForMobResponse result) {
                callback.onSuccess(result.adverts);
            }
        });
    }

    static public void getHomeTitleForMob(final Callback<List<MobAdvert>> callback) {
        svr.getHomeTitleForMob(new RCRetrofitCallback<Reponses.HomeTitleForMobResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.HomeTitleForMobResponse result) {
                callback.onSuccess(result.titles);
            }
        });
    }

    static public void getHomeAdvertsForPad(
            final Callback<HomeAdvertsForPadResponse> callback) {
        svr.getHomeAdvertsForPad(new RCRetrofitCallback<HomeAdvertsForPadResponse>(
                callback) {
            @Override
            protected void afterSuccess(HomeAdvertsForPadResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    static public void getYiGuoUrl(final Callback<Reponses.GetYiGuoUrlResponse> callback) {
        svr.getYiGuoUrl(new RCRetrofitCallback<Reponses.GetYiGuoUrlResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetYiGuoUrlResponse result) {
                LogUtils.i("20170222", "result:" + result.toString());
                callback.onSuccess(result);
            }
        });

    }

    static public void getFavorityImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        svr.getFavorityImagesForPad(new RCRetrofitCallback<CookbookImageReponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookImageReponse result) {
                callback.onSuccess(result.images);
            }
        });
    }

    static public void getRecommendImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        svr.getRecommendImagesForPad(new RCRetrofitCallback<CookbookImageReponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookImageReponse result) {
                callback.onSuccess(result.images);
            }
        });

    }

    static public void getAllBookImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        svr.getAllBookImagesForPad(new RCRetrofitCallback<CookbookImageReponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookImageReponse result) {
                callback.onSuccess(result.images);
            }
        });
    }

    // -------------------------------------------------------------------------------

    static public void applyAfterSale(String deviceId,
                                      final VoidCallback callback) {
        svr.applyAfterSale(new ApplyAfterSaleRequest(getUserId(), deviceId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getSmartParams(String deviceGuid,
                                      final Callback<SmartParamsReponse> callback) {
        svr.getSmartParams(new GetSmartParamsRequest(getUserId(), deviceGuid),
                new RCRetrofitCallback<SmartParamsReponse>(callback) {
                    @Override
                    protected void afterSuccess(SmartParamsReponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void setSmartParamsByDaily(String guid, boolean enable,
                                             int day, VoidCallback callback) {
        svr.setSmartParamsByDaily(new SetSmartParamsByDailyRequest(getUserId(),
                guid, enable, day), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));
    }

    static public void setSmartParamsByWeekly(String guid, boolean enable,
                                              int day, String time, VoidCallback callback) {
        svr.setSmartParamsByWeekly(new SetSmartParamsByWeeklyRequest(
                        getUserId(), guid, enable, day, time),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getSmartParams360(String guid, final Callback<Boolean> callback) {
        svr.getSmartParams360(new GetSmartParams360Request(getUserId(), guid),
                new RCRetrofitCallback<GetSmartParams360Reponse>(callback) {
                    @Override
                    protected void afterSuccess(GetSmartParams360Reponse result) {
                        Helper.onSuccess(callback, result.switchStatus);
                    }
                });
    }

    static public void setSmartParams360(String guid, boolean switchStatus, final VoidCallback callback) {
        svr.setSmartParams360(new SetSmartParams360Request(getUserId(), guid, switchStatus),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // -------------------------------------------------------------------------------
    // 订单配送
    // -------------------------------------------------------------------------------

    static public void getCustomerInfo(final Callback<OrderContacter> callback) {
        svr.getCustomerInfo(new UserRequest(getUserId()), new RCRetrofitCallback<GetCustomerInfoReponse>(callback) {
            @Override
            protected void afterSuccess(GetCustomerInfoReponse result) {
                Helper.onSuccess(callback, result.customer);
            }
        });
    }

    static public void saveCustomerInfo(String name, String phone, String city, String address, final VoidCallback callback) {
        svr.saveCustomerInfo(new SaveCustomerInfoRequest(getUserId(), name, phone, city, address), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void submitOrder(List<Long> ids, final Callback<Long> callback) {
        svr.submitOrder(new SubmitOrderRequest(getUserId(), ids), new RCRetrofitCallback<SubmitOrderReponse>(callback) {
            @Override
            protected void afterSuccess(SubmitOrderReponse result) {
                Helper.onSuccess(callback, result.orderId);
            }
        });
    }

    static public void getOrder(long orderId, final Callback<OrderInfo> callback) {
        svr.getOrder(new GetOrderRequest(orderId), new RCRetrofitCallback<GetOrderReponse>(callback) {
            @Override
            protected void afterSuccess(GetOrderReponse result) {
                Helper.onSuccess(callback, result.order);
            }
        });
    }

    static public void queryOrder(long time, int limit, final Callback<List<OrderInfo>> callback) {
        svr.queryOrder(new QueryOrderRequest(getUserId(), time, limit), new RCRetrofitCallback<QueryOrderReponse>(callback) {
            @Override
            protected void afterSuccess(QueryOrderReponse result) {
                Helper.onSuccess(callback, result.orders);
            }
        });
    }

    static public void cancelOrder(long orderId, final VoidCallback callback) {
        svr.cancelOrder(new UserOrderRequest(getUserId(), orderId), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void updateOrderContacter(long orderId, String name, String phone, String city, String address, final VoidCallback callback) {
        svr.updateOrderContacter(new Requests.UpdateOrderContacterRequest(getUserId(), orderId, name, phone, city, address),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void orderIfOpen(final Callback<Boolean> callback) {
        svr.orderIfOpen(new RCRetrofitCallback<OrderIfOpenReponse>(callback) {
            @Override
            protected void afterSuccess(OrderIfOpenReponse result) {
                Helper.onSuccess(callback, result.open);
            }
        });
    }

    static public void getEventStatus(final Callback<EventStatusReponse> callback) {
        svr.getEventStatus(new RCRetrofitCallback<EventStatusReponse>(callback) {
            @Override
            protected void afterSuccess(EventStatusReponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void deiverIfAllow(final Callback<Integer> callback) {
        svr.deiverIfAllow(new UserRequest(getUserId()), new retrofit.Callback<DeiverIfAllowReponse>() {
            @Override
            public void success(DeiverIfAllowReponse result, Response response) {
                Helper.onSuccess(callback, result.rc);
            }

            @Override
            public void failure(RetrofitError e) {
                Helper.onFailure(callback, ExceptionHelper.newRestfulException(e.getMessage()));
            }
        });
    }


    // -------------------------------------------------------------------------------
    // 清洁维保
    // -------------------------------------------------------------------------------

    static public void getCrmCustomer(String phone, final Callback<CrmCustomer> callback) {
        svr.getCrmCustomer(new GetCrmCustomerRequest(phone), new RCRetrofitCallback<GetCrmCustomerReponse>(callback) {
            @Override
            protected void afterSuccess(GetCrmCustomerReponse result) {
                Helper.onSuccess(callback, result.customerInfo);
            }
        });
    }

    static public void submitMaintain(CrmProduct product, long bookTime, String customerId, String customerName, String phone, String province, String city, String county, String address, VoidCallback callback) {
        svr.submitMaintain(new SubmitMaintainRequest(getUserId(), product, bookTime, customerId, customerName, phone, province, city, county, address), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void queryMaintain(final Callback<MaintainInfo> callback) {

        svr.queryMaintain(new QueryMaintainRequest(getUserId()), new RCRetrofitCallback<QueryMaintainReponse>(callback) {

            @Override
            protected void afterSuccess(QueryMaintainReponse result) {
                Helper.onSuccess(callback, result.maintainInfo);
            }
        });
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    static private long getUserId() {
        return Plat.accountService.getCurrentUserId();
    }


    //联网优化接口
    public static <T extends RCReponse> void getNetworkDeviceInfoRequest(String vendor, String dc, String dt,
                                                  final Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.GetNetworkDeviceInfoRequest(vendor, dc, dt).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse("application/json; Accept: application/json"), json);
        Call<ResponseBody> call = svr.getNetworkDeviceInfo(requestBody);
        enqueue(entity, call, callback);
    }

    private static <T extends RCReponse> void enqueue(final Class<T> entity, Call<ResponseBody> call, final RetrofitCallback<T> callback) {
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String body = response.body().string();

                    T object = gson.fromJson(body, entity);
                    RCReponse rcReponse = object;
                    if (null != rcReponse && rcReponse.rc == 0) {
                        if (null != callback) {
                            callback.onSuccess(object);
                            return;
                        }
                    }
                    if (null != callback && null != rcReponse)
                        callback.onFaild(rcReponse.error_description);
                } catch (Exception e) {
                    if (null != callback)
                        callback.onFaild("exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (null != callback)
                    callback.onFaild("exception:" + throwable.getMessage());
            }
        });
    }

    static public void getNetworkDeviceStepsRequest(String displayType,
                                                    final Callback<List<NetWorkingSteps>> callback) {
        svr.getNetworkDeviceStep(new Requests.GetNetworkDeviceStepRequest(displayType), new retrofit.Callback<Reponses.NetworkDeviceStepResponse>() {


            @Override
            public void success(Reponses.NetworkDeviceStepResponse networkDeviceStepResponse, Response response) {
                callback.onSuccess(networkDeviceStepResponse.networkingSteps);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    /**
     * 扫描二维码登录
     */
    static public void getScanQRLogin(long userId, String uuid, String phone, final Callback<Reponses.ScanQRLoginResponse> callback) {
        svr.getScanQRLogin(new Requests.ScanQRLoginRequest(userId, uuid, phone), new retrofit.Callback<Reponses.ScanQRLoginResponse>() {
            @Override
            public void success(Reponses.ScanQRLoginResponse scanQRLoginResponse, Response response) {
                callback.onSuccess(scanQRLoginResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });

    }

    /**
     * 掌厨更新视频观看数
     */
    static public void upDataVideoWatchCount(String seriesId, String courseId,
                                             final Callback<Reponses.upDataVideoWatchCountResponse> callback) {
        svr.upDataVideoWatchCount(new Requests.upDataVideoWatchCountRequest(seriesId, courseId),
                new retrofit.Callback<Reponses.upDataVideoWatchCountResponse>() {
                    @Override
                    public void success(Reponses.upDataVideoWatchCountResponse upDataVideoWatchCountResponse, Response response) {
                        callback.onSuccess(upDataVideoWatchCountResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onFailure(error);
                    }
                });
    }

    static public void checkVersionFireChicken(String mac, String deviceGuid, final Callback<Reponses.CheckChickenResponse> callback) {
        svr.checkVersionFireChicken(new Requests.CheckChickenRequest(mac, deviceGuid), new retrofit.Callback<Reponses.CheckChickenResponse>() {
            @Override
            public void success(Reponses.CheckChickenResponse checkChickenResponse, Response response) {
                callback.onSuccess(checkChickenResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    static public void toUpdate(String mac, String deviceGuid, String targetVersion, final Callback<Reponses.UpdateDeviceResponse> callback) {
        svr.toUpdate(new Requests.UpDateDevice(mac, deviceGuid, targetVersion), new retrofit.Callback<Reponses.UpdateDeviceResponse>() {
            @Override
            public void success(Reponses.UpdateDeviceResponse updateDeviceResponse, Response response) {

                callback.onSuccess(updateDeviceResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    static public void checkCookerStatus(String deviceGuid, final Callback<Reponses.CookerStatusResponse> callback) {
        svr.checkCookerStatus(deviceGuid, new retrofit.Callback<Reponses.CookerStatusResponse>() {
            @Override
            public void success(Reponses.CookerStatusResponse cookerStatusResponse, Response response) {
                callback.onSuccess(cookerStatusResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    public static <T extends RCReponse> void getGroundingRecipesByDc(long userId, String dc, String recipeType, int start, int limit, String devicePlat,
                                                                     Class<T> entity, final RetrofitCallback<T> callback) {
        String json = new Requests.getGroundingRecipesByDeviceRequest(userId, dc, recipeType, start, limit, devicePlat).toString();
        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(MediaType.parse(APPLICATION_JSON_ACCEPT_APPLICATION_JSON), json);
        Call<ResponseBody> call = svr.getGroundingRecipesByDevice(requestBody);
        enqueue(entity, call, callback);

    }


    public static void getRecipeTop4(long userId, String deviceGuid, String deviceCategory, String deviceType,
                                     final Callback<Reponses.GetRecipeTop4Response> callback) {
        svr.getRecipTop4(new Requests.getRecipTop4Request(userId, deviceGuid, deviceCategory, deviceType),
                new retrofit.Callback<Reponses.GetRecipeTop4Response>() {
                    @Override
                    public void success(Reponses.GetRecipeTop4Response getRecipeTop4Response, Response response) {
                        callback.onSuccess(getRecipeTop4Response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onFailure(error);
                    }
                }
        );

    }


    //035菜谱查询
    public static void getDiyRecipe(long userId, String deviceType, final Callback<Reponses.GetRecipeDiyCookbook> callback) {
        svr.getRecipe035Cookbook(new Requests.getRecipeDiyCookbook(userId, deviceType),
                new retrofit.Callback<Reponses.GetRecipeDiyCookbook>() {
                    @Override
                    public void success(Reponses.GetRecipeDiyCookbook getRecipeDiyCookbook, Response response) {
                        callback.onSuccess(getRecipeDiyCookbook);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onFailure(error);
                    }
                });
    }


    //035菜谱删除
    public static void Delete035Recipe(Long userId, final Callback<Reponses.Update035Recipe> callback) {
        svr.deleteRecipe035(new Requests.deleteRecipeDiyCookbook(userId), new retrofit.Callback<Reponses.Update035Recipe>() {
            @Override
            public void success(Reponses.Update035Recipe update035Recipe, Response response) {
                callback.onSuccess(update035Recipe);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }


    //035菜谱修改
    public static void Update035Recipe(long userId, String deviceType, String name, String modeCode, String temp, String minute,
                                       String hasRotate, String openRotate, String cookbookDesc, Long id, final Callback<Reponses.Update035Recipe> callback) {
        svr.updateRecipe035(new Requests.update035RecipeRequest(userId, deviceType, name, modeCode, temp, minute, hasRotate,
                openRotate, cookbookDesc, id), new retrofit.Callback<Reponses.Update035Recipe>() {
            @Override
            public void success(Reponses.Update035Recipe update035Recipe, Response response) {
                callback.onSuccess(update035Recipe);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    //915菜谱修改
    public static void update915Recipe(long userId, String deviceType, String name, String modeCode, String temp, String minute,
                                       String cookbookDesc, long id, final Callback<Reponses.Update035Recipe> callback) {
        svr.updateRecipe915(new Requests.update915RecipeRequest(userId, deviceType, name, modeCode, temp, minute,
                cookbookDesc, id), new retrofit.Callback<Reponses.Update035Recipe>() {
            @Override
            public void success(Reponses.Update035Recipe update035Recipe, Response response) {
                callback.onSuccess(update035Recipe);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    //908菜谱修改
    public static void updateCQ908Recipe(long userId, String deviceType, String name, String modeCode, String temp, String tempDown, String minute,
                                         String cookbookDesc, long id, final Callback<Reponses.Update035Recipe> callback) {
        svr.updateRecipe908(new Requests.updateRQ908RecipeRequest(userId, deviceType, name, modeCode, temp, tempDown, minute,
                cookbookDesc, id), new retrofit.Callback<Reponses.Update035Recipe>() {
            @Override
            public void success(Reponses.Update035Recipe update035Recipe, Response response) {
                callback.onSuccess(update035Recipe);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }


}
