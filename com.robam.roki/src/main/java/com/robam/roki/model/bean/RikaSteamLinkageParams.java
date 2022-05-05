package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/5/14.
 */

public class RikaSteamLinkageParams {

    /**
     * param : {"title":{"value":"开启后，烟机会与灶具联动，省心又省电。","paramType":"String"},"dec":{"value":"当开启灶具时，根据灶具炉头，烟机对应自动开启不同风量；当关闭灶具时，烟机延时1分钟关闭。","paramType":"String"}}
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
         * title : {"value":"开启后，烟机会与灶具联动，省心又省电。","paramType":"String"}
         * dec : {"value":"当开启灶具时，根据灶具炉头，烟机对应自动开启不同风量；当关闭灶具时，烟机延时1分钟关闭。","paramType":"String"}
         */

        private TitleBean title;
        private DecBean dec;

        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }

        public DecBean getDec() {
            return dec;
        }

        public void setDec(DecBean dec) {
            this.dec = dec;
        }

        public static class TitleBean {
            /**
             * value : 开启后，烟机会与灶具联动，省心又省电。
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

        public static class DecBean {
            /**
             * value : 当开启灶具时，根据灶具炉头，烟机对应自动开启不同风量；当关闭灶具时，烟机延时1分钟关闭。
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
    }

}


