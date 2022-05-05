package com.robam.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;
import com.robam.common.pojos.PlatformCode;

import java.io.Serializable;

/**
 * 烧菜步骤
 *
 * @author sylar
 */
public class paramCode extends AbsStorePojo<Long> implements Serializable {
    /*****
     * 灶具字段
     ****/
    public final static String STOVE_LEVEL = "stoveGear";
    public final static String FAN_LEVEL = "fanGear";
    public final static String NEED_TIME = "needTime";

    /*****
     * 烤箱字段
     ****/
    public final static String OVEN_MODE = "OvenMode";
    public final static String OVEN_TEMP = "OvenTemp";
    public final static String OVEN_TIME = "OvenTime";
    public final static String OVEN_TEMPUP = "OvenTempUp";
    public final static String OVEN_TEMPBELOW = "OvenTempBelow";
    public final static String OVEN_UPLAYERTEMP = "OvenUpLayerTemp";
    public final static String OVEN_UPLAYERTIME = "OvenUpLayerTime";
    public final static String OVEN_DOWNLAYERTEMP = "OvenDownLayerTemp";
    public final static String OVEN_DOWNLAYERTIME = "OvenDownLayerTime";
    /*****
     * 蒸汽炉字段
     ****/
    public final static String STEAM_TEMP = "SteamTemp";
    public final static String STEAM_TIME = "SteamTime";

    /*****
     * 微波炉字段
     ****/
    public final static String MICRO_MODE = "MicroWaveMode";
    public final static String MICRO_TIME = "MicroWaveTime";
    public final static String MICRO_POWER = "MicroWavePower";
    public final static String MICRO_WEIGHT = "MicroWaveWeight";

    public final static String STEAMOVENONE_MODE = "OvenSteamMode";
    public final static String STEAMOVENONE_TEMP = "OvenSteamTemp";
    public final static String STEAMOVENONE_TIME = "OvenSteamTime";
    public final static String STEAMOVENONE_UP = "OvenSteamUp";
    public final static String STEAMOVENONE_BELOW = "OvenSteamBelow";

    public final static String PARACODE_ID = "paramCode";

    @DatabaseField(generatedId = true)
    private long id;

    /**
     *
     */
    @DatabaseField
    @JsonProperty("code")
    public String code;


    /**
     *
     */
    @DatabaseField
    @JsonProperty("codeName")
    public String codeName;


    /**
     *
     */
    @DatabaseField
    @JsonProperty("value")
    public int value;

    /**
     *
     */
    @DatabaseField
    @JsonProperty("type")
    public int type;

    /**
     *
     */
    @DatabaseField
    @JsonProperty("valueName")
    public String valueName;


    @DatabaseField(foreign = true, columnName = PARACODE_ID)
    public PlatformCode platformCode;


    public paramCode() {

    }

    @Override
    public String getName() {
        return codeName;
    }

    @Override
    public Long getID() {
        return id;
    }


    public int getValue() {
        return value;
    }

    public PlatformCode getParent() {
        return platformCode;
    }
}