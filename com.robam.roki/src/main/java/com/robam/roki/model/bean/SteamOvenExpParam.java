package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Dell on 2018/7/10.
 */
public class SteamOvenExpParam {


    /**
     * param : {"cmd":{"value":154,"paramType":"int"},"model":{"value":"10","paramType":"String"},"upTemp":{"value":[100,200,1],"paramType":"section"},"upTempDefault":{"value":"160","paramType":"String"},"downTemp":{"value":[100,200,1],"paramType":"section"},"downTempDefault":{"value":"140","paramType":"String"},"minute":{"value":[5,90,1],"paramType":"String"},"minuteDefault":{"value":"20","paramType":"String"},"tempDiff":{"value":"20","paramType":"String"},"tempStart":{"value":"80","paramType":"String"},"tempMin":{"value":"20","paramType":"String"},"desc":{"value":"该功能可自行控制上下层加热温度，满足客户多样化烘烤及口感。","paramType":"String"}}
     */

    private ParamBean param;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * cmd : {"value":154,"paramType":"int"}
         * model : {"value":"10","paramType":"String"}
         * upTemp : {"value":[100,200,1],"paramType":"section"}
         * upTempDefault : {"value":"160","paramType":"String"}
         * downTemp : {"value":[100,200,1],"paramType":"section"}
         * downTempDefault : {"value":"140","paramType":"String"}
         * minute : {"value":[5,90,1],"paramType":"String"}
         * minuteDefault : {"value":"20","paramType":"String"}
         * tempDiff : {"value":"20","paramType":"String"}
         * tempStart : {"value":"80","paramType":"String"}
         * tempMin : {"value":"20","paramType":"String"}
         * desc : {"value":"该功能可自行控制上下层加热温度，满足客户多样化烘烤及口感。","paramType":"String"}
         */

        private CmdBean cmd;
        private ModelBean model;
        private UpTempBean upTemp;
        private UpTempDefaultBean upTempDefault;
        private DownTempBean downTemp;
        private DownTempDefaultBean downTempDefault;
        private MinuteBean minute;
        private MinuteDefaultBean minuteDefault;
        private TempDiffBean tempDiff;
        private TempStartBean tempStart;
        private TempMinBean tempMin;
        private DescBean desc;

        public CmdBean getCmd() {
            return cmd;
        }

        public void setCmd(CmdBean cmd) {
            this.cmd = cmd;
        }

        public ModelBean getModel() {
            return model;
        }

        public void setModel(ModelBean model) {
            this.model = model;
        }

        public UpTempBean getUpTemp() {
            return upTemp;
        }

        public void setUpTemp(UpTempBean upTemp) {
            this.upTemp = upTemp;
        }

        public UpTempDefaultBean getUpTempDefault() {
            return upTempDefault;
        }

        public void setUpTempDefault(UpTempDefaultBean upTempDefault) {
            this.upTempDefault = upTempDefault;
        }

        public DownTempBean getDownTemp() {
            return downTemp;
        }

        public void setDownTemp(DownTempBean downTemp) {
            this.downTemp = downTemp;
        }

        public DownTempDefaultBean getDownTempDefault() {
            return downTempDefault;
        }

        public void setDownTempDefault(DownTempDefaultBean downTempDefault) {
            this.downTempDefault = downTempDefault;
        }

        public MinuteBean getMinute() {
            return minute;
        }

        public void setMinute(MinuteBean minute) {
            this.minute = minute;
        }

        public MinuteDefaultBean getMinuteDefault() {
            return minuteDefault;
        }

        public void setMinuteDefault(MinuteDefaultBean minuteDefault) {
            this.minuteDefault = minuteDefault;
        }

        public TempDiffBean getTempDiff() {
            return tempDiff;
        }

        public void setTempDiff(TempDiffBean tempDiff) {
            this.tempDiff = tempDiff;
        }

        public TempStartBean getTempStart() {
            return tempStart;
        }

        public void setTempStart(TempStartBean tempStart) {
            this.tempStart = tempStart;
        }

        public TempMinBean getTempMin() {
            return tempMin;
        }

        public void setTempMin(TempMinBean tempMin) {
            this.tempMin = tempMin;
        }

        public DescBean getDesc() {
            return desc;
        }

        public void setDesc(DescBean desc) {
            this.desc = desc;
        }

        public static class CmdBean {
            /**
             * value : 154
             * paramType : int
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

        public static class ModelBean {
            /**
             * value : 10
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

        public static class UpTempBean {
            /**
             * value : [100,200,1]
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

        public static class UpTempDefaultBean {
            /**
             * value : 160
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

        public static class DownTempBean {
            /**
             * value : [100,200,1]
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

        public static class DownTempDefaultBean {
            /**
             * value : 140
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

        public static class MinuteBean {
            /**
             * value : [5,90,1]
             * paramType : String
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

        public static class MinuteDefaultBean {
            /**
             * value : 20
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

        public static class TempDiffBean {
            /**
             * value : 20
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

        public static class TempStartBean {
            /**
             * value : 80
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

        public static class TempMinBean {
            /**
             * value : 20
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
             * value : 该功能可自行控制上下层加热温度，满足客户多样化烘烤及口感。
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
