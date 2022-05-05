package com.robam.roki.model.bean;


import java.util.List;

/**
 * Created by 14807 on 2018/5/4.
 */

public class DevicePotVentilationParams {


    /**
     * param : {"tips":{"value":"烟锅联动","paramType":"String"},"desc":{"value":"当启用智能锅时，系统将自动开启烟机；当智能锅识别到温度变化时，系统将自动切换合适的烟机档位哦。","paramType":"string"}}
     * params : {"cmd":"144","param":{"onOff":{"value":[0,1],"paramType":"array"}}}
     */

    private ParamBean param;
    private ParamsBean params;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamBean {
        /**
         * tips : {"value":"烟锅联动","paramType":"String"}
         * desc : {"value":"当启用智能锅时，系统将自动开启烟机；当智能锅识别到温度变化时，系统将自动切换合适的烟机档位哦。","paramType":"string"}
         */

        private TipsBean tips;
        private DescBean desc;

        public TipsBean getTips() {
            return tips;
        }

        public void setTips(TipsBean tips) {
            this.tips = tips;
        }

        public DescBean getDesc() {
            return desc;
        }

        public void setDesc(DescBean desc) {
            this.desc = desc;
        }

        public static class TipsBean {
            /**
             * value : 烟锅联动
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
             * value : 当启用智能锅时，系统将自动开启烟机；当智能锅识别到温度变化时，系统将自动切换合适的烟机档位哦。
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

    public static class ParamsBean {
        /**
         * cmd : 144
         * param : {"onOff":{"value":[0,1],"paramType":"array"}}
         */

        private String cmd;
        private ParamBeanX param;

        public String getCmd() {
            return cmd;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
        }

        public ParamBeanX getParam() {
            return param;
        }

        public void setParam(ParamBeanX param) {
            this.param = param;
        }

        public static class ParamBeanX {
            /**
             * onOff : {"value":[0,1],"paramType":"array"}
             */

            private OnOffBean onOff;

            public OnOffBean getOnOff() {
                return onOff;
            }

            public void setOnOff(OnOffBean onOff) {
                this.onOff = onOff;
            }

            public static class OnOffBean {
                /**
                 * value : [0,1]
                 * paramType : array
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
        }
    }
}
