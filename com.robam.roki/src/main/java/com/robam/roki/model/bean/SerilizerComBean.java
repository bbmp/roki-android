//package com.robam.roki.model.bean;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
///**
// * Created by Dell on 2018/11/5.
// */
//
//public class SerilizerComBean {
//
//    /**
//     * cmd : 128
//     * state : 2
//     * setTimeType : 1
//     * setTimeParam : {"1":{"value":"130","paramType":"String"},"2":{"value":[],"paramType":"array"}}
//     */
//
//    private String cmd;
//    private String state;
//    private String setTimeType;
//    private SetTimeParamBean setTimeParam;
//
//    public String getCmd() {
//        return cmd;
//    }
//
//    public void setCmd(String cmd) {
//        this.cmd = cmd;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getSetTimeType() {
//        return setTimeType;
//    }
//
//    public void setSetTimeType(String setTimeType) {
//        this.setTimeType = setTimeType;
//    }
//
//    public SetTimeParamBean getSetTimeParam() {
//        return setTimeParam;
//    }
//
//    public void setSetTimeParam(SetTimeParamBean setTimeParam) {
//        this.setTimeParam = setTimeParam;
//    }
//
//    public static class SetTimeParamBean {
//        /**
//         * 1 : {"value":"130","paramType":"String"}
//         * 2 : {"value":[],"paramType":"array"}
//         */
//
//        @SerializedName("1")
//        private _$1Bean _$1;
//        @SerializedName("2")
//        private _$2Bean _$2;
//
//        public _$1Bean get_$1() {
//            return _$1;
//        }
//
//        public void set_$1(_$1Bean _$1) {
//            this._$1 = _$1;
//        }
//
//        public _$2Bean get_$2() {
//            return _$2;
//        }
//
//        public void set_$2(_$2Bean _$2) {
//            this._$2 = _$2;
//        }
//
//        public static class _$1Bean {
//            /**
//             * value : 130
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
//        public static class _$2Bean {
//            /**
//             * value : []
//             * paramType : array
//             */
//
//            private String paramType;
//            private List<?> value;
//
//            public String getParamType() {
//                return paramType;
//            }
//
//            public void setParamType(String paramType) {
//                this.paramType = paramType;
//            }
//
//            public List<?> getValue() {
//                return value;
//            }
//
//            public void setValue(List<?> value) {
//                this.value = value;
//            }
//        }
//    }
//}
