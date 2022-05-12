package com.robam.common.io.cloud;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.plat.Plat;
import com.legent.plat.pojos.AbsPostRequest;
import com.legent.utils.LogUtils;
import com.robam.common.events.AbsBookRefreshEvent;
import com.robam.common.pojos.CookStepDetails;
import com.robam.common.pojos.CrmProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sylar on 15/7/31.
 */
public interface Requests {

    class StoreRequest extends AbsPostRequest {
        @JsonProperty("storeId")
        public String storeId;

        public StoreRequest(String storeId) {
            this.storeId = storeId;
        }
    }

    class UserRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;

        public UserRequest(long userId) {
            this.userId = userId;
        }

//        @Override
//        public String toString() {
//            return "UserRequest{" +
//                    "userId=" + userId +
//                    '}';
//        }
    }


    class UserBookRequest extends UserRequest {

        @JsonProperty("cookbookId")
        public long cookbookId;

        @JsonProperty("entranceCode")
        public String entranceCode;

        @JsonProperty("needStepsInfo")
        public String needStepsInfo;

        public UserBookRequest(long userId, long cookbookId) {
            super(userId);
            this.cookbookId = cookbookId;
        }

        public UserBookRequest(long userId, long cookbookId, String entranceCode) {
            super(userId);
            LogUtils.i("20181116", "UserBookRequest_userId:" + userId);
            this.cookbookId = cookbookId;
            this.entranceCode = entranceCode;
        }

        public UserBookRequest(long userId, long cookbookId, String entranceCode, String needStepsInfo) {
            super(userId);
            this.cookbookId = cookbookId;
            this.entranceCode = entranceCode;
            this.needStepsInfo = needStepsInfo;
        }
    }

    class UserCookBookSteps extends AbsPostRequest {
        @JsonProperty("cookbookId")
        long cookbookId;

        @JsonProperty("categoryCode")
        String categoryCode;

        @JsonProperty("platCode")
        String platCode;

        public UserCookBookSteps(long cookbookId, String categoryCode, String platCode) {
            this.cookbookId = cookbookId;
            this.categoryCode = categoryCode;
            this.platCode = platCode;
        }

    }


    class UserCommentRequest extends UserRequest {

        @JsonProperty("id")
        public long id;

        public UserCommentRequest(long userId, long id) {
            super(userId);
            this.id = id;
        }
    }


    class GetYouzanRequst extends UserRequest {

        @JsonProperty("type")
        public String type;
        @JsonProperty("telephone")
        public String telephone;

        public GetYouzanRequst(long userId, String type, String telephone) {
            super(userId);
            this.type = type;
            this.telephone = telephone;
        }
    }


    class GetYouzanOrdersRequst extends UserRequest {

        @JsonProperty("statusList")
        public String[] statusList;

        public GetYouzanOrdersRequst(long userId, String[] statusList) {
            super(userId);
            this.statusList = statusList;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class MallManagementRequest {

    }

    class GetAppStartImg {

        @JsonProperty("appType")
        public String appType;

        public GetAppStartImg(String appType) {
            this.appType = appType;
        }
    }

    //by yinwei
    //设置家庭总人数
    class FamilyMember extends UserRequest {

        @JsonProperty("memberCount")
        public String memberCount;

        @JsonProperty("guid")
        public String guid;

        @Override
        public String toString() {
            return "FamilyMember{" +
                    "memberCount='" + memberCount + '\'' +
                    ", guid='" + guid + '\'' +
                    '}';
        }

        public FamilyMember(long userId, String memberCount, String guid) {
            super(userId);
            this.memberCount = memberCount;
            this.guid = guid;
        }
    }

    //获取家庭总人数
    class getFamilytotal extends UserRequest {
        @JsonProperty("guid")
        public String guid;

        @Override
        public String toString() {
            return super.toString() + "getFamilytotal{" +
                    "guid='" + guid + '\'' +
                    '}';
        }

        public getFamilytotal(long userId, String guid) {
            super(userId);
            this.guid = guid;
        }
    }

    class getChuYuan {
        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        @JsonProperty("statusisHistory")
        public int statusisHistory;

        public getChuYuan(int pageNo, int pageSize, int statusisHistory) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.statusisHistory = statusisHistory;
        }
    }

    //获取当前的饮水量
    class TodayDrinkingRequest extends UserRequest {

        @JsonProperty("guid")
        public String guid;

        @JsonProperty("timeType")
        public String timeType;

        public TodayDrinkingRequest(long userId, String guid, String timeType) {
            super(userId);
            this.guid = guid;
            this.timeType = timeType;
        }
    }

    //获取历史饮水量
    class HistoryDrinkingRequest extends UserRequest {

        @JsonProperty("timeType")
        public String timeType;
        @JsonProperty("startDate")
        public String startDate;
        @JsonProperty("endDate")
        public String endDate;
        @JsonProperty("guid")
        public String guid;

        public HistoryDrinkingRequest(long userId, String guid, String timeType, String startDate, String endDate) {
            super(userId);
            LogUtils.i("20180326", "userId::" + userId);
            this.guid = guid;
            this.timeType = timeType;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return "HistoryDrinkingRequest{" +
                    "timeType='" + timeType + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", guid='" + guid + '\'' +
                    '}';
        }
    }

    class CookingKnowledgeRequest extends AbsPostRequest {
        @JsonProperty("typeCode")
        public String typeCode;

        @JsonProperty("isActive")
        public int isActive;

        @JsonProperty("lable")
        public String lable;

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        public CookingKnowledgeRequest(String typeCode, int isActive, String lable, int pageNo, int pageSize) {
            this.typeCode = typeCode;
            this.isActive = isActive;
            this.lable = lable;
            this.pageNo = pageNo;
            this.pageSize = pageSize;

        }
    }


    class GetCookbooksByTagRequest extends AbsPostRequest {
        @JsonProperty("cookbookTagId")
        public long cookbookTagId;

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        public GetCookbooksByTagRequest(long cookbookTagId, int pageNo, int pageSize) {
            this.cookbookTagId = cookbookTagId;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }

    class GetReicpeOfTheme extends AbsPostRequest {
        @JsonProperty("CookbookIdList")
        public List<Long> cookbookIdList;
        @JsonProperty("userId")
        public long userId;

        public GetReicpeOfTheme(long userId, String[] strings) {
            this.userId = userId;
            if (strings != null && strings.length > 0) {
                cookbookIdList = new ArrayList<Long>();
                for (int i = 0; i < strings.length; i++) {
                    String cell = strings[i];
                    cookbookIdList.add(Long.parseLong(cell));
                }
            }
        }
    }

    class GetCookbooksByNameRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("name")
        public String name;

        public String contain3rd;
        @JsonProperty("notNeedSearchHistory")
        public boolean notNeedSearchHistory;
        @JsonProperty("needStatisticCookbook")
        public boolean needStatisticCookbook;

        public GetCookbooksByNameRequest(String name, String contain3rd, long userId) {
            this.name = name;
            this.contain3rd = contain3rd;
            this.userId = userId;
        }

        public GetCookbooksByNameRequest(String name, String contain3rd, long userId, boolean notNeedSearchHistory) {
            this.name = name;
            this.contain3rd = contain3rd;
            this.userId = userId;
            this.notNeedSearchHistory = notNeedSearchHistory;
        }

        public GetCookbooksByNameRequest(String name, String contain3rd, long userId, boolean notNeedSearchHistory,boolean needStatisticCookbook) {
            this.name = name;
            this.contain3rd = contain3rd;
            this.userId = userId;
            this.notNeedSearchHistory = notNeedSearchHistory;
            this.needStatisticCookbook = needStatisticCookbook;
        }

    }

    // 周定钧20160630
    class GetRecommendRecipesByDeviceForPadRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;

        public GetRecommendRecipesByDeviceForPadRequest(long userId, String dc) {
            this.userId = userId;
            this.dc = dc;
        }
    }

    // 周定钧20161212
    class GetNetworkDeviceInfoRequest extends AbsPostRequest {
        @JsonProperty("vendor")
        public String vendor;
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("dt")
        public String dt;

        public GetNetworkDeviceInfoRequest(String vendor, String dc, String dt) {
            this.vendor = vendor;
            this.dc = dc;
            this.dt = dt;
        }
    }

    class GetNetworkDeviceStepRequest extends AbsPostRequest {
        @JsonProperty("displayType")
        public String displayType;

        public GetNetworkDeviceStepRequest(String displayType) {
            this.displayType = displayType;
        }
    }

    // 周定钧20160630
    class getRecommendRecipesByDeviceForCellphoneRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;

        public getRecommendRecipesByDeviceForCellphoneRequest(long userId, String dc) {
            this.userId = userId;
            this.dc = dc;
        }
    }


    // 周定钧20160711
    class getNotRecommendRecipesByDeviceRequest extends AbsPostRequest {
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("start")
        public int start;
        @JsonProperty("limit")
        public int limit;

        public getNotRecommendRecipesByDeviceRequest(String dc, int start, int limit) {
            this.dc = dc;
            this.start = start;
            this.limit = limit;
        }
    }


    // 周定钧20160711
    class getGroundingRecipesByDeviceRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("start")
        public int start;
        @JsonProperty("limit")
        public int limit;
        @JsonProperty("cookbookType")
        public String cookbookType;
        @JsonProperty("lang")
        public String lang;
        //2020年6月4日 新增传改设备平台（由后台配置文件决定）
        @JsonProperty("devicePlat")
        public String devicePlat;

        public getGroundingRecipesByDeviceRequest(String dc, String cookbookType, String lang, int start, int limit, String devicePlat) {
            this.dc = dc;
            this.cookbookType = cookbookType;
            this.start = start;
            this.limit = limit;
            this.lang = lang;
            this.devicePlat = devicePlat;
        }

        public getGroundingRecipesByDeviceRequest(long userId, String dc, String cookbookType, int start, int limit, String devicePlat) {
            this.userId = userId;
            this.dc = dc;
            this.cookbookType = cookbookType;
            this.start = start;
            this.limit = limit;
            if (!"".equals(devicePlat)) {
                this.devicePlat = devicePlat;
            }

        }

        public getGroundingRecipesByDeviceRequest(String dc, String cookbookType, int start, int limit) {
            this.dc = dc;
            this.cookbookType = cookbookType;
            this.start = start;
            this.limit = limit;
        }

        public getGroundingRecipesByDeviceRequest(String dc, String cookbookType, int start, int limit, String devicePlat) {
            this.dc = dc;
            this.cookbookType = cookbookType;
            this.start = start;
            this.limit = limit;
            this.devicePlat = devicePlat;
        }

        public getGroundingRecipesByDeviceRequest(String dc, int start, int limit, String devicePlat) {
            this.dc = dc;
            this.start = start;
            this.limit = limit;
            this.devicePlat = devicePlat;
        }
    }


    // 周定钧20160711
    class GetTodayRecipesByDeviceRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;

        public GetTodayRecipesByDeviceRequest(long userId, String dc) {
            this.userId = userId;
            this.dc = dc;
        }
    }


    class UserMaterialRequest extends UserRequest {

        @JsonProperty("materialId")
        public long materialId;

        public UserMaterialRequest(long userId, long materialId) {
            super(userId);
            this.materialId = materialId;
        }
    }

    class GetCookAlbumsRequest extends UserBookRequest {

        @JsonProperty("start")
        public int start;

        @JsonProperty("limit")
        public int limit;

        public GetCookAlbumsRequest(long userId, long cookbookId, int start,
                                    int limit) {
            super(userId, cookbookId);
            this.start = start;
            this.limit = limit;
        }

    }

    class SubmitCookAlbumRequest extends UserBookRequest {

        @JsonProperty("image")
        public String image;

        @JsonProperty("desc")
        public String desc;

        public SubmitCookAlbumRequest(long userId, long cookbookId,
                                      String image, String desc) {
            super(userId, cookbookId);
            this.image = image;
            this.desc = desc;
        }

    }

    class CookAlbumRequest extends UserRequest {
        @JsonProperty("albumId")
        public long albumId;

        public CookAlbumRequest(long userId, long albumId) {
            super(userId);
            this.albumId = albumId;
        }
    }

    class ApplyAfterSaleRequest extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        public ApplyAfterSaleRequest(long userId, String deviceGuid) {
            super(userId);
            this.guid = deviceGuid;
        }
    }

    class GetSmartParamsRequest extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        public GetSmartParamsRequest(long userId, String deviceGuid) {
            super(userId);
            this.guid = deviceGuid;
        }
    }

    class SetSmartParamsByDailyRequest extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        @JsonProperty("on")
        public boolean enable;

        @JsonProperty("day")
        public int day;

        public SetSmartParamsByDailyRequest(long userId, String guid,
                                            boolean enable, int day) {
            super(userId);
            this.guid = guid;
            this.enable = enable;
            this.day = day;
        }
    }

    class SetSmartParamsByWeeklyRequest extends
            SetSmartParamsByDailyRequest {

        @JsonProperty("time")
        public String time;

        public SetSmartParamsByWeeklyRequest(long userId, String guid,
                                             boolean enable, int day, String time) {
            super(userId, guid, enable, day);
            this.time = time;
        }

    }


    class GetSmartParams360Request extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        public GetSmartParams360Request(long userId, String deviceGuid) {
            super(userId);
            this.guid = deviceGuid;
        }
    }


    class SetSmartParams360Request extends GetSmartParams360Request {

        @JsonProperty("switch")
        public boolean switchStatus;

        public SetSmartParams360Request(long userId, String deviceGuid, boolean status) {
            super(userId, deviceGuid);
            this.switchStatus = status;
        }
    }

    class CookingLogRequest extends UserBookRequest {

        @JsonProperty("stepCount")
        public int stepCount;

        @JsonProperty("deviceGuid")
        public String deviceGuid;

        @JsonProperty("appType")
        public String appType;

        @JsonProperty("startTime")
        public long start;

        @JsonProperty("endTime")
        public long end;

        @JsonProperty("finishType")
        public int finishType;

        @JsonProperty("stepDetails")
        public List<CookStepDetails> stepDetails;

        public CookingLogRequest(long userId, long cookbookId, String deviceGuid,
                                 long start, long end, boolean isBroken) {
            super(userId, cookbookId);
            this.deviceGuid = deviceGuid;
            this.start = start;
            this.end = end;
            this.finishType = isBroken ? 2 : 1;
        }

        public CookingLogRequest(long userId, long cookbookId, int stepCount, String deviceGuid,
                                 String appType, long start, long end, boolean isBroken,
                                 List<CookStepDetails> stepDetails) {
            super(userId, cookbookId);
            this.cookbookId = cookbookId;
            this.stepCount = stepCount;
            this.deviceGuid = deviceGuid;
            this.appType = appType;
            this.start = start;
            this.end = end;
            this.finishType = isBroken ? 2 : 1;
            this.stepDetails = stepDetails;

            LogUtils.i("20170901", " cookbookId:" + cookbookId + " stepCount:" + stepCount + " deviceGuid:" + deviceGuid
                    + " appType:" + appType + " start:" + start + " end:" + end + " finishType:" + finishType + " stepDetails:" + stepDetails);

        }

    }

    class CookbookSearchHistory extends UserRequest {

        public CookbookSearchHistory(long userId) {
            super(userId);
        }
    }


    class DeleteCookbookSearchHistory extends UserRequest {

        //        @JsonProperty("name")
        public String name;

        public DeleteCookbookSearchHistory(String name, long userId) {
            super(userId);
            this.name = name;
        }
    }

    class UserOrderRequest extends UserRequest {
        @JsonProperty
        public long orderId;

        public UserOrderRequest(long userId, long orderId) {
            super(userId);
            this.orderId = orderId;
        }
    }

    class SaveCustomerInfoRequest extends UserRequest {

        @JsonProperty()
        public String name;
        @JsonProperty()
        public String phone;
        @JsonProperty()
        public String city;
        @JsonProperty()
        public String address;


        public SaveCustomerInfoRequest(long userId, String name, String phone, String city, String address) {
            super(userId);
            this.name = name;
            this.phone = phone;
            this.city = city;
            this.address = address;
        }
    }

    class SubmitOrderRequest extends UserRequest {
        @JsonProperty("cookbooks")
        public List<Long> recipeIds;

        public SubmitOrderRequest(long userId, List<Long> ids) {
            super(userId);
            this.recipeIds = ids;
        }
    }

    class QueryOrderRequest extends UserRequest {
        @JsonProperty()
        public long time;
        @JsonProperty()
        public int limit;

        public QueryOrderRequest(long userId, long time, int limit) {
            super(userId);
            this.time = time;
            this.limit = limit;
        }
    }

    class UpdateOrderContacterRequest extends SaveCustomerInfoRequest {

        @JsonProperty()
        public long orderId;

        public UpdateOrderContacterRequest(long userId, long orderId, String name, String phone, String city, String address) {
            super(userId, name, phone, city, address);
            this.orderId = orderId;
        }
    }

    class GetOrderRequest extends AbsPostRequest {
        @JsonProperty()
        public long orderId;

        public GetOrderRequest(long orderId) {
            this.orderId = orderId;
        }
    }

    class GetGroudingRecipesRequest extends AbsPostRequest {

        @JsonProperty()
        public int start;
        @JsonProperty()
        public int limit;
        @JsonProperty()
        public String lang;

        public GetGroudingRecipesRequest(int start, int limit, String lang) {
            this.start = start;
            this.limit = limit;
            this.lang = lang;
        }

        public GetGroudingRecipesRequest(int start, int limit) {
            this.start = start;
            this.limit = limit;
        }
    }

    class ThemeRecipeDetailRequest extends AbsPostRequest {
        @JsonProperty()
        public long themeId;

        public ThemeRecipeDetailRequest(long themeId) {
            this.themeId = themeId;
        }
    }

    class GetCrmCustomerRequest extends AbsPostRequest {
        @JsonProperty()
        public String phone;

        public GetCrmCustomerRequest(String phone) {
            this.phone = phone;
        }
    }

    class GetPageRequest extends AbsPostRequest {
        @JsonProperty("start")
        public int start;

        @JsonProperty("limit")
        public int limit;

        public GetPageRequest(int start, int limit) {
            this.start = start;
            this.limit = limit;
        }
    }

    class GetPageUserRequest extends UserRequest {
        @JsonProperty("start")
        public int start;

        @JsonProperty("limit")
        public int limit;

        public GetPageUserRequest(int start, int limit) {
            super(Plat.accountService.getCurrentUserId());
            this.start = start;
            this.limit = limit;
        }
    }

    class ThemeCollectRequest extends UserRequest {
        @JsonProperty("themeId")
        public long themeId;

        public ThemeCollectRequest(long userId, long themeId) {
            super(userId);
            this.themeId = themeId;
        }
    }

    class ConsultationListRequest extends AbsPostRequest {
        @JsonProperty("page")
        public int page;
        @JsonProperty("size")
        public int size;

        public ConsultationListRequest(int page, int size) {
            this.page = page;
            this.size = size;
        }
    }


    class CategoryRecipeImgRequest extends AbsPostRequest {
        @JsonProperty("dc")
        public String dc;

        public CategoryRecipeImgRequest(String dc) {
            this.dc = dc;
        }
    }


    class PersonalizedRecipeRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        public PersonalizedRecipeRequest(long userId, int pageNo, int pageSize) {
            this.userId = userId;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }


    class RamdomCookBookRequest extends AbsPostRequest {

        @JsonProperty("randomNum")
        public int randomNum;


        public RamdomCookBookRequest(int randomNum) {
            this.randomNum = randomNum;
        }
    }


    class WeekTopsRequest extends AbsPostRequest {
//        @JsonProperty("weekYear")
//        public long weekYear;

        @JsonProperty("weekTime")
        public String weekTime;

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        public WeekTopsRequest(String weekTime, int pageNo, int pageSize) {
//            this.weekYear = weekYear;
            this.weekTime = weekTime;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }

    class SuggestApplyRequest extends AbsPostRequest {
        @JsonProperty("phone")
        public String phone;

        @JsonProperty("pic1")
        public String pic1;

        @JsonProperty("pic2")
        public String pic2;

        @JsonProperty("pic3")
        public String pic3;

        @JsonProperty("suggest")
        public String suggest;

        @JsonProperty("userId")
        public long userId;

        public SuggestApplyRequest(String phone, String pic1, String pic2, String pic3, String suggest, long userId) {
            this.phone = phone;
            this.pic1 = pic1;
            this.pic2 = pic2;
            this.pic3 = pic3;
            this.suggest = suggest;
            this.userId = userId;
        }
    }


    class TagOtherCooksRequest extends AbsPostRequest {
        @JsonProperty("cookbookTagId")
        public Long cookbookTagId;

        @JsonProperty("needStatisticCookbook")
        public boolean needStatisticCookbook;

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        @JsonProperty("type")
        public int type;
        @JsonProperty("excludeCookIds")
        public List<Long> excludeCookIds;


        public TagOtherCooksRequest(Long cookbookTagId, boolean needStatisticCookbook, int pageNo, int pageSize, int type) {
            this.cookbookTagId = cookbookTagId;
            this.needStatisticCookbook = needStatisticCookbook;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.type = type;
        }

        public TagOtherCooksRequest(Long cookbookTagId, boolean needStatisticCookbook, int pageNo, int pageSize, int type, List<Long> excludeCookIds) {
            this.cookbookTagId = cookbookTagId;
            this.needStatisticCookbook = needStatisticCookbook;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.type = type;
            this.excludeCookIds = excludeCookIds;
        }
    }

    class TagCooksRequest extends AbsPostRequest {
        @JsonProperty("cookbookTagId")
        public Long cookbookTagId;


        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        @JsonProperty("needStatisticCookbook")
        public boolean needStatisticCookbook = false;


        public TagCooksRequest(Long cookbookTagId, int pageNo, int pageSize) {
            this.cookbookTagId = cookbookTagId;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }

    class saveMultiRecipeRequest extends AbsPostRequest {
        @JsonProperty("deviceGuid")
        public String deviceGuid;

        @JsonProperty("id")
        public Integer id;

        @JsonProperty("multiStepDtoList")
        public List<saveMultiRecipeList> multiStepDtoList;

        @JsonProperty("name")
        public String name;

        @JsonProperty("userId")
        public long userId;

    }

    class saveMultiRecipeList {
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

//        @JsonProperty("upTemperature")
//        public String upTemperature;

    }

    class TagOtherThemesRequest extends AbsPostRequest {
        @JsonProperty("cookbookTagId")
        public Long cookbookTagId;

        @JsonProperty("needStatisticCookbook")
        public boolean needStatisticCookbook;

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        @JsonProperty("type")
        public int type;

        public TagOtherThemesRequest(Long cookbookTagId, boolean needStatisticCookbook, int pageNo, int pageSize, int type) {
            this.cookbookTagId = cookbookTagId;
            this.needStatisticCookbook = needStatisticCookbook;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.type = type;
        }
    }


    class CookbookbythemeIdRequest extends AbsPostRequest {

        @JsonProperty("lang")
        public String lang;

        @JsonProperty("limit")
        public long limit;

        @JsonProperty("start")
        public int start;

        @JsonProperty("themeId")
        public int themeId;


        @JsonProperty("needStatisticCookbook")
        public boolean needStatisticCookbook;

        public CookbookbythemeIdRequest(String lang, long limit, int start, int themeId) {
            this.lang = lang;
            this.limit = limit;
            this.start = start;
            this.themeId = themeId;
            this.needStatisticCookbook = false;
        }
    }


    class IsCollectRequest extends UserRequest {
        @JsonProperty()
        public long cookbookId;

        public IsCollectRequest(long userId, long cookbookId) {
            super(userId);
            this.userId = userId;
            this.cookbookId = cookbookId;
        }
    }


    class SubmitMaintainRequest extends UserRequest {
        @JsonProperty()
        public String customerId;

        @JsonProperty()
        public String customerName;

        @JsonProperty()
        public String phone;

        @JsonProperty()
        public String address;

        @JsonProperty()
        public String category;

        @JsonProperty()
        public String productType;

        @JsonProperty()
        public String productId;

        @JsonProperty()
        public String buyTime;

        @JsonProperty()
        public long bookTime;

        @JsonProperty()
        public String province;

        @JsonProperty()
        public String city;

        @JsonProperty()
        public String county;


        public SubmitMaintainRequest(long userId, CrmProduct product, long bookTime, String customerId, String customerName, String phone, String province, String city, String county, String address) {
            super(userId);

            this.productId = product.id;
            this.category = product.category;
            this.productType = product.type;
            this.productId = product.id;
            this.buyTime = product.buyTime;
            //
            this.customerId = customerId;
            this.customerName = customerName;
            this.bookTime = bookTime;
            this.phone = phone;
            this.province = province;
            this.city = city;
            this.county = county;
            this.address = address;
        }
    }

    class QueryMaintainRequest extends UserRequest {
        public QueryMaintainRequest(long userId) {
            super(userId);
        }
    }


    class upDataVideoWatchCountRequest extends AbsPostRequest {

        @JsonProperty("seriesId")
        public String seriesId;

        @JsonProperty("courseId")
        public String courseId;

        public upDataVideoWatchCountRequest(String seriesId, String courseId) {
            this.seriesId = seriesId;
            this.courseId = courseId;
        }
    }

    class CheckChickenRequest {
        @JsonProperty("mac")
        public String mac;

        @JsonProperty("deviceGuid")
        public String deviceGuid;

        public CheckChickenRequest(String mac, String deviceGuid) {
            this.mac = mac;
            this.deviceGuid = deviceGuid;
        }
    }

    class UpDateDevice {

        @JsonProperty("mac")
        public String mac;

        @JsonProperty("deviceGuid")
        public String deviceGuid;

        @JsonProperty("targetVersion")
        public String targetVersion;

        public UpDateDevice(String mac, String deviceGuid, String targetVersion) {
            this.mac = mac;
            this.deviceGuid = deviceGuid;
            this.targetVersion = targetVersion;
        }
    }


    class SeriesInfoListRequest extends AbsPostRequest {

        @JsonProperty("pageNo")
        public int pageNo;

        @JsonProperty("pageSize")
        public int pageSize;

        public SeriesInfoListRequest(int pageNo, int pageSize) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }

    class CourseDetailRequest extends AbsPostRequest {

        @JsonProperty("seriesId")
        public String seriesId;

        @JsonProperty("orderNo")
        public String orderNo;

        public CourseDetailRequest(String seriesId, String orderNo) {
            this.seriesId = seriesId;
            this.orderNo = orderNo;
        }
    }

    class ScanQRLoginRequest extends UserRequest {

        @JsonProperty("uuid")
        public String uuid;
        @JsonProperty("phone")
        public String phone;

        public ScanQRLoginRequest(long userId, String uuid) {
            super(userId);
            this.uuid = uuid;
        }

        public ScanQRLoginRequest(long userId, String uuid, String phone) {
            super(userId);
            this.uuid = uuid;
            this.phone = phone;
        }
    }

    class getRecipTop4Request extends UserRequest {

        @JsonProperty("deviceGuid")
        public String deviceGuid;

        @JsonProperty("deviceCategory")
        public String deviceCategory;

        @JsonProperty("deviceType")
        public String deviceType;


        public getRecipTop4Request(long userId, String deviceGuid, String deviceCategory, String deviceType) {
            super(userId);
            this.deviceGuid = deviceGuid;
            this.deviceCategory = deviceCategory;
            this.deviceType = deviceType;
        }
    }

    class getRecipeDiyCookbook extends UserRequest {
        @JsonProperty("deviceType")
        public String deviceType;


        public getRecipeDiyCookbook(long userId, String deviceType) {
            super(userId);
            this.deviceType = deviceType;
        }
    }


    class update035RecipeRequest extends UserRequest {
        @JsonProperty("deviceType")
        public String deviceType;

        @JsonProperty("name")
        public String name;

        @JsonProperty("modeCode")
        public String modeCode;

        @JsonProperty("temp")
        public String temp;

        @JsonProperty("minute")
        public String minute;

        @JsonProperty("hasRotate")
        public String hasRotate;

        @JsonProperty("openRotate")
        public String openRotate;

        @JsonProperty("cookbookDesc")
        public String cookbookDesc;

        @JsonProperty("id")
        public Long id;


        public update035RecipeRequest(long userId, String deviceType, String name, String modeCode, String temp, String minute,
                                      String hasRotate, String openRotate, String cookbookDesc, Long id) {
            super(userId);
            this.deviceType = deviceType;
            this.name = name;
            this.modeCode = modeCode;
            this.temp = temp;
            this.minute = minute;
            this.hasRotate = hasRotate;
            this.openRotate = openRotate;
            this.cookbookDesc = cookbookDesc;
            this.id = id;
        }


    }

    class update915RecipeRequest extends UserRequest {
        @JsonProperty("deviceType")
        public String deviceType;

        @JsonProperty("name")
        public String name;

        @JsonProperty("modeCode")
        public String modeCode;

        @JsonProperty("temp")
        public String temp;

        @JsonProperty("minute")
        public String minute;

        @JsonProperty("cookbookDesc")
        public String cookbookDesc;

        @JsonProperty("id")
        public Long id;


        public update915RecipeRequest(long userId, String deviceType, String name, String modeCode, String temp, String minute,
                                      String cookbookDesc, Long id) {
            super(userId);
            this.deviceType = deviceType;
            this.name = name;
            this.modeCode = modeCode;
            this.temp = temp;
            this.minute = minute;
            this.cookbookDesc = cookbookDesc;
            this.id = id;
        }


    }


    class updateRQ908RecipeRequest extends UserRequest {
        @JsonProperty("deviceType")
        public String deviceType;

        @JsonProperty("name")
        public String name;

        @JsonProperty("modeCode")
        public String modeCode;

        @JsonProperty("temp")
        public String temp;

        @JsonProperty("tempDown")
        public String tempDown;

        @JsonProperty("minute")
        public String minute;

        @JsonProperty("cookbookDesc")
        public String cookbookDesc;

        @JsonProperty("id")
        public Long id;

        public updateRQ908RecipeRequest(long userId, String deviceType, String name, String modeCode,
                                        String temp, String tempDown, String minute, String cookbookDesc, Long id) {

            super(userId);
            this.deviceType = deviceType;
            this.name = name;
            this.modeCode = modeCode;
            this.temp = temp;
            this.tempDown = tempDown;
            this.minute = minute;
            this.cookbookDesc = cookbookDesc;
            this.id = id;
        }
    }

    class deleteRecipeDiyCookbook {


        @JsonProperty("id")
        public Long id;

        public deleteRecipeDiyCookbook(Long id) {
            this.id = id;
        }
    }

    class deleteMultiRecipe {

        @JsonProperty("id")
        public Integer id;

        @JsonProperty("no")
        public Integer no;

        public deleteMultiRecipe(Integer id) {
            this.id = id;
        }

        public deleteMultiRecipe(Integer id, Integer no) {
            this.id = id;
            this.no = no;
        }
    }
}
