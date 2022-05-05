package com.robam.roki.model.bean;

import java.util.List;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/26.
 * @PS:
 */
public class HidKitHomeOtherParams {


    private List<StepsBean> steps;

    public List<StepsBean> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsBean> steps) {
        this.steps = steps;
    }

    public static class StepsBean {
        /**
         * name : {"value":"音乐","paramType":"String"}
         * tag : {"value":"技能一","paramType":"String"}
         * desc : {"value":"收听FM91.1,打开你身边的收音机，听伴助你解除地域的封印，过去只能收听本地电台的你，
         * 现在可以拥有全国各地的不同声音。音乐台、交通台、经济台、新闻台，不同类别，不同资讯，都能实时畅听。","paramType":"String"}
         */

        private NameBean name;
        private TagBean tag;
        private DescBean desc;

        public NameBean getName() {
            return name;
        }

        public void setName(NameBean name) {
            this.name = name;
        }

        public TagBean getTag() {
            return tag;
        }

        public void setTag(TagBean tag) {
            this.tag = tag;
        }

        public DescBean getDesc() {
            return desc;
        }

        public void setDesc(DescBean desc) {
            this.desc = desc;
        }

        public static class NameBean {
            /**
             * value : 音乐
             * paramType : String
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class TagBean {
            /**
             * value : 技能一
             * paramType : String
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }

        public static class DescBean {
            /**
             * value : 收听FM91.1,打开你身边的收音机，听伴助你解除地域的封印，过去只能收听本地电台的你，现在可以拥有
             * 全国各地的不同声音。音乐台、交通台、经济台、新闻台，不同类别，不同资讯，都能实时畅听。
             * paramType : String
             */

            private String value;
            private String paramType;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamType() {
                return paramType;
            }

            public void setParamType(String paramType) {
                this.paramType = paramType;
            }
        }
    }
}
