package com.robam.common.io.cloud;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.j256.ormlite.stmt.query.In;
import com.legent.plat.pojos.RCReponse;
import com.legent.pojos.IJsonPojo;
import com.robam.common.pojos.Advert;
import com.robam.common.pojos.AdvertImage;
import com.robam.common.pojos.CheckPayLoad;
import com.robam.common.pojos.ChuYuanActivity;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookBookVideo;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Cookbooks;
import com.robam.common.pojos.CookingCurveMarkStepList;
import com.robam.common.pojos.CookingKnowledge;
import com.robam.common.pojos.CrmCustomer;
import com.robam.common.pojos.DataInfo;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DicGroupDto;
import com.robam.common.pojos.NetWorkingSteps;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.FunctionsTop4;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.History;
import com.robam.common.pojos.Images;
import com.robam.common.pojos.MaintainInfo;
import com.robam.common.pojos.MallManagement;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.OrderContacter;
import com.robam.common.pojos.OrderInfo;
import com.robam.common.pojos.PayLoad;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.PayLoadKuF;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.pojos.RecipeLiveList;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.pojos.RecipeShow;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.SeriesInfo;
import com.robam.common.pojos.SeriesInfoCell;
import com.robam.common.pojos.ThemeRecipeDetail;
import com.robam.common.pojos.TodayDrinking;
import com.robam.common.pojos.Token;
import com.robam.common.pojos.YouzanOrdersCount;
import com.robam.common.pojos.liveshow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/7/31.
 */
public interface Reponses {
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookingCurveSaveRes extends RCReponse {
        @JsonProperty("msg")
        public String msg;


        @JsonProperty("payload")
        public long payload;

        public CookingCurveSaveRes(String msg, long payload) {
            this.msg = msg;
            this.payload = payload;
        }

        public CookingCurveSaveRes() {

        }
    }




