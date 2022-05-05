package com.robam.roki.model.bean;


import java.util.List;

/**
 * Created by 14807 on 2018/5/4.
 */

public class DeviceFanStoveLinkage {


    /**
     * param : {"minute":{"value":[1,9,1],"paramType":"section"},"defaultMinute":{"value":"1","paramType":"string"},"tips":{"value":"当开启灶具时，烟机自动开启","paramType":"String"},"desc":{"value":"当关闭灶具时，烟机延时button分钟关闭灶具调整到最大档时，烟机自动调整为爆炒模式","paramType":"string"}}
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
         * minute : {"value":[1,9,1],"paramType":"section"}
         * defaultMinute : {"value":"1","paramType":"string"}
         * tips : {"value":"当开启灶具时，烟机自动开启","paramType":"String"}
         * desc : {"value":"当关闭灶具时，烟机延时button分钟关闭灶具调整到最大档时，烟机自动调整为爆炒模式","paramType":"string"}
         */

        private MinuteBean minute;
        private DefaultMinuteBean defaultMinute;
        private TipsBean tips;
        private TitleBean title;
        private DescBean desc;


        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
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

        public static class MinuteBean {
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

        public static class DefaultMinuteBean {
            /**
             * value : 1
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
             * value : 当开启灶具时，烟机自动开启
             * paramType : String
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


        public static class TitleBean{
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
             * value : 当关闭灶具时，烟机延时button分钟关闭灶具调整到最大档时，烟机自动调整为爆炒模式
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
