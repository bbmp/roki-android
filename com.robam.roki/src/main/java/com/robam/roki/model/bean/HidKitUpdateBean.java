package com.robam.roki.model.bean;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/11/10.
 * @PS:
 */
public class HidKitUpdateBean {


    /**
     * version : {"value":"3","paramType":"String"}
     * desc : {"value":"1、新增8236s控制语音指;2、修复高温蒸无法读取的问题;3、优化产品稳定性","paramType":"String"}
     */

    private VersionBean version;
    private DescBean desc;

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public DescBean getDesc() {
        return desc;
    }

    public void setDesc(DescBean desc) {
        this.desc = desc;
    }

    public static class VersionBean {
        /**
         * value : 3
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

    public static class DescBean {
        /**
         * value : 1、新增8236s控制语音指;2、修复高温蒸无法读取的问题;3、优化产品稳定性
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
