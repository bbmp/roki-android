package com.robam.roki.ui.activity3.device.dishwasher.bean;

import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ParamBean {

    public List<WorkDesDTO> work_des;
    public List<AssistFuncDTO> assist_func;
    public int model;

    public static class WorkDesDTO {
        public String functionName;
        public String title;
        public String value;
        public String unit;
        public String icon;
    }

    public static class AssistFuncDTO {
        public String functionName;
        public String title;
        public String value;
    }
}