    @JsonIgnoreProperties(ignoreUnknown = true)
    class StoreVersionResponse extends RCReponse {
        @JsonProperty("version")
        public int version;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    class StoreCategoryResponse extends RCReponse {
        @JsonProperty("cookbookTagGroups")
        public List<Group> groups;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetYiGuoUrlResponse extends RCReponse {
        @JsonProperty("images")
        public ArrayList<Images> images;
    }

    /**
     * 设置家庭人数返回
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetFamilyResponse extends RCReponse {
        @JsonProperty("memberCount")
        public int memberCount;
    }

    /**
     * 有赞商城
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TokenResponses extends RCReponse {
        @JsonProperty("token")
        public Token token;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class YouzanOrdersReponses extends RCReponse {

        @JsonProperty("statusCount")
        public List<YouzanOrdersCount> statusCount;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class AppStartImgResponses extends RCReponse {

        @JsonProperty("images")
        public String[] images;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class AppAdvertImgResponses extends RCReponse {

        @JsonProperty("images")
        public List<Advert> images;

    }

    /**
     * 获取厨房知识列表
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookingKnowledgeResponse extends RCReponse {

        @JsonProperty("hasNext")
        public String hasNext;

        @JsonProperty("cookingKnowledges")
        public List<CookingKnowledge> cookingKnowledges;

    }

    /**
     * 获取今日饮水量
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class TodayDrinkingResponse extends RCReponse {
        @JsonProperty("item")
        public List<TodayDrinking> item;

        @Override
        public String toString() {
            if (item == null)
                return "TodayDrinkingResponse .item is null";
            return "TodayDrinkingResponse{" +
                    "item=" + item +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ChuYuanActivityResponse extends RCReponse {
        @JsonProperty("hasNext")
        public String hasNext;

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("activitys")
        public List<ChuYuanActivity> activities;

    }

    /**
     * 获取历史饮水量
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class HistoryDrinkingResponse extends RCReponse {
        @JsonProperty("data")
        public ArrayList<DataInfo> item;
    }

    /**
     * 获取菜谱详情及烹饪步骤
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookbookResponse extends RCReponse {
        @JsonProperty("cookbook")
        public Recipe cookbook;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookBookOrStepResponse extends RCReponse{
        @JsonProperty("cookbook")
        public Recipe cookbook;
        @JsonProperty("steps")
        public List<CookStep> cookSteps;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookbookStepResponse extends RCReponse {
        @JsonProperty("steps")
        public List<CookStep> cookSteps;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Cookbook3rdResponse extends RCReponse {
        @JsonProperty("cookbook")
        public Recipe3rd cookbook;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class PersonalizedRecipeResponse extends RCReponse {
        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class MultiRecipeResponse extends RCReponse {

        @JsonProperty("totalSize")
        public int totalSize;

        @JsonProperty("totalPage")
        public int totalPage;

        @JsonProperty("datas")
        public List<multiRecipeList> datas;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class multiRecipeList implements Serializable {
        @JsonProperty("id")
        public long id;

        @JsonProperty("userId")
        public long userId;

        @JsonProperty("deviceGuid")
        public String deviceGuid;

        @JsonProperty("deviceType")
        public String deviceType;

        @JsonProperty("name")
        public String name;

        @JsonProperty("multiStepDtoList")
        public List<saveMultiRecipeList> multiStepDtoList;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class saveMultiRecipeList implements Serializable  {
        @JsonProperty("downTemperature")
        public String downTemperature;

        @JsonProperty("modelCode")
        public String modelCode;

        @JsonProperty("modelName")
        public String modelName;

        @JsonProperty("no")
        public String no;

        @JsonProperty("steamQuantity")
        public String steamQuantity;

        @JsonProperty("temperature")
        public String temperature;

        @JsonProperty("time")
        public String time;

        @JsonProperty("upTemperature")
        public String upTemperature;

        public int getTime(String deviceGuid) {
            try {
                if (deviceGuid.contains("DB620") || deviceGuid.contains("CQ920")){
                    return (Integer.parseInt(time) / 60);
                }else {
                    return Integer.parseInt(time);
                }
            }catch (Exception e){
                    return 0 ;
            }
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    class GatewayDicResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("dicGroupDtoList")
        public HashMap<String, Object> dicGroupDtoList;


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class IsCollectBookResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("collect")
        public boolean isCollect;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetKufaRecipeResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public List<PayLoad> payLoads;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetKuFRecipeDetailResonse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public PayLoadKuF payLoadKuFs;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetCurveCookbooksResonse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public List<PayLoadCookBook> payload;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ThumbCookbookResponse extends RCReponse {
        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;
        @JsonProperty("cookbook_3rds")
        public List<Recipe3rd> cookbook_3rds;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class RecipeThemeResponse extends RCReponse {
        @JsonProperty("items")
        public List<RecipeTheme> recipeThemes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ThemeRecipeDetailResponse extends RCReponse {

        @JsonProperty("mgs")
        public String mgs;

        @JsonProperty("theme")
        public RecipeTheme themeRecipeDetail;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class RecipeThemeResponse2 extends RCReponse {
        @JsonProperty("ThemeLists")
        public List<RecipeTheme> recipeThemes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class RecipeThemeResponse3 extends RCReponse {
        @JsonProperty("themeLists")
        public List<RecipeTheme> recipeThemes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class RecipeDynamicCover extends RCReponse {
        @JsonProperty("id")
        public long id;
        @JsonProperty("imageUrl")
        public String imageUrl;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class RecipeLiveListResponse extends RCReponse {
        @JsonProperty("videoCookbooks")
        public List<RecipeLiveList> lives;

        @Override
        public String toString() {
            return "RecipeLiveList{" +
                    "lives=" + lives +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class RecipeShowListResponse extends RCReponse {
        @JsonProperty("albums")
        public List<RecipeShow> items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CollectStatusRespone extends RCReponse {
        @JsonProperty("status")
        public String status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ThemeFavorite extends RCReponse {
        @JsonProperty("isFavorite")
        public String isFavorite;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookbooksResponse extends RCReponse {

        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;

        @JsonProperty("cookbook3rds")
        public List<Recipe3rd> cookbooks3rd;

        public int count() {
            int res = 0;
            if (cookbooks != null)
                res += cookbooks.size();
            if (cookbooks3rd != null)
                res += cookbooks3rd.size();

            return res;
        }
    }






    class CookbooksClassifyResponse extends RCReponse {
        @JsonProperty("cookbooks")
        public List<Cookbooks> cookbooks;

        @JsonProperty("cookbook_3rds")
        public List<Recipe3rd> cookbooks3rd;

        public int count() {
            int res = 0;
            if (cookbooks != null)
                res += cookbooks.size();
            if (cookbooks3rd != null)
                res += cookbooks3rd.size();

            return res;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CategoryRecipeImgRespone extends RCReponse {
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("name")
        public String name;
        @JsonProperty("imgUrl")
        public String imgUrl;
        @JsonProperty("desc")
        public String desc;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ConsultationListResponse extends RCReponse {
        @JsonProperty("items")
        public List<RecipeConsultation> items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class MaterialFrequencyResponse extends RCReponse {

        @JsonProperty("accessorys")
        public List<MaterialFrequency> list;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class MaterialsResponse extends RCReponse {

        @JsonProperty("materials")
        public Materials materials;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookbookProviderResponse extends RCReponse {

        @JsonProperty("sources")
        public List<RecipeProvider> providers;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class HotKeysForCookbookResponse extends RCReponse {

        @JsonProperty("cookbooks")
        public List<String> hotKeys;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class WeekTopsResponse extends RCReponse {
        @JsonProperty("mgs")
        public String mgs;

        @JsonProperty("payload")
        public List<Recipe> payload;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class AlbumResponse extends RCReponse {

        @JsonProperty("album")
        public CookAlbum album;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class AlbumsResponse extends RCReponse {

        @JsonProperty("albums")
        public List<CookAlbum> cookAlbums;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class HomeAdvertsForMobResponse extends RCReponse {

        @JsonProperty("images")
        public List<Advert.MobAdvert> adverts;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class HomeTitleForMobResponse extends RCReponse {

        @JsonProperty("images")
        public List<Advert.MobAdvert> titles;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class HomeAdvertsForPadResponse extends RCReponse {

        @JsonProperty("left")
        public List<Advert.PadAdvert> left;

        @JsonProperty("middle")
        public List<Advert.PadAdvert> middle;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookbookImageReponse extends RCReponse {

        @JsonProperty("images")
        public List<AdvertImage> images;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class SmartParamsReponse extends RCReponse {

        @JsonProperty("daily")
        public Daily daily;

        @JsonProperty("weekly")
        public Weekly weekly;

        @JsonIgnoreProperties(ignoreUnknown = true)
        class Daily implements IJsonPojo {

            @JsonProperty("on")
            public boolean enable;

            @JsonProperty("day")
            public int day;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class Weekly extends Daily {

            @JsonProperty("time")
            public String time;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetSmartParams360Reponse extends RCReponse {

        @JsonProperty("switch")
        public boolean switchStatus;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetCustomerInfoReponse extends RCReponse {

        @JsonProperty("customer")
        public OrderContacter customer;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class QueryOrderReponse extends RCReponse {

        @JsonProperty()
        public List<OrderInfo> orders;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class OrderIfOpenReponse extends RCReponse {

        @JsonProperty()
        public boolean open;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class EventStatusReponse extends RCReponse {

        @JsonProperty()
        public String image;
        @JsonProperty()
        public int status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class SubmitOrderReponse extends RCReponse {
        @JsonProperty()
        public long orderId;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetOrderReponse extends RCReponse {
        @JsonProperty()
        public OrderInfo order;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DeiverIfAllowReponse extends RCReponse {

        @JsonProperty()
        public boolean allow;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetCrmCustomerReponse extends RCReponse {
        @JsonProperty()
        public CrmCustomer customerInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class QueryMaintainReponse extends RCReponse {
        @JsonProperty()
        public MaintainInfo maintainInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CurrentLiveResponse extends RCReponse {
        @JsonProperty()
        public liveshow liveshow;
    }

    //获取联网优化设备列表
    @JsonIgnoreProperties(ignoreUnknown = true)
    class NetworkDeviceInfoResponse extends RCReponse {
        @JsonProperty("rc")
        public int rc;
        @JsonProperty("items")
        public List<DeviceGroupList> deviceGroupList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class NetworkDeviceStepResponse extends RCReponse {
        @JsonProperty("networkingSteps")
        public List<NetWorkingSteps> networkingSteps;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class HistoryResponse extends RCReponse {

        @JsonProperty("historyList")
        public List<History> historyList;


        @JsonProperty("msg")
        public String msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DeleteHistoryResponse extends RCReponse {

        @JsonProperty("msg")
        public String msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class upDataVideoWatchCountResponse extends RCReponse {

        @JsonProperty("msg")
        public String msg;//返回的消息

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CheckChickenResponse extends RCReponse {
        @JsonProperty("msg")
        public String msgs;

        @JsonProperty("payload")
        public CheckPayLoad payLoad;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class UpdateDeviceResponse extends RCReponse {
        @JsonProperty("msg")
        public String mgs;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookerStatusResponse extends RCReponse {
        @JsonProperty("mgs")
        public String mgs;

        @JsonProperty("payload")
        public String payload;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class SeriesInfoLisResponse extends RCReponse {

        @JsonProperty("totalSize")
        public int totalSize;//结果总数

        @JsonProperty("totalPage")
        public int totalPage;//总页数

        @JsonProperty("datas")
        public ArrayList<SeriesInfo> datas;//栏目数组


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CourseDetailReponse extends RCReponse {

        @JsonProperty("success")
        public int success;

        @JsonProperty("payload")
        public SeriesInfoCell seriesInfoCell;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ScanQRLoginResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class MallManagementResponse extends RCReponse {

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("mallManagements")
        public List<MallManagement> mMallManagements;


    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetRecipeTop4Response extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("functionsTop4")
        public List<FunctionsTop4> mFunctionCode;


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GetRecipeDiyCookbook extends RCReponse {

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("diyCookbookList")
        public List<DiyCookbookList> diyCookbookLists;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Update035Recipe extends RCReponse {
        @JsonProperty("msg")
        public String msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class SuggestApplyReponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class UploadRepones extends RCReponse {
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("uuid")
        public String uuid;
        @JsonProperty("path")
        public String path;
    }

    //删除烹饪曲线
    class CurveStepDelete extends RCReponse{

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("rc")
        public String rc;
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    class PayloadDTO implements Serializable {
        @JsonProperty("id")
        public Long id;
        @JsonProperty("curveCookbookId")
        public Integer curveCookbookId;
        @JsonProperty("curveStageParams")
        public String curveStageParams;
        @JsonProperty("userId")
        public Integer userId;
        @JsonProperty("name")
        public String name;
        @JsonProperty("introduction")
        public Object introduction;
        @JsonProperty("imageCover")
        public Object imageCover;
        @JsonProperty("deviceCategoryCode")
        public Object deviceCategoryCode;
        @JsonProperty("devicePlatformCode")
        public Object devicePlatformCode;
        @JsonProperty("deviceTypeCode")
        public Object deviceTypeCode;
        @JsonProperty("deviceParams")
        public String deviceParams;
        @JsonProperty("temperatureCurveParams")
        public String temperatureCurveParams;
        @JsonProperty("time")
        public Object time;
        @JsonProperty("difficulty")
        public Object difficulty;
        @JsonProperty("needTime")
        public Object needTime;
        @JsonProperty("materialList")
        public Object materialList;
        @JsonProperty("prepareStepList")
        public List<?> prepareStepList;
        @JsonProperty("stepList")
        public List<CookingCurveMarkStepList> stepList;
        @JsonProperty("shareFrom")
        public String shareFrom;
        @JsonProperty("readFlag")
        public String readFlag;
        @JsonProperty("gmtCreate")
        public String gmtCreate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class CookingCurveMarkStepRequest  extends RCReponse implements Serializable {

        @JsonProperty("id")
        public String id;

        @JsonProperty("stepDtoList")
        public List<CookingCurveMarkStepList> stepDtoList;

        public CookingCurveMarkStepRequest(String id, List<CookingCurveMarkStepList> stepDtoList) {
            this.id = id;
            this.stepDtoList = stepDtoList;
        }
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public class CookingCurveQueryRes extends RCReponse implements Serializable {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public PayloadDTO payload;
    }





}


