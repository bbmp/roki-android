package com.robam.roki.model.bean;

import java.util.List;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/5/7.
 * PS: Not easy to write code, please indicate.
 */
public class PotOilTempParams {


    /**
     * onLin : [{"min":0,"max":50,"statue":"待机中","tips":"ROKI智能锅，让烹饪更简单。"},{"min":50,"max":80,"statue":"冷油温适合炒坚果。","tips":"适合冷炒花生、腰果、肉松、杂酱等，或空锅热油。"},{"min":85,"max":120,"statue":"低油温适合软炸。","tips":"低油温具有保鲜嫩、去水份的作用，适合软炸虾仁、炸鱿鱼，炸藕盒，必要时可复炸。"},{"min":120,"max":180,"statue":"中油温适合干炸、酥炸。","tips":"最适合爆香将葱姜蒜等辅料，放食材后蛋白质可快速凝固，原料不易碎烂。适合滑炒肉丝、鸡蛋等。"},{"min":180,"max":220,"statue":"高温度适合清炸、泼油。","tips":"适合水煮类菜肴，如水煮鱼片、油泼辣椒等菜肴的最后一道工序。"},{"min":220,"max":350,"statue":"过热。","tips":"健康饮食，请注意锅内温度。"},{"min":350,"max":1000,"statue":"防干烧警示。","tips":"智能锅识别到干烧，已自动为您关闭灶具。or智能锅识别到干烧，请立即关闭灶具。"}]
     * offLin : {"statue":"已离线。","tips":"1、请将智能锅放至在烟机附近，距离过远会影响其蓝牙连接；2、智能锅电池可能已经耗尽，请尝试更换新的电池。"}
     */

    private OffLinBean offLin;
    private List<OnLinBean> onLin;

    public OffLinBean getOffLin() {
        return offLin;
    }

    public void setOffLin(OffLinBean offLin) {
        this.offLin = offLin;
    }

    public List<OnLinBean> getOnLin() {
        return onLin;
    }

    public void setOnLin(List<OnLinBean> onLin) {
        this.onLin = onLin;
    }

    public static class OffLinBean {
        /**
         * statue : 已离线。
         * tips : 1、请将智能锅放至在烟机附近，距离过远会影响其蓝牙连接；2、智能锅电池可能已经耗尽，请尝试更换新的电池。
         */

        private String statue;
        private String tips;

        public String getStatue() {
            return statue;
        }

        public void setStatue(String statue) {
            this.statue = statue;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }

    public static class OnLinBean {
        /**
         * min : 0
         * max : 50
         * statue : 待机中
         * tips : ROKI智能锅，让烹饪更简单。
         */

        private int min;
        private int max;
        private String statue;
        private String tips;

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public String getStatue() {
            return statue;
        }

        public void setStatue(String statue) {
            this.statue = statue;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }
}
