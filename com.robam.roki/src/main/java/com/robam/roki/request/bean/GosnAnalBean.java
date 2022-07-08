package com.robam.roki.request.bean;

public interface GosnAnalBean {
    /**
     * [{\"mode\":7,\"modeName\":\"风焙烤\",\"segNo\":1,\"setDownTemp\":0,\"setTime\":60,\"setUpTemp\":35,\"steamVolume\":0}]
     */
    class DeviceParam {
        public int mode;
        public String modeName;
        public int segNo;
        public String setDownTemp;
        public String setTime;
        public String setUpTemp;
        public String steamVolume;
    }
}
