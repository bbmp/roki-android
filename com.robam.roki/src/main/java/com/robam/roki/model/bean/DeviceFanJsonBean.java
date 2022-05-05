package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/1/24.
 */

public class DeviceFanJsonBean {

    /**
     * deviceCategory : RYYJ
     * deviceType : R8700
     * viewBkimg : url
     * title : 8700
     */
    private String deviceCategory;
    private String deviceType;
    private String viewBkimg;
    private String title;
    private BackgroundFuncBean backgroundFunc;
    private MainFuncBean mainFunc;
    private OtherFuncBean otherFunc;

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getViewBkimg() {
        return viewBkimg;
    }

    public void setViewBkimg(String viewBkimg) {
        this.viewBkimg = viewBkimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BackgroundFuncBean getBackgroundFunc() {
        return backgroundFunc;
    }

    public void setBackgroundFunc(BackgroundFuncBean backgroundFunc) {
        this.backgroundFunc = backgroundFunc;
    }

    public MainFuncBean getMainFunc() {
        return mainFunc;
    }

    public void setMainFunc(MainFuncBean mainFunc) {
        this.mainFunc = mainFunc;
    }

    public OtherFuncBean getOtherFunc() {
        return otherFunc;
    }

    public void setOtherFunc(OtherFuncBean otherFunc) {
        this.otherFunc = otherFunc;
    }

    public static class BackgroundFuncBean {

        private String modelBkimg;
        private List<DeviceFunctionsBean> deviceFunctions;

        public String getModelBkimg() {
            return modelBkimg;
        }

        public void setModelBkimg(String modelBkimg) {
            this.modelBkimg = modelBkimg;
        }

        public List<DeviceFunctionsBean> getDeviceFunctions() {
            return deviceFunctions;
        }

        public void setDeviceFunctions(List<DeviceFunctionsBean> deviceFunctions) {
            this.deviceFunctions = deviceFunctions;
        }

        public static class DeviceFunctionsBean {
            /**
             * functionCode : checkValveState
             * functionName : 止回阀状态
             * Bkimg :
             * functionType : displayOnly
             * Params : {"cmd":"136","param":{"light":{"name":"灯开关","value":[0,1],"paramType":"array"}}}
             */

            private String functionCode;
            private String functionName;
            private String Bkimg;
            private String functionType;
            private ParamsBean Params;

            public String getFunctionCode() {
                return functionCode;
            }

            public void setFunctionCode(String functionCode) {
                this.functionCode = functionCode;
            }

            public String getFunctionName() {
                return functionName;
            }

            public void setFunctionName(String functionName) {
                this.functionName = functionName;
            }

            public String getBkimg() {
                return Bkimg;
            }

            public void setBkimg(String Bkimg) {
                this.Bkimg = Bkimg;
            }

            public String getFunctionType() {
                return functionType;
            }

            public void setFunctionType(String functionType) {
                this.functionType = functionType;
            }

            public ParamsBean getParams() {
                return Params;
            }

            public void setParams(ParamsBean Params) {
                this.Params = Params;
            }

            public static class ParamsBean {
                /**
                 * cmd : 136
                 * param : {"light":{"name":"灯开关","value":[0,1],"paramType":"array"}}
                 */
                private String cmd;
                private ParamBean param;

                public String getCmd() {
                    return cmd;
                }

                public void setCmd(String cmd) {
                    this.cmd = cmd;
                }

                public ParamBean getParam() {
                    return param;
                }

                public void setParam(ParamBean param) {
                    this.param = param;
                }

                public static class ParamBean {
                    /**
                     * light : {"name":"灯开关","value":[0,1],"paramType":"array"}
                     */

                    private LightBean light;

                    public LightBean getLight() {
                        return light;
                    }

                    public void setLight(LightBean light) {
                        this.light = light;
                    }

                    public static class LightBean {
                        /**
                         * name : 灯开关
                         * value : [0,1]
                         * paramType : array
                         */

                        private String name;
                        private String paramType;
                        private List<Integer> value;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

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
    }

    public static class MainFuncBean {
        private List<DeviceFunctionsBeanX> deviceFunctions;

        public List<DeviceFunctionsBeanX> getDeviceFunctions() {
            return deviceFunctions;
        }

        public void setDeviceFunctions(List<DeviceFunctionsBeanX> deviceFunctions) {
            this.deviceFunctions = deviceFunctions;
        }

        public static class DeviceFunctionsBeanX {
            /**
             * functionCode : fry
             * functionName : 炒
             * functionType : clickable
             * bkimg : url圆圈
             * Params : {"cmd":"134","param":{"gear":{"value":"1","name":"档位","paramType":"String"}}}
             */

            private String functionCode;
            private String functionName;
            private String functionType;
            private String bkimg;
            private ParamsBeanX Params;

            public String getFunctionCode() {
                return functionCode;
            }

            public void setFunctionCode(String functionCode) {
                this.functionCode = functionCode;
            }

            public String getFunctionName() {
                return functionName;
            }

            public void setFunctionName(String functionName) {
                this.functionName = functionName;
            }

            public String getFunctionType() {
                return functionType;
            }

            public void setFunctionType(String functionType) {
                this.functionType = functionType;
            }

            public String getBkimg() {
                return bkimg;
            }

            public void setBkimg(String bkimg) {
                this.bkimg = bkimg;
            }

            public ParamsBeanX getParams() {
                return Params;
            }

            public void setParams(ParamsBeanX Params) {
                this.Params = Params;
            }

            public static class ParamsBeanX {
                /**
                 * cmd : 134
                 * param : {"gear":{"value":"1","name":"档位","paramType":"String"}}
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
                     * gear : {"value":"1","name":"档位","paramType":"String"}
                     */

                    private GearBean gear;

                    public GearBean getGear() {
                        return gear;
                    }

                    public void setGear(GearBean gear) {
                        this.gear = gear;
                    }

                    public static class GearBean {

                        private String value;
                        private String name;
                        private String paramType;

                        public String getValue() {
                            return value;
                        }

                        public void setValue(String value) {
                            this.value = value;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
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
    }

    public static class OtherFuncBean {
        private List<DeviceFunctionsBeanXXX> deviceFunctions;

        public List<DeviceFunctionsBeanXXX> getDeviceFunctions() {
            return deviceFunctions;
        }

        public void setDeviceFunctions(List<DeviceFunctionsBeanXXX> deviceFunctions) {
            this.deviceFunctions = deviceFunctions;
        }

        public static class DeviceFunctionsBeanXXX {
            private String functionCode;
            private String functionName;
            private String functionType;
            private String subViewName;
            private SubViewBean subView;
            private ParamsBeanXXX Params;

            public String getFunctionCode() {
                return functionCode;
            }

            public void setFunctionCode(String functionCode) {
                this.functionCode = functionCode;
            }

            public String getFunctionName() {
                return functionName;
            }

            public void setFunctionName(String functionName) {
                this.functionName = functionName;
            }

            public String getFunctionType() {
                return functionType;
            }

            public void setFunctionType(String functionType) {
                this.functionType = functionType;
            }

            public String getSubViewName() {
                return subViewName;
            }

            public void setSubViewName(String subViewName) {
                this.subViewName = subViewName;
            }

            public SubViewBean getSubView() {
                return subView;
            }

            public void setSubView(SubViewBean subView) {
                this.subView = subView;
            }

            public ParamsBeanXXX getParams() {
                return Params;
            }

            public void setParams(ParamsBeanXXX Params) {
                this.Params = Params;
            }

            public static class SubViewBean {
                /**
                 * text : 文字描述
                 * deviceFunctions : [{"functionCode":"samllCooking","functionName":"小厨房","subViewName":"RYYJ_alert_myslef","Params":{"param":{"minute":{"value":"1","name":"分钟","paramType":"String"}}}},{"functionCode":"bigCooking","functionName":"大厨房","subViewName":"RYYJ_alert_myslef","Params":{"param":{"minute":{"value":"3","name":"分钟","paramType":"String"}}}},{"functionCode":"openCooking","functionName":"开放式厨房","subViewName":"RYYJ_alert_myslef","Params":{"param":{"minute":{"value":"5","name":"分钟","paramType":"String"}}}},{"functionCode":"timeReminding","functionName":"计时提醒","subViewName":"RYYJ_alert_timeView","Params":{"param":{"minute":{"value":"[0,59,1]","name":"分钟","paramType":"section"},"hour":{"value":"[0,1,2,3]","name":"小时","paramType":"array"}}}}]
                 */

                private String text;
                private List<DeviceFunctionsBeanXX> deviceFunctions;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public List<DeviceFunctionsBeanXX> getDeviceFunctions() {
                    return deviceFunctions;
                }

                public void setDeviceFunctions(List<DeviceFunctionsBeanXX> deviceFunctions) {
                    this.deviceFunctions = deviceFunctions;
                }

                public static class DeviceFunctionsBeanXX {
                    /**
                     * functionCode : samllCooking
                     * functionName : 小厨房
                     * subViewName : RYYJ_alert_myslef
                     * Params : {"param":{"minute":{"value":"1","name":"分钟","paramType":"String"}}}
                     */

                    private String functionCode;
                    private String functionName;
                    private String subViewName;
                    private ParamsBeanXX Params;

                    public String getFunctionCode() {
                        return functionCode;
                    }

                    public void setFunctionCode(String functionCode) {
                        this.functionCode = functionCode;
                    }

                    public String getFunctionName() {
                        return functionName;
                    }

                    public void setFunctionName(String functionName) {
                        this.functionName = functionName;
                    }

                    public String getSubViewName() {
                        return subViewName;
                    }

                    public void setSubViewName(String subViewName) {
                        this.subViewName = subViewName;
                    }

                    public ParamsBeanXX getParams() {
                        return Params;
                    }

                    public void setParams(ParamsBeanXX Params) {
                        this.Params = Params;
                    }

                    public static class ParamsBeanXX {

                        private ParamBeanXX param;

                        public ParamBeanXX getParam() {
                            return param;
                        }

                        public void setParam(ParamBeanXX param) {
                            this.param = param;
                        }

                        public static class ParamBeanXX {

                            private MinuteBean minute;

                            public MinuteBean getMinute() {
                                return minute;
                            }

                            public void setMinute(MinuteBean minute) {
                                this.minute = minute;
                            }

                            public static class MinuteBean {

                                private List<Integer> value;
                                private String name;
                                private String paramType;

                                public List<Integer> getValue() {
                                    return value;
                                }

                                public void setValue(List<Integer> value) {
                                    this.value = value;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getParamType() {
                                    return paramType;
                                }

                                public void setParamType(String paramType) {
                                    this.paramType = paramType;
                                }
                            }

                            private HourBean hour;

                            public HourBean getHour() {
                                return hour;
                            }

                            public void setHour(HourBean hour) {
                                this.hour = hour;
                            }
                            public static class HourBean {
                                private List<Integer> value;
                                private String name;
                                private String paramType;

                                public List<Integer> getValue() {
                                    return value;
                                }

                                public void setValue(List<Integer> value) {
                                    this.value = value;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
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
            }

            public static class ParamsBeanXXX {
                /**
                 * param : {"week":{"value":["周一","周二","周三","周四","周五","周六","周日"],"name":"星期","paramType":"array"},
                 * "day":{"value":[1,2,3,4,5,6,7,8,9,10],"name":"天","paramType":"array"},
                 * "time":{"value":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24],
                 * "name":"时间","paramType":"array"}}
                 */

                private ParamBeanXXX param;

                public ParamBeanXXX getParam() {
                    return param;
                }

                public void setParam(ParamBeanXXX param) {
                    this.param = param;
                }

                public static class ParamBeanXXX {
                    /**
                     * week : {"value":["周一","周二","周三","周四","周五","周六","周日"],"name":"星期","paramType":"array"}
                     * day : {"value":[1,2,3,4,5,6,7,8,9,10],"name":"天","paramType":"array"}
                     * time : {"value":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24],"name":"时间","paramType":"array"}
                     */

                    private WeekBean week;
                    private DayBean day;
                    private TimeBean time;

                    public WeekBean getWeek() {
                        return week;
                    }

                    public void setWeek(WeekBean week) {
                        this.week = week;
                    }

                    public DayBean getDay() {
                        return day;
                    }

                    public void setDay(DayBean day) {
                        this.day = day;
                    }

                    public TimeBean getTime() {
                        return time;
                    }

                    public void setTime(TimeBean time) {
                        this.time = time;
                    }

                    public static class WeekBean {
                        /**
                         * value : ["周一","周二","周三","周四","周五","周六","周日"]
                         * name : 星期
                         * paramType : array
                         */

                        private String name;
                        private String paramType;
                        private List<String> value;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getParamType() {
                            return paramType;
                        }

                        public void setParamType(String paramType) {
                            this.paramType = paramType;
                        }

                        public List<String> getValue() {
                            return value;
                        }

                        public void setValue(List<String> value) {
                            this.value = value;
                        }
                    }

                    public static class DayBean {
                        /**
                         * value : [1,2,3,4,5,6,7,8,9,10]
                         * name : 天
                         * paramType : array
                         */

                        private String name;
                        private String paramType;
                        private List<Integer> value;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

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

                    public static class TimeBean {
                        /**
                         * value : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]
                         * name : 时间
                         * paramType : array
                         */

                        private String name;
                        private String paramType;
                        private List<Integer> value;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

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
    }
}
