package com.robam.roki.model.bean;

import java.util.List;

public class RikaSubParams {
    public Model model;
    public WorkTempDefault workTempDefault;
    public WorkTimeDefault workTimeDefault;
    public WorkTemp workTemp;
    public WorkTime workTime;

    public class Model {
        public String value;
        public String paramType;
    }

    public class WorkTempDefault {
        public String value;
        public String paramType;
    }

    public class WorkTimeDefault {
        public String value;
        public String paramType;
    }

    public class WorkTemp {
        public List<Integer> value;
        public String paramType;
    }

    public class WorkTime {
        public List<Integer> value;
        public String paramType;
    }
}
