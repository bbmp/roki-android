package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/5/29.
 */

public class RikaCleaningDisinfectionParams {


    /**
     * cmd : 140
     * params : {"numberCatrgory":{"value":1,"paramType":"String"},"categoryCode":{"value":67,"paramType":"String"},"argumentNumber":{"value":1,"paramType":"String"},"key":{"value":49,"paramType":"String"},"keyLength":{"value":4,"paramType":"String"},"workModel":{"value":2,"paramType":"String"},"workTimeDef":{"value":130,"paramType":"String"},"workTime":{"value":[110,130,1],"paramType":"section"},"warmDishTemp":{"value":0,"paramType":"String"}}
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
         * numberCatrgory : {"value":1,"paramType":"String"}
         * categoryCode : {"value":67,"paramType":"String"}
         * argumentNumber : {"value":1,"paramType":"String"}
         * key : {"value":49,"paramType":"String"}
         * keyLength : {"value":4,"paramType":"String"}
         * workModel : {"value":2,"paramType":"String"}
         * workTimeDef : {"value":130,"paramType":"String"}
         * workTime : {"value":[110,130,1],"paramType":"section"}
         * warmDishTemp : {"value":0,"paramType":"String"}
         */

        private NumberCatrgoryBean numberCatrgory;
        private CategoryCodeBean categoryCode;
        private ArgumentNumberBean argumentNumber;
        private KeyBean key;
        private KeyLengthBean keyLength;
        private WorkModelBean workModel;
        private WorkTimeDefBean workTimeDef;
        private WorkTimeBean workTime;
        private WarmDishTempBean warmDishTemp;

        public NumberCatrgoryBean getNumberCatrgory() {
            return numberCatrgory;
        }

        public void setNumberCatrgory(NumberCatrgoryBean numberCatrgory) {
            this.numberCatrgory = numberCatrgory;
        }

        public CategoryCodeBean getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(CategoryCodeBean categoryCode) {
            this.categoryCode = categoryCode;
        }

        public ArgumentNumberBean getArgumentNumber() {
            return argumentNumber;
        }

        public void setArgumentNumber(ArgumentNumberBean argumentNumber) {
            this.argumentNumber = argumentNumber;
        }

        public KeyBean getKey() {
            return key;
        }

        public void setKey(KeyBean key) {
            this.key = key;
        }

        public KeyLengthBean getKeyLength() {
            return keyLength;
        }

        public void setKeyLength(KeyLengthBean keyLength) {
            this.keyLength = keyLength;
        }

        public WorkModelBean getWorkModel() {
            return workModel;
        }

        public void setWorkModel(WorkModelBean workModel) {
            this.workModel = workModel;
        }

        public WorkTimeDefBean getWorkTimeDef() {
            return workTimeDef;
        }

        public void setWorkTimeDef(WorkTimeDefBean workTimeDef) {
            this.workTimeDef = workTimeDef;
        }

        public WorkTimeBean getWorkTime() {
            return workTime;
        }

        public void setWorkTime(WorkTimeBean workTime) {
            this.workTime = workTime;
        }

        public WarmDishTempBean getWarmDishTemp() {
            return warmDishTemp;
        }

        public void setWarmDishTemp(WarmDishTempBean warmDishTemp) {
            this.warmDishTemp = warmDishTemp;
        }

        public static class NumberCatrgoryBean {
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

        public static class CategoryCodeBean {
            /**
             * value : 67
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

        public static class ArgumentNumberBean {
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

        public static class KeyBean {
            /**
             * value : 49
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

        public static class KeyLengthBean {
            /**
             * value : 4
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

        public static class WorkModelBean {
            /**
             * value : 2
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

        public static class WorkTimeDefBean {
            /**
             * value : 130
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

        public static class WarmDishTempBean {
            /**
             * value : 0
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
