package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/4/10.
 */
public class SteamOvenModelFunction620Params {


    /**
     * cmd : 154
     * param : {"model":{"value":"7","paramType":"String"},"setTemp":{"value":[50,250,1],"paramType":"section"},"defaultSetTemp":{"value":"180","paramType":"String"},"setTime":{"value":[5,90,1],"paramType":"section"},"defaultSetTime":{"value":"50","paramType":"String"}}
     */

    public int cmd;
    public ParamBean param;
    public Appoint appoint;


    public static class Appoint{
        public ParamBean.DataList setHour;
        public ParamBean.DataList setMin;
    }
    public static class ParamBean {
        public ModelBean model;
        public DataList  setPowerOrLevel;
        public DefaultData defaultSetPowerOrLevel;
        public DataList  setTime;
        public DefaultData defaultSetTime;
        public DataList  setMinTime;
        public DefaultData defaultSetMinTime;
        public static class ModelBean {
            public String value;
            public String paramType;
        }

        public static class DataList {
            public List<Integer> value;
            public String paramType;
        }
        public static class DefaultData {
            public String value;
            public String paramType;
        }
    }
}
