package com.robam.common.io.cloud;

import com.legent.plat.pojos.RCReponse;
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
import com.robam.common.io.cloud.Reponses.HomeTitleForMobResponse;
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
import com.robam.common.io.cloud.Requests.GetRecommendRecipesByDeviceForPadRequest;
import com.robam.common.io.cloud.Requests.GetSmartParams360Request;
import com.robam.common.io.cloud.Requests.GetSmartParamsRequest;
import com.robam.common.io.cloud.Requests.GetTodayRecipesByDeviceRequest;
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
import com.robam.common.io.cloud.Requests.UpdateOrderContacterRequest;
import com.robam.common.io.cloud.Requests.UserBookRequest;
import com.robam.common.io.cloud.Requests.UserMaterialRequest;
import com.robam.common.io.cloud.Requests.UserOrderRequest;
import com.robam.common.io.cloud.Requests.UserRequest;
import com.robam.common.io.cloud.Requests.getGroundingRecipesByDeviceRequest;
import com.robam.common.io.cloud.Requests.getNotRecommendRecipesByDeviceRequest;
import com.robam.common.io.cloud.Requests.getRecommendRecipesByDeviceForCellphoneRequest;
import com.squareup.okhttp.RequestBody;


import org.eclipse.jetty.util.MultiPartInputStream;

import java.io.File;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit2.Call;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;

public interface IRokiRestService {

    String Url_UserProfile = "http://h5.myroki.com/#/agreement";

    String UserNotice = "https://oss.myroki.com/userAgreement/userNotice.html";//????????????
    String RegisterAgreement = "https://oss.myroki.com/userAgreement/registerAgreement.html";//????????????

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    String getStoreVersion = "/rest/api/store/version/get";
    //?????????????????????????????????
    //String getStoreCategory = "/rest/api/cookbook/group-tag/get";
    String getStoreCategory = "/rest/cks/api/cookbook/group-tag/get";
    String getCookbookProviders = "/rest/api/cookbook/source/get";
    //????????????????????????????????????????????????
    //String getCookbooksByTag = "/rest/api/cookbook/by-tag/get";
    String getCookbooksByTag = "/rest/api/cookbook/by-tag/get";
    //???????????????????????????????????????
//    String getCookbooksByName = "/rest/cks/api/cookbook/by-name/get";

    String getCookbooksByName = "/rest/cks/api/cookbook/by-name/search";

    //???????????????
    // String getSeasonCookbooks = "/rest/api/cookbook/season/get";
    String getSeasonCookbooks = "/rest/cks/api/cookbook/seasion/get";
    String getRecommendCookbooksForMob = "/rest/api/cookbook/recommend/app/get";
    String getRecommendCookbooksForPad = "/rest/api/cookbook/recommend/pad/get-en";
    //    String getRecommendCookbooksForPadEn = "/rest/api/cookbook/recommend/pad/get-en";
    String getHotKeysForCookbook = "/rest/api/cookbook/hot/get";
    //    String getRecipeOfThemeList = "/rest/api/cookbook/get-by-ids";
    String getRecipeOfThemeList = "/rest/cks/api/cookbook/get-by-ids";
    String getOldCookbookById = "/rest/api/cookbook/by-id/get";
    String getCookbookById = "/rest/api/cookbook/detail/get-by-id";//??????????????????????????????????????????
    String getNewCookbookById = "/rest/cks/api/cookbook/details/get-by-id";//????????????????????????????????????
    String getAccessoryFrequencyForMob = "/rest/api/cookbook/recommend/app/accessory/get";
    String getCookbookSteps = "/rest/cks/api/cookbook/steps";

    String getPersonalizedRecipeBook = "/rest/cks/api/recommender/user/get";        //???????????????????????????

    String getRandomCookbook = "/rest/cks/api/cookbook/grounding/getRandom";//??????????????????????????????????????????
    String getWeekTops = "/rest/cks/api/cookbook/week/tops";//???????????????
    String getbyTagOtherCooks = "/rest/cks/api/cookbook/by-tag-other/get/cooks";//??????????????????????????????????????????????????????
    String getByTagOtherThemes = "/rest/cks/api/cookbook/by-tag-other/get/themes";//????????????????????????????????????????????????
    String getCookbookbythemeId = "/rest/cks/api/cookbook/get-by-themeId";//????????????id????????????????????????
    String getGatewayDic = "/rest/gateway/api/dic/get";//??????????????????
    String suggestApply = "/rest/ops/api/suggest/apply";
    String queryCurveCookbooks = "/rest/cks/api/curve_cookbook/queryCurveCookbooks";//??????????????????
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    String getTodayCookbooks = "/rest/api/cookbook/today/get";
    String addTodayCookbook = "/rest/api/cookbook/today/add";
    String deleteTodayCookbook = "/rest/api/cookbook/today/delete";
    String deleteKitComment = "/rest/ops/api/commentInfo/delete";
    String deleteAllTodayCookbook = "/rest/api/cookbook/today/all/delete";
    String exportMaterialsFromToday = "/rest/api/cookbook/today/material/export";
    String addMaterialsToToday = "/rest/api/cookbook/today/material/add";
    String deleteMaterialsFromToday = "/rest/cks/api/shopping-trolley/material/delete";
    String getFavorityCookbooks = "/rest/api/cookbook/collect/get";
    String addFavorityCookbooks = "/rest/cks/api/cookbook/collect/add";
    String delteFavorityCookbooks = "/rest/cks/api/cookbook/collect/delete";
    //String delteAllFavorityCookbooks = "/rest/api/cookbook/collect/all/delete";
    String delteAllFavorityCookbooks = "/rest/api/cookbook/collect/delete-all";
    String getGroundingRecipes = "/rest/api/cookbook/grounding/get";

