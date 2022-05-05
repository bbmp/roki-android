package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/9/14.
 */

public class MicroWaveHotRiceParams {


    /**
     * numberCompany : {"value":"g","paramType":"String"}
     * setNumber : {"value":[200,600,100],"paramType":"section"}
     * defaultSetNumber : {"value":"200","paramType":"String"}
     * setTime : {"value":[110,270,40],"paramType":"section"}
     * defaultSetTime : {"value":"110","paramType":"String"}
     */

    private NumberCompanyBean numberCompany;
    private SetNumberBean setNumber;
    private DefaultSetNumberBean defaultSetNumber;
    private SetTimeBean setTime;
    private DefaultSetTimeBean defaultSetTime;

    public NumberCompanyBean getNumberCompany() {
        return numberCompany;
    }

    public void setNumberCompany(NumberCompanyBean numberCompany) {
        this.numberCompany = numberCompany;
    }

    public SetNumberBean getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(SetNumberBean setNumber) {
        this.setNumber = setNumber;
    }

    public DefaultSetNumberBean getDefaultSetNumber() {
        return defaultSetNumber;
    }

    public void setDefaultSetNumber(DefaultSetNumberBean defaultSetNumber) {
        this.defaultSetNumber = defaultSetNumber;
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

    public static class NumberCompanyBean {
        /**
         * value : g
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

    public static class SetNumberBean {
        /**
         * value : [200,600,100]
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

    public static class DefaultSetNumberBean {
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
         * value : [110,270,40]
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
         * value : 110
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
