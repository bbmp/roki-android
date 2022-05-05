package com.robam.roki.model.bean;


import java.util.List;

/**
 * Created by 14807 on 2018/5/4.
 */

public class DeviceFanVentilation {


    /**
     * param : {"day":{"value":[1,9,1],"paramType":"section"},"defaultDay":{"value":"3","paramType":"string"},"week":{"value":["周一","周二","周三","周四","周五","周六","周日"],"paramType":"array"},"defaultWeek":{"value":"周一","paramType":"string"},"hour":{"value":[0,23,1],"paramType":"section"},"defaultHour":{"value":"12","paramType":"string"},"minute":{"value":[0,59,1],"paramType":"section"},"defaultMinute":{"value":"30","paramType":"string"},"stirFriedMinute":{"value":[1,9,1],"paramType":"string"},"defaultStirFriedMinute":{"value":"3","paramType":"string"},"title":{"value":"button天未使用烟机,自动开启换气","paramType":"string"},"tips":{"value":"每周固buttonbutton开启烟机3分钟自动换气","paramType":"string"},"desc":{"value":"延长变频爆炒时间button分钟","paramType":"string"}}
     */

    private ParamBean param;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * day : {"value":[1,9,1],"paramType":"section"}
         * defaultDay : {"value":"3","paramType":"string"}
         * week : {"value":["周一","周二","周三","周四","周五","周六","周日"],"paramType":"array"}
         * defaultWeek : {"value":"周一","paramType":"string"}
         * hour : {"value":[0,23,1],"paramType":"section"}
         * defaultHour : {"value":"12","paramType":"string"}
         * minute : {"value":[0,59,1],"paramType":"section"}
         * defaultMinute : {"value":"30","paramType":"string"}
         * stirFriedMinute : {"value":[1,9,1],"paramType":"string"}
         * defaultStirFriedMinute : {"value":"3","paramType":"string"}
         * title : {"value":"button天未使用烟机,自动开启换气","paramType":"string"}
         * tips : {"value":"每周固buttonbutton开启烟机3分钟自动换气","paramType":"string"}
         * desc : {"value":"延长变频爆炒时间button分钟","paramType":"string"}
         */

        private DayBean day;
        private DefaultDayBean defaultDay;
        private WeekBean week;
        private DefaultWeekBean defaultWeek;
        private HourBean hour;
        private DefaultHourBean defaultHour;
        private MinuteBean minute;
        private DefaultMinuteBean defaultMinute;
        private StirFriedMinuteBean stirFriedMinute;
        private DefaultStirFriedMinuteBean defaultStirFriedMinute;
        private TitleBean title;
        private TipsBean tips;
        private DescBean desc;

        public DayBean getDay() {
            return day;
        }

        public void setDay(DayBean day) {
            this.day = day;
        }

        public DefaultDayBean getDefaultDay() {
            return defaultDay;
        }

        public void setDefaultDay(DefaultDayBean defaultDay) {
            this.defaultDay = defaultDay;
        }

        public WeekBean getWeek() {
            return week;
        }

        public void setWeek(WeekBean week) {
            this.week = week;
        }

        public DefaultWeekBean getDefaultWeek() {
            return defaultWeek;
        }

        public void setDefaultWeek(DefaultWeekBean defaultWeek) {
            this.defaultWeek = defaultWeek;
        }

        public HourBean getHour() {
            return hour;
        }

        public void setHour(HourBean hour) {
            this.hour = hour;
        }

        public DefaultHourBean getDefaultHour() {
            return defaultHour;
        }

        public void setDefaultHour(DefaultHourBean defaultHour) {
            this.defaultHour = defaultHour;
        }

        public MinuteBean getMinute() {
            return minute;
        }

        public void setMinute(MinuteBean minute) {
            this.minute = minute;
        }

        public DefaultMinuteBean getDefaultMinute() {
            return defaultMinute;
        }

        public void setDefaultMinute(DefaultMinuteBean defaultMinute) {
            this.defaultMinute = defaultMinute;
        }

        public StirFriedMinuteBean getStirFriedMinute() {
            return stirFriedMinute;
        }

        public void setStirFriedMinute(StirFriedMinuteBean stirFriedMinute) {
            this.stirFriedMinute = stirFriedMinute;
        }

        public DefaultStirFriedMinuteBean getDefaultStirFriedMinute() {
            return defaultStirFriedMinute;
        }

        public void setDefaultStirFriedMinute(DefaultStirFriedMinuteBean defaultStirFriedMinute) {
            this.defaultStirFriedMinute = defaultStirFriedMinute;
        }

        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }

        public TipsBean getTips() {
            return tips;
        }

        public void setTips(TipsBean tips) {
            this.tips = tips;
        }

        public DescBean getDesc() {
            return desc;
        }

        public void setDesc(DescBean desc) {
            this.desc = desc;
        }

        public static class DayBean {
            /**
             * value : [1,9,1]
             * paramType : section
             */

            private String paramType;
            private List<Integer> value;

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }

            public List<Integer> getValue() {
                return value;
            }

            public void setValue(List<Integer> value) {
                this.value = value;
            }
        }

        public static class DefaultDayBean {
            /**
             * value : 3
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class WeekBean {
            /**
             * value : ["周一","周二","周三","周四","周五","周六","周日"]
             * paramType : array
             */

            private String paramType;
            private List<String> value;

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }

            public List<String> getValue() {
                return value;
            }

            public void setValue(List<String> value) {
                this.value = value;
            }
        }

        public static class DefaultWeekBean {
            /**
             * value : 周一
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class HourBean {
            /**
             * value : [0,23,1]
             * paramType : section
             */

            private String paramType;
            private List<Integer> value;

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }

            public List<Integer> getValue() {
                return value;
            }

            public void setValue(List<Integer> value) {
                this.value = value;
            }
        }

        public static class DefaultHourBean {
            /**
             * value : 12
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class MinuteBean {
            /**
             * value : [0,59,1]
             * paramType : section
             */

            private String paramType;
            private List<Integer> value;

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }

            public List<Integer> getValue() {
                return value;
            }

            public void setValue(List<Integer> value) {
                this.value = value;
            }
        }

        public static class DefaultMinuteBean {
            /**
             * value : 30
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class StirFriedMinuteBean {
            /**
             * value : [1,9,1]
             * paramType : string
             */

            private String paramType;
            private List<Integer> value;

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }

            public List<Integer> getValue() {
                return value;
            }

            public void setValue(List<Integer> value) {
                this.value = value;
            }
        }

        public static class DefaultStirFriedMinuteBean {
            /**
             * value : 3
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class TitleBean {
            /**
             * value : button天未使用烟机,自动开启换气
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class TipsBean {
            /**
             * value : 每周固buttonbutton开启烟机3分钟自动换气
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class DescBean {
            /**
             * value : 延长变频爆炒时间button分钟
             * paramType : string
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }
    }
}
