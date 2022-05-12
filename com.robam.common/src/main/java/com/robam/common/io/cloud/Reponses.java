package com.robam.common.io.cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
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

    class StoreVersionResponse extends RCReponse {
        @JsonProperty("version")
        public int version;
    }

    class StoreCategoryResponse extends RCReponse {
        @JsonProperty("cookbookTagGroups")
        public List<Group> cookbookTagGroups;
    }

    class GetYiGuoUrlResponse extends RCReponse {
        @JsonProperty("images")
        public ArrayList<Images> images;
    }

    /**
     * 设置家庭人数返回
     */
    class GetFamilyResponse extends RCReponse {
        @JsonProperty("memberCount")
        public int memberCount;
    }

    /**
     * 有赞商城
     */
    class TokenResponses extends RCReponse {
        @JsonProperty("token")
        public Token token;
    }


    class YouzanOrdersReponses extends RCReponse {

        @JsonProperty("statusCount")
        public List<YouzanOrdersCount> statusCount;
    }

    class AppStartImgResponses extends RCReponse {

        @JsonProperty("images")
        public String[] images;

    }

    class AppAdvertImgResponses extends RCReponse {

        @JsonProperty("images")
        public List<Advert> images;

    }

    /**
     * 获取厨房知识列表
     */
    class CookingKnowledgeResponse extends RCReponse {

        @JsonProperty("hasNext")
        public String hasNext;

        @JsonProperty("cookingKnowledges")
        public List<CookingKnowledge> cookingKnowledges;

    }

    /**
     * 获取今日饮水量
     */
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
    class HistoryDrinkingResponse extends RCReponse {
        @JsonProperty("data")
        public ArrayList<DataInfo> item;
    }

    /**
     * 获取菜谱详情及烹饪步骤
     */
    class CookbookResponse extends RCReponse {
        @JsonProperty("cookbook")
        public Recipe cookbook;

    }

    class CookBookOrStepResponse extends RCReponse{
        @JsonProperty("cookbook")
        public Recipe cookbook;
        @JsonProperty("steps")
        public List<CookStep> cookSteps;
    }

    class CookbookStepResponse extends RCReponse {
        @JsonProperty("steps")
        public List<CookStep> cookSteps;
    }

    class Cookbook3rdResponse extends RCReponse {
        @JsonProperty("cookbook")
        public Recipe3rd cookbook;
    }

    class PersonalizedRecipeResponse extends RCReponse {
        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;
    }

    class MultiRecipeResponse extends RCReponse {

        @JsonProperty("totalSize")
        public int totalSize;

        @JsonProperty("totalPage")
        public int totalPage;

        @JsonProperty("datas")
        public List<multiRecipeList> datas;
    }
    class multiRecipeList implements Serializable {
        @JsonProperty("id")
        public long id;

        @JsonProperty("userId")
        public long userId;

        @JsonProperty("deviceGuid")
        public String deviceGuid;

        @JsonProperty("name")
        public String name;

        @JsonProperty("multiStepDtoList")
        public List<saveMultiRecipeList> multiStepDtoList;
    }
    class saveMultiRecipeList  {
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

    }



    class GatewayDicResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("dicGroupDtoList")
        public HashMap<String, Object> dicGroupDtoList;


    }

    class IsCollectBookResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("collect")
        public boolean isCollect;
    }

    class GetKufaRecipeResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public List<PayLoad> payLoads;
    }

    class GetKuFRecipeDetailResonse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public PayLoadKuF payLoadKuFs;
    }
    class GetCurveCookbooksResonse extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("payload")
        public List<PayLoadCookBook> payload;
    }
    class ThumbCookbookResponse extends RCReponse {
        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;
        @JsonProperty("cookbook_3rds")
        public List<Recipe3rd> cookbook_3rds;
    }

    class RecipeThemeResponse extends RCReponse {
        @JsonProperty("items")
        public List<RecipeTheme> items;
    }

    class ThemeRecipeDetailResponse extends RCReponse {

        @JsonProperty("mgs")
        public String mgs;

        @JsonProperty("theme")
        public RecipeTheme theme;
    }

    class RecipeThemeResponse2 extends RCReponse {
        @JsonProperty("ThemeLists")
        public List<RecipeTheme> recipeThemes;
    }

    class RecipeThemeResponse3 extends RCReponse {
        @JsonProperty("themeLists")
        public List<RecipeTheme> recipeThemes;
    }

    class RecipeDynamicCover extends RCReponse {
        @JsonProperty("id")
        public long id;
        @JsonProperty("imageUrl")
        public String imageUrl;
    }

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

    class RecipeShowListResponse extends RCReponse {
        @JsonProperty("albums")
        public List<RecipeShow> items;
    }

    class CollectStatusRespone extends RCReponse {
        @JsonProperty("status")
        public String status;
    }

    class ThemeFavorite extends RCReponse {
        @JsonProperty("isFavorite")
        public String isFavorite;
    }

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

    class ConsultationListResponse extends RCReponse {
        @JsonProperty("items")
        public List<RecipeConsultation> items;
    }

    class MaterialFrequencyResponse extends RCReponse {

        @JsonProperty("accessorys")
        public List<MaterialFrequency> list;

    }

    class MaterialsResponse extends RCReponse {

        @JsonProperty("materials")
        public Materials materials;

    }

    class CookbookProviderResponse extends RCReponse {

        @JsonProperty("sources")
        public List<RecipeProvider> providers;

    }

    class HotKeysForCookbookResponse extends RCReponse {

        @JsonProperty("cookbooks")
        public List<String> hotKeys;

    }

    class WeekTopsResponse extends RCReponse {
        @JsonProperty("mgs")
        public String mgs;

        @JsonProperty("payload")
        public List<Recipe> payload;
    }

    class AlbumResponse extends RCReponse {

        @JsonProperty("album")
        public CookAlbum album;

    }

    class AlbumsResponse extends RCReponse {

        @JsonProperty("albums")
        public List<CookAlbum> cookAlbums;

    }

    class HomeAdvertsForMobResponse extends RCReponse {

        @JsonProperty("images")
        public List<Advert.MobAdvert> adverts;

    }

    class HomeTitleForMobResponse extends RCReponse {

        @JsonProperty("images")
        public List<Advert.MobAdvert> titles;

    }

    class HomeAdvertsForPadResponse extends RCReponse {

        @JsonProperty("left")
        public List<Advert.PadAdvert> left;

        @JsonProperty("middle")
        public List<Advert.PadAdvert> middle;

    }

    class CookbookImageReponse extends RCReponse {

        @JsonProperty("images")
        public List<AdvertImage> images;

    }

    class SmartParamsReponse extends RCReponse {

        @JsonProperty("daily")
        public Daily daily;

        @JsonProperty("weekly")
        public Weekly weekly;

        class Daily implements IJsonPojo {

            @JsonProperty("on")
            public boolean enable;

            @JsonProperty("day")
            public int day;
        }

        class Weekly extends Daily {

            @JsonProperty("time")
            public String time;
        }
    }

    class GetSmartParams360Reponse extends RCReponse {

        @JsonProperty("switch")
        public boolean switchStatus;

    }

    class GetCustomerInfoReponse extends RCReponse {

        @JsonProperty("customer")
        public OrderContacter customer;

    }

    class QueryOrderReponse extends RCReponse {

        @JsonProperty()
        public List<OrderInfo> orders;

    }

    class OrderIfOpenReponse extends RCReponse {

        @JsonProperty()
        public boolean open;
    }

    class EventStatusReponse extends RCReponse {

        @JsonProperty()
        public String image;
        @JsonProperty()
        public int status;
    }

    class SubmitOrderReponse extends RCReponse {
        @JsonProperty()
        public long orderId;

    }

    class GetOrderReponse extends RCReponse {
        @JsonProperty()
        public OrderInfo order;
    }

    class DeiverIfAllowReponse extends RCReponse {

        @JsonProperty()
        public boolean allow;

    }

    class GetCrmCustomerReponse extends RCReponse {
        @JsonProperty()
        public CrmCustomer customerInfo;
    }

    class QueryMaintainReponse extends RCReponse {
        @JsonProperty()
        public MaintainInfo maintainInfo;
    }

    class CurrentLiveResponse extends RCReponse {
        @JsonProperty()
        public liveshow liveshow;
    }

    //获取联网优化设备列表
    class NetworkDeviceInfoResponse extends RCReponse {
        @JsonProperty("rc")
        public int rc;
        @JsonProperty("items")
        public List<DeviceGroupList> deviceGroupList;
    }

    class NetworkDeviceStepResponse extends RCReponse {
        @JsonProperty("networkingSteps")
        public List<NetWorkingSteps> networkingSteps;
    }

    class HistoryResponse extends RCReponse {

        @JsonProperty("historyList")
        public List<History> historyList;


        @JsonProperty("msg")
        public String msg;
    }

    class DeleteHistoryResponse extends RCReponse {

        @JsonProperty("msg")
        public String msg;
    }

    class upDataVideoWatchCountResponse extends RCReponse {

        @JsonProperty("msg")
        public String msg;//返回的消息

    }

    class CheckChickenResponse extends RCReponse {
        @JsonProperty("msg")
        public String msgs;

        @JsonProperty("payload")
        public CheckPayLoad payLoad;
    }

    class UpdateDeviceResponse extends RCReponse {
        @JsonProperty("msg")
        public String mgs;

    }

    class CookerStatusResponse extends RCReponse {
        @JsonProperty("mgs")
        public String mgs;

        @JsonProperty("payload")
        public String payload;
    }

    class SeriesInfoLisResponse extends RCReponse {

        @JsonProperty("totalSize")
        public int totalSize;//结果总数

        @JsonProperty("totalPage")
        public int totalPage;//总页数

        @JsonProperty("datas")
        public ArrayList<SeriesInfo> datas;//栏目数组


    }

    class CourseDetailReponse extends RCReponse {

        @JsonProperty("success")
        public int success;

        @JsonProperty("payload")
        public SeriesInfoCell seriesInfoCell;
    }

    class ScanQRLoginResponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;
    }

    class MallManagementResponse extends RCReponse {

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("mallManagements")
        public List<MallManagement> mMallManagements;


    }


    class GetRecipeTop4Response extends RCReponse {
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("functionsTop4")
        public List<FunctionsTop4> mFunctionCode;


    }

    class GetRecipeDiyCookbook extends RCReponse {

        @JsonProperty("msg")
        public String msg;

        @JsonProperty("diyCookbookList")
        public List<DiyCookbookList> diyCookbookLists;
    }

    class Update035Recipe extends RCReponse {
        @JsonProperty("msg")
        public String msg;
    }

    class SuggestApplyReponse extends RCReponse {
        @JsonProperty("msg")
        public String msg;
    }

    class UploadRepones extends RCReponse {
        @JsonProperty("msg")
        public String msg;
        @JsonProperty("uuid")
        public String uuid;
        @JsonProperty("path")
        public String path;
    }

}