    //????????????????????????
    String getCookingKnowledge = "/rest/ops/api/cookingKnowledge/getCookingKnowledge";

    //????????????????????????????????????(?????????????????????)
    String getGroundingRecipesByDevice = "/rest/api/cookbook/grounding/get-by-dc";
    //????????????????????????Pad???????????????
    String getRecommendRecipesByDeviceForPad = "/rest/api/cookbook/recommend/pad/get-by-dc";
    //?????????????????????????????????????????????
    String getRecommendRecipesByDeviceForCellphone = "/rest/api/cookbook/recommend/app/get-by-dc";
    //??????????????????????????????????????????
    String getNotRecommendRecipesByDevice = "/rest/api/cookbook/not-recommend/get-by-dc";
    //????????????????????????????????????
    String getTodayRecipesByDevice = "/rest/api/cookbook/today/get-by-dc";
    //**RENT ADD**/
    //???????????????????????? ????????????
    String getThemeRecipeList = "/rest/cks/api/theme/list/get";
    String getThemeRecipeList_new = "/rest/api/theme-manage/theme/list/get";

    String getThemeRecipeDetail = "/rest/cks/api/theme/get-theme-detail-By-id";

    //?????????????????????????????????
    //??????????????????
    String getDynamicCover = "/rest/api/cooking/album/get-dynamics-first";
    //????????????????????????
    String getRecipeLiveList = "/rest/cks/api/cookbook/video-cookbook/get";
    //?????????????????????????????????
    // String getRecipeDynamicShow = "/rest/api/cooking/album/get-favorite-dynamics-new";
    String getRecipeDynamicShow = "/rest/cks/api/cooking/albums/newest/get";
    //??????????????????-new
    String getRecipeDynamicShow_new = "/rest/api/cooking/albums/newest/get";
    //????????????????????????
    String isThemeCollected = "/rest/api/user-theme-relation/is-favorite";
    //??????
    String setCollectOfTheme = "/rest/api/user-theme-relation/add-favorite-theme";
    //????????????
    String cancelCollectOfTheme = "/rest/api/user-theme-relation/delete";
    //?????????????????????????????????
    //String getConsultationList = "/rest/api/news-manage/list-all-new";
    String getConsultationList = "/rest/ops/api/news-manage/list-page";
    //????????????????????????
    String getDeviceRecipeImg = "/rest/api/device/get-detail-desc";
    //?????????????????????
    //String getRecipeThemeOfCollect="/rest/api/user-theme-relation/select-by-userId";//?????????
    String getRecipeThemeOfCollect = "/rest/cks/api/user-theme/collection/get";
    String getRecipeThemeOfCollect_new = "/rest/api/user-theme-relation/collection/get";
    //????????????????????????
    String getCurrentLiveShow = "/rest/api/liveshow/current/get";
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    String addCookingLog = "/rest/api/cooking/record/add";
    //?????????????????????
    String addCookingLog_New = "/rest/cks/api/cooking/record/submit";

    //????????????????????????
    String getCookbookSearchHistory = "/rest/cks/api/cookbook/search/history/get";

    //????????????????????????
    String deleteCookbookSearchHistory = "/rest/cks/api/cookbook/search/history/del";


    // -------------------------------------------------------------------------------
    //album
    // -------------------------------------------------------------------------------
    String getMyCookAlbumByCookbook = "/rest/api/cooking/album/my/get";
    //???????????????????????????????????????
    //String getOtherCookAlbumsByCookbook = "/rest/api/cooking/album/others/get";
    String getOtherCookAlbumsByCookbook = "/rest/cks/api/cooking/album/exclude/get";
    String getOtherCookAlbumsByCookbook_new = "/rest/api/cooking/album/exclude/get";
    String submitCookAlbum = "/rest/api/cooking/album/add";
    String removeCookAlbum = "/rest/api/cooking/album/delete";
    String praiseCookAlbum = "/rest/api/cooking/album/point-praise";
    String unpraiseCookAlbum = "/rest/api/cooking/album/cancel-point-praise";
    //???????????????????????????
    //String getMyCookAlbums = "/rest/api/cooking/album/my/get-all";
    String getMyCookAlbums = "/rest/cks/api/cooking/albums/my/get";
    String clearMyCookAlbums = "/rest/api/cooking/album/delete-all";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    String getHomeAdvertsForMob = "/rest/api//app/image/advert/drop-down/get";
    String getHomeTitleForMob = "/rest/api/app/image/title/get";
    String getHomeAdvertsForPad = "/rest/api/pad/image/advert/get";
    String getFavorityImagesForPad = "/rest/api/pad/image/collect/get";
    String getRecommendImagesForPad = "/rest/api/pad/image/recommend/get";
    String getAllBookImagesForPad = "/rest/api/pad/image/cookbook/get";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    String applyAfterSale = "/rest/api/customer-service/apply";
    String getSmartParams = "/rest/api/device/intelligence/get";
    String setSmartParamsByDaily = "/rest/api/device/intelligence/by-day";
    String setSmartParamsByWeekly = "/rest/api/device/intelligence/weekly";
    String getSmartParams360 = "/rest/api/device/360/get";
    String setSmartParams360 = "/rest/api/device/360/set";

