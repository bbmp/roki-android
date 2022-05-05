package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Dell on 2018/7/10.
 */

public class PengpaiZhengParamBean {


    public int cmd;
    public ParamDTO param;
    public AppointDTO appoint;

    public static class ParamDTO {
        public ModelDTO model;
        public SetTempDTO setTemp;
        public ModelDTO defaultSetTemp;
        public SetTimeDTO setTime;
        public ModelDTO defaultSetTime;
        public SetSteamDTO setSteam;
        public ModelDTO defaultSetSteam;

        public static class ModelDTO {
            public String value;
            public String paramType;
        }

        public static class SetTempDTO {
            public List<Integer> value;
            public String paramType;
        }

        public static class SetTimeDTO {
            public List<Integer> value;
            public String paramType;
        }

        public static class SetSteamDTO {
            public List<String> value;
            public List<String> title;
            public String paramType;
        }
    }

    public static class AppointDTO {
        public SetHourDTO setHour;
        public SetMinDTO setMin;

        public static class SetHourDTO {
            public List<Integer> value;
            public String paramType;
        }

        public static class SetMinDTO {
            public List<Integer> value;
            public String paramType;
        }
    }
}
