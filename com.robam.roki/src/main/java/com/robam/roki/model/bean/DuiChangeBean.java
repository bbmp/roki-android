package com.robam.roki.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/10.
 * 档位调节
 */

public class DuiChangeBean {


    /**
     * change : up
     * skillId : 2019061700000181
     * intentName : 档位调节
     *
     * nlu : {"input":"烟机风量大一点","version":"2019.1.15.20:40:58","skillVersion":"7","skillId":"2019061700000181","skill":"烟机控制技能","res":"5d10481482a67e000d96e93b","timestamp":1562749148,"pinyin":"yan ji feng liang da yi dian","source":"dui","inittime":10.028076171875,"semantics":{"request":{"slots":[{"rawvalue":"大一点","rawpinyin":"da yi dian","value":"up","name":"change","pos":[4,6]},{"name":"intent","value":"档位调节"}],"confidence":1,"task":"档位调节","slotcount":2}},"loadtime":2.1279296875,"systime":2.64111328125}
     */

    private String change;
    private String skillId;
    private String selectNo;
    private String intentName;
    private NluBean nlu;


    public String getSelectNo() {
        return selectNo;
    }

    public void setSelectNo(String selectNo) {
        this.selectNo = selectNo;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
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
         * input : 烟机风量大一点
         * version : 2019.1.15.20:40:58
         * skillVersion : 7
         * skillId : 2019061700000181
         * skill : 烟机控制技能
         * res : 5d10481482a67e000d96e93b
         * timestamp : 1562749148
         * pinyin : yan ji feng liang da yi dian
         * source : dui
         * inittime : 10.028076171875
         * semantics : {"request":{"slots":[{"rawvalue":"大一点","rawpinyin":"da yi dian","value":"up","name":"change","pos":[4,6]},{"name":"intent","value":"档位调节"}],"confidence":1,"task":"档位调节","slotcount":2}}
         * loadtime : 2.1279296875
         * systime : 2.64111328125
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
             * request : {"slots":[{"rawvalue":"大一点","rawpinyin":"da yi dian","value":"up","name":"change","pos":[4,6]},{"name":"intent","value":"档位调节"}],"confidence":1,"task":"档位调节","slotcount":2}
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
                 * slots : [{"rawvalue":"大一点","rawpinyin":"da yi dian","value":"up","name":"change","pos":[4,6]},{"name":"intent","value":"档位调节"}]
                 * confidence : 1
                 * task : 档位调节
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
                     * rawvalue : 大一点
                     * rawpinyin : da yi dian
                     * value : up
                     * name : change
                     * pos : [4,6]
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