    // -------------------------------------------------------------------------------
    //????????????
    // -------------------------------------------------------------------------------
    String getCustomerInfo = "/rest/api/shopping/customer-info/get";
    String saveCustomerInfo = "/rest/api/shopping/customer-info/save";
    String submitOrder = "/rest/api/shopping/order/save";
    String getOrder = "/rest/api/shopping/order/get-by-id";
    String updateOrderContacter = "/rest/api/shopping/order/customer-info/update";
    String queryOrder = "/rest/api/shopping/order/get";
    String cancelOrder = "/rest/api/shopping/order/delete";
    String orderIfOpen = "/rest/api/shopping/if-open";
    String deiverIfAllow = "/rest/api/shopping/if-allow-free-send";
    String getEventStatus = "/rest/api/event/status/get";


    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    String getCrmCustomerRequest = "/rest/api/maintain/customer/get";
    String submitMaintainRequest = "/rest/api/maintain/save";
    String queryMaintainRequest = "/rest/api/maintain/get";


    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    String setFamilyMember = "/rest/dms/api/device/family-member/set";
    String getSetFamilyMember = "/rest/dms/api/device/family-member/get";
    String getTodayDrinking = "/rest/dms/api/device/drinking-volume/today/get";
    String getHistoryDrinking = "/rest/dms/api/device/drinking-volume/history-new/get";

    String getYiGuo = "/rest/ops/api/advert/app/image/advert/drop-down/get";

    String getYouzanContent = "/rest/ums/api/open/youzan/login";

    String getYouzanOrders = "/rest/ops/api/orders/count/get/by-status";
    //???????????????????????????????????????
    String getMallManagement = "/rest/ops/api/mallManagement/getMallManagement";

    //??????????????????????????????????????????
    String getIsCollect = "/rest/cks/api/cookbook/isCollect";

    //App????????????????????????
    String getAppStartImages = "/rest/ops/api/advert/app/start-image/get";
    String getChuYuanActivity = "/rest/ops/api/activity/getHotActivicty";

    //App?????????
    String getAppAdvertImg = "/rest/ops/api/advert/app/image/advert/drop-down/get";

    //???????????????
    String getKuFRecipe = "/rest/cks/api/cloud/cool2/cookbooks";

    //?????????????????????
    String getKuFRecipeDetail = "/rest/cks/api/cloud/cool2/cookbook/detail/{cookbookId}";
    //?????????????????????
    String checkVersionFireChicken = "/rest/dms/api/cloud/cool2/deviceVersion/check";


    // String getCookBooksClassify="/rest/cks/api/cookbook/by-name/get";

  /*  @POST(getCookBooksClassify)
    void getCookBooksClassify(@Body GetCookbooksByNameRequest reqBody, Callback<Reponses.CookbooksClassifyResponse> callback);*/


    //??????????????????????????????
    String getSeriesInfoList = "/rest/ops/api/source/getSeriesInfoList";
    //??????????????????????????????
    String getCourseDetail = "/rest/ops/api/source/getCourseDetail";
    //???????????????????????????
    String upDataVideoWatchCount = "/rest/ops/api/source/incrVideoWatchCount";
    //?????????????????????
    String getScanQRLogin = "/rest/gateway/api/auth/scan/app/login";
    //??????035?????????Top4
    String getRecipeTop4 = "/rest/ops/api/deviceFunctionAcount/getTop4";
    //??????035Diy??????
    String getRecipe035Cookbook = "/rest/ops/api/diyCookbook/getList";

    //??????035diy??????
    String update035Recipe="/rest/ops/api/diyCookbook/save";

    /**
     * ????????????????????????
     */
    String getNetworkDeviceStepRequest = "/rest/dms/api/device-type/getNetworkingSteps";

    //035diy????????????
    String delete035DiyRecipe="/rest/ops/api/diyCookbook/delete";

    /**
     * ????????????????????????
     */
    String upLoad = "/rest/ops/api/file/upload" ;
    /**
     * ?????????????????????main??????
     */
    String getCookbookByTag = "/rest/cks/api/cookbook/by-tag/get";

    /**
     * ????????????????????????????????????
     */
    String getMultiRecipe = "/rest/cks/api/multi/page";

    /**
     * ??????/?????? ????????????
     */
    String saveMultiRecipe = "/rest/cks/api/multi/save";
    /**
     * ????????????????????????
     */
    String deleteMultiRecipe = "/rest/cks/api/multi/delete/{id}";
    /**
     * ????????????????????????
     */
    String deleteMultiItem = "/rest/cks/api/multi/delete/step/{id}";
    /**
     * ???????????????????????????
     *
     * @param request  ??????????????????
     * @param callback ????????????????????????
     */
    @POST(getScanQRLogin)
    void getScanQRLogin(@Body Requests.ScanQRLoginRequest request, Callback<Reponses.ScanQRLoginResponse> callback);

