package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/4/10.
 */
public class SteamOvenModelFunctionParams {


    /**
     * cmd : 154
     * param : {"model":{"value":"7","paramType":"String"},"setTemp":{"value":[50,250,1],"paramType":"section"},"defaultSetTemp":{"value":"180","paramType":"String"},"setTime":{"value":[5,90,1],"paramType":"section"},"defaultSetTime":{"value":"50","paramType":"String"}}
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
         * model : {"value":"7","paramType":"String"}
         * setTemp : {"value":[50,250,1],"paramType":"section"}
         * defaultSetTemp : {"value":"180","paramType":"String"}
         * setTime : {"value":[5,90,1],"paramType":"section"}
         * defaultSetTime : {"value":"50","paramType":"String"}
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
             * value : 7
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
             * value : 180
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
