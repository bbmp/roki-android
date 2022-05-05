package com.robam.roki.model.bean;

import java.util.List;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/13.
 * PS: Not easy to write code, please indicate.
 */
public class RikaAppointmentParams {


    /**
     * params : {"disinfection":{"workModelName":{"value":"消毒","paramType":"String"},"workModel":{"value":"11","paramType":"String"},"workTime":{"value":[110,130,1],"paramType":"section"},"workTimeDef":{"value":"130","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}},"dry":{"workModelName":{"value":"烘干","paramType":"String"},"workModel":{"value":12,"paramType":"String"},"workTime":{"value":[40,60,1],"paramType":"section"},"workTimeDef":{"value":"60","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}},"quickCleaning":{"workModelName":{"value":"快速保洁","paramType":"String"},"workModel":{"value":13,"paramType":"String"},"workTime":{"value":[60,60,1],"paramType":"section"},"workTimeDef":{"value":"60","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}}}
     * paramsDef : {"disinfection":{"workModelName":{"value":"消毒","paramType":"String"},"workModel":{"value":"11","paramType":"String"},"workTime":{"value":[110,30,1],"paramType":"section"},"workTimeDef":{"value":"130","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}}}
     */

    private ParamsBean params;
    private ParamsDefBean paramsDef;

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public ParamsDefBean getParamsDef() {
        return paramsDef;
    }

    public void setParamsDef(ParamsDefBean paramsDef) {
        this.paramsDef = paramsDef;
    }

    public static class ParamsBean {
        /**
         * disinfection : {"workModelName":{"value":"消毒","paramType":"String"},"workModel":{"value":"11","paramType":"String"},"workTime":{"value":[110,130,1],"paramType":"section"},"workTimeDef":{"value":"130","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}}
         * dry : {"workModelName":{"value":"烘干","paramType":"String"},"workModel":{"value":12,"paramType":"String"},"workTime":{"value":[40,60,1],"paramType":"section"},"workTimeDef":{"value":"60","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}}
         * quickCleaning : {"workModelName":{"value":"快速保洁","paramType":"String"},"workModel":{"value":13,"paramType":"String"},"workTime":{"value":[60,60,1],"paramType":"section"},"workTimeDef":{"value":"60","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}}
         */

        private DisinfectionBean disinfection;
        private DryBean dry;
        private QuickCleaningBean quickCleaning;

        public DisinfectionBean getDisinfection() {
            return disinfection;
        }

        public void setDisinfection(DisinfectionBean disinfection) {
            this.disinfection = disinfection;
        }

        public DryBean getDry() {
            return dry;
        }

        public void setDry(DryBean dry) {
            this.dry = dry;
        }

        public QuickCleaningBean getQuickCleaning() {
            return quickCleaning;
        }

        public void setQuickCleaning(QuickCleaningBean quickCleaning) {
            this.quickCleaning = quickCleaning;
        }

        public static class DisinfectionBean {
            /**
             * workModelName : {"value":"消毒","paramType":"String"}
             * workModel : {"value":"11","paramType":"String"}
             * workTime : {"value":[110,130,1],"paramType":"section"}
             * workTimeDef : {"value":"130","paramType":"string"}
             * waitTime : {"value":[1,24,1],"paramType":"section"}
             * waitTimeDef : {"value":"1","paramType":"string"}
             */

            private WorkModelNameBean workModelName;
            private WorkModelBean workModel;
            private WorkTimeBean workTime;
            private WorkTimeDefBean workTimeDef;
            private WaitTimeBean waitTime;
            private WaitTimeDefBean waitTimeDef;

            public WorkModelNameBean getWorkModelName() {
                return workModelName;
            }

            public void setWorkModelName(WorkModelNameBean workModelName) {
                this.workModelName = workModelName;
            }

            public WorkModelBean getWorkModel() {
                return workModel;
            }

            public void setWorkModel(WorkModelBean workModel) {
                this.workModel = workModel;
            }

            public WorkTimeBean getWorkTime() {
                return workTime;
            }

            public void setWorkTime(WorkTimeBean workTime) {
                this.workTime = workTime;
            }

            public WorkTimeDefBean getWorkTimeDef() {
                return workTimeDef;
            }

            public void setWorkTimeDef(WorkTimeDefBean workTimeDef) {
                this.workTimeDef = workTimeDef;
            }

            public WaitTimeBean getWaitTime() {
                return waitTime;
            }

