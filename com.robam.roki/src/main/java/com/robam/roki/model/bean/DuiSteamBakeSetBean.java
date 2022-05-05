package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/9/2.
 */

public class DuiSteamBakeSetBean {

    /**
     * intentName : sys.确认
     * skillId : 2019082900000542
     * temp : 100
     * time : 00:10:00
     * mode : 1
     * nlu : {"input":"确认","version":"2019.1.15.20:40:58","skillVersion":"5","skillId":"2019082900000542","skill":"蒸烤一体机控制技能","res":"5d6c6bfbd5fdd7000d154d99","timestamp":1567390636,"pinyin":"que ren","source":"dui","inittime":17.123046875,"semantics":{"request":{"confidence":1,"slotcount":1,"slots":[{"name":"intent","value":"sys.确认"}],"task":"一体机专业模式"}},"loadtime":1.506103515625,"systime":2.59375}
     */

    private String intentName;
    private String skillId;
    private String temp;
    private String time;
    private String mode;
    private String selectNo;
    private String bottomTemp;

    private NluBean nlu;

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


    public String getBottomTemp() {
        return bottomTemp;
    }

    public void setBottomTemp(String bottomTemp) {
        this.bottomTemp = bottomTemp;
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
         * skillVersion : 5
         * skillId : 2019082900000542
         * skill : 蒸烤一体机控制技能
         * res : 5d6c6bfbd5fdd7000d154d99
         * timestamp : 1567390636
         * pinyin : que ren
         * source : dui
         * inittime : 17.123046875
         * semantics : {"request":{"confidence":1,"slotcount":1,"slots":[{"name":"intent","value":"sys.确认"}],"task":"一体机专业模式"}}
         * loadtime : 1.506103515625
         * systime : 2.59375
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
             * request : {"confidence":1,"slotcount":1,"slots":[{"name":"intent","value":"sys.确认"}],"task":"一体机专业模式"}
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
                 * confidence : 1
                 * slotcount : 1
                 * slots : [{"name":"intent","value":"sys.确认"}]
                 * task : 一体机专业模式
                 */

                private int confidence;
                private int slotcount;
                private String task;
                private List<SlotsBean> slots;

                public int getConfidence() {
                    return confidence;
                }

                public void setConfidence(int confidence) {
                    this.confidence = confidence;
                }

                public int getSlotcount() {
                    return slotcount;
                }

                public void setSlotcount(int slotcount) {
                    this.slotcount = slotcount;
                }

                public String getTask() {
                    return task;
                }

                public void setTask(String task) {
                    this.task = task;
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
