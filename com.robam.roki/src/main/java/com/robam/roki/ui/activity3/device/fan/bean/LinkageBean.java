package com.robam.roki.ui.activity3.device.fan.bean;

import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LinkageBean {

    public Order order;
    public List<Function> function;

    public static class Order {
        public int order;
    }

    public static class Function {
        public String des;
        public int order;
    }
}