            public void setWaitTime(WaitTimeBean waitTime) {
                this.waitTime = waitTime;
            }

            public WaitTimeDefBean getWaitTimeDef() {
                return waitTimeDef;
            }

            public void setWaitTimeDef(WaitTimeDefBean waitTimeDef) {
                this.waitTimeDef = waitTimeDef;
            }

            public static class WorkModelNameBean {
                /**
                 * value : 消毒
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

            public static class WorkModelBean {
                /**
                 * value : 11
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

            public static class WorkTimeBean {
                /**
                 * value : [110,130,1]
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

            public static class WorkTimeDefBean {
                /**
                 * value : 130
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

            public static class WaitTimeBean {
                /**
                 * value : [1,24,1]
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

            public static class WaitTimeDefBean {
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

        public static class DryBean {
            /**
             * workModelName : {"value":"烘干","paramType":"String"}
             * workModel : {"value":12,"paramType":"String"}
             * workTime : {"value":[40,60,1],"paramType":"section"}
             * workTimeDef : {"value":"60","paramType":"string"}
             * waitTime : {"value":[1,24,1],"paramType":"section"}
             * waitTimeDef : {"value":"1","paramType":"string"}
             */

            private WorkModelNameBeanX workModelName;
            private WorkModelBeanX workModel;
            private WorkTimeBeanX workTime;
            private WorkTimeDefBeanX workTimeDef;
            private WaitTimeBeanX waitTime;
            private WaitTimeDefBeanX waitTimeDef;

            public WorkModelNameBeanX getWorkModelName() {
                return workModelName;
            }

            public void setWorkModelName(WorkModelNameBeanX workModelName) {
                this.workModelName = workModelName;
            }

            public WorkModelBeanX getWorkModel() {
                return workModel;
            }

            public void setWorkModel(WorkModelBeanX workModel) {
                this.workModel = workModel;
            }

            public WorkTimeBeanX getWorkTime() {
                return workTime;
            }

            public void setWorkTime(WorkTimeBeanX workTime) {
                this.workTime = workTime;
            }

            public WorkTimeDefBeanX getWorkTimeDef() {
                return workTimeDef;
            }

            public void setWorkTimeDef(WorkTimeDefBeanX workTimeDef) {
                this.workTimeDef = workTimeDef;
            }

            public WaitTimeBeanX getWaitTime() {
                return waitTime;
            }

            public void setWaitTime(WaitTimeBeanX waitTime) {
                this.waitTime = waitTime;
            }

            public WaitTimeDefBeanX getWaitTimeDef() {
                return waitTimeDef;
            }

            public void setWaitTimeDef(WaitTimeDefBeanX waitTimeDef) {
                this.waitTimeDef = waitTimeDef;
            }

            public static class WorkModelNameBeanX {
                /**
                 * value : 烘干
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

            public static class WorkModelBeanX {
                /**
                 * value : 12
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

            public static class WorkTimeBeanX {
                /**
                 * value : [40,60,1]
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

            public static class WorkTimeDefBeanX {
                /**
                 * value : 60
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

            public static class WaitTimeBeanX {
                /**
                 * value : [1,24,1]
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

            public static class WaitTimeDefBeanX {
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

        public static class QuickCleaningBean {
            /**
             * workModelName : {"value":"快速保洁","paramType":"String"}
             * workModel : {"value":13,"paramType":"String"}
             * workTime : {"value":[60,60,1],"paramType":"section"}
             * workTimeDef : {"value":"60","paramType":"string"}
             * waitTime : {"value":[1,24,1],"paramType":"section"}
             * waitTimeDef : {"value":"1","paramType":"string"}
             */

            private WorkModelNameBeanXX workModelName;
            private WorkModelBeanXX workModel;
            private WorkTimeBeanXX workTime;
            private WorkTimeDefBeanXX workTimeDef;
            private WaitTimeBeanXX waitTime;
            private WaitTimeDefBeanXX waitTimeDef;

            public WorkModelNameBeanXX getWorkModelName() {
                return workModelName;
            }

            public void setWorkModelName(WorkModelNameBeanXX workModelName) {
                this.workModelName = workModelName;
            }

            public WorkModelBeanXX getWorkModel() {
                return workModel;
            }

            public void setWorkModel(WorkModelBeanXX workModel) {
                this.workModel = workModel;
            }

            public WorkTimeBeanXX getWorkTime() {
                return workTime;
            }

            public void setWorkTime(WorkTimeBeanXX workTime) {
                this.workTime = workTime;
            }

