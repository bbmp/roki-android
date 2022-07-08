package com.robam.roki.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/21.
 * @PS:
 */
public class FunctionSmartHomeParams {


    private List<DeviceInfoBean> deviceInfo;

    public List<DeviceInfoBean> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(List<DeviceInfoBean> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public static class DeviceInfoBean implements Serializable {
        /**
         * name : 油烟机
         * dt : ["8236S"]
         * canSay : ["老板老板","打开烟机"]
         * cookbooks : ["烤牛排","烤披萨"]
         */

        private String name;
        private String dt;
        private List<String> canSay;
        private String cookbooks;

        public String getName() {
            return name;
        }


        public String getDt() {
            return dt;
        }

        public List<String> getCanSay() {
            return canSay;
        }


        public String getCookbooks() {
            return cookbooks;
        }

        public void setCookbooks(String cookbooks) {
            this.cookbooks = cookbooks;
        }
    }
}
