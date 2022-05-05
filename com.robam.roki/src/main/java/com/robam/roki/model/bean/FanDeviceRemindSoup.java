package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/5/3.
 */

public class FanDeviceRemindSoup {


    /**
     * param : {"hour":{"value":[0,1,2,3],"paramType":"array"},"minute":{"value":[0,60,1],"paramType":"section"},"hourDefault":{"value":0,"paramType":"String"},"minuteDefault":{"value":30,"paramType":"String"}}
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
         * hour : {"value":[0,1,2,3],"paramType":"array"}
         * minute : {"value":[0,60,1],"paramType":"section"}
         * hourDefault : {"value":0,"paramType":"String"}
         * minuteDefault : {"value":30,"paramType":"String"}
         */

        private HourBean hour;
        private MinuteBean minute;
        private HourDefaultBean hourDefault;
        private MinuteDefaultBean minuteDefault;

        public HourBean getHour() {
            return hour;
        }

        public void setHour(HourBean hour) {
            this.hour = hour;
        }

        public MinuteBean getMinute() {
            return minute;
        }

        public void setMinute(MinuteBean minute) {
            this.minute = minute;
        }

        public HourDefaultBean getHourDefault() {
            return hourDefault;
        }

        public void setHourDefault(HourDefaultBean hourDefault) {
            this.hourDefault = hourDefault;
        }

        public MinuteDefaultBean getMinuteDefault() {
            return minuteDefault;
        }

        public void setMinuteDefault(MinuteDefaultBean minuteDefault) {
            this.minuteDefault = minuteDefault;
        }

        public static class HourBean {
            /**
             * value : [0,1,2,3]
             * paramType : array
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

        public static class MinuteBean {
            /**
             * value : [0,60,1]
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

        public static class HourDefaultBean {
            /**
             * value : 0
             * paramType : String
             */

            private int value;
            private String paramType;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class MinuteDefaultBean {
            /**
             * value : 30
             * paramType : String
             */

            private int value;
            private String paramType;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
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
