package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/4/10.
 */
public class FunctionParams {


    /**
     * cmd : 140
     * params : {"dc":{"value":"RRZQ","paramType":"String"},"fc":{"value":"2","paramType":"String"},"setMeum":{"value":17,"paramType":"String"},"workTemp":{"value":[85,95,1],"paramType":"section"},"workTime":{"value":[20,90,1],"paramType":"section"},"workTempDefault":{"value":90,"paramType":"String"},"workTimeDefault":{"value":30,"paramType":"String"}}
     */

    private int cmd;
    private ParamsBean params;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * dc : {"value":"RRZQ","paramType":"String"}
         * fc : {"value":"2","paramType":"String"}
         * setMeum : {"value":17,"paramType":"String"}
         * workTemp : {"value":[85,95,1],"paramType":"section"}
         * workTime : {"value":[20,90,1],"paramType":"section"}
         * workTempDefault : {"value":90,"paramType":"String"}
         * workTimeDefault : {"value":30,"paramType":"String"}
         */

        private DcBean dc;
        private FcBean fc;
        private SetMeumBean setMeum;
        private WorkTempBean workTemp;
        private WorkTimeBean workTime;
        private WorkTempDefaultBean workTempDefault;
        private WorkTimeDefaultBean workTimeDefault;

        public DcBean getDc() {
            return dc;
        }

        public void setDc(DcBean dc) {
            this.dc = dc;
        }

        public FcBean getFc() {
            return fc;
        }

        public void setFc(FcBean fc) {
            this.fc = fc;
        }

        public SetMeumBean getSetMeum() {
            return setMeum;
        }

        public void setSetMeum(SetMeumBean setMeum) {
            this.setMeum = setMeum;
        }

        public WorkTempBean getWorkTemp() {
            return workTemp;
        }

        public void setWorkTemp(WorkTempBean workTemp) {
            this.workTemp = workTemp;
        }

        public WorkTimeBean getWorkTime() {
            return workTime;
        }

        public void setWorkTime(WorkTimeBean workTime) {
            this.workTime = workTime;
        }

        public WorkTempDefaultBean getWorkTempDefault() {
            return workTempDefault;
        }

        public void setWorkTempDefault(WorkTempDefaultBean workTempDefault) {
            this.workTempDefault = workTempDefault;
        }

        public WorkTimeDefaultBean getWorkTimeDefault() {
            return workTimeDefault;
        }

        public void setWorkTimeDefault(WorkTimeDefaultBean workTimeDefault) {
            this.workTimeDefault = workTimeDefault;
        }

        public static class DcBean {
            /**
             * value : RRZQ
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

        public static class FcBean {
            /**
             * value : 2
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

        public static class SetMeumBean {
            /**
             * value : 17
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

        public static class WorkTempBean {
            /**
             * value : [85,95,1]
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

        public static class WorkTimeBean {
            /**
             * value : [20,90,1]
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

        public static class WorkTempDefaultBean {
            /**
             * value : 90
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

        public static class WorkTimeDefaultBean {
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
