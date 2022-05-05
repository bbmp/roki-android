package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/8/31.
 */

public class DuiSteamBakePowerBean {

    /**
     * skillId : 2019082900000542
     * nlu : {"input":"关闭蒸烤一体机","version":"2019.1.15.20:40:58","skillVersion":"3","skillId":"2019082900000542","skill":"蒸烤一体机控制技能","res":"5d67a85ed5fdd7000d13e526","timestamp":1567231361,"pinyin":"guan bi zheng kao yi ti ji","source":"dui","inittime":22.22412109375,"semantics":{"request":{"confidence":1,"slotcount":2,"slots":[{"rawvalue":"关闭","rawpinyin":"guan bi","value":"off","name":"power","pos":[0,1]},{"name":"intent","value":"一体机开关"}],"task":"一体机的开关"}},"loadtime":6.9599609375,"systime":15.7919921875}
     * intentName : 一体机开关
     * power : off
     */

    private String skillId;
    private NluBean nlu;
    private String intentName;
    private String power;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public NluBean getNlu() {
        return nlu;
    }

    public void setNlu(NluBean nlu) {
        this.nlu = nlu;
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

    public static class NluBean {
        /**
         * input : 关闭蒸烤一体机
         * version : 2019.1.15.20:40:58
         * skillVersion : 3
         * skillId : 2019082900000542
         * skill : 蒸烤一体机控制技能
         * res : 5d67a85ed5fdd7000d13e526
         * timestamp : 1567231361
         * pinyin : guan bi zheng kao yi ti ji
         * source : dui
         * inittime : 22.22412109375
         * semantics : {"request":{"confidence":1,"slotcount":2,"slots":[{"rawvalue":"关闭","rawpinyin":"guan bi","value":"off","name":"power","pos":[0,1]},{"name":"intent","value":"一体机开关"}],"task":"一体机的开关"}}
         * loadtime : 6.9599609375
         * systime : 15.7919921875
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
             * request : {"confidence":1,"slotcount":2,"slots":[{"rawvalue":"关闭","rawpinyin":"guan bi","value":"off","name":"power","pos":[0,1]},{"name":"intent","value":"一体机开关"}],"task":"一体机的开关"}
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
                 * slotcount : 2
                 * slots : [{"rawvalue":"关闭","rawpinyin":"guan bi","value":"off","name":"power","pos":[0,1]},{"name":"intent","value":"一体机开关"}]
                 * task : 一体机的开关
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
                     * rawvalue : 关闭
                     * rawpinyin : guan bi
                     * value : off
                     * name : power
                     * pos : [0,1]
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
