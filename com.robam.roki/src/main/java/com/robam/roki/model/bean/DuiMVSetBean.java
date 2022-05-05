package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/8/5.
 */

public class DuiMVSetBean {

    /**
     * time : 00:10:00
     * intentName : sys.确认
     * power : 7
     * skillId : 2019073100000074
     * selectNo : 1
     * mode : 51
     * nlu : {"input":"确认","version":"2019.1.15.20:40:58","skillVersion":"6","skillId":"2019073100000074","skill":"微波炉技能","res":"5d43acc185d53e000d19ff1d","timestamp":1564994388,"pinyin":"que ren","source":"dui","inittime":0,"semantics":{"request":{"slots":[{"name":"intent","value":"sys.确认"}],"confidence":1,"task":"微波炉专业模式","slotcount":1}},"loadtime":0,"systime":0.403076171875}
     */

    private String time;
    private String intentName;
    private String power;
    private String skillId;
    private String selectNo;
    private String mode;
    private NluBean nlu;

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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSelectNo() {
        return selectNo;
    }

    public void setSelectNo(String selectNo) {
        this.selectNo = selectNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public NluBean getNlu() {
        return nlu;
    }

    public void setNlu(NluBean nlu) {
        this.nlu = nlu;
    }

    public static class NluBean {
        /**
         * input : 确认
         * version : 2019.1.15.20:40:58
         * skillVersion : 6
         * skillId : 2019073100000074
         * skill : 微波炉技能
         * res : 5d43acc185d53e000d19ff1d
         * timestamp : 1564994388
         * pinyin : que ren
         * source : dui
         * inittime : 0
         * semantics : {"request":{"slots":[{"name":"intent","value":"sys.确认"}],"confidence":1,"task":"微波炉专业模式","slotcount":1}}
         * loadtime : 0
         * systime : 0.403076171875
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
        private int inittime;
        private SemanticsBean semantics;
        private int loadtime;
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

        public int getInittime() {
            return inittime;
        }

        public void setInittime(int inittime) {
            this.inittime = inittime;
        }

        public SemanticsBean getSemantics() {
            return semantics;
        }

        public void setSemantics(SemanticsBean semantics) {
            this.semantics = semantics;
        }

        public int getLoadtime() {
            return loadtime;
        }

        public void setLoadtime(int loadtime) {
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
             * request : {"slots":[{"name":"intent","value":"sys.确认"}],"confidence":1,"task":"微波炉专业模式","slotcount":1}
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
                 * slots : [{"name":"intent","value":"sys.确认"}]
                 * confidence : 1
                 * task : 微波炉专业模式
                 * slotcount : 1
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
                     * name : intent
                     * value : sys.确认
                     */

                    private String name;
                    private String value;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }
                }
            }
        }
    }
}
