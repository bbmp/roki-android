package com.robam.roki.model.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Dell on 2018/7/6.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OvenParams{


    /**
     * cmd : 154
     * param : {"model":{"value":"1","paramType":"String"},"setTemp":{"value":[50,250,1],"paramType":"section"},"defaultSetTemp":{"value":"200","paramType":"String"},"setTime":{"value":[5,90,1],"defaultValue":50,"paramType":"section"},"defaultSetTime":{"value":"50","paramType":"String"}}
     */

    private int cmd;
    private String hasRotate;

    public String getHasRotate() {
        return hasRotate;
    }

    public void setHasRotate(String hasRotate) {
        this.hasRotate = hasRotate;
    }

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
         * model : {"value":"1","paramType":"String"}
         * setTemp : {"value":[50,250,1],"paramType":"section"}
         * defaultSetTemp : {"value":"200","paramType":"String"}
         * setTime : {"value":[5,90,1],"defaultValue":50,"paramType":"section"}
         * defaultSetTime : {"value":"50","paramType":"String"}
         * desc : {"value":"适合烤蛋挞、玉米、葱油饼。","paramType":"String"}
         */

        private ModelBean model;
        private SetTempBean setTemp;
        private DefaultSetTempBean defaultSetTemp;
        private SetTimeBean setTime;
        private DefaultSetTimeBean defaultSetTime;

        public ModelBean getModel() {
            return model;
        }

        public void setModel(ModelBean model) {
            this.model = model;
        }

        public SetTempBean getSetTemp() {
            return setTemp;
        }

        public void setSetTemp(SetTempBean setTemp) {
            this.setTemp = setTemp;
        }

        public DefaultSetTempBean getDefaultSetTemp() {
            return defaultSetTemp;
        }

        public void setDefaultSetTemp(DefaultSetTempBean defaultSetTemp) {
            this.defaultSetTemp = defaultSetTemp;
        }

        public SetTimeBean getSetTime() {
            return setTime;
        }

        public void setSetTime(SetTimeBean setTime) {
            this.setTime = setTime;
        }

        public DefaultSetTimeBean getDefaultSetTime() {
            return defaultSetTime;
        }

        public void setDefaultSetTime(DefaultSetTimeBean defaultSetTime) {
            this.defaultSetTime = defaultSetTime;
        }


        public static class ModelBean {
            /**
             * value : 1
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

        public static class SetTempBean {
            /**
             * value : [50,250,1]
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

        public static class DefaultSetTempBean {
            /**
             * value : 200
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

        public static class SetTimeBean {
            /**
             * value : [5,90,1]
             * defaultValue : 50
             * paramType : section
             */

            private int defaultValue;
            private String paramType;
            private List<Integer> value;

            public int getDefaultValue() {
                return defaultValue;
            }

            public void setDefaultValue(int defaultValue) {
                this.defaultValue = defaultValue;
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

        public static class DefaultSetTimeBean {
            /**
             * value : 50
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