    //?????????????????????
    String toUpdate = "/rest/dms/api/cloud/cool2/deviceVersion/notify";
    //????????????????????????
    String checkCookerStatus = "/rest/dms/api/device/cooker/ota/status";

    @GET(checkCookerStatus)
    void checkCookerStatus(@Query("deviceGuid") String deviceGuid, Callback<Reponses.CookerStatusResponse> callback);

    @POST(toUpdate)
    void toUpdate(@Body Requests.UpDateDevice request, Callback<Reponses.UpdateDeviceResponse> callback);

    @POST(checkVersionFireChicken)
    void checkVersionFireChicken(@Body Requests.CheckChickenRequest request, Callback<Reponses.CheckChickenResponse> callback);

    /**
     * ???????????????????????????
     */
    @POST(upDataVideoWatchCount)
    void upDataVideoWatchCount(@Body Requests.upDataVideoWatchCountRequest request, Callback<Reponses.upDataVideoWatchCountResponse> callback);

    /**
     * ??????????????????????????????
     */
    @POST(getSeriesInfoList)
    void getSeriesInfoList(@Body Requests.SeriesInfoListRequest seriesInfoListRequest,
                           Callback<Reponses.SeriesInfoLisResponse> callback);

    /**
     * ??????????????????????????????
     */
    @POST(getCourseDetail)
    void getCourseDetail(@Body Requests.CourseDetailRequest courseDetailRequest,
                         Callback<Reponses.CourseDetailReponse> callback);

    /**
     * ?????????????????????????????????
     */
    @retrofit2.http.POST(getIsCollect)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getIsCollectBook(@retrofit2.http.Body okhttp3.RequestBody body);


    @GET(getKuFRecipe)
    void getKuFRecipe(Callback<Reponses.GetKufaRecipeResponse> callback);

    @GET(getKuFRecipeDetail)
    void setGetKuFRecipeDetail(@Path("cookbookId") String id, Callback<Reponses.GetKuFRecipeDetailResonse> callback);

    /**
     * ??????????????????
     */
    @POST(getPersonalizedRecipeBook)
    void getPersonalizedRecipeBooks(@Body Requests.PersonalizedRecipeRequest reqBody,
                                    Callback<Reponses.PersonalizedRecipeResponse> callback);


