package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/31.
 */

public class DuiOvenSetBean {


    /**
     * intentName : sys.确认
     * skillId : 2019072600000387
     * temp : 50
     * time : 00:05:00
     * mode : 1
     * nlu : {"input":"确认","version":"2019.1.15.20:40:58","skillVersion":"13","skillId":"2019072600000387","skill":"烤箱控制技能","res":"5d43a25b39105b000d042315","timestamp":1564801327,"pinyin":"que ren","source":"dui","inittime":5.30712890625,"semantics":{"request":{"slots":[{"name":"intent","value":"sys.确认"}],"confidence":1,"task":"烤箱专业模式","slotcount":1}},"loadtime":1.85595703125,"systime":2.694091796875}
     */
    private String selectNo;
    private String intentName;
    private String skillId;
    private String temp;
    private String time;
    private String mode;
    private String bottomTemp;
    private NluBean nlu;

    public String getBottomTemp() {
        return bottomTemp;
    }

    public void setBottomTemp(String bottomTemp) {
        this.bottomTemp = bottomTemp;
    }

    public String getSelectNo() {
        return selectNo;
    }

    public void setSelectNo(String selectNo) {
        this.selectNo = selectNo;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
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
         * skillVersion : 13
         * skillId : 2019072600000387
         * skill : 烤箱控制技能
         * res : 5d43a25b39105b000d042315
         * timestamp : 1564801327
         * pinyin : que ren
         * source : dui
         * inittime : 5.30712890625
         * semantics : {"request":{"slots":[{"name":"intent","value":"sys.确认"}],"confidence":1,"task":"烤箱专业模式","slotcount":1}}
         * loadtime : 1.85595703125
         * systime : 2.694091796875
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
             * request : {"slots":[{"name":"intent","value":"sys.确认"}],"confidence":1,"task":"烤箱专业模式","slotcount":1}
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
                 * task : 烤箱专业模式
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
