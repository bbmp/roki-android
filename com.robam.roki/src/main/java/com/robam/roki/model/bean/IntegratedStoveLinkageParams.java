package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by 14807 on 2018/5/14.
 */

public class IntegratedStoveLinkageParams {


    public ParamDTO param;

    public static class ParamDTO {
        public MinuteDTO minute;
        public MinuteDefaultDTO minuteDefault;
        public MinuteDefaultDTO title;
        public MinuteDefaultDTO tips;
        public MinuteDefaultDTO desc;

        public static class MinuteDTO {
            public List<Integer> value;
            public String paramType;
        }

        public static class MinuteDefaultDTO {
            public String value;
            public String paramType;

        }
    }
}


