package com.robam.common.pojos;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.LazyForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.pojos.AbsStorePojo;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.paramCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 烧菜步骤
 *
 * @author sylar
 */
public class CookStep extends AbsStorePojo<Long> implements Serializable , MultiItemEntity {

    public final static String COLUMN_BOOK_ID = "book_id";
    public final static int IMAGE = 1;
    public final static int VIDEO = 2;


    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("no")
    public int order;

    @DatabaseField()
    @JsonProperty("desc")
    public String desc;

    @DatabaseField
    @JsonProperty("image")
    public String imageUrl;

    @DatabaseField
    @JsonProperty("fanGear")
    public short fanLevel;

    @DatabaseField
    @JsonProperty("stoveGear")
    public short stoveLevel;

    @DatabaseField
    @JsonProperty("stepNote")
    public String stepNote;

    @DatabaseField
    @JsonProperty("videoSrc")
    public String videoSrc;

    @DatabaseField
    @JsonProperty("video")
    public String video;

    @DatabaseField
    @JsonProperty("dc")
    public String dc;

    @DatabaseField
    @JsonProperty("needPreheat")
    public boolean needPreheat;

    /**
     * 所需时间 (秒)
     */
    @DatabaseField
    @JsonProperty("needTime")
    public int needTime;

    @JsonProperty("stepVideo")
    public String stepVideo;

    @JsonProperty("showType")
    public String showType;
    /**
     * 是否备菜步骤
     */
    @JsonProperty("prepareStep")
    public boolean isPrepareStep;
    public CookStep(){

    }

    /**
     * 根据平台获取对应key 的值
     *
     * @param platCode
     * @param itemname
     * @return
     */
    // -------------------------------------------------------------------------------
    public int getParamByCodeName(String platCode, String itemname) {
        if(Plat.DEBUG){
            LogUtils.i("dcdpdt", platCode);
        }
        if (StringUtils.isNullOrEmpty(itemname))
            return 0;
        if (StringUtils.isNullOrEmpty(platCode)) {
            List<PlatformCode> platformCodes = getjs_PlatformCodes();
            if (platformCodes != null && platformCodes.size() > 0) {
                List<paramCode> paramCodes = platformCodes.get(0).getJs_paramCodes();
                if (paramCodes != null && paramCodes.size() > 0) {
                    for (paramCode code : paramCodes) {
                        if (itemname.equals(code.code)) {
                            return code.value;
                        }
                    }
                }
            }
        } else {

            List<PlatformCode> platformCodes = getjs_PlatformCodes();

            if (platformCodes != null && platformCodes.size() > 0) {
                for (PlatformCode platformCode : platformCodes) {
                    if (!platCode.equals(platformCode.getPlatCode()))
                        continue;
                    List<paramCode> paramCodes = platformCode.getJs_paramCodes();
                    if (paramCodes != null && paramCodes.size() > 0) {
                        for (paramCode code : paramCodes) {
                            if (itemname.equals(code.code)) {
                                return code.value;
                            }
                        }
                    }
                }
            }
        }

        if ("fanGear".equals(itemname)) {
            return fanLevel;
        } else if ("stoveGear".equals(itemname)) {
            return stoveLevel;
        } else if ("needTime".equals(itemname)) {
            return needTime;
        }
        return 0;
    }

    /**
     * 根据平台获取股则参数
     *
     * @param platCode
     * @return
     */
    public List<Rules> getRulesByCodeName(String platCode) {
        if (StringUtils.isNullOrEmpty(platCode)) {
            List<PlatformCode> platformCodes = getjs_PlatformCodes();
            if (platformCodes != null && platformCodes.size() > 0) {
                return platformCodes.get(0).getjs_Rule();
            }
        } else {
            List<PlatformCode> platformCodes = getjs_PlatformCodes();
            if (platformCodes != null && platformCodes.size() > 0) {
                for (PlatformCode platformCode : platformCodes) {
                    if (!platCode.equals(platformCode.getPlatCode()))
                        continue;
                    return platformCode.getjs_Rule();
                }
            }
        }
        return new ArrayList<Rules>();
    }