            public WorkTimeDefBeanXX getWorkTimeDef() {
                return workTimeDef;
            }

            public void setWorkTimeDef(WorkTimeDefBeanXX workTimeDef) {
                this.workTimeDef = workTimeDef;
            }

            public WaitTimeBeanXX getWaitTime() {
                return waitTime;
            }

            public void setWaitTime(WaitTimeBeanXX waitTime) {
                this.waitTime = waitTime;
            }

            public WaitTimeDefBeanXX getWaitTimeDef() {
                return waitTimeDef;
            }

            public void setWaitTimeDef(WaitTimeDefBeanXX waitTimeDef) {
                this.waitTimeDef = waitTimeDef;
            }

            public static class WorkModelNameBeanXX {
                /**
                 * value : 快速保洁
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

            public static class WorkModelBeanXX {
                /**
                 * value : 13
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

            public static class WorkTimeBeanXX {
                /**
                 * value : [60,60,1]
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

            public static class WorkTimeDefBeanXX {
                /**
                 * value : 60
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

            public static class WaitTimeBeanXX {
                /**
                 * value : [1,24,1]
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

            public static class WaitTimeDefBeanXX {
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

    public static class ParamsDefBean {
        /**
         * disinfection : {"workModelName":{"value":"消毒","paramType":"String"},"workModel":{"value":"11","paramType":"String"},"workTime":{"value":[110,30,1],"paramType":"section"},"workTimeDef":{"value":"130","paramType":"string"},"waitTime":{"value":[1,24,1],"paramType":"section"},"waitTimeDef":{"value":"1","paramType":"string"}}
         */

        private DisinfectionBeanX disinfection;

        public DisinfectionBeanX getDisinfection() {
            return disinfection;
        }

        public void setDisinfection(DisinfectionBeanX disinfection) {
            this.disinfection = disinfection;
        }

        public static class DisinfectionBeanX {
            /**
             * workModelName : {"value":"消毒","paramType":"String"}
             * workModel : {"value":"11","paramType":"String"}
             * workTime : {"value":[110,30,1],"paramType":"section"}
             * workTimeDef : {"value":"130","paramType":"string"}
             * waitTime : {"value":[1,24,1],"paramType":"section"}
             * waitTimeDef : {"value":"1","paramType":"string"}
             */

            private WorkModelNameBeanXXX workModelName;
            private WorkModelBeanXXX workModel;
            private WorkTimeBeanXXX workTime;
            private WorkTimeDefBeanXXX workTimeDef;
            private WaitTimeBeanXXX waitTime;
            private WaitTimeDefBeanXXX waitTimeDef;

            public WorkModelNameBeanXXX getWorkModelName() {
                return workModelName;
            }

            public void setWorkModelName(WorkModelNameBeanXXX workModelName) {
                this.workModelName = workModelName;
            }

            public WorkModelBeanXXX getWorkModel() {
                return workModel;
            }

            public void setWorkModel(WorkModelBeanXXX workModel) {
                this.workModel = workModel;
            }

            public WorkTimeBeanXXX getWorkTime() {
                return workTime;
            }

            public void setWorkTime(WorkTimeBeanXXX workTime) {
                this.workTime = workTime;
            }

            public WorkTimeDefBeanXXX getWorkTimeDef() {
                return workTimeDef;
            }

            public void setWorkTimeDef(WorkTimeDefBeanXXX workTimeDef) {
                this.workTimeDef = workTimeDef;
            }

            public WaitTimeBeanXXX getWaitTime() {
                return waitTime;
            }

            public void setWaitTime(WaitTimeBeanXXX waitTime) {
                this.waitTime = waitTime;
            }

            public WaitTimeDefBeanXXX getWaitTimeDef() {
                return waitTimeDef;
            }

            public void setWaitTimeDef(WaitTimeDefBeanXXX waitTimeDef) {
                this.waitTimeDef = waitTimeDef;
            }

            public static class WorkModelNameBeanXXX {
                /**
                 * value : 消毒
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

            public static class WorkModelBeanXXX {
                /**
                 * value : 11
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

            public static class WorkTimeBeanXXX {
                /**
                 * value : [110,30,1]
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

            public static class WorkTimeDefBeanXXX {
                /**
                 * value : 130
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

            public static class WaitTimeBeanXXX {
                /**
                 * value : [1,24,1]
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

            public static class WaitTimeDefBeanXXX {
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
}