    /**
     * ??????????????????????????????????????????
     */
    @retrofit2.http.POST(getRandomCookbook)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getRamdomCookBook(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ???????????????
     */
    @retrofit2.http.POST(getWeekTops)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getWeekTops(@retrofit2.http.Body okhttp3.RequestBody body);

    @POST(suggestApply)
    void submitSuggestApply(@Body Requests.SuggestApplyRequest reqBody, Callback<Reponses.SuggestApplyReponse> callback);

    /**
     * ??????????????????????????????????????????????????????
     */
    @retrofit2.http.POST(getbyTagOtherCooks)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getbyTagOtherCooks(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ??????????????????????????????
     * @param reqBody
     * @param callback
     */
    @retrofit2.http.POST(getCookbookByTag)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCookbookByTag(@retrofit2.http.Body okhttp3.RequestBody body);
    /**
     * ????????????????????????????????????
     */
    @GET(getMultiRecipe)
    void getMultiRecipe(@Query("userId") String userId,@Query("page") Integer page ,@Query("pageSize") Integer pageSize  ,
                          Callback<Reponses.MultiRecipeResponse> callback);

    /**
     *
     * ??????/?????? ????????????
     * @param reqBody
     * @param callback
     */
    @POST(saveMultiRecipe)
    void saveMultiRecipe(@Body Requests.saveMultiRecipeRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     *
     * ????????????????????????
     * @param id
     * @param callback
     */
    @DELETE(deleteMultiRecipe)
    void deleteMultiRecipe(@Path("id") Integer id,  Callback<RCReponse> callback);
    /**
     *
     * ????????????????????????
     * @param id
     * @param callback
     */
    @DELETE(deleteMultiItem)
    void deleteMultiItem(@Path("id") Integer id,@Query("no") Integer no,  Callback<RCReponse> callback);

    /**
     * ????????????????????????????????????????????????
     */
    @retrofit2.http.POST(getByTagOtherThemes)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getByTagOtherThemes(@retrofit2.http.Body okhttp3.RequestBody body);


    /**
     * ????????????id????????????????????????
     *
     * @param reqBody
     * @param callback
     */
    @retrofit2.http.POST(getCookbookbythemeId)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCookBookBythemeId(@retrofit2.http.Body okhttp3.RequestBody body);


    /**
     * ??????????????????
     * @param callback
     */
    @POST(getGatewayDic)
    void getGatewayDic(Callback<Reponses.GatewayDicResponse> callback);


    @POST(getYouzanContent)
    void GetYouzanDetailContent(@Body Requests.GetYouzanRequst reqBody, Callback<Reponses.TokenResponses> callback);

    @POST(getYouzanOrders)
    void getYouzanOrders(@Body Requests.GetYouzanOrdersRequst reqBody, Callback<Reponses.YouzanOrdersReponses> callback);


    @POST(getMallManagement)
    void getMallManagement(@Body Requests.MallManagementRequest reqBody, Callback<Reponses.MallManagementResponse> callback);


    @retrofit2.http.POST(getAppStartImages)
    @Headers("Content-Type: application/json")
    Call<Reponses.AppStartImgResponses> getAppStartImages(@retrofit2.http.Body okhttp3.RequestBody body);

    @retrofit2.http.POST(getAppAdvertImg)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getAppAdvertImg(@retrofit2.http.Body okhttp3.RequestBody body);

    @POST(getYiGuo)
    void getYiGuoUrl(Callback<Reponses.GetYiGuoUrlResponse> callback);

    @POST(getChuYuanActivity)
    void getGetChuYuanActivityMethod(@Body Requests.getChuYuan reqBoby, Callback<Reponses.ChuYuanActivityResponse> callback);

    /**
     * ?????????????????????
     */
    @POST(getTodayDrinking)
    void getTodayDrinking(@Body Requests.TodayDrinkingRequest reqBody,
                          Callback<Reponses.TodayDrinkingResponse> callback);

    /**
     * ?????????????????????
     */
    @POST(getHistoryDrinking)
    void getHistoryDrinking(@Body Requests.HistoryDrinkingRequest reqBody,
                            Callback<Reponses.HistoryDrinkingResponse> callback
    );


    /**
     * ????????????????????????
     */
    @retrofit2.http.POST(getCookingKnowledge)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCookingKnowledge(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ????????????
     */
    String getNetworkDeviceInfoRequest = "/rest/dms/api/device/instruction-book/get";

    /**
     * ??????????????????
     */
    @POST(getStoreVersion)
    void getStoreVersion(@Body StoreRequest reqBody,
                         Callback<StoreVersionResponse> callback);

    /**
     * ????????????
     */
    @retrofit2.http.POST(getStoreCategory)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getStoreCategory(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ???????????????????????????
     */
    @POST(getCookbookProviders)
    void getCookbookProviders(Callback<CookbookProviderResponse> callback);

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    @POST(getCookbooksByTag)
    void getCookbooksByTag(@Body GetCookbooksByTagRequest reqBody,
                           Callback<CookbooksResponse> callback);

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    @retrofit2.http.POST(getCookbooksByName)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCookbooksByName(@retrofit2.http.Body okhttp3.RequestBody body);


    /**
     * ???????????????????????????????????? for Pad
     * 20160630?????????
     */
    @POST(getRecommendRecipesByDeviceForPad)
    void getRecommendRecipesByDeviceForPad(@Body GetRecommendRecipesByDeviceForPadRequest reqBody,
                                           Callback<ThumbCookbookResponse> callback);

    /**
     * ???????????????????????????????????? for Cellphone
     * 201600711?????????
     */
    @POST(getRecommendRecipesByDeviceForCellphone)
    void getRecommendRecipesByDeviceForCellphone(@Body getRecommendRecipesByDeviceForCellphoneRequest reqBody,
                                                 Callback<ThumbCookbookResponse> callback);


    /**
     * ??????????????????????????????????????????
     * 201600711?????????
     */
    @POST(getNotRecommendRecipesByDevice)
    void getNotRecommendRecipesByDevice(@Body getNotRecommendRecipesByDeviceRequest reqBody,
                                        Callback<ThumbCookbookResponse> callback);


    /**
     * ????????????????????????????????????
     * 201600711?????????
     */
    @retrofit2.http.POST(getGroundingRecipesByDevice)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getGroundingRecipesByDevice(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ????????????????????????????????????
     * 201600711?????????
     */
    @POST(getTodayRecipesByDevice)
    void getTodayRecipesByDevice(@Body GetTodayRecipesByDeviceRequest reqBody,
                                 Callback<CookbooksResponse> callback);

    /**
     * ??????????????????
     */
    @POST(getSeasonCookbooks)
    void getSeasonCookbooks(Callback<CookbooksResponse> callback);

    /**
     * ?????????????????? for ??????
     */
    @POST(getRecommendCookbooksForMob)
    void getRecommendCookbooksForMob(@Body UserRequest reqBody,
                                     Callback<ThumbCookbookResponse> callback);

    /**
     * ?????????????????? for Pad
     */
    @POST(getRecommendCookbooksForPad)
    void getRecommendCookbooksForPad(@Body UserRequest reqBody,
                                     Callback<ThumbCookbookResponse> callback);

    /**
     * ???????????????????????????
     */
    @retrofit2.http.POST(getHotKeysForCookbook)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getHotKeysForCookbook(@retrofit2.http.Body okhttp3.RequestBody body);

    @POST(getRecipeOfThemeList)
    void getRecipeOfThemeList(@Body Requests.GetReicpeOfTheme reqBody, Callback<CookbooksResponse> callback);

    /**
     * ??????????????????
     */
    @retrofit2.http.POST(getCookbookById)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCookbookById(@retrofit2.http.Body okhttp3.RequestBody body);

    //????????????????????????
    @POST(getCookbookSteps)
    void getCookbookSteps(@Body Requests.UserCookBookSteps reqBody,
                          Callback<Reponses.CookbookStepResponse> callback);

    /**
     * ???????????????????????????
     */
    @retrofit2.http.POST(getNewCookbookById)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getNewCookbookById(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ?????????????????????
     */
    @POST(getOldCookbookById)
    void getOldCookbookById(@Body UserBookRequest reqBody,
                            Callback<CookbookResponse> callback);

    /**
     * ??????mob???????????????????????????
     */
    @POST(getAccessoryFrequencyForMob)
    void getAccessoryFrequencyForMob(@Body UserRequest reqBody, Callback<MaterialFrequencyResponse> callback);

    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    /**
     * ??????????????????
     */
    @POST(getTodayCookbooks)
    void getTodayCookbooks(@Body UserRequest reqBody,
                           Callback<CookbooksResponse> callback);

    /**
     * ???????????????????????????
     */
    @POST(addTodayCookbook)
    void addTodayCookbook(@Body UserBookRequest reqBody,
                          Callback<RCReponse> callback);

    /**
     * ??????????????????
     */
    @POST(setFamilyMember)
    void setFamilyMember(@Body Requests.FamilyMember reqBody,
                         Callback<RCReponse> callback);

    /**
     * ??????????????????
     */
    @POST(getSetFamilyMember)
    void getFamilyMember(@Body Requests.getFamilytotal reqBody,
                         Callback<Reponses.GetFamilyResponse> callback);

    /**
     * ??????????????????????????????
     */
    @POST(deleteTodayCookbook)
    void deleteTodayCookbook(@Body UserBookRequest reqBody,
                             Callback<RCReponse> callback);

    /**
     * ??????????????????
     */

    @POST(deleteKitComment)
    void deleteKitComment(@Body Requests.UserCommentRequest reqBody,
                          Callback<RCReponse> callback);

    /**
     * ????????????????????????????????????
     */
    @POST(deleteAllTodayCookbook)
    void deleteAllTodayCookbook(@Body UserRequest reqBody,
                                Callback<RCReponse> callback);

    /**
     * ??????????????????????????????
     */
    @POST(exportMaterialsFromToday)
    void exportMaterialsFromToday(@Body UserRequest reqBody,
                                  Callback<MaterialsResponse> callback);

    /**
     * ??????????????????????????????????????????
     */
    @POST(addMaterialsToToday)
    void addMaterialsToToday(@Body UserMaterialRequest reqBody,
                             Callback<RCReponse> callback);

    /**
     * ??????????????????????????????????????????
     */
    @POST(deleteMaterialsFromToday)
    void deleteMaterialsFromToday(@Body UserMaterialRequest reqBody,
                                  Callback<RCReponse> callback);

    /**
     * ??????????????????????????????
     */
    @GET(queryCurveCookbooks)
    void queryCurveCookbooks(@Query("userId") String userId,
                                  Callback<Reponses.GetCurveCookbooksResonse> callback);


/****RENT**/
    /**
     * ??????????????????????????????
     */
    @retrofit2.http.POST(getThemeRecipeList)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> setGetThemeRecipeList(@retrofit2.http.Body okhttp3.RequestBody body);

    @POST(getThemeRecipeList_new)
    @Headers("Content-Type: application/json")
    void setGetThemeRecipeList_new(Callback<Reponses.RecipeThemeResponse> callback);

    @retrofit2.http.POST(getThemeRecipeDetail)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getThemeRecipeDetail(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ??????????????????
     */
    @GET(getDynamicCover)
    void getDynamicCover(Callback<Reponses.RecipeDynamicCover> callback);

    /**
     * ????????????????????????
     */
    @POST(getRecipeLiveList)
    void getRecipeLiveList(@Body Requests.GetPageRequest request, Callback<Reponses.RecipeLiveListResponse> callback);

    /**
     * ?????????????????????
     */
    @POST(getRecipeDynamicShow)
    void getRecipeShowList(@Body Requests.GetPageUserRequest request, Callback<Reponses.RecipeShowListResponse> callback);

    @POST(getRecipeDynamicShow_new)
    void getRecipeShowList_new(@Body Requests.GetPageUserRequest request, Callback<Reponses.RecipeShowListResponse> callback);

    /**
     * ????????????????????????
     */
    @POST(isThemeCollected)
    void getThemeCollectStatus(@Body Requests.ThemeCollectRequest request, Callback<Reponses.ThemeFavorite> callback);

    /**
     * ????????????
     */
    @retrofit2.http.POST(setCollectOfTheme)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> setSetCollectOfTheme(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ????????????
     */
    @POST(cancelCollectOfTheme)
    void setCancelCollectOfTheme(@Body Requests.ThemeCollectRequest request, Callback<RCReponse> callback);

    /**
     * ??????????????????
     */
    @POST(getConsultationList)
    void getConsultationList(@Body Requests.ConsultationListRequest request, Callback<Reponses.ConsultationListResponse> callback);

    /**
     * ???????????????????????????
     */
    @POST(getConsultationList)
    void getConsultationList(Callback<Reponses.ConsultationListResponse> callback);

    /**
     * ????????????????????????
     */
    @retrofit2.http.POST(getDeviceRecipeImg)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getDeviceRecipeImg(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ?????????????????????
     */
    @GET(getRecipeThemeOfCollect)
    void getMyFavoriteThemeRecipeList(@Query("userId") String userId, Callback<Reponses.RecipeThemeResponse2> callback);

    @retrofit2.http.GET(getRecipeThemeOfCollect_new)
    Call<ResponseBody> getMyFavoriteThemeRecipeList_new(@retrofit2.http.Query("userId") String userId);
    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    /**
     * ????????????-????????????
     */
    @retrofit2.http.POST(getFavorityCookbooks)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getFavorityCookbooks(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ???????????????????????????
     */
    @retrofit2.http.POST(addFavorityCookbooks)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> addFavorityCookbooks(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ??????????????????????????????
     */
    @POST(delteFavorityCookbooks)
    void deleteFavorityCookbooks(@Body UserBookRequest reqBody,
                                 Callback<RCReponse> callback);

    /**
     * ??????????????????
     */
    @POST(delteAllFavorityCookbooks)
    void delteAllFavorityCookbooks(@Body UserRequest reqBody,
                                   Callback<RCReponse> callback);

    @POST(getGroundingRecipes)
    void getGroundingRecipes(@Body GetGroudingRecipesRequest reqBody,
                             Callback<ThumbCookbookResponse> callback);


    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    @POST(addCookingLog)
    void addCookingLog(@Body CookingLogRequest reqBody,
                       Callback<RCReponse> callback);

    @POST(addCookingLog_New)
    void addCookingLog_New(@Body CookingLogRequest reqBody,
                           Callback<RCReponse> callback);

    @retrofit2.http.POST(getCookbookSearchHistory)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCookbookSearchHistory(@retrofit2.http.Body okhttp3.RequestBody body);


    @POST(deleteCookbookSearchHistory)
    void deleteCookbookSearchHistory(@Body Requests.DeleteCookbookSearchHistory reqBody,
                                     Callback<Reponses.DeleteHistoryResponse> callback);

    /**
     */
    @POST(getMyCookAlbumByCookbook)
    void getMyCookAlbumByCookbook(@Body UserBookRequest reqBody,
                                  Callback<AlbumResponse> callback);

    /**
     * ????????????????????????
     */
    @POST(getOtherCookAlbumsByCookbook)
    void getOtherCookAlbumsByCookbook(@Body GetCookAlbumsRequest reqBody,
                                      Callback<AlbumsResponse> callback);

    @POST(getOtherCookAlbumsByCookbook_new)
    void getOtherCookAlbumsByCookbook_new(@Body GetCookAlbumsRequest reqBody,
                                          Callback<AlbumsResponse> callback);

    /**
     * ??????????????????
     */
    @POST(submitCookAlbum)
    void submitCookAlbum(@Body SubmitCookAlbumRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     * ????????????????????????
     */
    @POST(removeCookAlbum)
    void removeCookAlbum(@Body CookAlbumRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     * ??????
     */
    @POST(praiseCookAlbum)
    void praiseCookAlbum(@Body CookAlbumRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     * ????????????
     */
    @POST(unpraiseCookAlbum)
    void unpraiseCookAlbum(@Body CookAlbumRequest reqBody,
                           Callback<RCReponse> callback);

    /**
     * ?????????????????????
     */
    @retrofit2.http.POST(getMyCookAlbums)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getMyCookAlbums(@retrofit2.http.Body okhttp3.RequestBody body);

    /**
     * ?????????????????????
     */
    @POST(clearMyCookAlbums)
    void clearMyCookAlbums(@Body UserRequest reqBody,
                           Callback<RCReponse> callback);

    /**
     * ????????????????????????
     */
    @POST(getCurrentLiveShow)
    void getCurrentLiveShow(Callback<Reponses.CurrentLiveResponse> callback);

    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    /**
     * ??????app??????????????????
     */
    @POST(getHomeAdvertsForMob)
    void getHomeAdvertsForMob(Callback<HomeAdvertsForMobResponse> callback);

    /**
     * ??????mob??????????????????title??????????????????
     */
    @POST(getHomeTitleForMob)
    void getHomeTitleForMob(Callback<HomeTitleForMobResponse> callback);

    /**
     * ??????pad??????????????????
     */
    @POST(getHomeAdvertsForPad)
    void getHomeAdvertsForPad(Callback<HomeAdvertsForPadResponse> callback);

    /**
     * ??????pad??????????????????????????????
     */
    @POST(getFavorityImagesForPad)
    void getFavorityImagesForPad(Callback<CookbookImageReponse> callback);

    /**
     * ??????pad??????????????????????????????
     */
    @POST(getRecommendImagesForPad)
    void getRecommendImagesForPad(Callback<CookbookImageReponse> callback);

    /**
     * ??????pad????????????????????????
     */
    @POST(getAllBookImagesForPad)
    void getAllBookImagesForPad(Callback<CookbookImageReponse> callback);

    /**
     * ??????????????????
     */
    @POST(applyAfterSale)
    void applyAfterSale(@Body ApplyAfterSaleRequest reqBody,
                        Callback<RCReponse> callback);

    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------

    /**
     * ??????????????????????????????????????????
     */
    @POST(getSmartParams)
    void getSmartParams(@Body GetSmartParamsRequest reqBody,
                        Callback<SmartParamsReponse> callback);

    @POST(setSmartParamsByDaily)
    void setSmartParamsByDaily(@Body SetSmartParamsByDailyRequest reqBody,
                               Callback<RCReponse> callback);

    @POST(setSmartParamsByWeekly)
    void setSmartParamsByWeekly(@Body SetSmartParamsByWeeklyRequest reqBody,
                                Callback<RCReponse> callback);

    @POST(getSmartParams360)
    void getSmartParams360(@Body GetSmartParams360Request reqBody,
                           Callback<GetSmartParams360Reponse> callback);

    @POST(setSmartParams360)
    void setSmartParams360(@Body SetSmartParams360Request reqBody,
                           Callback<RCReponse> callback);


    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------
    @POST(getCustomerInfo)
    void getCustomerInfo(@Body UserRequest reqBody,
                         Callback<GetCustomerInfoReponse> callback);


    @POST(saveCustomerInfo)
    void saveCustomerInfo(@Body SaveCustomerInfoRequest reqBody,
                          Callback<RCReponse> callback);

    @POST(submitOrder)
    void submitOrder(@Body SubmitOrderRequest reqBody,
                     Callback<SubmitOrderReponse> callback);


    @POST(getOrder)
    void getOrder(@Body GetOrderRequest reqBody,
                  Callback<GetOrderReponse> callback);

    @POST(queryOrder)
    void queryOrder(@Body QueryOrderRequest reqBody,
                    Callback<QueryOrderReponse> callback);

    @POST(cancelOrder)
    void cancelOrder(@Body UserOrderRequest reqBody,
                     Callback<RCReponse> callback);

    @POST(updateOrderContacter)
    void updateOrderContacter(@Body UpdateOrderContacterRequest reqBody, Callback<RCReponse> callback);

    @POST(orderIfOpen)
    void orderIfOpen(Callback<OrderIfOpenReponse> callback);

    @POST(getEventStatus)
    void getEventStatus(Callback<EventStatusReponse> callback);

    @POST(deiverIfAllow)
    void deiverIfAllow(@Body UserRequest reqBody, Callback<DeiverIfAllowReponse> callback);

    // -------------------------------------------------------------------------------
    // ????????????
    // -------------------------------------------------------------------------------
    @POST(getCrmCustomerRequest)
    void getCrmCustomer(@Body GetCrmCustomerRequest reqBody,
                        Callback<GetCrmCustomerReponse> callback);

    @POST(submitMaintainRequest)
    void submitMaintain(@Body SubmitMaintainRequest reqBody,
                        Callback<RCReponse> callback);

    @POST(queryMaintainRequest)
    void queryMaintain(@Body QueryMaintainRequest reqBody,
                       Callback<QueryMaintainReponse> callback);


    // -------------------------------------------------------------------------------
    // other
    // -------------------------------------------------------------------------------

    /**
     * ??????????????????
     * ??????????????????????????????
     * 20161212?????????
     */
    @retrofit2.http.POST(getNetworkDeviceInfoRequest)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getNetworkDeviceInfo(@retrofit2.http.Body okhttp3.RequestBody body);


    @POST(getRecipeTop4)
    void getRecipTop4(@Body Requests.getRecipTop4Request reqBody,
                      Callback<Reponses.GetRecipeTop4Response> callback);

    //035DIY????????????
    @POST(getRecipe035Cookbook)
    void getRecipe035Cookbook(@Body Requests.getRecipeDiyCookbook reqBody,
                              Callback<Reponses.GetRecipeDiyCookbook> callback);

    //???????????? 035??????
    @POST(update035Recipe)
    void updateRecipe035(@Body Requests.update035RecipeRequest reqBody,
                         Callback<Reponses.Update035Recipe> callback);

    //???????????? 915???????????????
    @POST(update035Recipe)
    void updateRecipe915(@Body Requests.update915RecipeRequest reqBody,
                         Callback<Reponses.Update035Recipe> callback);


    //????????????
    @POST(delete035DiyRecipe)
    void deleteRecipe035(@Body Requests.deleteRecipeDiyCookbook reqBody,
                         Callback<Reponses.Update035Recipe> callback);

    /**
     * ??????????????????
     */
    @POST(getNetworkDeviceStepRequest)
    void getNetworkDeviceStep(@Body Requests.GetNetworkDeviceStepRequest reqBody,
                              Callback<Reponses.NetworkDeviceStepResponse> callback);

    //???????????? 908???????????????
    @POST(update035Recipe)
    void updateRecipe908(@Body Requests.updateRQ908RecipeRequest reqBody,
                         Callback<Reponses.Update035Recipe> callback);

    /**
     * ????????????
     * @param type
     * @param params
     * @param rcRetrofitCallback
     */
    @Multipart
    @POST(upLoad)
    void upLoad(@Query("type") String type, @PartMap Map<String, RequestBody> params,
                Callback<Reponses.UploadRepones> rcRetrofitCallback) ;
    /**
     * ????????????
     * @param type
     * @param params
     * @param rcRetrofitCallback
     */

    @POST(upLoad)
    @Multipart
    void upLoad(@Part("type") String type, @Part("file") File params,
                Callback<Reponses.UploadRepones> rcRetrofitCallback) ;


    @POST(upLoad)
    @Multipart
    void upLoad(@Part("type") String type, @Part("file") MultiPartInputStream.MultiPart params,
                Callback<Reponses.UploadRepones> rcRetrofitCallback) ;
}
