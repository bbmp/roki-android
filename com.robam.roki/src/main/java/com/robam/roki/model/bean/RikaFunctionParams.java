package com.robam.roki.model.bean;

import java.util.List;

public class RikaFunctionParams {
    public String name;
    public List<ModeList> modeList;

    public class ModeList {
        public Mode mode;
    }

    public class Mode {
        public String title;
        public String image;
        public int cmd;
        public Params params;

        public class Params {
            public Dc dc;
            public Fc fc;
            public SetMeum setMeum;
            public WorkTemp workTemp;
            public WorkTime workTime;
            public WorkTempDefault workTempDefault;
            public WorkTimeDefault workTimeDefault;

            public class Fc {
                public String value;
                public String paramType;
            }

            public class Dc {
                public String value;
                public String paramType;
            }

            public class SetMeum {
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

            public class WorkTempDefault {
                public String value;
                public String paramType;
            }

            public class WorkTimeDefault {
                public String value;
                public String paramType;
            }
        }
    }

    public static class MultiParams {
        public String mode;
        public String modeName;
        public List<Integer> timeList;
        public List<Integer> tempList;
        public short defaultTemp;
        public short defaultTime;
    }
}
