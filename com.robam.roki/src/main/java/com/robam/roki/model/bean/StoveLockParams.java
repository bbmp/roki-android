package com.robam.roki.model.bean;

/**
 * Created by 14807 on 2018/7/24.
 */

public class StoveLockParams {

    /**
     * cmd : 136
     * param : {"power":{"value":"1","paramType":"string"}}
     */

    private String cmd;
    private ParamBean param;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * power : {"value":"1","paramType":"string"}
         */

        private PowerBean power;

        public PowerBean getPower() {
            return power;
        }

        public void setPower(PowerBean power) {
            this.power = power;
        }

        public static class PowerBean {
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
    }
}
