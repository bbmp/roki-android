package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/7/20.
 */

public class StoveBurnWithoutWaterParams {


    /**
     * param : {"gears":[{"title":"5档火力工作button分钟关火","button":"设置","minuteDefault":"20","minutePlush":"10","gear":5,"paramType":"String"},{"title":"4档火力工作button分钟关火","button":"设置","minuteDefault":"60","minutePlush":"20","gear":4,"paramType":"String"},{"title":"3档火力工作button分钟关火","button":"设置","minuteDefault":"90","minutePlush":"40","gear":3,"paramType":"String"},{"title":"2档火力工作button分钟关火","button":"设置","minuteDefault":"120","minutePlush":"60","gear":2,"paramType":"String"},{"title":"1档火力工作button分钟关火","button":"设置","minuteDefault":"180","minutePlush":"80","gear":1,"paramType":"String"}],"minute":{"value":[1,180,1],"paramType":"section"},"desc":{"value":"防干烧功能是为了防止空锅干烧，保护厨房安全；每个火力档位的默认工作时间是专业的实验测试结果；当定时关火开启时，优先执行定时关火设定的时间；","paramType":"string"}}
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
         * gears : [{"title":"5档火力工作button分钟关火","button":"设置","minuteDefault":"20","minutePlush":"10","gear":5,"paramType":"String"},{"title":"4档火力工作button分钟关火","button":"设置","minuteDefault":"60","minutePlush":"20","gear":4,"paramType":"String"},{"title":"3档火力工作button分钟关火","button":"设置","minuteDefault":"90","minutePlush":"40","gear":3,"paramType":"String"},{"title":"2档火力工作button分钟关火","button":"设置","minuteDefault":"120","minutePlush":"60","gear":2,"paramType":"String"},{"title":"1档火力工作button分钟关火","button":"设置","minuteDefault":"180","minutePlush":"80","gear":1,"paramType":"String"}]
         * minute : {"value":[1,180,1],"paramType":"section"}
         * desc : {"value":"防干烧功能是为了防止空锅干烧，保护厨房安全；每个火力档位的默认工作时间是专业的实验测试结果；当定时关火开启时，优先执行定时关火设定的时间；","paramType":"string"}
         */

        private MinuteBean minute;
        private DescBean desc;
        private List<GearsBean> gears;

        public MinuteBean getMinute() {
            return minute;
        }

        public void setMinute(MinuteBean minute) {
            this.minute = minute;
        }

        public DescBean getDesc() {
            return desc;
        }

        public void setDesc(DescBean desc) {
            this.desc = desc;
        }

        public List<GearsBean> getGears() {
            return gears;
        }

        public void setGears(List<GearsBean> gears) {
            this.gears = gears;
        }

        public static class MinuteBean {
            /**
             * value : [1,180,1]
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

        public static class DescBean {
            /**
             * value : 防干烧功能是为了防止空锅干烧，保护厨房安全；每个火力档位的默认工作时间是专业的实验测试结果；当定时关火开启时，优先执行定时关火设定的时间；
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

        public static class GearsBean {
            /**
             * title : 5档火力工作button分钟关火
             * button : 设置
             * minuteDefault : 20
             * minutePlush : 10
             * gear : 5
             * paramType : String
             */

            private String title;
            private String button;
            private String minuteDefault;
            private String minutePlush;
            private int gear;
            private String paramType;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getButton() {
                return button;
            }

            public void setButton(String button) {
                this.button = button;
            }

            public String getMinuteDefault() {
                return minuteDefault;
            }

            public void setMinuteDefault(String minuteDefault) {
                this.minuteDefault = minuteDefault;
            }

            public String getMinutePlush() {
                return minutePlush;
            }

            public void setMinutePlush(String minutePlush) {
                this.minutePlush = minutePlush;
            }

            public int getGear() {
                return gear;
            }

            public void setGear(int gear) {
                this.gear = gear;
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
