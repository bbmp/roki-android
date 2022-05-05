package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/24.
 */

public class DuiSteamTempTimeSetBean {


    /**
     * selectNo : 2
     * skillId : 2019061800000219
     * temp : 100
     * time : 00:20:00
     * intentName : 蒸箱选择
     * nlu : {"input":"第二个","version":"2019.1.15.20:40:58","skillVersion":"4","skillId":"2019061800000219","skill":"蒸箱控制技能","res":"5d36df4431c682000d90c8a9","timestamp":1563954143,"pinyin":"di er ge","source":"dui","inittime":26.72216796875,"semantics":{"request":{"slots":[{"rawvalue":"第二个","rawpinyin":"di er ge","value":"2","name":"selectNo","pos":[0,2]},{"name":"intent","value":"蒸箱选择"}],"confidence":1,"task":"专业模式设置","slotcount":2}},"loadtime":8.202880859375,"systime":16.680908203125}
     */

    private String selectNo;
    private String skillId;
    private String temp;
    private String time;
    private String intentName;
    private NluBean nlu;

    public String getSelectNo() {
        return selectNo;
    }

    public void setSelectNo(String selectNo) {
        this.selectNo = selectNo;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public NluBean getNlu() {
        return nlu;
    }

    public void setNlu(NluBean nlu) {
        this.nlu = nlu;
    }

    public static class NluBean {
        /**
         * input : 第二个
         * version : 2019.1.15.20:40:58
         * skillVersion : 4
         * skillId : 2019061800000219
         * skill : 蒸箱控制技能
         * res : 5d36df4431c682000d90c8a9
         * timestamp : 1563954143
         * pinyin : di er ge
         * source : dui
         * inittime : 26.72216796875
         * semantics : {"request":{"slots":[{"rawvalue":"第二个","rawpinyin":"di er ge","value":"2","name":"selectNo","pos":[0,2]},{"name":"intent","value":"蒸箱选择"}],"confidence":1,"task":"专业模式设置","slotcount":2}}
         * loadtime : 8.202880859375
         * systime : 16.680908203125
         */

        private String input;
        private String version;
        private String skillVersion;
        private String skillId;
        private String skill;
        private String res;
        private int timestamp;
        private String pinyin;
        private String source;
        private double inittime;
        private SemanticsBean semantics;
        private double loadtime;
        private double systime;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSkillVersion() {
            return skillVersion;
        }

        public void setSkillVersion(String skillVersion) {
            this.skillVersion = skillVersion;
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public double getInittime() {
            return inittime;
        }

        public void setInittime(double inittime) {
            this.inittime = inittime;
        }

        public SemanticsBean getSemantics() {
            return semantics;
        }

        public void setSemantics(SemanticsBean semantics) {
            this.semantics = semantics;
        }

        public double getLoadtime() {
            return loadtime;
        }

        public void setLoadtime(double loadtime) {
            this.loadtime = loadtime;
        }

        public double getSystime() {
            return systime;
        }

        public void setSystime(double systime) {
            this.systime = systime;
        }

        public static class SemanticsBean {
            /**
             * request : {"slots":[{"rawvalue":"第二个","rawpinyin":"di er ge","value":"2","name":"selectNo","pos":[0,2]},{"name":"intent","value":"蒸箱选择"}],"confidence":1,"task":"专业模式设置","slotcount":2}
             */

            private RequestBean request;

            public RequestBean getRequest() {
                return request;
            }

            public void setRequest(RequestBean request) {
                this.request = request;
            }

            public static class RequestBean {
                /**
                 * slots : [{"rawvalue":"第二个","rawpinyin":"di er ge","value":"2","name":"selectNo","pos":[0,2]},{"name":"intent","value":"蒸箱选择"}]
                 * confidence : 1
                 * task : 专业模式设置
                 * slotcount : 2
                 */

                private int confidence;
                private String task;
                private int slotcount;
                private List<SlotsBean> slots;

                public int getConfidence() {
                    return confidence;
                }

                public void setConfidence(int confidence) {
                    this.confidence = confidence;
                }

                public String getTask() {
                    return task;
                }

                public void setTask(String task) {
                    this.task = task;
                }

                public int getSlotcount() {
                    return slotcount;
                }

                public void setSlotcount(int slotcount) {
                    this.slotcount = slotcount;
                }

                public List<SlotsBean> getSlots() {
                    return slots;
                }

                public void setSlots(List<SlotsBean> slots) {
                    this.slots = slots;
                }

                public static class SlotsBean {
                    /**
                     * rawvalue : 第二个
                     * rawpinyin : di er ge
                     * value : 2
                     * name : selectNo
                     * pos : [0,2]
                     */

                    private String rawvalue;
                    private String rawpinyin;
                    private String value;
                    private String name;
                    private List<Integer> pos;

                    public String getRawvalue() {
                        return rawvalue;
                    }

                    public void setRawvalue(String rawvalue) {
                        this.rawvalue = rawvalue;
                    }

                    public String getRawpinyin() {
                        return rawpinyin;
                    }

                    public void setRawpinyin(String rawpinyin) {
                        this.rawpinyin = rawpinyin;
                    }

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

                    public List<Integer> getPos() {
                        return pos;
                    }

                    public void setPos(List<Integer> pos) {
                        this.pos = pos;
                    }
                }
            }
        }
    }
}
