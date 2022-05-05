package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/7/3.
 */

public class StoveTimingParams{

    /**
     * cmd : 134
     * param : {"off":{"value":[1,99,1],"heardId":"1","paramType":"section"},"defaultmin":{"value":"30","paramType":"string"}}
     */

    private int cmd;
    private ParamBean param;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
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
         * off : {"value":[1,99,1],"heardId":"1","paramType":"section"}
         * defaultmin : {"value":"30","paramType":"string"}
         */

        private OffBean off;
        private DefaultminBean defaultmin;

        public OffBean getOff() {
            return off;
        }

        public void setOff(OffBean off) {
            this.off = off;
        }

        public DefaultminBean getDefaultmin() {
            return defaultmin;
        }

        public void setDefaultmin(DefaultminBean defaultmin) {
            this.defaultmin = defaultmin;
        }

        public static class OffBean {
            /**
             * value : [1,99,1]
             * heardId : 1
             * paramType : section
             */

            private String heardId;
            private String paramType;
            private List<Integer> value;

            public String getHeardId() {
                return heardId;
            }

            public void setHeardId(String heardId) {
                this.heardId = heardId;
            }

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

        public static class DefaultminBean {
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
    }
}