    @DatabaseField(foreign = true, columnName = COLUMN_BOOK_ID)
    protected Recipe cookbook;

    @ForeignCollectionField
    private ForeignCollection<CookStepTip> db_tips;

    public List<CookStepTip> getJs_tips() {
        if (db_tips != null && db_tips.size() > 0)
            return Lists.newArrayList(db_tips);
        if (js_tips == null)
            js_tips = Lists.newArrayList();
        return js_tips;
    }

    /**
     * 子步骤列表
     */
    @JsonProperty("tips")
    protected List<CookStepTip> js_tips;

    /**
     * 获取设备平台参数
     */
    @ForeignCollectionField(eager = true)
    private ForeignCollection<PlatformCode> db_platformCodes;

    // 周定钧
    public List<PlatformCode> getjs_PlatformCodes() {
        if (db_platformCodes != null && db_platformCodes.size() > 0){
            platformCodes=Lists.newArrayList(db_platformCodes);
            return platformCodes;
        }

        if (platformCodes == null)
            platformCodes = Lists.newArrayList();
        return platformCodes;
    }

    public List<PlatformCode> getPlatformCodes() {
        if(this.platformCodes==null){
            return null;
        }
        if(this.platformCodes instanceof LazyForeignCollection){
            List<PlatformCode> tmp = new ArrayList<PlatformCode>();
            Iterator<PlatformCode> iterator = this.platformCodes.iterator();
            while(iterator.hasNext()){
                PlatformCode car = iterator.next();
                tmp.add(car);
            }
            this.platformCodes = tmp;
        }
        return platformCodes;
    }

    @JsonProperty("params")
    public List<PlatformCode> platformCodes;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return desc;
    }

    public String getDc() {
        return dc;
    }

    public Recipe getParent() {
        return cookbook;
    }

    public boolean getNeedPreHeat(){
        return needPreheat;
    }

    @Override
    public String toString() {
        try {
            return JsonUtils.pojo2Json(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "抛异常了" + e.toString();
        }
    }


    @Override
    public void save2db() {
        super.save2db();
        if (platformCodes != null) {
            for (PlatformCode platformCode : platformCodes) {
                platformCode.cookStep = this;
                platformCode.save2db();
            }
        }
        if (js_tips != null) {
            for (CookStepTip tip : js_tips) {
                tip.cookStep = this;
                tip.save2db();
            }
        }

        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            if (db_platformCodes != null) {
                for (PlatformCode platformCode : db_platformCodes) {
                    platformCode.delete(0);
                }
            }
            if (db_tips != null) {
                for (CookStepTip tip : db_tips) {
                    tip.delete(0);
                }
            }
            super.delete(id);
        } catch (Exception e) {
        }
    }


    /***************
     * 新增方法用于 自动烹饪
     ***************/

    public paramCode getParamByCodeName(String category, String platCode, String itemname) {
        if (StringUtils.isNullOrEmpty(category) || StringUtils.isNullOrEmpty(platCode)
                || StringUtils.isNullOrEmpty(itemname))
            return null;
        List<PlatformCode> platformCodes = getjs_PlatformCodes();
        if (platformCodes != null && platformCodes.size() > 0) {
            for (PlatformCode platformCode : platformCodes) {
                if (!platCode.equals(platformCode.getPlatCode()) || !category.equals(platformCode.getDeviceCategory()))
                    continue;
                List<paramCode> paramCodes = platformCode.getJs_paramCodes();
                if (paramCodes != null && paramCodes.size() > 0) {
                    for (paramCode code : paramCodes) {
                        if (itemname.equals(code.code)) {
                            return code;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getItemType() {
        if ("1".equals(showType)){
            return IMAGE ;
        }else if ("2".equals(showType)){
            return VIDEO;
        }
        return IMAGE;
    }
}