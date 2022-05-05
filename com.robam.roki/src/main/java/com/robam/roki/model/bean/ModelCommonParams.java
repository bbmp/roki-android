package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/9/29.
 */

public class ModelCommonParams {


    /**
     * cmd : 134
     * model : 9
     * numberCompany : {"value":"g","paramType":"String"}
     * setNumber : {"value":[100,400,100],"paramType":"section"}
     * defaultSetNumber : {"value":"100","paramType":"String"}
     */

    private String cmd;
    private String model;
    private NumberCompanyBean numberCompany;
    private SetNumberBean setNumber;
    private DefaultSetNumberBean defaultSetNumber;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

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
         * value : [100,400,100]
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
         * value : 100
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
