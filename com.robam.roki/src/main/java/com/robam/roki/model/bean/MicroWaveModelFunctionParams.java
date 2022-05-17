//package com.robam.roki.model.bean;
//
//import java.util.List;
//
///**
// * Created by 14807 on 2018/4/10.
// */
//public class MicroWaveModelFunctionParams {
//
//
//    /**
//     * cmd : 140
//     * param : {"model":{"value":"50","paramType":"String"},"minuteTime":{"value":[0,30,1],"paramType":"section"},"secondTime":{"value":[0,55,5],"paramType":"section"},"minuteTimeDefault":{"value":"1","paramType":"Steing"},"secondTimeDefault":{"value":"0","paramType":"section"},"power":{"value":[1,6,1],"paramType":"section"},"powerDefault":{"value":"1","paramType":"String"}}
//     * title : {"value":"使食物均匀加热，越大越厚的食物需选用越高的火力。","paramType":"Steing"}
//     */
//
//    private int cmd;
//    private ParamBean param;
//    private TitleBean title;
//
//    public int getCmd() {
//        return cmd;
//    }
//
//    public void setCmd(int cmd) {
//        this.cmd = cmd;
//    }
//
//    public ParamBean getParam() {
//        return param;
//    }
//
//    public void setParam(ParamBean param) {
//        this.param = param;
//    }
//
//    public TitleBean getTitle() {
//        return title;
//    }
//
//    public void setTitle(TitleBean title) {
//        this.title = title;
//    }
//
//    public static class ParamBean {
//        /**
//         * model : {"value":"50","paramType":"String"}
//         * minuteTime : {"value":[0,30,1],"paramType":"section"}
//         * secondTime : {"value":[0,55,5],"paramType":"section"}
//         * minuteTimeDefault : {"value":"1","paramType":"Steing"}
//         * secondTimeDefault : {"value":"0","paramType":"section"}
//         * power : {"value":[1,6,1],"paramType":"section"}
//         * powerDefault : {"value":"1","paramType":"String"}
//         */
//
//        private ModelBean model;
//        private MinuteTimeBean minuteTime;
//        private SecondTimeBean secondTime;
//        private MinuteTimeDefaultBean minuteTimeDefault;
//        private SecondTimeDefaultBean secondTimeDefault;
//        private PowerBean power;
//        private PowerDefaultBean powerDefault;
//
//        public ModelBean getModel() {
//            return model;
//        }
//
//        public void setModel(ModelBean model) {
//            this.model = model;
//        }
//
//        public MinuteTimeBean getMinuteTime() {
//            return minuteTime;
//        }
//
//        public void setMinuteTime(MinuteTimeBean minuteTime) {
//            this.minuteTime = minuteTime;
//        }
//
//        public SecondTimeBean getSecondTime() {
//            return secondTime;
//        }
//
//        public void setSecondTime(SecondTimeBean secondTime) {
//            this.secondTime = secondTime;
//        }
//
//        public MinuteTimeDefaultBean getMinuteTimeDefault() {
//            return minuteTimeDefault;
//        }
//
//        public void setMinuteTimeDefault(MinuteTimeDefaultBean minuteTimeDefault) {
//            this.minuteTimeDefault = minuteTimeDefault;
//        }
//
//        public SecondTimeDefaultBean getSecondTimeDefault() {
//            return secondTimeDefault;
//        }
//
//        public void setSecondTimeDefault(SecondTimeDefaultBean secondTimeDefault) {
//            this.secondTimeDefault = secondTimeDefault;
//        }
//
//        public PowerBean getPower() {
//            return power;
//        }
//
//        public void setPower(PowerBean power) {
//            this.power = power;
//        }
//
//        public PowerDefaultBean getPowerDefault() {
//            return powerDefault;
//        }
//
//        public void setPowerDefault(PowerDefaultBean powerDefault) {
//            this.powerDefault = powerDefault;
//        }
//
//        public static class ModelBean {
//            /**
//             * value : 50
//             * paramType : String
//             */
//
//            private String value;
//            private String paramType;
//
//            public String getValue() {
//                return value;
//            }
//
//            public void setValue(String value) {
//                this.value = value;
//            }
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//        }
//
//        public static class MinuteTimeBean {
//            /**
//             * value : [0,30,1]
//             * paramType : section
//             */
//
//            private String paramType;
//            private List<Integer> value;
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//
//            public List<Integer> getValue() {
//                return value;
//            }
//
//            public void setValue(List<Integer> value) {
//                this.value = value;
//            }
//        }
//
//        public static class SecondTimeBean {
//            /**
//             * value : [0,55,5]
//             * paramType : section
//             */
//
//            private String paramType;
//            private List<Integer> value;
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//
//            public List<Integer> getValue() {
//                return value;
//            }
//
//            public void setValue(List<Integer> value) {
//                this.value = value;
//            }
//        }
//
//        public static class MinuteTimeDefaultBean {
//            /**
//             * value : 1
//             * paramType : Steing
//             */
//
//            private String value;
//            private String paramType;
//
//            public String getValue() {
//                return value;
//            }
//
//            public void setValue(String value) {
//                this.value = value;
//            }
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//        }
//
//        public static class SecondTimeDefaultBean {
//            /**
//             * value : 0
//             * paramType : section
//             */
//
//            private String value;
//            private String paramType;
//
//            public String getValue() {
//                return value;
//            }
//
//            public void setValue(String value) {
//                this.value = value;
//            }
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//        }
//
//        public static class PowerBean {
//            /**
//             * value : [1,6,1]
//             * paramType : section
//             */
//
//            private String paramType;
//            private List<Integer> value;
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//
//            public List<Integer> getValue() {
//                return value;
//            }
//
//            public void setValue(List<Integer> value) {
//                this.value = value;
//            }
//        }
//
//        public static class PowerDefaultBean {
//            /**
//             * value : 1
//             * paramType : String
//             */
//
//            private String value;
//            private String paramType;
//
//            public String getValue() {
//                return value;
//            }
//
//            public void setValue(String value) {
//                this.value = value;
//            }
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//        }
//    }
//
//    public static class TitleBean {
//        /**
//         * value : 使食物均匀加热，越大越厚的食物需选用越高的火力。
//         * paramType : Steing
//         */
//
//        private String value;
//        private String paramType;
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//        public String getParamType() {
//            return paramType;
//        }
//
//        public void setParamType(String paramType) {
//            this.paramType = paramType;
//        }
//    }
//}
