package com.robam.roki.model.bean;

/**
 * Created by 14807 on 2018/6/6.
 */

public class FanKitchenCleanParams {


    /**
     * param : {"minute":{"value":1,"paramType":"String"},"power":{"value":1,"paramType":"String"}}
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
         * minute : {"value":1,"paramType":"String"}
         * power : {"value":1,"paramType":"String"}
         */

        private MinuteBean minute;
        private PowerBean power;

        public MinuteBean getMinute() {
            return minute;
        }

        public void setMinute(MinuteBean minute) {
            this.minute = minute;
        }

        public PowerBean getPower() {
            return power;
        }

        public void setPower(PowerBean power) {
            this.power = power;
        }

        public static class MinuteBean {
            /**
             * value : 1
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

        public static class PowerBean {
            /**
             * value : 1
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
